package com.tobe.fishking.v2.model.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

public interface QnADetailDto {
    Long getId();
    @Value("#{@mapperUtility.transEnumQuestionType(target.questionType)}")
    String getQuestionType();
    Boolean getReplied();
    String getDate();
    String getContents();
    @Value("#{@mapperUtility.transFileUrlArray(target.fileNameList, target.filePathList)}")
    String[] getFileList();

    String getReplyContents();
    @Value("#{@mapperUtility.transFileUrlArray(target.replyFileNameList, target.replyFilePathList)}")
    String[] getReplyFileList();

}
