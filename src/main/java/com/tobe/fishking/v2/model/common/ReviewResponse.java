//package com.tobe.fishking.v2.model.common;
//
//import com.querydsl.core.annotations.QueryProjection;
//import com.tobe.fishking.v2.entity.auth.Member;
//import com.tobe.fishking.v2.entity.common.CommonCode;
//import com.tobe.fishking.v2.entity.common.Review;
//import com.tobe.fishking.v2.entity.fishing.Goods;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class ReviewResponse {
//
//    private Long id;
//    private Long goodsId;
//    private Long shipId;
//    private String profileImage;
//    private String nickName;
//    private String fishingDate;
//    private String goodsFishingSpecies;
//    private String merideim;
//    private Double distance;
//    private String fishingTideTime;
//    private Double totalAvgByReview;
//    private Double tasteByReview;
//    private Double serviceByReview;
//    private Double cleanByReview;
//    private String content;
//
//    @QueryProjection
//    public ReviewResponse(Review review, Goods goods, Long shipId, Member member) {
//        this.id = review.getId();
//        this.goodsId = goods.getId();
//        this.shipId = shipId;
//        this.profileImage = member.getProfileImage();
//        this.nickName = member.getNickName();
//        this.fishingDate = goods.getFishingDate();
//        this.goodsFishingSpecies = goods.getFishSpecies().stream().map(CommonCode::getCodeName).reduce(",", String::concat);
//        this.merideim = goods.getMeridiem().getValue();
//        this.distance = Double.valueOf("0");
//        this.fishingTideTime = goods.getFishingTideTime();
//    }
//}
