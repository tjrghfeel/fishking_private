package com.tobe.fishking.v2.model.fishing;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface FishingDiaryCommentDtoForPage {
    Long getId();
    @Value("#{@mapperUtility.transEnumDependentType(target.dependentType)}")
    String getDependentType();
    String getTitle();
    String getContents();
    LocalDateTime getTime();
    Long getFishingDiaryId();


}
