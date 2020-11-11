package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Places;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ParamsGoods {

    /*@NotEmpty
    @Size(min = 2, max = 10)
    @ApiModelProperty(value = "상품ID", required = true)
    private Long goodsId;
    */
    @NotEmpty
    @Size(min = 2, max = 100)
    @ApiModelProperty(value = "상품명", required = true)
    private String goodsName;

    @NotNull
    @ApiModelProperty(notes = "구분 :선상/갯바위")
    private FishingType fishingType;

    @NotEmpty
    @Size(min = 2, max = 8)
    @ApiModelProperty(value = "일자", required = true)
    private String fishingDate;

    @Size(min = 2, max = 4)
    @ApiModelProperty(value = "배출발시간", required = true)
    private String shipStartTime;

    @NotNull
    @ApiModelProperty(notes = "오전오후구분")
    private Meridiem meridiem;

    @Size(min = 2, max = 4)
    @ApiModelProperty(value = "낚시시작시간", required = true)
    private String fishingStartTime;

    @Size(min = 2, max = 4)
    @ApiModelProperty(value = "낚시종료시간", required = true)
    private String fishingEndTime;

    @Size(min = 2, max = 10)
    @ApiModelProperty(value = "총액", required = true)
    private double totalAmount;

    @Size(min = 1, max = 2)
    @ApiModelProperty(value = " '정원(min)'", required = true)
    private int minPersonnel;

    @Size(min = 1, max = 2)
    @ApiModelProperty(value = " '정원(max)'", required = true)
    private int maxPersonnel;

    @ApiModelProperty(notes = "마감여부", example = "false", required = false)
    private boolean isClose;

    @ApiModelProperty(notes = "초보가능여부", example = "true", required = true)
    private boolean isBeginnerPossible;

    @Size(min = 1, max = 3)
    @ApiModelProperty(value = "예약인수", required = true)
    private double reservationPersonnel;

    @Size(min = 1, max = 3)
    @ApiModelProperty(value = "대기인수", required = true)
    private double waitingPersonnel;

    @ApiModelProperty(notes = "상태(노출여부)", example = "false", required = false)
    private boolean isVisible;

    @Size(min = 1, max = 3)
    @ApiModelProperty(value = "전체평균평점", required = true)
    private double totalAvgByReview;

    @Size(min = 1, max = 3)
    @ApiModelProperty(value = "손맛평점", required = true)
    private double tasteByReview;

    @Size(min = 1, max = 3)
    @ApiModelProperty(value = "서비스평점", required = true)
    private double serviceByReview;

    @Size(min = 1, max = 3)
    @ApiModelProperty(value = "청결도평점", required = true)
    private double cleanByReview;

    @Size(min = 1, max = 3)
    @ApiModelProperty(value = "적립포인트", required = true)
    private double accumulatePoint;

    @Size(min = 2, max = 200)
    @ApiModelProperty(value = "공지사항", required = true)
    private String notice;

    @Size(min = 2, max = 200)
    @ApiModelProperty(value = "구매", required = true)
    private String onSitePurchase;

    @Size(min = 2, max = 1000)
    @ApiModelProperty(value = "기타", required = true)
    private String etc;

    @ApiModelProperty(notes = "사용여부", example = "false", required = true)
    private boolean isUse;

    @ApiModelProperty(notes = "주요어종")
    private List<String> fishSpecies;

    @ApiModelProperty(notes = "낚시기법")
    private List<String> fishingLures ;









}
