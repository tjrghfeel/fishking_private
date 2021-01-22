package com.tobe.fishking.v2.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneAuthForModifyPhoneNumber {
    private String memberName;
    private String areaCode;
    private String localNumber;
}
