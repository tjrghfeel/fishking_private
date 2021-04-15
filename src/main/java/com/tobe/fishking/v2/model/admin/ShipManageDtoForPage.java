package com.tobe.fishking.v2.model.admin;

import org.springframework.beans.factory.annotation.Value;

public interface ShipManageDtoForPage {
    Long getShipId();
    String getShipName();
    @Value("#{@mapperUtility.transEnumFishingType(target.fishingType)}")
    String getFishingType();
    String getAddress();
    String getCompanyPhoneNumber();
    String getFishSpecies();
    String getServices();
    String getFacilities();
    String getDevices();
    @Value("#{@mapperUtility.transIntToBoolean(target.isLive)}")
    Boolean getIsLive();
    String getCompanyName();
    Double getTotalAvgByReview();
    Boolean getIsActive();
    Boolean getDepartStatus();
    Long getMemberId();
}
