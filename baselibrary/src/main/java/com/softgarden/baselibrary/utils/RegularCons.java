package com.softgarden.baselibrary.utils;

/**
 * Created by MirkoWu on 2017/4/20 0020.
 */

public class RegularCons {
    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
     * <p>电信：133、153、173、177、180、181、189</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    /**
     * 正则：手机号 简单验证
     * 第一位必定为1，第二位必定为3或4或5或7或8，其他位置的可以为0-9
     */
    public static final String REGEX_MOBILE_SIMPLE = "[1][34578]\\d{9}";
    /**
     * 正则：邮箱
     * 更严格
     */
    public static final String REGEX_EMAIL_EXACT = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

    /**
     * 正则：邮箱
     */
    public static final String REGEX_EMAIL_SIMPLE = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 正则：URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
    /**
     * 正则：汉字
     */
    public static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";
    /**
     * 正则：IP地址
     */
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    /**
     * 正则：密码
     * (长度在6~18之间，只能包含字母、数字和下划线)
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6}$";
}
