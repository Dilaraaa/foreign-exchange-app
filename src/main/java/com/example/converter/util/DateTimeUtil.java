package com.example.converter.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateTimeUtil {
    public static ZonedDateTime toStartOfDay(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .atStartOfDay(ZoneId.systemDefault());
    }
}
