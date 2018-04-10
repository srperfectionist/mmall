package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @author SR
 * @date 2017/11/22
 */
public class DateTimeUtil {

    public static final String STANDARD_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串转Date
     *
     * @param dateTimeStr
     * @return
     */
    public static Date strToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMATTER);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * Date转字符串
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMATTER);
    }

    /**
     * 字符串转Date
     *
     * @param dateTimeStr
     * @param formatterStr
     * @return
     */
    public static Date strToDate(String dateTimeStr, String formatterStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatterStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /**
     * Date转字符串
     *
     * @param date
     * @param formatterStr
     * @return
     */
    public static String dateToString(Date date, String formatterStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatterStr);
    }
}
