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
     String getFishingDate();
     boolean getIsClose();
     boolean getIsUse();
     String getShipStartTime();
     float getTotalAmount();
     float getTotalAvgByReview();
    //장소명
     String getPlaceName();
    //장소로부터 대상어종
     String getFishSpicesInfo();
     //섬네일 파일.
     @Value("#{@mapperUtility.transFileUrlArray(target.thumbnailFile, target.filePath)}")
     ArrayList<String> getDownloadThumbnailUrl();

}
