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
    private String fishingDiaryType;
    private Long shipId;
    private String shipAddress;
    private String shipImageUrl;
    private String address;
    private Double latitude;
    private Double longitude;
    private String nickName;
    private String profileImage;
    private Boolean isLive;//!!!!!현장실시간 여부버튼 어케할지 결정후 수정.
    private String fishingType;
    private String fishingTypeCode;
    private String title;
    private LocalDateTime createdDate;//!!!!!데이터 타입 확인.
    private String fishingSpecies;
    private ArrayList<String> fishingSpeciesCodeList;
    private String fishingDate;
    private String tide;
    private String tideCode;
    private String fishingLure;
    private ArrayList<String> fishingLureCodeList;
    private String fishingTechnic;
    private ArrayList<String> fishingTechnicCodeList;
    private String content;
    private ArrayList<String> imageUrlList;
    private ArrayList<Long> imageIdList;
    private String videoUrl;
    private Long videoId;
    private Boolean isLikeTo;
    private Boolean isScraped;
    private int likeCount;
    private int commentCount;
    private int scrapCount;
    private int viewCount;
    private Boolean isMine;
}
