package com.softgarden.baselibrary.utils;

import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * Created by MirkoWu on 2017/4/20 0020.
 */

public class L {

    /**
     * Logger 工具打印
     */
    public static void d(String msg) {
        Logger.d(msg);
    }

    public static void i(String msg) {
        Logger.i(msg);
    }

    public static void v(String msg) {
        Logger.v(msg);
    }

    public static void w(String msg) {
        Logger.w(msg);
    }

    public static void e(String msg) {
        Logger.e(msg);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void xml(String xml) {
        Logger.xml(xml);
    }

    /**
     * 以下是系统自带的
     * 打印简单的log
     */
    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }


}
