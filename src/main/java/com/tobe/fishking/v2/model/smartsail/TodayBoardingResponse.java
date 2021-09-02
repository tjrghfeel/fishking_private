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
    private String birthday;
    private String sex;
    private String addr;
    private Integer visitCount;
    private String fingerType;
    private Integer fingerTypeNum;
    private String status;
    private String reserveComment;

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
            String birthday,
            String sex,
            String residenceAddr,
            Boolean isRide,
            String reserveComment
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
        this.birthday = birthday == null ? "" : birthday;
        this.sex = sex == null ? "" : sex.equals("M") ? "남" : "여";
        this.addr = residenceAddr == null ? "" : residenceAddr;
        this.status = isRide ? "금일 승선 확인" : "확인 전";
        this.reserveComment = reserveComment;
    }

}
