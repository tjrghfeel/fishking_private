package com.tobe.fishking.v2.model.fishing;

import org.springframework.beans.factory.annotation.Value;

public interface ShipListForWriteFishingDiary {
    Long getShipId();
    String getName();
    @Value("#{@mapperUtility.transEnumFishingType(target.fishingType)}")
    String getFishingType();
    String getAddress();
    String getDistance();
    @Value("#{@mapperUtility.transDownLoadUrl(target.fileUrl, target.fileName)}")
    String getThumbnailUrl();
    @Value("#{@mapperUtility.transIntToBoolean(target.isVideo)}")
    Boolean getIsVideo();

//    Boolean getIsLive();
//    String getVideoTime();
//    String getFishingType();



}
