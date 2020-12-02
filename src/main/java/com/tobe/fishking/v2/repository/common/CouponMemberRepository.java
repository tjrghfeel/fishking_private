package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.CouponMember;
import com.tobe.fishking.v2.model.common.CouponMemberDTO;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponMemberRepository extends BaseRepository<CouponMember, Long> {

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

    /*member가 사용가능한 couponMember의 개수 반환. */
    @Query(value = "select count(cm.id) " +
            "from coupon_member cm, coupon c " +
            "where cm.coupon_member_id = :member " +
            "   and cm.member_coupon_id = c.id " +
            "   and cm.is_use = :isUse " +
            "   and c.is_use = true " +
            "   and DATE_ADD(cm.reg_date, INTERVAL c.effective_days DAY) >= :today ",
            countQuery = "select cm.id " +
                    "from coupon_member cm, coupon c " +
                    "where cm.coupon_member_id = :member " +
                    "   and cm.member_coupon_id = c.id " +
                    "   and cm.is_use = :isUse " +
                    "   and c.is_use = true " +
                    "   and DATE_ADD(cm.reg_date, INTERVAL c.effective_days DAY) >= :today ",
            nativeQuery = true
    )
    int countByMemberAndIsUseAndDays(
            @Param("member") Member member, @Param("isUse") boolean isUse, @Param("today") LocalDateTime today);


    List<CouponMember> findByMember(Member member);
}