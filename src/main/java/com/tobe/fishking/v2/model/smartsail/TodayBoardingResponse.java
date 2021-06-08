package com.tobe.fishking.v2.model.smartsail;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodayBoardingResponse {

    private Long riderId;
    private String username;
    private String shipName;
    private String goodsName;
    private String fishingDate;
    private String fishingStartTime;
    private String fishingEndTime;
    private String phone;
    private String emergencyPhone;
    private Integer visitCount;
    private String fingerType;
    private Integer fingerTypeNum;
    private String status;

    @QueryProjection
    public TodayBoardingResponse(
            Long riderId,
            String username,
            String shipName,
            String goodsName,
            String fishingDate,
            String fishingStartTime,
            String fishingEndTime,
            String phone,
            String emergencyPhone,
            Boolean isRide
    ) {
        this.riderId = riderId;
        this.username = username;
        this.shipName = shipName;
        this.goodsName = goodsName;
        this.fishingDate = fishingDate;
        this.fishingStartTime = fishingStartTime;
        this.fishingEndTime = fishingEndTime;
        this.phone = phone;
        this.emergencyPhone = emergencyPhone;
        this.status = isRide ? "금일 승선 확인" : "확인 전";
    }

}
