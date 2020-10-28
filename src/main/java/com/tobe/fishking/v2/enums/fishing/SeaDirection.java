package com.tobe.fishking.v2.enums.fishing;

public enum SeaDirection {

    NorthWest("서해북부"),
    CentrolWest("서해중부"),
    SouthWest("서해남부"),
    WestSouth("남해서부"),
    CentrolSouth("남해중부"),
    EastSouth("남해동부"),
    NorthEast("동해북부"),
    CentrolEast("동해중부"),
    SouthEast("동해남부");

    private String value;

    SeaDirection(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }



}
