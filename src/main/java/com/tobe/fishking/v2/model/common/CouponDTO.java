package com.tobe.fishking.v2.model.common;

import com.tobe.fishking.v2.enums.common.CouponType;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface CouponDTO {

    Long getId();//not null
    @Value("#{@mapperUtility.transEnumCouponType(target.CouponType)}")
    String getCouponType();//정액인지 정률인지
    String getCouponCode();
    String getCouponName();
    //String getExposureStartDate();
    String getExposureEndDate();
    Double getSaleValues();//할인률
    //private int maxIssueCount;//최대발행수량.
    Integer getEffectiveDays();//유효일수
    //private double issueQty;//발행수량
    //private double useQty;//사용수량
    //boolean getIsIssue();//발행/발행중지
    Boolean getIsUsable();//사용/사용중지
    String getBrfIntroduction();//간략 소개
    String getCouponDescription();//설명,유의사항,제한사항
    //private Member createdBy;//not null
    //private Member modifiedBy;//not null
    @Value("#{@mapperUtility.transDownloadUrl(target.couponImage)}")
    String getCouponImage();
}
