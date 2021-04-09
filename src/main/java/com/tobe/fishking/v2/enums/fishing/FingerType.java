package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum FingerType implements IEnumModel {

    rightThumb("오른손 엄지"),
    rightIndex("오른손 검지"),
    rightMiddle("오른손 중지"),
    rightRing("오른손 약지"),
    rightLittle("오른손 새끼"),
    leftThumb("왼손 엄지"),
    leftIndex("왼손 검지"),
    leftMiddle("왼손 중지"),
    leftRing("왼손 약지"),
    leftLittle("왼손 새끼");


    private String value;
    FingerType(String value) {
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
