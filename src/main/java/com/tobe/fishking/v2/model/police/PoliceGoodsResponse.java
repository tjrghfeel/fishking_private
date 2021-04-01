package com.tobe.fishking.v2.model.police;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class PoliceGoodsResponse {

    private Long shipId;
    private Long goodsId;
    private String shipName;
    private String runningTime;
    private String status;
    private Boolean hasCamera;
    private Long maxPersonnel;
    private Long ridePersonnel;
    private Double latitude;
    private Double longitude;


    @QueryProjection
    public PoliceGoodsResponse(Long shipId,
                               Long goodsId,
                               String shipName,
                               String fishingStartTime,
                               String fishingEndTime,
                               Integer maxPersonnel,
                               Long ridePersonnel,
                               Double latitude,
                               Double longitude,
                               Long cameraCount) {
        LocalTime now = LocalTime.now();
        LocalTime from = LocalTime.of(Integer.parseInt(fishingStartTime.split(":")[0]), Integer.parseInt(fishingStartTime.split(":")[1]));
        LocalTime to = LocalTime.of(Integer.parseInt(fishingEndTime.split(":")[0]), Integer.parseInt(fishingEndTime.split(":")[1]));
        this.shipId = shipId;
        this.goodsId = goodsId;
        this.shipName = shipName;
        this.runningTime = fishingStartTime + " ~ " + fishingEndTime;
        this.maxPersonnel = maxPersonnel.longValue();
        this.ridePersonnel = ridePersonnel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = (now.isAfter(from) && now.isBefore(to)) ? "출항 중" : "출항 전";
        this.hasCamera = (cameraCount != 0);
    }

}
