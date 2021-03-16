package com.tobe.fishking.v2.model.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.common.CouponType;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

public interface CouponManageDtoForPage {
     Long getCouponId();
     @Value("#{@mapperUtility.transEnumCouponType(target.CouponType)}")
     String getCouponType();
     String getCouponCreateCode();
     String getCouponName();
     @Value("#{@mapperUtility.transDateString(target.exposureStartDate)}")
     String getExposureStartDate();
     @Value("#{@mapperUtility.transDateString(target.exposureEndDate)}")
     String getExposureEndDate();
     Integer getSaleValues();
     Integer getMaxIssueCount();
     @Value("#{@mapperUtility.transDateString(target.effectiveStartDate)}")
     String getEffectiveStartDate();
     @Value("#{@mapperUtility.transDateString(target.effectiveEndDate)}")
     String getEffectiveEndDate();
     Integer getIssueQty();
     Integer getUseQty();
     Boolean getIsIssue();
     Boolean getIsUse();
//     Integer getFromPurchaseAmount();
//     Integer getToPurchaseAmount();
//     String getBrfIntroduction();
     String getCouponDescription();
     Long getCreatedBy();
     Long getModifiedBy();

}
