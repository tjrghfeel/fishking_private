package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Review;
import com.tobe.fishking.v2.model.common.ReviewDTOForList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    /*마이메뉴 - 내글관리 - 리뷰에서 내 리뷰리스트를 가져오는 메소드. */
    @Query(value = "select " +
            "   m.profile_image profileImage, " +
            "   m.nick_name nickName, " +
            "   g.fishing_date fishingDate, " +
            "   g.goods_fish_lure goodsFishLure, " +
            "   g.meridiem meridiem, " +
            "   g.ship_start_time shipStartTIme, " +
            "   r.total_avg_by_review totalAvgByReview, " +
            "   r.taste_by_review tasteByReview, " +
            "   r.service_by_review serviceByReview, " +
            "   r.clean_by_review cleanByReview, " +
            "   r.content content " +
            "from review r, member m, goods g " +
            "where r.review_good_id = g.id" +
            "   and r.member_id = m.id " +
            "   and r.member_id = :member " +
            "order by r.created_date desc  ",
            countQuery = "select r " +
                    "from review r, member m, goods g " +
                    "where r.review_good_id = g.id" +
                    "   and r.member_id = m.id " +
                    "order by r.created_date desc  ",
            nativeQuery = true
    )
    Page<ReviewDTOForList> findMyReviewList(@Param("member") Member member, Pageable pageable);




}
