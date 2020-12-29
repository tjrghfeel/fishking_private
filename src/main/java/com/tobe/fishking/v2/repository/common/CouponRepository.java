package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
import com.tobe.fishking.v2.entity.fishing.CouponMember;
import com.tobe.fishking.v2.model.common.CouponDTO;
import com.tobe.fishking.v2.model.common.CouponMemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /*유효기간 내의 모든 Coupon의 개수. */
    @Query("select count(a) from Coupon a where a.createdDate >= :from and a.createdDate < :to")
    int countByRegistDate(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("select a from Coupon a where a.couponCode = :couponCode")
    Optional<Coupon> findByCouponCode(@Param("couponCode") String couponCode);

    /*멤버가 가진 사용가능한 모든 쿠폰 리스트. */
    @Query("select a from Coupon_member a where  a.member = :member and a.isUse = true")
    List<Coupon> findAllMember(@Param("member") Member member);

    @Query("select a from Coupon_member a " +
            "where a.member = :member and a.isUse = true " +
            "and a.coupon.exposureStartDate >= :today " +
            "and a.coupon.couponName like concat('%', :searchText, '%')")
    Page<Coupon> findAvailableByMember(@Param("member") Member member, @Param("today") LocalDate today, @Param("searchText") String searchText, Pageable pageable);

    /*멤버가 가진 유효기간이 지나지 않고 사용가능한 coupon_member의 리스트 Page로 조회. */
    @Query("select a from Coupon_member a where a.member = :member and a.isUse = false and a.coupon.exposureEndDate >= :today")
    Page<Coupon> findUsedByMember(@Param("member") Member member, @Param("today") LocalDate today, Pageable pageable);

    /*멤버가 가진 유효기간이 지나지않고 사용가능한 coupon_member의 개수. */
    @Query("select count(a) from Coupon_member a where a.member = :member and a.isUse = true and a.coupon.exposureEndDate >= :today ")
    Integer getCountByMember(@Param("member") Member member, @Param("today") LocalDate today);

    /*다운가능한 쿠폰의 리스트를 반환. */
    @Query(value = "select " +
            "c.id id, " +
            "c.coupon_type couponType, " +
            "c.coupon_code couponCode, " +
            "c.coupon_name couponName, " +
//            "c.exposure_start_date exposureStartDate, " +
            "c.exposure_end_date exposureEndDate, " +
            "c.sale_values saleValues, " +
            "c.effective_days effectiveDays, " +
//            "c.is_issue isIssue, " +
            "c.is_use isUsable, " +
            "c.from_purchase_amount fromPurchaseAmount, " +
            "c.to_purchase_amount toPurchaseAmount, " +
            "c.brf_introduction brfIntroduction, " +
            "c.coupon_description description, " +
            "cc.extra_value1 couponImage " +
            "from coupon c, coupon_member cm, common_code cc " +
            "where " +
            "   c.exposure_start_date <= :today and " +
            "   c.exposure_end_date >= :today and " +
            "   c.is_issue = true and " +
            "   c.max_issue_count > c.issue_qty and " +
            "   c.id not in (select cm.member_coupon_id from coupon_member cm join member m on cm.coupon_member_id=m.id " +
            "                   where m.session_token = :sessionToken) and " +
            "   cc.code_group_id = 91 and " +
            "   c.coupon_type = cc.code " +
            "group by c.id " +
            "order by c.exposure_end_date ",
            countQuery = "select c.id " +
                    "from coupon c, coupon_member cm " +
                    "where " +
                    "   c.exposure_start_date <= :today and " +
                    "   c.exposure_end_date >= :today and " +
                    "   c.is_issue = true and " +
                    "   c.max_issue_count > c.issue_qty and " +
                    "   c.id not in (select cm.member_coupon_id from coupon_member cm join member m on cm.coupon_member_id=m.id " +
                    "                   where m.session_token = :sessionToken) " +
                    "group by c.id " +
                    "order by c.exposure_end_date ",
            nativeQuery = true
    )
    Page<CouponDTO> findCouponList(@Param("sessionToken") String sessionToken, @Param("today") LocalDateTime today, Pageable pageable);

}