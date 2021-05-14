package com.tobe.fishking.v2.enums.board;

//import com.tobe.fishking.v2.enums.IEnumModel;

import com.tobe.fishking.v2.enums.IEnumModel;

public  enum QuestionType implements IEnumModel {
    /* sql
     update board set board_type = 'free' ;
     update board set board_type = 'notice' where id  = 6;
     */

    member("회원정보"),
    use("이용방법"),
    order("예약/결제"),
    accuse("신고/건의"),
    etc("기타");

    private String value;

    QuestionType(String value) {
        this.value = value;
    }


    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }

}

