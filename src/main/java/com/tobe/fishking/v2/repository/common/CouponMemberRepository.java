package com.tobe.fishking.v2.repository.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
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
            "   cm.coupon_issue_code couponCode, " +
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
            "   c.effective_start_date effectiveStartDate, " +
            "   c.effective_end_date effectiveEndDate, " +
            "   c.is_issue isIssue, " +
            "   c.is_use isUsable, " +
            "   c.brf_introduction brfIntroduction, " +
            "   c.coupon_description couponDescription " +
            "from coupon_member cm, coupon c " +
            "where cm.coupon_member_id = :member " +
            "   and cm.member_coupon_id = c.id "+
            "   and cm.is_use = :isUse " +
            "   and c.is_use = true " +
            "   and c.effective_start_date <= :today " +
            "   and c.effective_end_date >= :today " +
            "order by effectiveEndDate",
            countQuery = "select cm.id from coupon_member cm, coupon c " +
                    "where cm.coupon_member_id = :member " +
                    "   and cm.member_coupon_id = c.id "+
                    "   and cm.is_use = :isUse " +
                    "   and c.is_use = true " +
                    "   and c.effective_start_date <= :today " +
                    "   and c.effective_end_date >= :today " +
                    "order by effectiveEndDate",
            nativeQuery = true
    )
    Page<CouponMemberDTO> findCouponMemberListOrderByBasic(
            @Param("member") Member member, @Param("isUse") boolean isUse, @Param("today") LocalDate today, Pageable pageable);

    /*member가 사용가능한 coupon_member리스트를 Page로 반환 메소드. 쿠폰사용량이 가장 많은순으로 반환.
     * 사용가능한 쿠폰이면서, 아직 사용하지 않았으면서, 유효기간이 지나지 않은 등록된 쿠폰 리스트 */
    @Query(value = "select " +
            "   cm.id id, " +
            "   cm.member_coupon_id coupon, " +
            "   cm.coupon_issue_code couponCode, " +
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
            "   c.effective_start_date effectiveStartDate, " +
            "   c.effective_end_date effectiveEndDate, " +
            "   c.is_issue isIssue, " +
            "   c.is_use isUsable, " +
            "   c.brf_introduction brfIntroduction, " +
            "   c.coupon_description couponDescription " +
            "from coupon_member as cm, coupon as c " +
            "where cm.coupon_member_id = :member " +
            "   and cm.member_coupon_id = c.id "+
            "   and cm.is_use = :isUse " +
            "   and c.is_use = true " +
            "   and c.effective_start_date <= :today " +
            "   and c.effective_end_date >= :today " +
            "order by c.use_qty DESC",
            countQuery = "select cm.id from coupon_member cm, coupon c " +
                    "where cm.coupon_member_id = :member " +
                    "   and cm.member_coupon_id = c.id "+
                    "   and cm.is_use = :isUse " +
                    "   and c.is_use = true " +
                    "   and c.effective_start_date <= :today " +
                    "   and c.effective_end_date >= :today " +
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
            "   and c.effective_start_date <= :today " +
            "   and c.effective_end_date >= :today ",
            countQuery = "select cm.id " +
                    "from coupon_member cm, coupon c " +
                    "where cm.coupon_member_id = :member " +
                    "   and cm.member_coupon_id = c.id " +
                    "   and cm.is_use = :isUse " +
                    "   and c.is_use = true " +
                    "   and c.effective_start_date <= :today " +
                    "   and c.effective_end_date >= :today ",
            nativeQuery = true
    )
    int countByMemberAndIsUseAndDays(
            @Param("member") Member member, @Param("isUse") boolean isUse, @Param("today") LocalDateTime today);


    List<CouponMember> findByMember(Member member);

    /*쿠폰만료체크용 메소드
     * - coupon이 사용가능하면서, coupon_member가 아직 사용되지 않았으면서, 남은 사용기간이 7일인 쿠폰목록을 모두 검색해반환. */
    @Query(value = "select " +
            "   cm.id id, " +
            "   c.coupon_name couponName, " +
            "   cm.coupon_member_id member " +
            "from coupon_member as cm, coupon as c " +
            "where " +
            "   cm.member_coupon_id = c.id "+
            "   and cm.is_use = false " +
            "   and c.is_use = true " +
            "   and if( DATE_SUB(DATE(c.effective_end_date), INTERVAL 7 DAY) = CURDATE(), true, false) " +
//            "   and if( DATE_SUB(DATE_ADD(DATE(cm.reg_date), INTERVAL c.effective_days DAY), INTERVAL 7 DAY) = CURDATE(), true, false) " +
            "order by c.use_qty DESC",
            countQuery = "select cm.id from coupon_member cm, coupon c " +
                    "where " +
                    "   cm.member_coupon_id = c.id "+
                    "   and cm.is_use = false " +
                    "   and c.is_use = true " +
                    "   and if( DATE_SUB(DATE(c.effective_end_date), INTERVAL 7 DAY) = CURDATE(), true, false) " +
//            "   and if( DATE_SUB(DATE_ADD(DATE(cm.reg_date), INTERVAL c.effective_days DAY), INTERVAL 7 DAY) = CURDATE(), true, false) " +
                    "order by c.use_qty DESC",
            nativeQuery = true
    )
    List<CouponMemberDTO> checkCouponExpire();



    /*member가 사용가능한 coupon_member리스트를 Page로 반환 메소드. 쿠폰사용량이 가장 많은순으로 반환.
     * 사용가능한 쿠폰이면서, 아직 사용하지 않았으면서, 유효기간이 지나지 않은 등록된 쿠폰 리스트 */
    @Query(value = "select " +
            "   cm.id id, " +
            "   cm.member_coupon_id coupon, " +
            "   cm.coupon_issue_code couponCode, " +
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
            "   c.effective_start_date effectiveStartDate, " +
            "   c.effective_end_date effectiveEndDate, " +
            "   c.is_issue isIssue, " +
            "   c.is_use isUsable, " +
            "   c.brf_introduction brfIntroduction, " +
            "   c.coupon_description couponDescription " +
            "from coupon_member as cm, coupon as c " +
            "where cm.coupon_member_id = :member " +
            "   and cm.member_coupon_id = c.id "+
            "   and cm.is_use = :isUse " +
            "   and c.is_use = true " +
            "   and c.effective_start_date < :today " +
            "   and c.effective_end_date > :today " +
            "order by c.use_qty DESC",
            nativeQuery = true
    )
    List<CouponMemberDTO> findAllCouponMemberListOrderByPopular(
            @Param("member") Member member, @Param("isUse") boolean isUse, @Param("today") LocalDateTime today);

    Boolean existsByMemberAndCoupon(Member member, Coupon coupon);

    CouponMember findTopByCouponOrderByCreatedDateDesc(Coupon coupon);
}
