package com.tobe.fishking.v2.model.common;

import com.tobe.fishking.v2.entity.common.CommonCode;

import java.util.List;

/*마이메뉴 - 내글관리 - 리뷰에서 내 리뷰리스트 가져오기위해 repository로부터 값을 바로받는 interface dto. */
public interface ReviewDto {
    Long getId();
    Long getGoodsId();
    Long getShipId();
    String getProfileImage();
    String getNickName();
    String getFishingDate();
    String getGoodsFishSpecies();
    String getMeridiem();
    Double getDistance();
    String getFishingTideTime();
    Double getTotalAvgByReview();
    Double getTasteByReview();
    Double getServiceByReview();
    Double getCleanByReview();
    String getContent();

    String getFileList();

}
