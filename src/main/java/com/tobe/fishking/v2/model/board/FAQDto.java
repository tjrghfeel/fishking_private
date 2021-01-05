package com.tobe.fishking.v2.model.board;

import org.springframework.beans.factory.annotation.Value;

public interface FAQDto {
    Long getId();
    @Value("#{@mapperUtility.transEnumQuestionType(target.questionType)}")
    String getQuestionType();
    String getTitle();
    String getContents();
}
