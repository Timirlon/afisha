package com.practice.mainsvc.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateTimeFormatConstants {
    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static DateTimeFormatter getDefault() {
        return DateTimeFormatter.ofPattern(FORMAT_PATTERN);
    }

    public static LocalDateTime parse(String str) {
        return LocalDateTime.parse(str, getDefault());
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(getDefault());
    }
}
