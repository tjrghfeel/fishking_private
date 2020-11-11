package com.tobe.fishking.v2.model.fishing;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingTechnic;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
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
    */

    private Long fishingDiaryId;
    private String title;
    private String fishingSpeciesName;
    private String fishingLocation;

    public static FishingDiaryDTO of(FishingDiary fishingDiary){
        return FishingDiaryDTO.builder()
                .fishingDiaryId(fishingDiary.getId())
                .title(fishingDiary.getTitle())
                .fishingSpeciesName(fishingDiary.getFishingSpeciesName())
                .fishingLocation(fishingDiary.getFishingLocation())
                .build();
    }
}
