package com.tobe.fishking.v2.model.fishing;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface OrdersDtoForPage {
    Long getId();
    Long getGoodsId();
    Long getShipId();
    @Value("#{@mapperUtility.transShipThumbnailImg(target.shipImageUrl)}")
    String getShipImageUrl();
    String getShipName();
    @Value("#{@mapperUtility.transEnumFishingType(target.fishingType)}")
    String getFishingType();
    String getSigungu();
//    Double getDistance();
    @Value("#{@mapperUtility.transEnumOrderStatus(target.ordersStatus)}")
    String getOrdersStatus();
//    Integer getDDay();
    @Value("#{@mapperUtility.transFishingDate(target.fishingDate, target.fishingStartTime)}")
    String getFishingDate();
    String getOrdersNum();
    Integer getPersonnel();
    Double getLatitude();
    Double getLongitude();

}
