package com.softgarden.baselibrary.utils;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * @author by DELL
 * @date on 2017/11/29
 * @describe SharedPreferences 数据保存
 */

public class DBUtil {
    public static final String ENGLISH = "english";//语言
    public static final String THEME_COLOR = "theme_Color";//主题颜色

    /*** 是否英语语言 */
    public static boolean getEnglish() {
        return (boolean) SPUtil.get(ENGLISH, false);
    }

    public static void setEnglish(boolean isEnglish) {
        SPUtil.put(ENGLISH, isEnglish);
    }


    /*** 主题颜色 */
    public static int getThemeColor() {
        return (int) SPUtil.get(THEME_COLOR, Color.parseColor("#54ACEC"));
    }

    public static void setThemeColor(@ColorInt int colorResId) {
        SPUtil.put(THEME_COLOR, colorResId);
    }


}
