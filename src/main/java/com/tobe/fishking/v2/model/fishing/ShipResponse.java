package com.tobe.fishking.v2.model.fishing;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ShipResponse {

    private Long id;
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
    private Set<GoodsResponse> goods;
    private List<String> services;
    private List<String> facilities;
    private List<String> devices;
    private List<FishingDiaryDTO.FishingDiaryDTOResp> fishingDiary;
    private Integer fishingDiaryCount;
    private List<FishingDiaryDTO.FishingDiaryDTOResp> fishingBlog;
    private Integer fishingBlogCount;
    private String ownerWordingTitle;
    private String ownerWording;
    private String noticeTitle;
    private String notice;
    private List<String> events;

    @QueryProjection
    public ShipResponse(Ship ship) {
        this.id = ship.getId();
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
        this.goods = ship.getGoods().stream().map(GoodsResponse::new).collect(Collectors.toSet());
        this.services = ship.getServices().stream().map(CommonCode::getCodeName).collect(Collectors.toList());
        this.facilities = ship.getFacilities().stream().map(CommonCode::getCodeName).collect(Collectors.toList());
        this.devices = ship.getDevices().stream().map(CommonCode::getCodeName).collect(Collectors.toList());
        this.ownerWordingTitle = ship.getOwnerWordingTitle();
        this.ownerWording = ship.getOwnerWording();
        this.noticeTitle = ship.getNoticeTitle();
        this.notice = ship.getNotice();
    }
}
