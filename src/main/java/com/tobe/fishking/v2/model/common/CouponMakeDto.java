package com.tobe.fishking.v2.model.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponMakeDto {
    @NotNull
    @Size(max = 50, message = "쿠폰명은 50자 이하이어야 합니다.")
    private String name;
    @NotNull
    private Integer saleValue;
    @NotNull
    @Size(max = 200, message = "쿠폰 설명은 200자 이하이어야 합니다.")
    private String description;
    @NotNull
    private Integer maxIssueCount;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate issueStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate issueEndDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate effectiveStartDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate effectiveEndDate;
}
