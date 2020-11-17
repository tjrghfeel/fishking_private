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
    int countByRegistDate(LocalDateTime from, LocalDateTime to);

    @Query("select a from Coupon a where a.couponCode = :couponCode")
    Optional<Coupon> findByCouponCode(String couponCode);

    /*멤버가 가진 사용가능한 모든 쿠폰 리스트. */
    @Query("select a from Coupon_member a where  a.member = :member and a.isUse = true")
    List<Coupon> findAllMember(Member member);

    @Query("select a from Coupon_member a " +
            "where a.member = :member and a.isUse = true " +
            "and a.coupon.exposureStartDate >= :today " +
            "and a.coupon.couponName like concat('%', :searchText, '%')")
    Page<Coupon> findAvailableByMember(Member member, LocalDate today, String searchText, Pageable pageable);

    /*멤버가 가진 유효기간이 지나지 않고 사용가능한 coupon_member의 리스트 Page로 조회. */
    @Query("select a from Coupon_member a where a.member = :member and a.isUse = false and a.coupon.exposureEndDate >= :today")
    Page<Coupon> findUsedByMember(Member member, LocalDate today, Pageable pageable);

    /*멤버가 가진 유효기간이 지나지않고 사용가능한 coupon_member의 개수. */
    @Query("select count(a) from Coupon_member a where a.member = :member and a.isUse = true and a.coupon.exposureEndDate >= :today ")
    Integer getCountByMember(Member member, LocalDate today);

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
            "c.coupon_description description " +
            "from coupon c " +
            "where " +
            "   c.exposure_start_date <= :today and " +
            "   c.exposure_end_date >= :today and " +
            "   c.is_issue = true and " +
            "   c.max_issue_count > c.issue_qty " +
            "order by c.exposure_end_date ",
            countQuery = "select c.id " +
                    "from coupon c " +
                    "where " +
                    "   c.exposure_start_date <= :today and " +
                    "   c.exposure_end_date >= :today and " +
                    "   c.is_issue = true and " +
                    "   c.max_issue_count > c.issue_qty " +
                    "order by c.exposure_end_date ",
            nativeQuery = true
    )
    Page<CouponDTO> findCouponList(@Param("today") LocalDateTime today, Pageable pageable);


    /*member가 사용가능한 coupon_member리스트를 Page로 반환 메소드. 남은 사용기간이 가장 적은순으로 반환.
    * 사용가능한 쿠폰이면서, 아직 사용하지 않았으면서, 유효기간이 지나지 않은 등록된 쿠폰 리스트 */
    @Query(value = "select " +
            "   cm.id id, " +
            "   cm.member_coupon_id coupon, " +
            "   cm.coupon_code couponCode, " +
            "   cm.coupon_member_id member, " +
            "   cm.is_use isUse, " +
            "   cm.reg_date regDate, " +
            "   cm.use_date useDate, " +
            "   cm.coupon_orders_id orders, " +
            "   cm.created_by createdBy, " +
            "   cm.modified_by modifiedBy, " +
            "   c.coupon_type couponType, " +
            "   c.coupon_name couponName, " +
            "   c.sale_values saleValues, " +
            "   c.effective_days effectiveDays, " +
            "   c.is_issue isIssue, " +
            "   c.is_use isUsable, " +
            "   c.brf_introduction brfIntroduction, " +
            "   c.coupon_description couponDescription " +
            "from coupon_member cm, coupon c " +
            "where cm.coupon_member_id = :member " +
            "   and cm.member_coupon_id = c.id "+
            "   and cm.is_use = :isUse " +
            "   and c.is_use = true " +
            "   and DATE_ADD(cm.reg_date, INTERVAL c.effective_days DAY) >= :today " +
            "order by DATE_ADD(cm.reg_date, INTERVAL c.effective_days DAY)",
            countQuery = "select cm.id from coupon_member cm, coupon c " +
                "where cm.coupon_member_id = :member " +
                    "   and cm.member_coupon_id = c.id "+
                    "   and cm.is_use = :isUse " +
                    "   and c.is_use = true " +
                    "   and DATE_ADD(cm.reg_date, INTERVAL c.effective_days DAY) >= :today " +
                "order by DATE_ADD(cm.reg_date, INTERVAL c.effective_days DAY)",
            nativeQuery = true
    )
    Page<CouponMemberDTO> findCouponMemberListOrderByBasic(
            @Param("member") Member member, @Param("isUse") boolean isUse, @Param("today") LocalDateTime today, Pageable pageable);

    /*member가 사용가능한 coupon_member리스트를 Page로 반환 메소드. 쿠폰사용량이 가장 많은순으로 반환.
    * 사용가능한 쿠폰이면서, 아직 사용하지 않았으면서, 유효기간이 지나지 않은 등록된 쿠폰 리스트 */
    @Query(value = "select " +
            "   cm.id id, " +
            "   cm.member_coupon_id coupon, " +
            "   cm.coupon_code couponCode, " +
            "   cm.coupon_member_id member, " +
            "   cm.is_use isUse, " +
            "   cm.reg_date regDate, " +
            "   cm.use_date useDate, " +
            "   cm.coupon_orders_id orders, " +
            "   cm.created_by createdBy, " +
            "   cm.modified_by modifiedBy, " +
            "   c.coupon_type couponType, " +
            "   c.coupon_name couponName, " +
            "   c.sale_values saleValues, " +
            "   c.effective_days effectiveDays, " +
            "   c.is_issue isIssue, " +
            "   c.is_use isUsable, " +
            "   c.brf_introduction brfIntroduction, " +
            "   c.coupon_description couponDescription " +
            "from coupon_member as cm, coupon as c " +
            "where cm.coupon_member_id = :member " +
            "   and cm.member_coupon_id = c.id "+
            "   and cm.is_use = :isUse " +
            "   and c.is_use = true " +
            "   and DATE_ADD(cm.reg_date, INTERVAL c.effective_days DAY) >= :today " +
            "order by c.use_qty DESC",
            countQuery = "select cm.id from coupon_member cm, coupon c " +
                    "where cm.coupon_member_id = :member " +
                    "   and cm.member_coupon_id = c.id "+
                    "   and cm.is_use = :isUse " +
                    "   and c.is_use = true " +
                    "   and DATE_ADD(cm.reg_date, INTERVAL c.effective_days DAY) >= :today " +
                    "order by c.use_qty DESC",
            nativeQuery = true
    )
    Page<CouponMemberDTO> findCouponMemberListOrderByPopular(
            @Param("member") Member member, @Param("isUse") boolean isUse, @Param("today") LocalDateTime today, Pageable pageable);

}