package com.tobe.fishking.v2.enums.common;

public enum UseType {

    Y("예"),
    N("아니오");

    private String value;
    UseType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

}