package com.tobe.fishking.v2.model.admin;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface OrderManageDtoForPage {
    Long getOrderId();
    Long getOrderDetailId();
    @Value("#{@mapperUtility.transDateTimeString(target.orderDate)}")
    String getOrderDate();//
    String getFishingDate();
    Integer getTotalAmount();//
    Integer getDiscountAmount();//
    Integer getPaymentAmount();//
    Boolean getIsPay();
    @Value("#{@mapperUtility.transEnumPayMethod(target.payMethod)}")
    String getPayMethod();//
    @Value("#{@mapperUtility.transEnumOrderStatus(target.orderStatus)}")
    String getOrderStatus();//
    String getOrderNumber();//
    String getTradeNumber();//
//    String getConfirmNumber();
    Long getGoodsId();//
    String getGoodsName();//
    @Value("#{@mapperUtility.transDateString(target.cancelDate)}")
    String getCancelDate();//
    String getCancelNumber();//
    Long getMemberId();//
    String getMemberName();//
    Long getShipId();//
    String getShipName();//
    Long getCompanyId();//
    String getCompanyName();//
    Long getShipownerId();
}
