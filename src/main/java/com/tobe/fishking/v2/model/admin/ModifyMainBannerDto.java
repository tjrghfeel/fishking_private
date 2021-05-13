package com.tobe.fishking.v2.model.admin;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModifyMainBannerDto {
    private Long id;
    private Long fileId;
    private String imageUrl;
    private String linkUrl;
}
