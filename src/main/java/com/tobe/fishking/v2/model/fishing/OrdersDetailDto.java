package com.tobe.fishking.v2.model.fishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Places;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.Meridiem;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrdersDetailDto {
    private Long id;
    private  String shipName;
    private String orderStatus;
    private String fishingType;
    private String sigungu;
    private Double distance;
    private String fishingDate;
    private String fishSpecies;
    private String meridiem;
    private String shipStartTime;
    private Integer goodsPrice;
    private Integer personnel;
    private String ordersNum;
    private String memberName;
    private String areaCode;
    private String localNumber;
    private String orderDate;
    private String paymentGroup;
    private Integer totalAmount;
    private Integer discountAmount;
    private Integer paymentAmount;


}
/*
* public interface OrdersDetailDto {
    Long getId();
      String getShipName();
     String getOrderStatus();
     String getFishingType();
     String getSigungu();
     Double getDistance();
     String getFishingDate();
     String getFishSpecies();
     String getMeridiem();
     String getShipStartTime();
     int getGoodsPrice();
     int getPersonnel();
     String getOrdersNum();
     String getNickName();
     String getAreaCode();
     String getLocalNumber();
     String getOrderDate();
     String getPaymentGroup();
     int getTotalAmount();
     int getDiscountAmount();
     int getPaymentAmount();


}
*/