package com.softgarden.baselibrary.utils;

import java.security.MessageDigest;

/**
 * 类描述：MD5加密工具类
 */
public class MD5Util {

    private MD5Util() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * 将字符串进行MD5加密
     *
     * @param secretKey 密钥
     * @param pstr      被加密的字符串
     * @return
     */
    public static String ToMD5(String secretKey, String pstr) {
        //加密
        pstr = secretKey + pstr;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            MessageDigest md5Temp = MessageDigest.getInstance("MD5");
            md5Temp.update(pstr.getBytes("UTF8"));
            byte[] md = md5Temp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 不带Key 的加密
     * 多用于加密登录密码等
     *
     * @param pstr
     * @return
     */
    public static String ToMD5NOKey(String pstr) {
        return ToMD5(null, pstr);
    }

}
