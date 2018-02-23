package com.softgarden.baselibrary.utils;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.softgarden.baselibrary.BaseApplication;

/**
 * @author by DELL
 * @date on 2017/12/21
 * @describe
 */

public class ContextUtil {

    private ContextUtil() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    @ColorInt
    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(BaseApplication.getInstance(), id);
    }

    public static String getString(@StringRes int resId) {
        return BaseApplication.getInstance().getString(resId);
    }

    public static String getString(@StringRes int resId, Object... formatArgs) {
        return BaseApplication.getInstance().getString(resId, formatArgs);
    }


}
