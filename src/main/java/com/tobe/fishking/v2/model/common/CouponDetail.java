package com.tobe.fishking.v2.model.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.common.CouponType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDetail {
    public Long id;
    private String couponType;
    public String couponCreateCode;
    public String couponName;
    private String exposureStartDate;
    private String exposureEndDate;
    private Integer saleValues;
    private Integer maxIssueCount;
    private String effectiveStartDate;
    private String effectiveEndDate;
    private Integer issueQty;
    private Integer useQty;
    private Boolean isIssue;
    private Boolean isUse;
//    private Integer fromPurchaseAmount;
//    private Integer toPurchaseAmount;
//    private String brfIntroduction;
    private String couponDescription;
    private Long createdBy;
    private Long modifiedBy;
}
