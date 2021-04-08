package com.tobe.fishking.v2.model.smartsail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodayBoardingResponse {

    private String username;
    private String shipName;
    private String phone;
    private String emergencyPhone;

}
