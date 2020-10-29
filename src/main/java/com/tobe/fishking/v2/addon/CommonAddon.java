package com.tobe.fishking.v2.addon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonAddon {

    public static String boolToYN(boolean bool) {
        return bool?"Y":"N";
    }

    public static String boolToBoolString(boolean bool) {
        return bool?"true":"false";
    }

    public static String boolStringToYN(String bool) {
        return bool.equals("true")?"Y":"N";
    }

    public static String addDashToPhoneNum(String phone) {
        if (phone.startsWith("02")) {
            return phone.replaceFirst("(\\d{2})(\\d{3}|\\d{4})(\\d{4})$", "$1-$2-$3");
        } else {
            return phone.replaceFirst("(\\d{3})(\\d{3}|\\d{4})(\\d{4})$", "$1-$2-$3");
        }
    }

    public static String LocalDateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static LocalDateTime StringToLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static String LocalDateToString(LocalDate dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static LocalDate StringToLocalDate(String dateTime) {
        return LocalDate.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
