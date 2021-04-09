package com.tobe.fishking.v2.model.fishing;


import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface FishingDiaryDtoForPage {

    /*Member에서 가져와야하는 필드들.*/
    @Value("#{@mapperUtility.transDownloadUrl(target.profileImage)}")
    String getProfileImage();//프사
    String getNickName();//이름(일반사용자의 넥네임을말하는지, 업주회원의 선상이름을 말하는지 애매.

    /*ship에서 가져와야하는 필드들.*/
    String getAddress();

    /*fishingDiary의 필드들.*/
    Long getId();
    //private Board board;
    //private FilePublish filePublish;
    Long getShipId();
    Long getMemberId() ;
    //private Goods goods;
    String getTitle();
    String getContents();
    @Value("#{@mapperUtility.transEnumFishingType(target.fishingType)}")
    String getFishingType();
    Boolean getHasLiveCam();
    Boolean getIsLikeTo();
    Boolean getIsScraped();
    //private String location; //사진 찍은 위치 (로마)
    //private String fishingSpeciesName;
    //private String fishingDate;
    //private String fishingTideTime;
    //private double fishLength;
    //private double fishWeight;
    //private FishingTechnic fishingTechnic;
    //private String fishingLure;
    //private String fishingLocation;
    //private String writeLocation;
    //private Long writeLatitude;
    //private Long writeLongitude;
    //private final List<Member> scrabMembers = new ArrayList<>();
    //private ShareStatus status;
    //public Member createdBy;
    //public Member modifiedBy;
    LocalDateTime getCreatedDate();
    @Value("#{@mapperUtility.transEnumFilePublish(target.fishingDiaryType)}")
    String getFishingDiaryType();

    Integer getLikeCount();
    Integer getCommentCount();
    Integer getScrapCount();

    /*파일들*/
    @Value("#{@mapperUtility.transFileUrlArray(target.fileNameList, target.filePathList)}")
    ArrayList<String> getFileList();

    //관리자에서 추가로 사용할 항목들
    String getShipName();
    String getMemberNickName();
    Boolean getIsActive();
    Boolean getIsHidden();
    Integer getAccuseCount();

}
