package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.ErrorCodes;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.TideTime;
import com.tobe.fishking.v2.enums.fishing.*;
import com.tobe.fishking.v2.exception.ApiException;
import com.tobe.fishking.v2.entity.vo.Location;
import com.tobe.fishking.v2.model.common.ShareStatus;
import com.tobe.fishking.v2.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class FishingDiaryDtoGroup {
    FishingDiaryDtoGroup() {
        throw new ApiException(ErrorCodes.PARAMETER_ERROR, "Outer class can not be an instance");
    }


    @ToString
    @Setter
    @Getter
    @ApiModel("글쓰기 저장 요청")
    public static class SaveRequestDto {

        @ApiModelProperty(name = "카테고리")
        private FilePublish filePublish;


        @ApiModelProperty(name = "제목")
        private String title;


        @ApiModelProperty(name = "내용")
        private String contents;

        @ApiModelProperty(name = "어종")
        @NotEmpty
        private Set<FishSpecies> fishSpecies;


        @ApiModelProperty(name = "날짜")
        private LocalDate date;


        @ApiModelProperty(name = "물때")
        private TideTime tideTime;

        @ApiModelProperty(name = "낚시 방법")
        @NotEmpty
        private Set<FishingTechnic> fishingTechnics;

        @ApiModelProperty(name = "미끼")
        @NotEmpty
        private Set<FishingLure> fishingLures;


        @ApiModelProperty(name = "낚시 형태")
        private FishingType fishingType;


        @ApiModelProperty(name = "상품 id")
        private Long goodsId;


        @ApiModelProperty(name = "작성 위치")
        private String writeLocation;


        @ApiModelProperty(name = "위치")
        private Location location;


        @ApiModelProperty(name = "회원 id")
        private Long memberId;


        @ApiModelProperty(name = "선박 id")
        private Long fishingDiaryShipId;


        @ApiModelProperty(name = "게시판 번호")
        private Long boardId;

        @Builder
        public SaveRequestDto(
                FilePublish filePublish
                , String title, String contents, Set<FishSpecies> fishSpecies
                , LocalDate date, TideTime tideTime, Set<FishingTechnic> fishingTechnics
                , Set<FishingLure> fishingLures, FishingType fishingType, Long goodsId
                , String writeLocation, Location location, Long memberId, Long fishingDiaryShipId
                , Long boardId) {
            this.filePublish = filePublish;
            this.title = title;
            this.contents = contents;
            this.fishSpecies = fishSpecies;
            this.date = date;
            this.tideTime = tideTime;
            this.fishingTechnics = fishingTechnics;
            this.fishingLures = fishingLures;
            this.fishingType = fishingType;
            this.goodsId = goodsId;
            this.writeLocation = writeLocation;
            this.location = location;
            this.memberId = memberId;
            this.fishingDiaryShipId = fishingDiaryShipId;
            this.boardId = boardId;
        }

        public FishingDiary toEntity() {

            Member member = Member.builder()
                    .id(memberId)
                    .build();


            FishingDiary entity = FishingDiary.builder()
                    .board(Board.builder()
                            .id(boardId)
                            .build())
                    .filePublish(filePublish)
                    .title(title)
                    .contents(contents)
                    .fishingSpeciesName(fishSpecies.stream()
                            .map(FishSpecies::getKey)
                            .collect(Collectors.joining(","))
                    )
                    .fishingDate(DateUtils.getDateInFormat(date).replace("-", ""))
                    .fishingTideTime(tideTime.getKey())
                    .writeLocation(writeLocation)
                    .writeLongitude(location.getLongitude())
                    .writeLatitude(location.getLatitude())
                    .ship(Ship.builder().id(fishingDiaryShipId)
                            .build())
                    .goods(Goods.builder()
                            .id(goodsId)
                            .build())
                    .member(member)
                    .createdBy(member)
                    .modifiedBy(member)
//                    .status(ShareStatus.builder()
//                            .shareCount(0)
//                            .build())
                    .build();


            Set<FishingDiaryFishingLures> fishingDiaryFishingLures
                    = fishingLures.stream()
                    .map(x -> new FishingDiaryFishingLures(entity, x))
                    .collect(Collectors.toSet());

            Set<FishingDiaryFishingTechnics> fishingDiaryFishingTechnics
                    = fishingTechnics.stream()
                    .map(x -> new FishingDiaryFishingTechnics(entity, x))
                    .collect(Collectors.toSet());

            entity.setFishingDiaryFishingLures(fishingDiaryFishingLures);
            entity.setFishingDiaryFishingTechnics(fishingDiaryFishingTechnics);

            return entity;
        }
    }
}
