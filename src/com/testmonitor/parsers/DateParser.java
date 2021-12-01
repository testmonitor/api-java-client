package com.testmonitor.parsers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateParser {
    /**
     * Parse a date object to a datestring that is accepted by the TestMonitor API.
     *
     * @param date The date you want to parse into a string
     * @return The datestring in year-month-day format.
     */
    public static String toDateString(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    /**
     * Parse a datestring from the TestMonitor API to a date object.
     *
     * @param date The datestring you want to parse into a dateobject
     * @return The datestring in year-month-day format.
     */
    public static LocalDate toDateObject(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    }
}
