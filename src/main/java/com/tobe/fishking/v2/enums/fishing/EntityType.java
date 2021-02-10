package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum EntityType implements IEnumModel {
    member(""),
    post(""),
    alert(""),
    codeGroup(""),
    commonCode(""),
    coupon(""),
    loveTo(""),
    review(""),
    take(""),
    company(""),
    couponMember(""),
    fishingDiary(""),
    fishingDiaryComment(""),
    goods(""),
    orderDetails(""),
    orders(""),
    phoneAuth(""),
    ship(""),
    fileEntity(""),
    tblSubmitQueue(""),
    observerCode("");


    private String value;
    EntityType(String value) {
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
