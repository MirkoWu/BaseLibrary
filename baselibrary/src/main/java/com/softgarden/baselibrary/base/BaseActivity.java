package com.softgarden.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;

import com.mirkowu.statusbarutil.StatusBarUtil;
import com.softgarden.baselibrary.BuildConfig;
import com.softgarden.baselibrary.R;
import com.softgarden.baselibrary.utils.BaseSPManager;
import com.softgarden.baselibrary.utils.L;
import com.softgarden.baselibrary.utils.LanguageUtil;
import com.softgarden.baselibrary.utils.ToastUtil;
import com.softgarden.baselibrary.widget.LoadingDialogManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Activity 基类  已实现以下功能
 * <p>
 * 1.切换语言
 * 2.切换日夜模式
 * 3.检测横竖屏
 * 4.显示/隐藏Loading弹框
 * 5.ButterKnife 绑定数据
 * 6.控制RxJava生命周期，防止内存泄漏
 * 7.MVP模式
 * 需要时 可重写createPresenter() {@link BaseActivity#createPresenter()}  并且使用泛型 <P extends BasePresenter> 为当前Presenter
 */

public abstract class BaseActivity<P extends IBasePresenter> extends RxAppCompatActivity implements IBaseDisplay {
    public final String TAG = getClass().getSimpleName();

    /*** 通用的 用于传递数据的Key  */
    public static final String KEY_DATA = "data";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TYPE = "type";

    public static final int REQUEST_LOGIN = 0x00001234;
    public static final int REQUEST_CODE = 0x00005678;

    protected Unbinder unbinder;

    protected boolean mOrientationPortrait = true;//是否强制竖屏默认开启,视频界面全屏播放就要设置false

    private boolean isEnglish;//是否英语
    private boolean isNightMode;//是否夜间模式

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //语言切换 要在setContentView()前
        isEnglish = BaseSPManager.isEnglish();
        changeLanguage(isEnglish);

        isNightMode = BaseSPManager.isNightMode();
        changeDayNightMode(isNightMode);

        initContentView();
        initPresenter();
        initialize();

        //显示当前的Activity路径
        if (BuildConfig.DEBUG) L.e("当前打开的Activity:  " + getClass().getName());
    }

    protected void initContentView() {
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);//ButterKnife
    }


    @Override
    protected void onResume() {
        checkScreenOrientation();
        checkDayNightMode();
        checkLanguage();
        super.onResume();
    }

    /**
     * 检查是否为竖屏
     */
    private void checkScreenOrientation() {
        if (mOrientationPortrait && getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //调用此方法会重新创建Activity导致onCreate()执行二次,最好在manifest中配置 android:screenOrientation="portrait"
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 检查语言
     */
    private void checkLanguage() {
        boolean english = BaseSPManager.isEnglish();
        if (english != isEnglish) {
            isEnglish = english;
            reload();
        }
    }

    /**
     * 检查日夜模式
     */
    private void checkDayNightMode() {
        //检查日夜模式
        boolean nightMode = BaseSPManager.isNightMode();
        if (nightMode != isNightMode) {
            isNightMode = nightMode;
            reload();
        }
    }

    /**
     * 切换日夜模式
     * <p>
     * 需要注意的两个地方，
     * 一是app或者activity引用的style需要是Theme.AppCompat.DayNight或者它的子style，
     * 二是调用getDelegate().setLocalNightMode()你的Activity必须是继承AppCompatActivity的。
     *
     * @param isNightMode
     */
    @Override
    public void changeDayNightMode(boolean isNightMode) {
        BaseSPManager.setNightMode(isNightMode);
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        //这是第二种方式
//        UiModeManager uiModeManager= (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
//        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);

    }

    /**
     * 切换
     *
     * @param isEnglish
     */
    public void changeLanguage(boolean isEnglish) {
        LanguageUtil.switchLanguage(isEnglish ? Locale.ENGLISH : Locale.SIMPLIFIED_CHINESE);//英语 和简体中文
        BaseSPManager.setEnglish(isEnglish);

    }


    /**
     * 此方法会比 recreate() 效果更好
     */
    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        System.gc();
    }

    /**
     * 权限提示对话框
     */
    public void showPermissionDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.prompt_message)
                .setMessage(R.string.permission_lack)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    public void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    /**
     * 设置横屏竖屏
     *
     * @param mOrientationPortrait true 竖屏 false 横屏
     */
    public void setOrientationPortrait(boolean mOrientationPortrait) {
        this.mOrientationPortrait = mOrientationPortrait;
    }

    public boolean isOrientationPortrait() {
        return mOrientationPortrait;
    }

    /**
     * 设置为亮色模式 状态栏 颜色变黑
     */
    public void setStatusBarLightMode() {
        if (!BaseSPManager.isNightMode()) {
            StatusBarUtil.setStatusBarLightModeWithNoSupport(this, true);
        }
    }

    /**
     * 回复状态栏颜色状态
     */
    public void setStatusBarDarkMode() {
        if (!BaseSPManager.isNightMode()) {
            if (StatusBarUtil.setStatusBarDarkMode(this) == 0) {//不支持 亮色 模式
//                //恢复过来时的 处理
//                StatusBarUtil.setTransparent(this);
            }
        }
    }

    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void startActivity(Class<? extends Activity> cls) {
        this.startActivity(new Intent(this, cls));
    }

    public void startActivityFinishSelf(Class<? extends Activity> cls) {
        this.startActivity(new Intent(this, cls));
        finish();
    }

    @Override
    public synchronized void showProgressDialog() {
        LoadingDialogManager.showLoading(getActivity());
    }

    @Override
    public synchronized void hideProgressDialog() {
        LoadingDialogManager.dismissLoading();
    }

    @Override
    public void showError(Throwable throwable) {
        ToastUtil.s(throwable.getMessage());
        if (BuildConfig.DEBUG) throwable.printStackTrace();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_LOGIN) {
            int eventId = 0;
            if (data != null) eventId = data.getIntExtra("eventId", 0);
            backFromLogin(eventId);//从登陆界面返回  登录成功
        }
    }

    /**
     * 登录返回
     *
     * @param eventId
     */
    protected void backFromLogin(int eventId) {

    }

    @Override
    public void showReLoginDialog() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mPresenter != null) mPresenter.detachView();

    }


    /******************************************* MVP **********************************************/
    private P mPresenter;


    protected void initPresenter() {
        mPresenter = createPresenter();
        if (mPresenter != null) mPresenter.attachView(this);
    }

    public P getPresenter() {
        return mPresenter;
    }


    /**
     * 创建Presenter 此处已重写 需要时重写即可
     *
     * @return
     */
    public P createPresenter() {
        return null;
    }

    /******************************************* MVP **********************************************/

    @LayoutRes
    protected abstract int getLayoutId();


    protected abstract void initialize();


}
