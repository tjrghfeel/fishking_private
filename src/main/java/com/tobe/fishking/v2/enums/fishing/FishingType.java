package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum FishingType   implements IEnumModel   {

    ship("선상"),
    seaRocks("갯바위"),
    breakwater("방파제");

    private String value;
    FishingType(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return this.value;
    }

}
