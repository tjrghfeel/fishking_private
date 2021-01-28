package com.tobe.fishking.v2.enums.board;

//import com.tobe.fishking.v2.enums.IEnumModel;

import com.tobe.fishking.v2.enums.IEnumModel;

public  enum BoardType implements IEnumModel {
    /* sql
     update board set board_type = 'free' ;
     update board set board_type = 'notice' where id  = 6;
     */

    notice("공지사항"),
    free("자유게시판"),
    qna("QnA"),
    faq("FAQ"),
    guestbook("방명록"),
    blog("블로그"),
    companyRequest("업체등록요청"),
    profile("프로필"),
    fishingBlog("유저조행기"),
    fishingDiary("조항일지"),
    review("리뷰"),
    comment("댓글");

    private String value;

    BoardType(String value) {
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


