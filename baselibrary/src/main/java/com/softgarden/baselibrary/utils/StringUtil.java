package com.softgarden.baselibrary.utils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class StringUtil {


    /**
     * 将字符串列表 用逗号隔开 组合成新的字符串
     *
     * @param list
     * @return
     */
    public static String insertDot(List<String> list) {
        return insert(list, ",");
    }

    /**
     * 将字符串列表 用逗号或别的符号隔开 组合成新的字符串
     *
     * @param list 字符串列表
     * @param reg  要隔开的符号
     * @return
     */
    public static String insert(List<String> list, String reg) {
        if (EmptyUtil.isEmpty(list)) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(reg + list.get(i));
        }
        return subPrefix(sb.toString(), reg);
    }

    /**
     * 截取前缀 第一个 ，逗号
     *
     * @return
     */
    public static String subPrefix(String str) {
        return subPrefix(str, ",");
    }

    public static String subPrefix(String str, String reg) {
        if (EmptyUtil.isNotEmpty(str) && str.startsWith(reg)) {
            str = str.substring(reg.length());
        }
        return str;
    }

    /**
     * 截取后缀
     *
     * @param str
     * @return
     */
    public static String subSuffix(String str) {
        return subSuffix(str, ",");
    }

    public static String subSuffix(String str, String reg) {
        if (EmptyUtil.isNotEmpty(str) && str.endsWith(reg)) {
            str = str.substring(0, str.length() - reg.length());
        }
        return str;
    }

    //将16进制的字符串转换成byte数组，每2个16进制数字转成一个byte
    public static byte[] hex2Bytes(String hex) {
        hex = hex.replaceAll("[^0-9,a-f,A-F]", "");
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2),
                    16);
        }

        return bytes;
    }

    // 将字节数组转换成16进制字符串
    public static String byte2Hexstr(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            int tmp = (bytes[i] >> 4) & 0x0F;
            buf.append(getHexChar(tmp));

            tmp = bytes[i] & 0x0F;
            buf.append(getHexChar(tmp));
        }

        return buf.toString();
    }

    private static char getHexChar(int value) {
        if (value >= 0 && value < 10) {
            return (char) ('0' + value);
        } else {
            return (char) ('A' + (value - 10));
        }
    }

    public static String toBinaryString(int num) {
        String value = Integer.toBinaryString(num);

        if (value.length() < 8) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8 - value.length(); i++) {
                sb.append("0");
            }
            sb.append(value);
            return sb.toString();
        }
        return value;
    }
}
