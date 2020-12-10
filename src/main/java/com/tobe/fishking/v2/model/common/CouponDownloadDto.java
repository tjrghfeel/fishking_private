package com.tobe.fishking.v2.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CouponDownloadDto {
    private Long memberId;
    private Long couponId;
}
