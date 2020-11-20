package com.tobe.fishking.v2.model.fishing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyMenuPageDTO {

    private String profileImage;//프사
    private String nickName;//닉네임
    private Integer bookingCount;//예약건수
    private Integer couponCount;//쿠폰수

}
