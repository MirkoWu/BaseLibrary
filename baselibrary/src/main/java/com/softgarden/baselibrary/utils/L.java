package com.softgarden.baselibrary.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * Created by MirkoWu on 2017/4/20 0020.
 */

public class L {

    public static boolean isDebug = true;//是否Debug 控制打印

    /**
     * Logger 工具打印
     */
    public static void d(String msg) {
        if (isDebug) Logger.d(msg);
    }

    public static void i(String msg) {
        if (isDebug) Logger.i(msg);
    }

    public static void v(String msg) {
        if (isDebug) Logger.v(msg);
    }

    public static void w(String msg) {
        if (isDebug) Logger.w(msg);
    }

    public static void e(String msg) {
        if (isDebug) Logger.e(msg);
    }

    public static void json(String json) {
        if (isDebug) Logger.json(json);
    }

    public static void xml(String xml) {
        if (isDebug) Logger.xml(xml);
    }


    /**
     * 以下是系统自带的
     * 打印简单的log
     */
    public static void e(String tag, String msg) {
        if (isDebug) Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (isDebug) Log.w(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug) Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (isDebug) Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug) Log.v(tag, msg);
    }


}
