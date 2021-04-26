package com.tobe.fishking.v2.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoSettingDtoForPage {
    private String code;
    private String codeName;
    private Boolean isSet;
}
