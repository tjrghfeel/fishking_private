package com.tobe.fishking.v2.model.police;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.enums.auth.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiderResponse {

    private String name;
    private String birthdate;
    private String phone;
    private String fingerPrint;

    @QueryProjection
    public RiderResponse(String name,
                         String birthdate,
                         String phone,
                         Boolean fingerprint) {
        this.name = name;
        this.birthdate = birthdate;
        this.phone = phone;
        this.fingerPrint = fingerprint ? "지문완료" : "지문미등록";
    }
}
