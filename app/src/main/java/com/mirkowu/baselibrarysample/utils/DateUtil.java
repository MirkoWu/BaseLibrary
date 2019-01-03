package com.mirkowu.baselibrarysample.utils;

import java.text.SimpleDateFormat;

/**
 * @author by DELL
 * @date on 2018/5/28
 * @describe
 */
public class DateUtil {

    public static final String FORMAT_HMS = "HH:mm:ss";
    public static final String FORMAT_YMD = "yyyy-MM-dd";
    public static final String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    public static String getDataStr(long time, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(time);
    }


    /**
     * 整数转换为时分秒
     *
     * @param endTime
     * @return
     */
    // a integer to xx:xx:xx
    public static String secToTimeStr(long endTime) {
        String timeStr = null;
        long hour = 0;
        long minute = 0;
        long second = 0;

        if (endTime <= 0)
            return "00:00:00";
        else {
            minute = endTime / 60;
            if (minute < 60) {
                second = endTime % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = endTime - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String secToDayTime(long secondTime) {
        StringBuilder timeStr = new StringBuilder();
        long day = secondTime / 86400;
        secondTime = secondTime % 86400;
        long hour = secondTime / 3600 % 24;
        long min = secondTime / 60 % 60;
        long second = secondTime % 60;
        if (day > 0) {
            timeStr.append(day + "天");
            timeStr.append(hour + "时");
            timeStr.append(min + "分");
            timeStr.append(second + "秒");
        }
        return timeStr.toString();
    }

    /**
     * 为小于10的数补零
     *
     * @param i
     * @return
     */
    public static String unitFormat(long i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
