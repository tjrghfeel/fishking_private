package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Review;
import com.tobe.fishking.v2.model.common.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    /*회원이 작성한 리뷰 개수 카운트*/
    int countByMember(Member member);

    /*마이메뉴 - 내글관리 - 리뷰에서 내 리뷰리스트를 가져오는 메소드. */
    @Query(value = "select " +
            "   r.id id, " +
            "   g.id goodsId, " +
            "   s.id shipId, " +
            "   m.profile_image profileImage, " +
            "   m.nick_name nickName, " +
            "   g.fishing_date fishingDate, " +
            "   (select group_concat(c.code_name separator ',') from goods_fish_species gs, common_code c " +
            "       where gs.goods_id = g.id and gs.fish_species_id = c.id group by gs.goods_id ) goodsFishSpecies, " +
//            "   g.meridiem meridiem, " +
//            "   s.distance distance, " +
            "   r.total_avg_by_review totalAvgByReview, " +
            "   r.taste_by_review tasteByReview, " +
            "   r.service_by_review serviceByReview, " +
            "   r.clean_by_review cleanByReview, " +
            "   r.content content, " +
            "   (select GROUP_CONCAT(f.stored_file separator ',') " +
            "       from files f where f.pid = r.id and f.file_publish = 11 " +
            "       group by f.pid) fileNameList, " +
            "   (select GROUP_CONCAT(f.file_url separator ',') " +
            "       from files f where f.pid = r.id and f.file_publish = 11 " +
            "       group by f.pid) filePathList " +
            "from review r, member m, goods g, ship s " +
            "where r.member_id = :member " +
            "   and r.review_good_id = g.id" +
            "   and r.member_id = m.id " +
            "   and g.goods_ship_id = s.id " +
            "order by r.created_date desc  ",
            countQuery = "select r.id " +
                    "from review r, member m, goods g, ship s " +
                    "where r.member_id = :member " +
                    "   and r.review_good_id = g.id" +
                    "   and r.member_id = m.id " +
                    "   and g.goods_ship_id = s.id " +
                    "order by r.created_date desc  ",
            nativeQuery = true
    )
    Page<ReviewDto> findMyReviewList(@Param("member") Member member, Pageable pageable);

    /*회원탈퇴시 회원이 작성한 review를 비활성화 처리해주는 메소드.*/
    /*@Modifying
    @Query("update Review r set r.isActive = false where r.member = :member")
    int updateIsActiveByMember(@Param("member") Member member);*/


}
