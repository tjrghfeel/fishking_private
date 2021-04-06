package com.tobe.fishking.v2.model.common;

import com.tobe.fishking.v2.entity.auth.Member;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponSearchConditionDto {
    private Long couponId;
    private String couponType;
    public String couponCreateCode;
    public String couponName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate exposureStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate exposureEndDate;
    private Integer saleValuesStart;
    private Integer saleValuesEnd;
    private Integer maxIssueCount;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate effectiveStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate effectiveEndDate;
    private Integer issueQtyStart;
    private Integer issueQtyEnd;
    private Integer useQtyStart;
    private Integer useQtyEnd;
    private Boolean isIssue;
    private Boolean isUse;
//    private Integer fromPurchaseAmount;
//    private Integer toPurchaseAmount;
//    private String brfIntroduction;
    private String couponDescription;
    private Long createdBy;
    private Long modifiedBy;
    private String sort="createdDate";
    private Integer pageCount=10;
}
