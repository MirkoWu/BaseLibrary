package com.softgarden.baselibrary.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * @author by DELL
 * @date on 2017/11/29
 * @describe
 */

public class LanguageUtil {


    /**
     * 应用语言
     *
     * @param context 此context必须和{@link #attachBaseContext(Context, Locale)} 中的context一致 ，否则切换会失效
     * @param locale
     */
    public static void switchLanguage(Context context, Locale locale) {
        //应用内配置语言
        Resources resources = context.getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);
    }

    /**
     * 获取对应的context
     *
     * @param context
     * @param locale
     * @return
     */
    public static Context attachBaseContext(Context context, Locale locale) {
        //8.0 updateConfiguration 失效  用createConfigurationContext 代替
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context, locale);
        } else {
            return context;
        }
    }


    /**
     * 8.0 配置语言方法
     *
     * @param context
     * @param locale
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(new LocaleList(locale));
        return context.createConfigurationContext(configuration);
    }
}
