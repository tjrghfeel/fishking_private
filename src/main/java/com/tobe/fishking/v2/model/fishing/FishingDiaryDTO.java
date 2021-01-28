package com.tobe.fishking.v2.model.fishing;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.exception.FileNotFoundException;
import com.tobe.fishking.v2.model.common.ShareStatus;
import com.tobe.fishking.v2.repository.common.FileRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FishingDiaryDTO {

    @Getter
    @Setter
    public static class Create {

   /*     private String content;

        @Builder
        public Create(String content) {
         ...
        }*/
    }

    @Getter
    @Setter
    public static class Update {

        //   private Long   id;
        //   private String content;
        //     ...


    }


    //@NoArgsConstructor
    @Data
    //@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class FishingDiaryDTORespData{


        private Long fishingDiaryId;

        private String title;

        private String contents;

        private String fishingDiaryRepresentUrl;

        private String fishingSpeciesName;

        private String fishingLocation;

        private ShareStatus status;

        private Date createdDate;

        private Long createdById;

        private String createdByNickName;

        private String createdByProfileImage;

        private double writeLongitude;

        private double writeLatitude;




    }


    @Getter
    @Setter
    @Builder
    public static class FishingDiaryDTOResp {
    /*
      id,
      제목
      어종명
      위치
      공유Status (뷰 수, 공유 수, 좋아요 수, 댓글수)
    */

        @ApiModelProperty(value="id")
        private Long fishingDiaryId;

        @ApiModelProperty(value="제목")
        private String title;

        @ApiModelProperty(value="내용")
        private String contents;

        @ApiModelProperty(value="조행기대표사진")
        private String fishingDiaryRepresentUrl;


        @ApiModelProperty(value="어종명")
        private String fishingSpeciesName;

        @ApiModelProperty(value="낚시위치")
        private String fishingLocation;

        @ApiModelProperty(value="좋아요상태")
        private ShareStatus status;

        @ApiModelProperty(value="생성일자")
        private LocalDateTime createDate;

        @ApiModelProperty(value="생성자")
        private Member createdBy;

        @ApiModelProperty(value="경도")
        private double writeLongitude;

        @ApiModelProperty(value="위도")
        private double writeLatitude;

        public static FishingDiaryDTOResp of(FishingDiary fishingDiary) {

            return FishingDiaryDTOResp.builder()
                    .fishingDiaryId(fishingDiary.getId())
                    .title(fishingDiary.getTitle())
                    .contents(fishingDiary.getContents())
                    .createDate(fishingDiary.getCreatedDate())
                    .fishingSpeciesName(fishingDiary.getFishingSpeciesName())
                    .fishingLocation(fishingDiary.getFishingLocation())
                    .status(fishingDiary.getStatus())
                    .createdBy(fishingDiary.getMember())
                    .writeLatitude(fishingDiary.getWriteLatitude()==null?0L:fishingDiary.getWriteLatitude())
                    .writeLongitude(fishingDiary.getWriteLongitude()==null?0L:fishingDiary.getWriteLongitude())
                    .build();
        }



    }

}
