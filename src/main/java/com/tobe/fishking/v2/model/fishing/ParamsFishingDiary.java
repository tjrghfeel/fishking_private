package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingTechnic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ParamsFishingDiary {

    @NotEmpty
    @Size(min = 2, max = 200)
    @ApiModelProperty(value = "제목", required = true)
    private String title;
    @Size(min = 2, max = 1000)
    @ApiModelProperty(value = "내용", required = true)
    private String contents;
    @Size(min = 2, max = 200)
    @ApiModelProperty(value = "사진 찍은 위치", required = true)
    private String location;
    @Size(min = 2, max = 200)
    @ApiModelProperty(value = "어종명", required = true)
    private String fishingSpeciesName;
    @Size(min = 2, max = 8)  //'yyyyMMdd'
    @ApiModelProperty(value = "날짜", required = true)
    private String fishingDate;
    @Size(min = 2, max = 100)
    @ApiModelProperty(value = "물때", required = true)
    private String fishingTideTime;
    @ApiModelProperty(value = "물고기길이", required = true)
    private double fishLength;
    @ApiModelProperty(value = "물고기무게", required = true)
    private double fishWeight;
    @ApiModelProperty(value = "낚시기법", required = true)
    private FishingTechnic fishingTechnic;
    @Size(min = 2, max = 200)
    @ApiModelProperty(value = "미끼", required = true)
    private String fishingLure;
    @Size(min = 2, max = 50)
    @ApiModelProperty(value = "낚시장소", required = true)
    private String fishingLocation;
    @Size(min = 2, max = 200)
    @ApiModelProperty(value = "작성장소", required = true)
    private String writeLocation;
    @ApiModelProperty(value = "작성장소-위도", required = true)
    @Column(columnDefinition = "float   comment '위도'  ")
    private Long writeLatitude;
    @ApiModelProperty(value = "작성장소-경도", required = true)
    @Column(columnDefinition = "float   comment '경도'  ")
    private Long writelongitude;
    @ApiModelProperty(value = "스크랩 사용자", required = true)
    private final List<Member> scrabMembers = new ArrayList<>();


}
