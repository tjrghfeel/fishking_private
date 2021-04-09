package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum EntityType implements IEnumModel {
    member(""),
    post(""),//1
    alert(""),//2
    codeGroup(""),//3
    commonCode(""),//4
    coupon(""),//5
    loveTo(""),//6
    review(""),//7
    take(""),//8
    company(""),//9
    couponMember(""),//10
    fishingDiary(""),//11
    fishingDiaryComment(""),//12
    goods(""),//13
    orderDetails(""),//14
    orders(""),//15
    phoneAuth(""),//16
    ship(""),//17
    fileEntity(""),//18
    tblSubmitQueue(""),//19
    observerCode("");//20


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
