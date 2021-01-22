package com.tobe.fishking.v2.model.fishing;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.exception.FileNotFoundException;
import com.tobe.fishking.v2.model.common.ShareStatus;
import com.tobe.fishking.v2.repository.common.FileRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FishingDiaryDTO {

    private static FileRepository fileRepo;

    /*
      id,
      제목
      어종명
      위치
      공유Status (뷰 수, 공유 수, 좋아요 수, 댓글수)
    */

    private Long fishingDiaryId;
    private String title;
    private String contents;
    private LocalDateTime createDate;
    private Ship ship;
    private String shipThumbnailUrl;

    private String fishingDiaaryRepresentUrl;

    private String fishingSpeciesName;
    private String fishingLocation;
    private ShareStatus status;


    public static FishingDiaryDTO of(FishingDiary fishingDiary){

        FileEntity shipFile2 = fileRepo.findFileEntityByAndFilePublish(FilePublish.fishingDiary, true)
                    .orElseThrow(FileNotFoundException::new);

        FileEntity shipFile = fileRepo.findById(fishingDiary.getShip().getId())
                .orElseThrow(FileNotFoundException::new);


        return FishingDiaryDTO.builder()
                .fishingDiaryId(fishingDiary.getId())
                .title(fishingDiary.getTitle())
                .contents(fishingDiary.getContents())
                .createDate(fishingDiary.getCreatedDate())
                .ship(fishingDiary.getShip())
                .shipThumbnailUrl(shipFile.getDownloadUrl() + shipFile.getThumbnailFile())
                .fishingSpeciesName(fishingDiary.getFishingSpeciesName())
                .fishingLocation(fishingDiary.getFishingLocation())
//                .status(fishingDiary.getStatus())
                .build();
    }
}
