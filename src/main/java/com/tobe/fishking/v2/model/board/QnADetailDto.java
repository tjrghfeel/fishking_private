package com.tobe.fishking.v2.model.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public interface QnADetailDto {
    Long getId();
    String getQuestionType();
    Boolean getReplied();
    String getDate();
    String getContents();
    String getFileList();

    String getReplyContents();
    String getReplyFileList();

}
