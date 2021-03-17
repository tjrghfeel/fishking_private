package com.tobe.fishking.v2.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.tobe.fishking.v2.addon.CommonAddon.addDashToPhoneNum;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetailResponse {

    private Long id;
    private String name;
    private String fishingStartTime;
    private Integer amount;
    private String orderNumber;
    private String status;

    private String reserveName;
    private String reservePhone;

    private List<Map<String, Object>> rideList;
    private List<String> shipPositions;
    private List<String> reservePositions;
    private Double shipType;

    private String cancelDate;
    private Integer refundAmount;

    @QueryProjection
    public OrderDetailResponse(Long id,
                               String name,
                               String fishingStartTime,
                               Integer amount,
                               String orderNumber,
                               OrderStatus status,
                               String reserveName,
                               String reservePhone,
                               String shipPositions,
                               String reservePositions,
                               Double weight) {
        this.id = id;
        this.name = name;
        this.fishingStartTime = fishingStartTime;
        this.amount = amount;
        this.orderNumber = orderNumber;
        this.status = status.getValue();
        this.reserveName = reserveName;
        this.reservePhone = addDashToPhoneNum(reservePhone);
        this.rideList = new ArrayList<>();
        this.shipPositions = Arrays.asList(shipPositions.split(",").clone());
        this.reservePositions = Arrays.asList(reservePositions.split(",").clone());
        this.cancelDate = "";
        this.refundAmount = null;
        this.shipType = weight;
    }

}
