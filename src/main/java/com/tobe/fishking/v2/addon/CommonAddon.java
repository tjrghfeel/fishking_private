package com.tobe.fishking.v2.addon;

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

}
