package com.tobe.fishking.v2.model.fishing;

import java.time.LocalDateTime;

public interface FishingDiaryCommentDtoForPage {
    Long getId();
    String getDependentType();
    String getTitle();
    String getContents();
    LocalDateTime getTime();
    Long getFishingDiaryId();


}
