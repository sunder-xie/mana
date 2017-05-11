package com.tqmall.mana.component.util;

import org.apache.http.client.utils.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by huangzhangting on 15/9/2.
 */
public class DateUtil {
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String yyMMdd = "yyMMdd";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyyyMM = "yyyyMM";

    public static String dateToString(Date date, String dateFormat){
        if(date==null){
            return "";
        }
        DateTime dt = new DateTime(date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);

        return dt.toString(formatter);
    }

    public static Date stringToDate(String str, String dateFormat){
        if(StringUtils.isEmpty(str)){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
        DateTime dt = DateTime.parse(str, formatter);

        return dt.toDate();
    }

    public static Date getFormatDate(Date date,String dateFormat){
        SimpleDateFormat dataFormat = new SimpleDateFormat(dateFormat);
        String nowStr = dataFormat.format(date);
        SimpleDateFormat sdf=new SimpleDateFormat(dateFormat);
        try {
            Date date1 = sdf.parse(nowStr);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getTimestamp(){
        return System.currentTimeMillis();
    }

    /**
     * 获取当天时间的前d天（-d）或者后d天的时间
     *
     * @param cal     基准时间
     * @param d       加减天数
     * @param pattern 格式化的日期格式
     * @return 满足格式的日期String
     */
    public static String getFewDaysAgoOrAfterDate(Calendar cal, Integer d, String pattern) {
        Calendar calendar = (Calendar) cal.clone();
        calendar.add(Calendar.DATE, d);
        Date date = new Date(calendar.getTimeInMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * * 获取传入时间的前n年（-d）或者后n年的时间
     * @param date 传入时间
     * @param yearNum 前后 n 年,可为null
     * @param monthNum 前后 n 月,可为null
     * @param dayNum 前后 n 天,可为null
     * @return
     */
    public static Date getDaysBeforeOrAfter(Date date, Integer yearNum, Integer monthNum,Integer dayNum){
        DateTime resultTime = new DateTime(date);
        if(yearNum != null) {
            if (yearNum < 0) {resultTime = resultTime.minusYears(-yearNum);}
            else {resultTime = resultTime.plusYears(yearNum);}
        }
        if(monthNum != null){
            if(monthNum < 0) resultTime = resultTime.minusMonths(-monthNum);
            else resultTime = resultTime.plusMonths(monthNum);
        }
        if(dayNum != null){
            if(dayNum < 0) resultTime = resultTime.minusDays(-dayNum);
            else resultTime = resultTime.plusDays(dayNum);
        }

        return resultTime.toDate();
    }

}
