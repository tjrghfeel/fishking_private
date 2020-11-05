package com.tobe.fishking.v2.enums.board;

public  enum QuestionType {
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

    public String getValue() {
        return this.value;
    }

}

