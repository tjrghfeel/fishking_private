package com.tobe.fishking.v2.model.admin.member;

import org.springframework.beans.factory.annotation.Value;

public interface MemberManageDtoForPage {

    Long getId();
    String getUid();
    String getMemberName();
    String getNickName();
//    String getPassword();
//    @Value("#{@mapperUtility.decodeString(target.email)}")
    String getEmail();
    Integer getGender();
    @Value("#{@mapperUtility.transEnumRoles(target.roles)}")
    String getRoles();
    String getProfileImage();
    Boolean getIsActive();
    String getCertifiedNo();
    Boolean getIsCertified();
    String getJoinDt();
    @Value("#{@mapperUtility.transEnumSnsType(target.snsType)}")
    String getSnsType();
//    @Value("#{@mapperUtility.decodeString(target.snsId)}")
    String getSnsId();
    String getStatusMessage();
    @Value("#{@mapperUtility.decodeString(target.city)}")
    String getCity();
    @Value("#{@mapperUtility.decodeString(target.gu)}")
    String getGu();
    @Value("#{@mapperUtility.decodeString(target.dong)}")
    String getDong();
    String getAreaCode();
    String getLocalNumber();


}
