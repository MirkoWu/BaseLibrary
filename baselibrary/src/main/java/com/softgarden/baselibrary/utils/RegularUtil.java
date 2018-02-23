package com.softgarden.baselibrary.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

import static com.softgarden.baselibrary.utils.RegularCons.REGEX_CHZ;
import static com.softgarden.baselibrary.utils.RegularCons.REGEX_EMAIL_EXACT;
import static com.softgarden.baselibrary.utils.RegularCons.REGEX_EMAIL_SIMPLE;
import static com.softgarden.baselibrary.utils.RegularCons.REGEX_IP;
import static com.softgarden.baselibrary.utils.RegularCons.REGEX_MOBILE_EXACT;
import static com.softgarden.baselibrary.utils.RegularCons.REGEX_MOBILE_SIMPLE;
import static com.softgarden.baselibrary.utils.RegularCons.REGEX_PASSWORD;
import static com.softgarden.baselibrary.utils.RegularCons.REGEX_URL;


/**
 * Created by MirkoWu on 2017/4/20 0020.
 */

public class RegularUtil {

    /**
     * If u want more please visit http://toutiao.com/i6231678548520731137/
     */

    private RegularUtil() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * string是否匹配regex
     *
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, String string) {
        return !TextUtils.isEmpty(string) && Pattern.matches(regex, string);
    }

    /**
     * 验证手机号（简单）
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileSimple(String string) {
        return isMatch(REGEX_MOBILE_SIMPLE, string);
    }

    /**
     * 验证手机号（精确）
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(String string) {
        return isMatch(REGEX_MOBILE_EXACT, string);
    }

    /**
     * 验证邮箱（简单）
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isEmailSimple(String string) {
        return isMatch(REGEX_EMAIL_SIMPLE, string);
    }
    /**
     * 验证邮箱(精确)
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isEmail(String string) {
        return isMatch(REGEX_EMAIL_EXACT, string);
    }


    /**
     * 验证URL
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isURL(String string) {
        return isMatch(REGEX_URL, string);
    }

    /**
     * 验证汉字
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isChz(String string) {
        return isMatch(REGEX_CHZ, string);
    }

    /**
     * 验证IP地址
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIP(String string) {
        return isMatch(REGEX_IP, string);
    }

    /**
     * 验证密码
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isPassword(String string) {
        return isMatch(REGEX_PASSWORD, string);
    }

}
