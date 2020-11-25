package com.tobe.fishking.v2.model.fishing;

import java.time.LocalDateTime;

public interface OrdersDtoForPage {
    Long getId();
    Long getGoodsId();
    String getShipImageUrl();
    String getShipName();
    String getFishingType();
    String getSigungu();
    Double getDistance();
    String getOrdersStatus();
//    Integer getDDay();
    String getFishingDate();
    String getOrdersNum();

}
