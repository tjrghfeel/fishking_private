package com.tobe.fishking.v2.model.fishing;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface OrdersDtoForPage {
    Long getId();
    Long getGoodsId();
    @Value("#{@mapperUtility.transDownLoadUrl(target.shipImageFileUrl, target.shipImageFileName)}")
    String getShipImageUrl();
    String getShipName();
    @Value("#{@mapperUtility.transEnumFishingType(target.fishingType)}")
    String getFishingType();
    String getSigungu();
    Double getDistance();
    @Value("#{@mapperUtility.transEnumQuestionType(target.orderStatusType)}")
    String getOrdersStatus();
//    Integer getDDay();
    String getFishingDate();
    String getOrdersNum();

}
