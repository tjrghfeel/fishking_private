package com.tobe.fishking.v2.enums.board;

//import com.tobe.fishking.v2.enums.IEnumModel;

import com.tobe.fishking.v2.enums.IEnumModel;

public  enum BoardType implements IEnumModel {
    /* sql
     update board set board_type = 'free' ;
     update board set board_type = 'notice' where id  = 6;
     */

    notice("공지사항"),//0
    free("자유게시판"),//1
    qna("QnA"),//2
    faq("FAQ"),//3
    guestbook("방명록"),//4
    blog("블로그"),//5
    companyRequest("업체등록요청"),//6
    profile("프로필"),//7
    fishingBlog("유저조행기"),//8
    fishingDiary("조항일지"),//9
    review("리뷰"),//10
    comment("조항일지, 유저조행기 댓글"),//11
    event("이벤트"),//12
    ship("선박"),//13
    commonComment("공통 댓글");//14

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


