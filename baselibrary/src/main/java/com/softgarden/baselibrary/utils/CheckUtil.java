package com.softgarden.baselibrary.utils;

import android.text.TextUtils;

/**
 * Created by MirkoWu on 2018/3/26 0026.
 */

public class CheckUtil {


    public static boolean isEmpty(String str, String hint) {
        if (TextUtils.isEmpty(str)) {
            ToastUtil.s(hint);
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str, String hint) {
        return !isEmpty(str, hint);
    }

    public static boolean isNotEquals(String str1, String str2, String hint) {
        if (!TextUtils.equals(str1, str2)) {
            ToastUtil.s(hint);
            return true;
        }
        return false;
    }

    public static boolean isEquals(String str1, String str2, String hint) {
        if (TextUtils.equals(str1, str2)) {
            ToastUtil.s(hint);
            return true;
        }
        return false;
    }


    /**
     * 密码是否非法 6位以上数字或数字+字母
     *
     * @param str
     * @param hint
     * @return
     */
    public static boolean isIllegalPwd(String str, String hint) {
        if (TextUtils.isEmpty(str)) {
            ToastUtil.s(hint);
            return true;
        }

//        if (!Pattern.matches("^[a-zA-Z0-9]{6,}$", str)) {
//            ToastUtil.s(hint);
//            return true;
//        }
        //这里只判断了长度 任意字符都可以
        if (str.length() < 6 || str.contains(" ")) {
            ToastUtil.s(hint);
            return true;
        }
        return false;
    }


    /**
     * 邮箱是否非法
     *
     * @param str
     * @param hint
     * @return
     */
    public static boolean isIllegalEmail(String str, String hint) {
        if (TextUtils.isEmpty(str)) {
            ToastUtil.s(hint);
            return true;
        }

        if (!RegularUtil.isEmail(str)) {
            ToastUtil.s(hint);
            return true;
        }
        return false;
    }

    public static boolean isIllegalIDCard(String str, String hint) {
        if (TextUtils.isEmpty(str)) {
            ToastUtil.s(hint);
            return true;
        }

        if (!RegularUtil.isIDCard18(str)) {
            ToastUtil.s(hint);
            return true;
        }
        return false;
    }

    /**
     * 手机号码是否非法
     *
     * @param str
     * @param hint
     * @return
     */
    public static boolean isIllegalPhone(String str, String hint) {
        if (TextUtils.isEmpty(str)) {
            ToastUtil.s(hint);
            return true;
        }

        if (!RegularUtil.isMobileSimple(str)) {
            ToastUtil.s(hint);
            return true;
        }
        return false;
    }


}
