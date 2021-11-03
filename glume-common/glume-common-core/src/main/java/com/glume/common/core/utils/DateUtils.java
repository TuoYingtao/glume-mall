package com.glume.common.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author tuoyingtao
 * @create 2021-10-27 22:13
 */
public class DateUtils {
    /** 默认日期格式 */
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间 默认格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getPresentDate() {
        SimpleDateFormat dateFormat = getDateFormat(FORMAT);
        return dateFormat.format(getSysDateTime());
    }

    /**
     * 获取当前时间 默认格式：自定义
     * @param format
     * @return
     */
    public static String getPresentDate(String format) {
        SimpleDateFormat dateFormat = getDateFormat(format);
        return dateFormat.format(getSysDateTime());
    }

    /**
     * 字符串转日期
     * @param date 字符串日期
     * @return
     */
    public static Date strTransitionDate(String date) {
        SimpleDateFormat dateFormat = getDateFormat(FORMAT);
        Date parse = null;
        try {
            parse = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    /**
     * 日期转字符串
     * @return
     */
    public static String dateTransitionStr() {
        SimpleDateFormat dateFormat = getDateFormat(FORMAT);
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        return dateFormat.format(time);
    }

    /**
     * 获取时间戳
     * @return
     */
    public static Long getDateTime() {
        return new Date().getTime();
    }

    /**
     * 获取系统时间戳
     * @return
     */
    public static Long getSysDateTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取日期格式
     * @param format
     * @return
     */
    public static SimpleDateFormat getDateFormat(String format) {
        return new SimpleDateFormat(format);
    }

    /**
     * 秒转化为天小时分秒字符串
     * @param seconds 时间戳
     * @return String
     */
    public static String formatSeconds(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }
}
