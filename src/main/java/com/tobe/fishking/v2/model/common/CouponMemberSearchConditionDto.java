package com.tobe.fishking.v2.model.common;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponMemberSearchConditionDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate useDateStart;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate useDateEnd;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate effectiveStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate effectiveEndDate;
    private String couponName;
    private String areaCode;
    private String localNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate exposureStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate exposureEndDate;
    private Boolean isUse;
    private String sort="createdDate";
    private Integer pageCount=10;
}
