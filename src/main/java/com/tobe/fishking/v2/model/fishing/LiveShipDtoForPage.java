package com.tobe.fishking.v2.model.fishing;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;


public interface LiveShipDtoForPage {
    Long getShipId();
    String getName();
    @Value("#{@mapperUtility.transEnumFishingType(target.fishingType)}")
    String getFishingType();
    String getAddress();
    Double getDistance();
    Boolean getIsLive();
    Float getPrice();
    //섬네일 파일.
    @Value("#{@mapperUtility.transDownLoadUrl(target.filePath, target.thumbnailFile)}")
    String getDownloadThumbnailUrl();

}
