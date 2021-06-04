package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum TideTime   implements IEnumModel   {

    one("1물"),
    two("2물"),
    three("3물"),
    four("4물"),
    five("5물"),
    six("6물"),
    seven("7물"),
    eight("8물"),
    nine("9물"),
    ten("10물"),
    eleven("11물"),
    twelve("12물"),
    thirteen("13물"),
    little("조금");

    private String value;
    TideTime(String value) {
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