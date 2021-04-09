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
    private String phone;
    private String emergencyPhone;
    private Integer visitCount;
    private String fingerType;

    @QueryProjection
    public TodayBoardingResponse(
            Long riderId,
            String username,
            String shipName,
            String goodsName,
            String phone,
            String emergencyPhone
    ) {
        this.riderId = riderId;
        this.username = username;
        this.shipName = shipName;
        this.goodsName = goodsName;
        this.phone = phone;
        this.emergencyPhone = emergencyPhone;
    }

}
