package com.testmonitor.parsers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    /**
     * Parse a date object to a datestring that is accepted by the TestMonitor API.
     *
     * @param date The date you want to parse into a string
     * @return The datestring in year-month-day format.
     */
    public static String toDateString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(date);
    }

    /**
     * Parse a datestring from the TestMonitor API to a date object.
     *
     * @param date The datestring you want to parse into a dateobject
     * @return The datestring in year-month-day format.
     */
    public static Date toDateObject(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date parse = null;

        try {
            parse = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parse;
    }
}
