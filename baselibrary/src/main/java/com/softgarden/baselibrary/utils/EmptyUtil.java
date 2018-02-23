package com.softgarden.baselibrary.utils;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by DELL on 2017/8/19.
 * 判断 列表是否为空
 * 判断 字符串是否为空
 * 判断 数字是否为0
 */

public class EmptyUtil {
    private EmptyUtil() {
        throw new UnsupportedOperationException("u can`t fuck me...");
    }

    /**
     * 列表是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(List<?> list) {
        return !isEmpty(list);
    }

    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(@Nullable CharSequence str) {
        return !isEmpty(str);
    }

    public static String nullIfEmpty(@Nullable String str) {
        return isEmpty(str) ? null : str;
    }

    /**
     * 是否为0
     *
     * @param numStr
     * @return
     */
    public static boolean isFloatZero(String numStr) {
        if (numStr == null) return true;//为空时也当成0
        float n;
        try {
            n = Float.valueOf(numStr);
        } catch (NumberFormatException e) {
            return false;
        }
        return n == 0;

    }

    public static boolean isNotFloatZero(String numStr) {
        return !isFloatZero(numStr);
    }
    public static boolean isIntZero(String numStr) {
        if (numStr == null) return true;//为空时也当成0
        int n;
        try {
            n = Integer.valueOf(numStr);
        } catch (NumberFormatException e) {
            return false;
        }
        return n == 0;

    }

    public static boolean isNotIntZero(String numStr) {
        return !isIntZero(numStr);
    }
}
