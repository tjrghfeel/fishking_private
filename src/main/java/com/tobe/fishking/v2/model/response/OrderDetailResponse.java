package com.tobe.fishking.v2.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.enums.fishing.PayMethod;
import com.tobe.fishking.v2.utils.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;

import java.time.LocalDateTime;
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
    private String reserveComment;

    private List<Map<String, Object>> rideList;
    private List<String> shipPositions;
    private List<String> reservePositions;
    private Double shipType;

    private String orderDate;
    private String payMethod;
    private Integer payTotalAmount;
    private Integer discountAmount;

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
                               String reserveComment,
                               LocalDateTime orderDate,
                               PayMethod payMethod,
                               Integer payTotalAmount,
                               Integer discountAmount,
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
        this.reserveComment = reserveComment;
        this.orderDate = DateUtils.getDateTimeInFormat(orderDate);
        this.payMethod = payMethod.getValue();
        this.payTotalAmount = payTotalAmount;
        this.discountAmount = discountAmount;
        this.rideList = new ArrayList<>();
        this.shipPositions = Arrays.asList(shipPositions.split(",").clone());
        this.reservePositions = Arrays.asList(reservePositions.split(",").clone());
        this.cancelDate = "";
        this.refundAmount = null;
        this.shipType = weight;
    }

}
