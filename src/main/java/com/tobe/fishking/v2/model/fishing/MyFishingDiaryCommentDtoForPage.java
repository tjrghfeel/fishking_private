package com.tobe.fishking.v2.model.fishing;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface MyFishingDiaryCommentDtoForPage {
    Long getId();
    @Value("#{@mapperUtility.transEnumDependentType(target.dependentType)}")
    String getDependentType();
    @Value("#{@mapperUtility.transEnumFishingType(target.fishingType)}")
    String getFishingType();
    String getTitle();
    String getContents();
    LocalDateTime getTime();
    Long getFishingDiaryId();


}
