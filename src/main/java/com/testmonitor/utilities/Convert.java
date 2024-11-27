package com.testmonitor.utilities;

public class Convert {
    public static int booleanToInt(boolean source) {
        return Boolean.compare(source, false);
    }

    public static String booleanToNumericString(boolean source) {
        return Integer.toString(Convert.booleanToInt(source));
    }
}
