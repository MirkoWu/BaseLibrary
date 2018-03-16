package com.softgarden.baselibrary.utils;


/**
 * @author by DELL
 * @date on 2018/3/1
 * @describe
 */

public class ColorUtil {

    public static int[] getRGB(/*@ColorInt*/ int color) {
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        return new int[]{red, green, blue};
    }

    /**
     * 判断是不是深颜色
     *
     * @return
     */
    public static boolean isDarkRGB(int[] colors) {

        int grayLevel = (int) (colors[0] * 0.299 + colors[1] * 0.587 + colors[2] * 0.114);
        if (grayLevel <= 192) {
            return true;
        }
        return false;
    }

    public static boolean isDarkRGB(/*@ColorInt*/ int color) {
        return isDarkRGB(getRGB(color));
    }


    public static void main(String[] args){
        System.out.println("是否深浅"+isDarkRGB(0x7E4834));
    }
}
