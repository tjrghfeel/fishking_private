package com.tobe.fishking.v2.model.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
import com.tobe.fishking.v2.entity.fishing.Orders;
import com.tobe.fishking.v2.enums.common.CouponType;

import javax.persistence.*;
import java.time.LocalDateTime;

/*다운 받은 뒤의 쿠폰인 couponMember를 나타내는 DTO. */
public interface CouponMemberDTO {
    /*coupon_member 필드*/
     Long getId();//not null
     Long getcoupon();
     String getcouponCode();
     Long getMember();
     boolean getIsUse();//사용여부
     LocalDateTime getRegDate();//등록일
     LocalDateTime getUseDate();//사용일
     Long getOrders();
     Long getCreatedBy();//not null
     Long getModifiedBy();//not null

    /*coupon 필드.*/
    //public Long id;//not null
     CouponType getCouponType();//정액인지 정률인지
    //public String couponCode;
     String getCouponName();
    //private String exposureStartDate;
    //private String exposureEndDate;
     double getSaleValues();//할인률
    //private int maxIssueCount;//최대발행수량.
     int getEffectiveDays();//유효일수
    //private double issueQty;//발행수량
    //private double useQty;//사용수량
     boolean getIsIssue();//발행/발행중지
     boolean getIsUsable();//사용/사용중지
    //private double fromPurchaseAmount;
    //private double toPurchaseAmount;
     String getBrfIntroduction();//간략 소개
     String getCouponDescription();//설명,유의사항,제한사항
    //private Member createdBy;//not null
    //private Member modifiedBy;//not null


}
