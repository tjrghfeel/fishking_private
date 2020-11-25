package com.tobe.fishking.v2.enums.fishing;

//import com.tobe.fishking.v2.enums.IEnumModel;

public enum OrderStatus /*implements IEnumModel*/ {

    waite(" 접수대기"),
    receipt("접수"),
    payment("결제"),

    book("예약 대기"),
    waitBook("대기자 예약"),
    bookFix("예약 확정"),
    bookCancel("예약 취소"),
    fishingComplete("출조 완료"),
    bookConfirm("예약 완료");


    private String value;
    OrderStatus(String value) {
        this.value = value;
    }

    /*@Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return this.value;
    }*/

}