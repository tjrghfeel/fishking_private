package com.tobe.fishking.v2.model.common;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface AlertListForPage {
    Long getAlertId();
    @Value("#{@mapperUtility.transEnumAlertType(target.alertType)}")
    String getAlertType();
//    private String EntityType;
//    private Long pid;
     LocalDateTime getCreatedDate();
    @Value("#{@mapperUtility.makeAlertMessage(target.alertType,target.content)}")
     String getContent();
    @Value("#{@mapperUtility.transDownloadUrl(target.iconDownloadUrl)}")
     String getIconDownloadUrl();
}
