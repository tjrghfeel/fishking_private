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
    Long getAuthorId();
    String getAuthorName();
    String getReturnType();
    String getReturnAddress();
    Long getCreatedBy();
    Long getModifiedBy();
    @Value("#{@mapperUtility.transFileUrlArray(target.fileUrlList)}")
    String[] getFileList();
    @Value("#{@mapperUtility.transFileIdArray(target.fileIdList)}")
    Long[] getFileIdList();//

    Long getReplyId();//
    String getReplyContents();
    String getReplyDate();
    Long getReplyAuthorId();
    String getReplyAuthorName();//
    Long getReplyCreatedBy();
    Long getReplyModifiedBy();
    @Value("#{@mapperUtility.transFileUrlArray(target.replyFileUrlList)}")
    String[] getReplyFileList();
    @Value("#{@mapperUtility.transFileIdArray(target.replyFileIdList)}")
    Long[] getReplyFileIdList();//

}
