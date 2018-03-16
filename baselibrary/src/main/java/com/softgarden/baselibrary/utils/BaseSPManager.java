package com.softgarden.baselibrary.utils;

/**
 * @author by DELL
 * @date on 2017/11/29
 * @describe SharedPreferences 数据保存管理类
 */

public class BaseSPManager {
    public static final String ENGLISH = "english";//语言
    public static final String DAY_NIGHT_MODE = "day_night_mode";//日夜模式
    public static final String IS_FIRST_LUNCH = "is_first_lunch";//日夜模式

    /*** 是否英语语言 */
    public static boolean isEnglish() {
        return (boolean) SPUtil.get(ENGLISH, false);
    }

    public static void setEnglish(boolean isEnglish) {
        SPUtil.put(ENGLISH, isEnglish);
    }


    /*** 日夜模式 */
    public static boolean isNightMode() {
        return (boolean) SPUtil.get(DAY_NIGHT_MODE, false);
    }

    public static void setNightMode(boolean nightMode) {
        SPUtil.put(DAY_NIGHT_MODE, nightMode);
    }

    /*** 是否第一次启动 */
    public static boolean isFirstLunch() {
        return (boolean) SPUtil.get(DAY_NIGHT_MODE, true);
    }

    public static void setIsFirstLunch(boolean isFirstLunch) {
        SPUtil.put(DAY_NIGHT_MODE, isFirstLunch);
    }


}
