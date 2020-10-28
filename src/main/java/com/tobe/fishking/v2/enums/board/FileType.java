package com.tobe.fishking.v2.enums.board;

public enum FileType {
        /* sql
     update board set board_type = 'free' ;
     update board set board_type = 'notice' where id  = 6;
     */
    image("이미지"),
    txt("텍스트"),
    attachments("첨부파일"),
    video("동영상");

    private String value;

    FileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
