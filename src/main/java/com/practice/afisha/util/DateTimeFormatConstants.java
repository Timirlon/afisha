package com.practice.afisha.util;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeFormatConstants {
    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static DateTimeFormatter getDefaultFormatter() {
        return DateTimeFormatter.ofPattern(FORMAT_PATTERN);
    }
}
