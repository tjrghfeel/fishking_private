package com.tobe.fishking.v2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;


public interface TakeResponse {
    Long getId();
    String getName();
    String getFishSpicesInfo();
    Integer getFishSpicesCount();
    @Value("#{@mapperUtility.transEnumFishingType(target.fishingType)}")
    String getFishingType();
    String getAddress();
    Double getDistance();
    Integer getPrice();

     /*String getFishingDate();
     boolean getIsClose();
     boolean getIsUse();
     String getShipStartTime();
    //장소명
     String getPlaceName();*/

     //섬네일 파일.
     @Value("#{@mapperUtility.transDownLoadUrl(target.filePath, target.thumbnailFile)}")
     String getDownloadThumbnailUrl();

}
