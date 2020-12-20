package com.tobe.fishking.v2.utils;

import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtils
{
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String getDateInFormat(LocalDate value)
    {
        return value.format(FORMATTER);
    }

    public static String getDateTimeInFormat(LocalDateTime value)
    {
        return value.format(FORMATTER_TIME);
    }

    public static DateTimeTemplate<LocalDateTime> getDateTimeInQuery(LocalDateTime value)
    {
        return Expressions.dateTimeTemplate(
                LocalDateTime.class,
                "STR_TO_DATE({0}, '%Y-%m-%d %H:%i:%s')",
                getDateTimeInFormat(value));
    }
    public static DateTemplate<LocalDate> getDateInQuery(LocalDate value)
    {
        return Expressions.dateTemplate(
                LocalDate.class,
                "STR_TO_DATE({0}, '%Y-%m-%d %H:%i:%s')",
                getDateInFormat(value));
    }
}