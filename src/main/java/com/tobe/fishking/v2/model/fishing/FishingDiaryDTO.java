package com.tobe.fishking.v2.model.fishing;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingTechnic;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
import com.tobe.fishking.v2.model.common.ShareStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FishingDiaryDTO {

    /*
      id,
      제목
      어종명
      위치
      공유Status (뷰 수, 공유 수, 좋아요 수, 댓글수)
    */

    private Long fishingDiaryId;
    private String title;
    private String fishingSpeciesName;
    private String fishingLocation;
    private ShareStatus status;


    public static FishingDiaryDTO of(FishingDiary fishingDiary){
        return FishingDiaryDTO.builder()
                .fishingDiaryId(fishingDiary.getId())
                .title(fishingDiary.getTitle())
                .fishingSpeciesName(fishingDiary.getFishingSpeciesName())
                .fishingLocation(fishingDiary.getFishingLocation())
                .status(fishingDiary.getStatus())
                .build();
    }
}
