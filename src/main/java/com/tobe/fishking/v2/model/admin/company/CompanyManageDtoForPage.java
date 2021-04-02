package com.tobe.fishking.v2.model.admin.company;

import org.springframework.beans.factory.annotation.Value;

public interface CompanyManageDtoForPage {

    Long getCompanyId();
    Long getMemberId();
    String getMemberName();
    String getCompanyName();
    String getSido();
    String getGungu();
    String getTel();
    String getBizNo();
    String getBank();
    String getAccountNum();
    String getHarbor();
    String getBizNoFileDownloadUrl();
    String getRepresentFileDownloadUrl();
    String getAccountFileDownloadUrl();
    String getOwnerWording();
    Boolean getIsOpen();
    String getSkbAccount();
    String getCompanyAddress();
    Boolean getIsRegistered();
    String getCreatedDate();

}
