package com.tobe.fishking.v2.enums.common;

//import com.tobe.fishking.v2.enums.IEnumModel;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum AlertType implements IEnumModel {

    member("사용자"),
    coupon("쿠폰 "),
    fishingDiary("조행기"),
    fishingDaily("조황일지"),
    oneToQuery("1:1문의"),
    reservationComplete("예약완료");


    private String value;
    AlertType(String value) {
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
