package com.tobe.fishking.v2.enums.board;

//import com.tobe.fishking.v2.enums.IEnumModel;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum FilePublish implements IEnumModel {


    ship("선상"),
    post("게시판"),
    one2one("1:1문의"),
    faq("FAQ"),
    notice("공지사항"),
    fishingBlog("조행기"),
    fishingDiary("조행일지"),
    comment("답글"),
    fishkingTv("어복TV"),
    companyRequest("업체요청"),
    profile("프로필"),
    review("리뷰");


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
