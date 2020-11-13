package com.tobe.fishking.v2.enums.board;

//import com.tobe.fishking.v2.enums.IEnumModel;

public  enum BoardType /*implements IEnumModel*/ {
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


    /*@Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }*/
}


