package com.tobe.fishking.v2.enums.board;

//import com.tobe.fishking.v2.enums.IEnumModel;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum FilePublish implements IEnumModel {


    ship("선상"),//0
    post("게시판"),//1
    one2one("1:1문의"),//2
    faq("FAQ"),//3
    notice("공지사항"),//4
    fishingBlog("조행기"),//5
    fishingDiary("조행일지"),//6
    comment("조항일지,조행기 댓글"),//7
    fishkingTv("어복TV"),//8
    companyRequest("업체요청"),//9
    profile("프로필"),//10
    review("리뷰"),//11
    seaRocks("갯바위"),//12
    event("이벤트"),//13
    commonComment("댓글"),//14
    banner("배너")//15
    ;


    private String value;
    FilePublish(String value) {
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
