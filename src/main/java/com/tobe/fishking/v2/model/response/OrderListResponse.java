package com.tobe.fishking.v2.model.response;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.utils.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
public class OrderListResponse {

    private Long id;
    private String shipName;
    private String goodsName;
    private String orderNumber;
    private String fishingDate;
    private String orderDate;
    private Integer reservePersonnel;
    private Integer totalAmount;
    private String status;
    private String profileImage;
    private String username;
    private Boolean isExtra;
    private String reserveComment;

    @QueryProjection
    public OrderListResponse(Long id,
                             String shipName,
                             String goodsName,
                             String orderNumber,
                             String fishingDate,
                             String fishingStartTime,
                             String fishingEndTime,
                             LocalDateTime orderDate,
                             Integer personnel,
                             Integer totalAmount,
                             OrderStatus status,
                             String profileImage,
                             String username,
                             Boolean isExtraRun,
                             String reserveComment) {
        LocalDate date = DateUtils.getDateFromString(fishingDate);
        this.id = id;
        this.shipName = shipName;
        this.goodsName = goodsName;
        this.orderNumber = orderNumber;
        this.fishingDate = date.getYear() + "년 " + date.getMonthValue() + "월 " + date.getDayOfMonth() + "일 " + "(" + date.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREAN) + ")"
                + fishingStartTime + " ~ " + fishingEndTime;
        this.orderDate = orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm:ss"));
        this.reservePersonnel = personnel;
        this.totalAmount = totalAmount;
        this.status = status.getValue();
        this.profileImage = "/resource" + profileImage;
        this.username = username;
        this.isExtra = isExtraRun;
        this.reserveComment = reserveComment;
    }

}
