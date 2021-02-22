package com.tobe.fishking.v2.model.common;

import com.querydsl.core.annotations.QueryProjection;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Review;
import com.tobe.fishking.v2.entity.fishing.Goods;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponse {

    private Long id;
    private Long goodsId;
    private Long shipId;
    private Long memberId;
    private String profileImage;
    private String nickName;
    private String fishingDate;
    private String goodsName;
    private String fishingTideTime;
    private Double avgByReview;
    private Double tasteByReview;
    private Double serviceByReview;
    private Double cleanByReview;
    private String content;
    private List<FilesDTO> images;

    @QueryProjection
    public ReviewResponse(Review review, Goods goods, Long shipId, Member member) {
        this.id = review.getId();
        this.goodsId = goods.getId();
        this.shipId = shipId;
        this.memberId = member.getId();
        this.profileImage = "/resource" + member.getProfileImage();
        this.nickName = member.getNickName();
        this.fishingDate = goods.getFishingDate();
        this.goodsName = goods.getName();
        this.fishingTideTime = goods.getFishingTideTime();
        this.avgByReview = review.getTotalAvgByReview();
        this.tasteByReview = review.getTasteByReview();
        this.serviceByReview = review.getServiceByReview();
        this.cleanByReview = review.getCleanByReview();
        this.content = review.getContent();
        this.images = null;
    }
}
