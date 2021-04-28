package com.tobe.fishking.v2.model.common;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyCouponDto {
    @NotNull
    private Long couponId;
    private String couponType;
    @Size(max = 50, message = "쿠폰명은 50자 이하이어야 합니다.")
    private String couponName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate exposureStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate exposureEndDate;
    private Integer saleValues;
    private Integer maxIssueCount;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate effectiveStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate effectiveEndDate;
    private Boolean isIssue;
    private Boolean isUse;
    @Size(max = 4000, message = "쿠폰 설명은 4000자 이하이어야 합니다.")
    private String couponDescription;
}
