package com.tobe.fishking.v2.enums.common;

public enum AdType {

    premium("프리미엄"),
    recommend("추천");

    private String value;
    AdType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

}
