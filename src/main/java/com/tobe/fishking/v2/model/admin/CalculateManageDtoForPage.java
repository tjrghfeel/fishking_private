package com.tobe.fishking.v2.model.admin;


import org.springframework.beans.factory.annotation.Value;

public interface CalculateManageDtoForPage {

//    Long getCalculateId();
    Long getShipId();
    String getShipName();
    Long getCompanyId();
    String getCompanyName();
    String getYear();
    String getMonth();
    Long getModifiedById();
    Integer getCancelAmount();
    Integer getAmount();
    Integer getTotalAmount();
    @Value("#{@mapperUtility.transIntToBoolean(target.isCalculated)}")
    Boolean getIsCalculated();
}
