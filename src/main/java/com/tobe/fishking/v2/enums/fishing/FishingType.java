package com.tobe.fishking.v2.enums.fishing;

public enum FishingType {

    ship("선상"),
    sealocks("갯바위");

    private String value;
    FishingType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }


}
