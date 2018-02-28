package com.softgarden.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.softgarden.baselibrary.BuildConfig;
import com.softgarden.baselibrary.R;
import com.softgarden.baselibrary.utils.L;
import com.softgarden.baselibrary.utils.ToastUtil;
import com.softgarden.baselibrary.widget.LoadingDialogManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author by DELL
 * @date on 2017/12/2
 * @describe
 */

public abstract class BaseActivity extends RxAppCompatActivity implements IBaseDisplay {
    public final String TAG = getClass().getSimpleName();

    /*** 通用的 用于传递数据的Key  */
    public static final String KEY_DATA = "data";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TYPE = "type";

    public static final int REQUEST_LOGIN = 0x00001234;
    public static final int REQUEST_CODE = 0x00005678;

    protected boolean mOrientationPortrait = true;//是否强制竖屏默认开启,视频界面全屏播放就要设置false

    protected Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_LOGIN) {
            int eventId = 0;
            if (data != null) eventId = data.getIntExtra("eventId", 0);
            backFromLogin(eventId);//从登陆界面返回  登录成功
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @LayoutRes
    protected abstract int getLayoutId();


    protected abstract void initialize();


}
