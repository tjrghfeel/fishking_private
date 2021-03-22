package com.tobe.fishking.v2.model.board;

import org.springframework.beans.factory.annotation.Value;

public interface FAQDto {
    Long getId();
    @Value("#{@mapperUtility.transEnumQuestionType(target.questionType)}")
    String getQuestionType();
    @Value("#{@mapperUtility.transEnumQuestionTypeKey(target.questionType)}")
    String getQuestionTypeCode();
    String getTitle();
    String getContents();
    String getDate();

    Long getAuthorId();
    Long getCreatedBy();
    Long getModifiedBy();
}
