package com.tobe.fishking.v2.enums.board;

public enum FilePublish {


    ship("선상"),
    post("게시판"),
    one2one("1:1문의"),
    faq("FAQ"),
    qna("1:1"),
    fishingBlog("조행기"),
    fishingDaily("조행일지"),
    comment("답글"),
    fishkingTv("어복TV");


    private String value;
    FilePublish(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
