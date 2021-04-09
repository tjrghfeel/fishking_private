package com.tobe.fishking.v2.enums.fishing;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum OrderStatus   implements IEnumModel   {

    book("예약 대기"),
    bookRunning("예약 진행중"),//1
    waitBook("대기자 예약"),//2
    bookFix("예약 확정"),//3
    bookCancel("예약 취소"),//4
    fishingComplete("출조 완료"),//5
    bookConfirm("예약 완료");//6


    private String value;
    OrderStatus(String value) {
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