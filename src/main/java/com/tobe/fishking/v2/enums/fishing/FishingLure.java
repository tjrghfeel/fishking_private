package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum FishingLure implements IEnumModel {

    LUGWORM("갯지렁이류"),
    CRUSTACEAN("갑각류"),
    LURE("루어"),
    METAL("메탈지그"),
    EGI("에기"),
    AEJA("애자"),
    SHRIMP("새우"),
    SEAURCHIN("성게류"),
    LIVEBAIT("생미끼"),
    PISCES("어류"),
    SQUID("오징어류"),
    KRILL("크릴");

    private String value;
    FishingLure(String value) {
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