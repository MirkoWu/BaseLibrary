package com.softgarden.baselibrary.utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.softgarden.baselibrary.BaseApplication;

import java.util.Locale;

/**
 * @author by DELL
 * @date on 2017/11/29
 * @describe
 */

public class LanguageUtil {


    public static void switchLanguage(Locale locale) {
        DBUtil.setEnglish(locale == Locale.ENGLISH);
        //应用内配置语言
        Resources resources = BaseApplication.getInstance().getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, dm);
    }

}
