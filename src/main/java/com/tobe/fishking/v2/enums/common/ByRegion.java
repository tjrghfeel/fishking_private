package com.tobe.fishking.v2.enums.common;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum ByRegion implements IEnumModel {

    NorthWestSEA("서해북부"),
    CentralWestSeq("서해중부"),
    SouthWestSea("서해남부"),
    SouthSea("남해서부"),
    CentralSouthSea("남해중부"),
    SouthSeaEas("남해동부"),
    NorthEastSea("동해북부"),
    CentralEastSea ("동해중부"),
    SouthEastSea ("동해남부");

    private String value;
    ByRegion(String value) {
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