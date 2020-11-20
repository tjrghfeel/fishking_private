package com.tobe.fishking.v2.model.common;

import com.tobe.fishking.v2.entity.common.CommonCode;

import java.util.List;

/*마이메뉴 - 내글관리 - 리뷰에서 내 리뷰리스트 가져오기위해 repository로부터 값을 바로받는 interface dto. */
public interface ReviewDTOForList {
    String getProfileImage();
    String getNickName();
    String getFishingDate();
    List<CommonCode> getGoodsFishLure();
    int getMeridiem();
    String getShipStartTime();
    double getTotalAvgByReview();
    double getTasteByReview();
    double getServiceByReview();
    double getCleanByReview();
    String getContent();

    String[] getImageFiles();

}
