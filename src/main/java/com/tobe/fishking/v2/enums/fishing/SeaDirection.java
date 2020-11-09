package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum SeaDirection implements IEnumModel {

    northWest("서해북부"),
    centralWest("서해중부"),
    southWest("서해남부"),
    westSouth("남해서부"),
    centralSouth("남해중부"),
    eastSouth("남해동부"),
    northEast("동해북부"),
    centralEast("동해중부"),
    southEast("동해남부");

    private String value;

    SeaDirection(String value) {
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
