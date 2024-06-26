package com.hhplus.assignment.lecture.common.dateTimeUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static LocalDateTime stringToLocalDateTime(String formattedDateTime, DateTimeFormatter formatter) {
        if (formattedDateTime == null || formatter == null) {
            return null;
        }

        return LocalDateTime.parse(formattedDateTime, formatter);
    }
}
