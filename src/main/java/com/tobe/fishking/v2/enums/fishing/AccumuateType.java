package com.tobe.fishking.v2.enums.fishing;

public enum AccumuateType {

    use("사용"),
    accumulate("적립");

    private String value;
    AccumuateType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

}