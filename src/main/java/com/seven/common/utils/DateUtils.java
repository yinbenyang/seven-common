package com.seven.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author yinbenyang
 * @version 1.0
 * @description: 日期工具类
 * @date 2020-12-03 13:36
 */
public class DateUtils {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * @description:  指定日期往后延迟day天，day为负数则往前退day天
     * @param date
     * @param day
     * @return: java.lang.String
     * @author yinbenyang
     * @date: 2020-12-03 13:42
     */
    public static String getFutureDate(String date, int day) {
        LocalDate localDate = LocalDate.parse(date, DATE_FORMATTER);
        return localDate.plusDays(day).toString();
    }

    /**
     * @description: 当前日期往后延迟day天，day为负数则往前退day天
     * @param day
     * @return: java.lang.String
     * @author yinbenyang
     * @date: 2020-12-03 14:34
     */
    public static String getFutureDate(int day) {
        return LocalDate.now().plusDays(day).toString();
    }

    /**
     * @description: 指定时间往后延迟day天，day为负数则往前退day天
     * @param dateTime
     * @param day
     * @return: java.lang.String
     * @author yinbenyang
     * @date: 2020-12-03 14:34
     */
    public static String getFutureDateTime(String dateTime, int day) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
        LocalDateTime plusDays = localDateTime.plusDays(day);
        return plusDays.format(DATE_TIME_FORMATTER);
    }

    /**
     * @description: 当前时间往后延迟day天，day为负数则往前退day天
     * @param day
     * @return: java.lang.String
     * @author yinbenyang
     * @date: 2020-12-03 14:34
     */
    public static String getFutureDateTime(int day) {
        return LocalDateTime.now().plusDays(day).format(DATE_TIME_FORMATTER);
    }


    public static void main(String[] args) {
        System.out.println(DateUtils.getFutureDate("2020-10-01",-7));
        System.out.println(getFutureDate(1));
        System.out.println(getFutureDateTime("2020-10-01 12:20:20",1));
        System.out.println(getFutureDateTime(1));
    }

}
