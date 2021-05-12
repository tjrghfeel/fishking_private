package com.tobe.fishking.v2.model.admin;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyMainBannerDto {
    private Map<String, Object>[] bannerList;
}
