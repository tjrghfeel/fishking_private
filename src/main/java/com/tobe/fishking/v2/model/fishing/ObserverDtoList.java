package com.tobe.fishking.v2.model.fishing;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;


public interface ObserverDtoList {
    Long getObserverId();
     String getObserverName();
    @Value("#{@mapperUtility.transIntToBoolean(target.isAlerted)}")
     Boolean getIsAlerted();


}
