package com.tobe.fishking.v2.enums.common;

//import com.tobe.fishking.v2.enums.IEnumModel;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum AlertType implements IEnumModel {

    member("사용자",""),
    couponExpire("쿠폰 만료 알림","보유하고 계신 쿠폰 사용기간 만료일이 1일 남았습니다. 1일 이후에는 자동 소멸됩니다. "),
    fishingBlog("조행기",""),
    fishingDiary("조황일지",""),
    oneToQuery("1:1문의 답변 완료 알림", "작성하신 문의 글에 답변이 완료되었습니다. "),
    reservationComplete("예약완료", "예약이 완료되었습니다. 어복황제와 함께 똑똑한 바다낚시를 즐겨보세요!"),
    tideTime("물때알림","");


    private String value;
    private String message;
    AlertType(String value,String message) {
        this.value = value;
        this.message=message;
    }

    public String getMessage() {
        return message;
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
