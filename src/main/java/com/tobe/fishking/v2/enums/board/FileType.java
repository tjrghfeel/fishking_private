package com.tobe.fishking.v2.enums.board;

import com.tobe.fishking.v2.enums.IEnumModel;

public enum FileType implements IEnumModel {
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


    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }

}
