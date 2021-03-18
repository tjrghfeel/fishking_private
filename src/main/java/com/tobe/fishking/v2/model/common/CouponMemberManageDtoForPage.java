package com.tobe.fishking.v2.model.common;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface CouponMemberManageDtoForPage {
    Long getCouponMemberId();
    String getCouponIssueCode();
    Long getCouponId();
    String getCouponName();
    Long getMemberId();
    String getNickName();
    String getMemberName();
    String getAreaCode();
    String getLocalNumber();
    @Value("#{@mapperUtility.decodeString(target.city)}")
    String getCity();
    @Value("#{@mapperUtility.decodeString(target.dong)}")
    String getDong();
    @Value("#{@mapperUtility.decodeString(target.gu)}")
    String getGu();
    String getSaleValues();
    String getCouponDescription();
    @Value("#{@mapperUtility.transDateString(target.regDate)}")
    String getRegDate();
    @Value("#{@mapperUtility.transDateString(target.effectiveStartDate)}")
    String getEffectiveStartDate();
    @Value("#{@mapperUtility.transDateString(target.effectiveEndDate)}")
    String getEffectiveEndDate();
    String getCouponCreateCode();
    @Value("#{@mapperUtility.transDateString(target.useDate)}")
    String getUseDate();
    Long getOrderId();
    Double getDiscountAmount();
    Double getTotalAmount();
    Double getPaymentAmount();
    Long getGoodsId();
    String getOrderDate();
    @Value("#{@mapperUtility.transEnumOrderStatus(target.orderStatus)}")
    String getOrderStatus();
    String getOrderNumber();
}
