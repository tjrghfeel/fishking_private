package com.tobe.fishking.v2.model.fishing;


import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.FishingTechnic;
import com.tobe.fishking.v2.model.common.ShareStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public interface FishingDiaryDtoForPage {

    /*Member에서 가져와야하는 필드들.*/
    String getProfileImage();//프사
    String getNickName();//이름(일반사용자의 넥네임을말하는지, 업주회원의 선상이름을 말하는지 애매.

    /*ship에서 가져와야하는 필드들.*/
    String getAddress();

    /*fishingDiary의 필드들.*/
    Long getId();
    //private Board board;
    //private FilePublish filePublish;
    //private Ship ship;
    Long getMember() ;
    //private Goods goods;
    String getTitle();
    String getContents();
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
    //!!!!!private final List<Member> scrabMembers = new ArrayList<>();
    //private ShareStatus status;
    //public Member createdBy;
    //public Member modifiedBy;

    int getLikeCount();
    int getCommentCount();
    /*!!!!!스크랩수 필요. 물어봐야할게있어서 일단 패쓰. */

    /*파일들*/
    List<String> getFileList();



}
