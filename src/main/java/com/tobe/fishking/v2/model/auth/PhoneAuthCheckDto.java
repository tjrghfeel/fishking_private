package com.tobe.fishking.v2.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PhoneAuthCheckDto {
    @NotNull
    private String areaCode;
    @NotNull
    private String localNumber;
    @NotNull
    private String authNum;
}
