package com.tobe.fishking.v2.model.admin;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.common.Banner;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainBannerDto {
    private Long id;
    private String imageUrl;
    private String linkUrl;
    private Long fileId;

    public MainBannerDto(Banner banner, FileEntity file){
        this.id = banner.getId();
        this.imageUrl = banner.getImagePath();
        this.linkUrl = banner.getLinkURL();
        this.fileId = file.getId();
    }
}
