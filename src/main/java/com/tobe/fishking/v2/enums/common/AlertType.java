package com.tobe.fishking.v2.enums.common;

//import com.tobe.fishking.v2.enums.IEnumModel;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum AlertType implements IEnumModel {

    pushAll("관리자",""),//0
    couponExpire("쿠폰 만료 알림","보유하고 계신 쿠폰 사용기간 만료일이 1일 남았습니다. 1일 이후에는 자동 소멸됩니다. "),//1
    fishingBlog("조행기",""),//2
    fishingDiary("글알림",""),//3
    oneToQuery("1:1문의 답변 완료 알림", "작성하신 문의 글에 답변이 완료되었습니다. "),//4
    reservationComplete("예약확정", "예약 확정되었습니다. 어복황제와 함께 똑똑한 바다낚시를 즐겨보세요!"),//5
    reservationCancel("예약취소", "예약 취소되었습니다. 다음 기회에 함께하겠습니다. "),//6
    tideLevel("간조,만조 알림",""),//7
    tide("물때 알림",""),//8
    reservationCompleteCompany("고객예약", "고객이 예약하였습니다. "),//9
    reservationCancelCompany("고객취소", "고객이 예약 취소했습니다. "),//10
    systemConfirm("시스템 예약 확정", "시스템 예약 확정. "),//11
    reservationConfirm("예약 승인", "접수 되었습니다. ")//12
//    pushAll("전체 알람","")//13
    ;





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
