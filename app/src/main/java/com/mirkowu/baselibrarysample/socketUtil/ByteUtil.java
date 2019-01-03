package com.mirkowu.baselibrarysample.socketUtil;

/**
 * @author by DELL
 * @date on 2018/1/15
 * @describe
 */

public class ByteUtil {
    /**
     * 合并二个数组
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[] concat(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    /**
     * 截取数组
     * @param src
     * @param length
     * @return
     */
    public static byte[] sub(byte[] src, int length) {
        byte[] des = new byte[length];
        System.arraycopy(src, 0, des, 0, length);
        return des;
    }

    public static byte[] concatBuffer(byte[] src, int length, byte[] des) {
        byte[] newSrc = sub(src, length);
        return concat(newSrc, des);
    }


}
