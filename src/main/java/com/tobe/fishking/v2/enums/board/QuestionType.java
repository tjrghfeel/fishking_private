package com.tobe.fishking.v2.enums.board;

import com.tobe.fishking.v2.enums.IEnumModel;

public  enum QuestionType implements IEnumModel {
    /* sql
     update board set board_type = 'free' ;
     update board set board_type = 'notice' where id  = 6;
     */

    order("예약결제"),
    canccel("취소");

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

