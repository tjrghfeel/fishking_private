package com.tobe.fishking.v2.model.fishing;


import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface CameraPointDetailDtoForPage {
    Long getId();
    String getName();//
    String getSido();
    String getGungu();
    String getAddress();
    Double getLatitude();
    Double getLongitude();
    String getAdtId();
    String getAdtPw();
    @Value("#{@mapperUtility.transDownloadUrl(target.imgUrl)}")
    String getImgUrl();
    @Value("#{@mapperUtility.transShipThumbnailImg(target.imgUrl)}")
    String getThumbUrl();
//    Boolean getIsDeleted();
    Boolean getIsActive();


    /*파일들*/
//    @Value("#{@mapperUtility.transFileUrlArray(target.fileNameList, target.filePathList)}")
//    ArrayList<String> getFileList();
//    Long getVideoId();
//
//    //관리자에서 추가로 사용할 항목들
//    String getShipName();
//    String getMemberNickName();
//    Boolean getIsActive();
//    Boolean getIsHidden();
//    Integer getAccuseCount();

}

