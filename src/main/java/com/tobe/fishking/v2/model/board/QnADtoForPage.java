package com.tobe.fishking.v2.model.board;

import org.springframework.beans.factory.annotation.Value;

public interface QnADtoForPage {
    Long getId();
    @Value("#{@mapperUtility.transEnumQuestionType(target.questionType)}")
    String getQuestionType();
    String getCreatedDate();
    Boolean getReplied();

}
