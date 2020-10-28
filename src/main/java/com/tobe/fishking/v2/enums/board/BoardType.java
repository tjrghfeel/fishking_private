package com.tobe.fishking.v2.enums.board;

public  enum BoardType {
    /* sql
     update board set board_type = 'free' ;
     update board set board_type = 'notice' where id  = 6;
     */

    notice("공지사항"),
    free("자유게시판"),
    qna("QnA"),
    faq("FAQ"),
    guestbook("방명록"),
    blog("블로그");

    private String value;

    BoardType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}

