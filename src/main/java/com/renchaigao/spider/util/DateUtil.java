package com.renchaigao.spider.util;

import normal.dateUse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Integer CompareTwoDays(String day1,String day2)
    {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        StringBuilder a = new StringBuilder(day1);
        StringBuilder b = new StringBuilder(day2);
        a.insert(4,"-").insert(7,"-");
        b.insert(4,"-").insert(7,"-");
        day1 = a.toString();
        day2 = b.toString();
        calendar1.setTime(StringToDate(day1));
        calendar2.setTime(StringToDate(day2));
        Integer ret = calendar1.get(Calendar.DAY_OF_YEAR) - calendar2.get(Calendar.DAY_OF_YEAR);
        return ret;
    }
    public static Date StringToDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dateret = formatter.parse(dateStr);
            return dateret;
        } catch (ParseException var3) {
            System.out.println(var3);
            return null;
        }
    }
}
