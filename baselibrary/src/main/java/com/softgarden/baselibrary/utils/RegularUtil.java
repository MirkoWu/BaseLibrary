package com.softgarden.baselibrary.utils;

import android.text.TextUtils;

import java.util.HashMap;
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
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isPassword(String string) {
        return isMatch(REGEX_PASSWORD, string);
    }

    /**
     * Return whether input matches regex of id card number which length is 15.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */

    public static boolean isIDCard15(final String input) {

        return isMatch(RegularCons.REGEX_ID_CARD15, input);

    }


    /**
     * Return whether input matches regex of id card number which length is 18.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */

    public static boolean isIDCard18(final String input) {

        return isMatch(RegularCons.REGEX_ID_CARD18, input);

    }


    /**
     * Return whether input matches regex of exact id card number which length is 18.
     *
     * @param input The input.
     * @return {@code true}: yes<br>{@code false}: no
     */

    public static boolean isIDCard18Exact(final String input) {

        if (isIDCard18(input)) {

            int[] factor = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

            char[] suffix = new char[]{'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

            HashMap<String, String> CITY_MAP = new HashMap<>();
            if (CITY_MAP.isEmpty()) {

                CITY_MAP.put("11", "北京");

                CITY_MAP.put("12", "天津");

                CITY_MAP.put("13", "河北");

                CITY_MAP.put("14", "山西");

                CITY_MAP.put("15", "内蒙古");


                CITY_MAP.put("21", "辽宁");

                CITY_MAP.put("22", "吉林");

                CITY_MAP.put("23", "黑龙江");


                CITY_MAP.put("31", "上海");

                CITY_MAP.put("32", "江苏");

                CITY_MAP.put("33", "浙江");

                CITY_MAP.put("34", "安徽");

                CITY_MAP.put("35", "福建");

                CITY_MAP.put("36", "江西");

                CITY_MAP.put("37", "山东");


                CITY_MAP.put("41", "河南");

                CITY_MAP.put("42", "湖北");

                CITY_MAP.put("43", "湖南");

                CITY_MAP.put("44", "广东");

                CITY_MAP.put("45", "广西");

                CITY_MAP.put("46", "海南");


                CITY_MAP.put("50", "重庆");

                CITY_MAP.put("51", "四川");

                CITY_MAP.put("52", "贵州");

                CITY_MAP.put("53", "云南");

                CITY_MAP.put("54", "西藏");


                CITY_MAP.put("61", "陕西");

                CITY_MAP.put("62", "甘肃");

                CITY_MAP.put("63", "青海");

                CITY_MAP.put("64", "宁夏");

                CITY_MAP.put("65", "新疆");


                CITY_MAP.put("71", "台湾");

                CITY_MAP.put("81", "香港");

                CITY_MAP.put("82", "澳门");

                CITY_MAP.put("91", "国外");

            }

            if (CITY_MAP.get(input.subSequence(0, 2).toString()) != null) {

                int weightSum = 0;

                for (int i = 0; i < 17; ++i) {

                    weightSum += (input.charAt(i) - '0') * factor[i];

                }

                int idCardMod = weightSum % 11;

                char idCardLast = input.charAt(17);

                return idCardLast == suffix[idCardMod];

            }

        }

        return false;

    }


}
