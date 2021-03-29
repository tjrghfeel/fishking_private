package com.tobe.fishking.v2.model.common;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;


public interface EventDtoForPage {
    Long getEventId();
    Long getShipId();
    @Value("#{@mapperUtility.transDownloadUrl(target.imageUrl)}")
    String getImageUrl();
    String getEventTitle();
    String getContent();
    String getShipName();
    String getStartDay();
    String getEndDay();
    Boolean getIsActive();

}
