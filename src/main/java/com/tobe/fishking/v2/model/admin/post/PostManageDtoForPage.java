package com.tobe.fishking.v2.model.admin.post;

import org.springframework.beans.factory.annotation.Value;

//관리자페이지에서 공지사항, FAQ, 1:1문의 검색시 뿌려줄 리스트 dto.
public interface PostManageDtoForPage {
    Long getId();
    Long getBoardId();
    String getBoardName();
    Long getParentId();
    @Value("#{@mapperUtility.transEnumChannelType(target.channelType)}")
    String getChannelType();
    @Value("#{@mapperUtility.transEnumQuestionType(target.questionType)}")
    String getQuestionType();
    String getTitle();
    @Value("#{@mapperUtility.decodeString(target.authorName)}")
    String getAuthorName();
    Long getAuthorId();
    Boolean getIsSecret();
    String getCreatedDate();
    String getModifiedDate();

}