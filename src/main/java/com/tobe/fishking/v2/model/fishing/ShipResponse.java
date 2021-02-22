package com.tobe.fishking.v2.model.fishing;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.board.FishingDiarySmallResponse;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ShipResponse {

    private Long id;
    private String fishingType;
    private String name;
    private String address;
    private String sido;
    private String sigungu;
    private String tel;
    private Double weight;
    private Integer boardingPerson;
    private Double latitude;
    private Double longitude;
    private Double avgReview;
    private Double tasteByReview;
    private Double serviceByReview;
    private Double cleanByReview;
    private Integer reviewCount;
    private String profileImage;
    private String liveVideo;
    private Set<GoodsResponse> goods;
    private List<String> services;
    private List<String> facilities;
    private List<String> devices;
    private List<FishingDiarySmallResponse> fishingDiary;
    private Integer fishingDiaryCount;
    private List<FishingDiarySmallResponse> fishingBlog;
    private Integer fishingBlogCount;
    private String ownerWordingTitle;
    private String ownerWording;
    private String noticeTitle;
    private String notice;
    private List<String> events;
    private Boolean liked;

    @QueryProjection
    public ShipResponse(Ship ship) {
        this.id = ship.getId();
        this.fishingType = ship.getFishingType().getKey();
        this.name = ship.getShipName();
        this.address = ship.getAddress();
        this.sido = ship.getSido();
        this.sigungu = ship.getSigungu();
        this.tel = ship.getTel();
        this.weight = ship.getWeight();
        this.boardingPerson = ship.getBoardingPerson();
        this.latitude = ship.getLocation().getLatitude();
        this.longitude = ship.getLocation().getLongitude();
        this.avgReview = ship.getTotalAvgByReview();
        this.tasteByReview = ship.getTasteByReview();
        this.serviceByReview = ship.getServiceByReview();
        this.cleanByReview = ship.getCleanByReview();
        this.reviewCount = ship.getReviewCount();
        this.goods = ship.getGoods().stream().filter(Goods::getIsUse).map(GoodsResponse::new).collect(Collectors.toSet());
        this.services = ship.getServices().stream().map(CommonCode::getCodeName).collect(Collectors.toList());
        this.facilities = ship.getFacilities().stream().map(CommonCode::getCodeName).collect(Collectors.toList());
        this.devices = ship.getDevices().stream().map(CommonCode::getCodeName).collect(Collectors.toList());
        this.ownerWordingTitle = ship.getOwnerWordingTitle();
        this.ownerWording = ship.getOwnerWording();
        this.noticeTitle = ship.getNoticeTitle();
        this.notice = ship.getNotice();
        this.liked = false;
        this.profileImage = "/resource" + ship.getProfileImage();
        this.liveVideo = "";
        this.fishingDiary = new ArrayList<>();
        this.fishingBlog = new ArrayList<>();
    }
}
