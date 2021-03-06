package com.tobe.fishking.v2.model.board;

import org.springframework.beans.factory.annotation.Value;

public interface NoticeDtoForPage {
    Long getId();
    @Value("#{@mapperUtility.transEnumChannelType(target.channelType)}")
    String getChannelType();
    String getTitle();
    String getDate();
}
