package com.tobe.fishking.v2.enums.common;

public enum AlertType {

    mmmber("사용자");

    private String value;
    AlertType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

}
