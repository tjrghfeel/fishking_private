package com.tobe.fishking.v2.enums.common;

//import com.tobe.fishking.v2.enums.IEnumModel;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum AdType implements IEnumModel {

    MAIN_LIVE("메인 실시간"),
    MAIN_SHIP("메인 출조"),
    MAIN_AD("메인 광고"),
    SEARCH_AD("통합검색 광고"),
    SHIP_PREMIUM_AD("선상 프리미엄 광고"),
    SHIP_AD("선상 광고"),
    ROCK_PREMIUM_AD("갯바위 프리미엄 광고"),
    ROCK_AD("갯바위 광고"),
    LIVE("어복TV 라이브");

    private String value;
    AdType(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }

}
