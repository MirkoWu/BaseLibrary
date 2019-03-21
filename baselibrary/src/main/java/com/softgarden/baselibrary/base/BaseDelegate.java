package com.softgarden.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;

import com.softgarden.baselibrary.BuildConfig;
import com.softgarden.baselibrary.dialog.LoadingDialog;
import com.softgarden.baselibrary.utils.BaseSPManager;
import com.softgarden.baselibrary.utils.L;
import com.softgarden.baselibrary.utils.LanguageUtil;
import com.softgarden.baselibrary.utils.ToastUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * @author by DELL
 * @date on 2018/8/29
 * @describe
 */
public class BaseDelegate {

    protected Activity mActivity;
    protected Locale mLanguage;//语言
    protected boolean isNightMode;//是否夜间模式
    protected boolean mIsAdapterScreen = true;//是否适配屏幕
    protected boolean mOrientationPortrait = true;//是否强制竖屏默认开启,视频界面全屏播放就要设置false

    public BaseDelegate(Activity activity) {
        this.mActivity = activity;
    }


    public Context getContext() {
        return mActivity;
    }


    public Activity getActivity() {
        return mActivity;
    }

    public void onCreate(Bundle savedInstanceState) {
        //语言切换 要在setContentView()前

        mLanguage = BaseSPManager.getLanguage();
        L.d("SP 语言=" + mLanguage.getLanguage() + "  " + mLanguage.getCountry());

        changeLanguage(mLanguage, false);


        //日夜模式
        isNightMode = BaseSPManager.isNightMode();
        changeDayNightMode(isNightMode);

        //屏幕适配 已使用AutoSize
    }


    public void onStart() {

    }


    public void onResume() {
        checkScreenOrientation();
        checkDayNightMode();
        checkLanguage();
    }


    public void onPause() {

    }


    public void onStop() {

    }


    public void onSaveInstanceState(Bundle outState) {

    }


    public void onDestroy() {

    }


    /***********************************  LoadingDialog start   ***********************************/
    /**
     * 显示加载框
     */

    public void showProgressDialog() {
        showLoading(mActivity, null);
    }

    /**
     * 显示加载框（带文字）
     */

    public void showProgressDialog(CharSequence message) {
        showLoading(mActivity, message);
    }

    /**
     * 隐藏加载框
     */

    public void hideProgressDialog() {
        dismissLoading();
    }


    private int mCount = 0;
    private LoadingDialog mLoadingDialog;

    public synchronized void showLoading(Activity activity, CharSequence message) {
        if (mCount == 0) {
            mLoadingDialog = new LoadingDialog(activity, message);
            mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mCount = 0;
                }
            });
            mLoadingDialog.show();
        }
        mCount++;
    }

    public synchronized void dismissLoading() {
        if (mCount == 0) {
            return;
        }
        mCount--;
        if (mCount == 0 && mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void clearLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mCount = 0;
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
    /*******************************  LoadingDialog end  *****************************************/


    /**
     * 检查是否为竖屏
     */
    public void checkScreenOrientation() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            L.e("avoid calling setRequestedOrientation when Oreo.");
            return;
        }

        if (mOrientationPortrait && mActivity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            //调用此方法会重新创建Activity导致onCreate()执行二次,
            // 最好在manifest中配置 android:screenOrientation="portrait"
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 是否是透明的Activity
     *
     * @return
     */
    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = mActivity.obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置横屏竖屏
     *
     * @param mOrientationPortrait true 竖屏 false 横屏
     */
    public void setOrientationPortrait(boolean mOrientationPortrait) {
        this.mOrientationPortrait = mOrientationPortrait;
    }

    /**
     * 配置变化时 （当前用于切换语言）
     *
     * @param newConfig
     */
    public void onConfigurationChanged(Configuration newConfig) {
        LanguageUtil.switchLanguage(mActivity, mLanguage);
    }

    /**
     * 7.0后适配语言
     *
     * @param newBase
     * @return
     */
    public Context attachBaseContext(Context newBase) {
        mLanguage = BaseSPManager.getLanguage();
        return LanguageUtil.attachBaseContext(newBase, mLanguage);
    }


    /**
     * 切换语言 (设置完后要重启Activity才生效 {@link #reload()})
     *
     * @param language
     */
    public void changeLanguage(Locale language, boolean fromUser) {
        LanguageUtil.switchLanguage(mActivity, language);
        BaseSPManager.setLanguage(language);
        //改了之后就不会
        if (fromUser) {
            BaseSPManager.setFollowSystemLanguage(false);
        }
    }


    /**
     * 检查语言
     */
    public void checkLanguage() {
        Locale language = BaseSPManager.getLanguage();
        if (!isEqualsLanguage(mLanguage, language)) {
            mLanguage = language;

            reload();
        }
    }

    /**
     * 重启Activity
     * 此方法会比 recreate() 效果更好
     */
    public void reload() {
        Intent intent = mActivity.getIntent();
        mActivity.overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mActivity.finish();
        mActivity.overridePendingTransition(0, 0);
        mActivity.startActivity(intent);
        System.gc();
    }

    /**
     * 是否相同 二种语言 （语言和 国家都相同才算是相同）
     *
     * @param mLanguage
     * @param locale
     * @return
     */
    public boolean isEqualsLanguage(Locale mLanguage, Locale locale) {
        if (TextUtils.equals(mLanguage.getLanguage(), locale.getLanguage())
                && TextUtils.equals(mLanguage.getCountry(), locale.getCountry())) {
            return true;
        }
        return false;
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
     * 显示异常
     *
     * @param throwable
     */
    public void showError(Throwable throwable) {
        ToastUtil.s(throwable.getMessage());
        if (BuildConfig.DEBUG) throwable.printStackTrace();
    }

}
