package com.tobe.fishking.v2.model.smartsail;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
public class RiderGoodsListResponse {

    private Long orderId;
    private String shipName;
    private String profileImage;
    private String goodsName;
    private String status;
    private String dateInterval;
    private String date;
    private String orderNumber;
    private String orderName;
    private String orderEmail;
    private Integer personnel;

    @QueryProjection
    public RiderGoodsListResponse(
            Long orderId,
            String shipName,
            String profileImage,
            String goodsName,
            OrderStatus status,
            String date,
            String time,
            String orderNumber,
            String orderName,
            String orderEmail,
            Integer personnel
    ) {
        LocalDateTime times = LocalDateTime.parse(date+time, DateTimeFormatter.ofPattern("yyyy-MM-ddHHmm"));
        LocalDateTime now = LocalDateTime.now();
        this.orderId = orderId;
        this.shipName = shipName;
        this.profileImage = "/resource/" + profileImage.split("/")[1] + "/thumb_" + profileImage.split("/")[2];
        this.goodsName = goodsName;
        this.date = times.format(DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm"));
        this.dateInterval = String.valueOf(ChronoUnit.DAYS.between(times, now));
        this.status = status.equals(OrderStatus.bookCancel) ? "취소완료" : "이용예정";
        this.goodsName = goodsName;
        this.orderNumber = orderNumber;
        this.orderName = orderName;
        this.orderEmail = orderEmail;
        this.personnel = personnel;
    }
}
