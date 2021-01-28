package com.tobe.fishking.v2.model.fishing;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingDiaryDetailDto {
    private Long authorId;
    private Long fishingDiaryId;
    private Long shipId;
    private String nickName;
    private String profileImage;
    private Boolean isLive;//!!!!!현장실시간 여부버튼 어케할지 결정후 수정.
    private String fishingType;
    private String title;
    private LocalDateTime createdDate;//!!!!!데이터 타입 확인.
    private String fishingSpecies;
    private String fishingDate;
    private String tide;
    private String fishingLure;
    private String fishingTechnic;
    private String content;
    private ArrayList<String> imageUrlList;
    private String videoUrl;
    private Boolean isLikeTo;
    private Boolean isScraped;
    private int likeCount;
    private int commentCount;
    private int scrapCount;
    private int viewCount;
}
