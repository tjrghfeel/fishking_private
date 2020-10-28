package com.tobe.fishking.v2.enums.fishing;

public enum Meridiem {

    am("오전"),
    pm("오후");

    private String value;
    Meridiem(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }

}