package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum RegionMiddle implements IEnumModel {

    Jung_gu("인천 중구"),
    Siheung("경기도 시흥시"),
    Hwaseong("경기도 화성시"),
    Ongjin("인천 옹진군"),
    Pyeongtaek("경기도 평택시"),
    Dangjin("충남 당진시"),
    Taean("충남 태안군"),
    Boryeong("충남 보령군"),
    Seocheon("충남 서천군"),
    Buan("전북 부안군"),
    Gunsan("전북 군산시"),
    Mokpo("전남 목포시");




    private String value;

    RegionMiddle(String value) {
        this.value = value;
    }

    public String getViewName() {
        return value;
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