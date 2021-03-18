package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
import com.tobe.fishking.v2.entity.fishing.CouponMember;
import com.tobe.fishking.v2.model.PageRequest;
import com.tobe.fishking.v2.model.common.CouponDTO;
import com.tobe.fishking.v2.model.common.CouponManageDtoForPage;
import com.tobe.fishking.v2.model.common.CouponMemberDTO;
import com.tobe.fishking.v2.model.common.CouponMemberManageDtoForPage;
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
            "c.coupon_create_code couponCode, " +
            "c.coupon_name couponName, " +
//            "c.exposure_start_date exposureStartDate, " +
            "c.exposure_end_date exposureEndDate, " +
            "c.sale_values saleValues, " +
            "c.effective_start_date effectiveStartDate, " +
            "c.effective_end_date effectiveEndDate, " +
//            "c.is_issue isIssue, " +
            "c.is_use isUsable, " +
            "c.from_purchase_amount fromPurchaseAmount, " +
            "c.to_purchase_amount toPurchaseAmount, " +
            "c.brf_introduction brfIntroduction, " +
            "c.coupon_description description, " +
            "cc.extra_value1 couponImage " +
            "from coupon c, common_code cc " +
            "where " +
            "   c.exposure_start_date <= now() and " +
            "   c.exposure_end_date >= now() and " +
            "   c.is_issue = true and " +
            "   c.max_issue_count > c.issue_qty and " +
            "   c.id not in (select cm.member_coupon_id from coupon_member cm join member m on cm.coupon_member_id=m.id " +
            "                   where m.id = :memberId) and " +
            "   cc.code_group_id = 91 and " +
            "   c.coupon_type = cc.code " +
            "order by c.exposure_end_date ",
            countQuery = "select c.id " +
                    "from coupon c, common_code cc " +
                    "where " +
                    "   c.exposure_start_date <= now() and " +
                    "   c.exposure_end_date >= now() and " +
                    "   c.is_issue = true and " +
                    "   c.max_issue_count > c.issue_qty and " +
                    "   c.id not in (select cm.member_coupon_id from coupon_member cm join member m on cm.coupon_member_id=m.id " +
                    "                   where m.id = :memberId) and " +
                    "   cc.code_group_id = 91 and " +
                    "   c.coupon_type = cc.code " +
                    "order by c.exposure_end_date ",
            nativeQuery = true
    )
    Page<CouponDTO> findCouponList(@Param("memberId") Long memberId, /*@Param("today") LocalDateTime today,*/ Pageable pageable);

    /*위의 findCouponList와 동일하나 List형으로 coupon엔터티의 id만 반환하는 쿼리메소드*/
    @Query(value = "select c.id id "+
            "from coupon c, coupon_member cm " +
            "where " +
            "   c.exposure_start_date <= :today and " +
            "   c.exposure_end_date >= :today and " +
            "   c.is_issue = true and " +
            "   c.max_issue_count > c.issue_qty and " +
            "   c.id not in (select cm.member_coupon_id from coupon_member cm join member m on cm.coupon_member_id=m.id " +
            "                   where m.id = :memberId) " +
            "group by c.id ",
            countQuery = "select c.id " +
                    "from coupon c, coupon_member cm " +
                    "where " +
                    "   c.exposure_start_date <= :today and " +
                    "   c.exposure_end_date >= :today and " +
                    "   c.is_issue = true and " +
                    "   c.max_issue_count > c.issue_qty and " +
                    "   c.id not in (select cm.member_coupon_id from coupon_member cm join member m on cm.coupon_member_id=m.id " +
                    "                   where m.id = :memberId) " +
                    "group by c.id ",
            nativeQuery = true
    )
    List<Long> findDownloadableCouponList(@Param("memberId") Long memberId, @Param("today") LocalDateTime today);

    /*이번 달에 생성된 쿠폰중 가장 최근 쿠폰 하나 반환*/
    Coupon findTop1ByCreatedDateGreaterThanAndCreatedDateLessThanOrderByCreatedDateDesc(LocalDateTime start, LocalDateTime end);

    @Query(value = "select " +
            "   c.id couponId," +
            "   c.coupon_type couponType," +
            "   c.coupon_create_code couponCreateCode," +
            "   c.coupon_name couponName, " +
            "   c.exposure_start_date exposureStartDate, " +
            "   c.exposure_end_date exposureEndDate, " +
            "   c.sale_values saleValues, " +
            "   c.max_issue_count maxIssueCount, " +
            "   c.effective_start_date effectiveStartDate, " +
            "   c.effective_end_date effectiveEndDate, " +
            "   c.issue_qty issueQty, " +
            "   c.use_qty useQty, " +
            "   c.is_issue isIssue, " +
            "   c.is_use isUse, " +
//            "   c.from_purchase_amount fromPhurchaseAmount, " +
//            "   c.to_purchase_amount toPhurchaseAmoutn, " +
//            "   c.brf_introduction brfIntroduction, " +
            "   c.coupon_description couponDescription, " +
            "   c.created_by createdBy, " +
            "   c.modified_by modifiedBy   "+
            "from coupon c " +
            "where " +
            "   if(:couponId is null, true, c.id = :couponId) " +
            "   and if(:couponType is null, true, c.coupon_type = :couponType) " +
            "   and if(:couponCreateCode is null, true, c.coupon_create_code = :couponCreateCode) " +
            "   and if(:couponName is null, true, c.coupon_name like %:couponName%) " +
            "   and if(:exposureStartDate is null, true, c.exposure_start_date >= :exposureStartDate) " +
            "   and if(:exposureEndDate is null, true, c.exposure_end_date <= :exposureEndDate) " +
            "   and if(:saleValuesStart is null, true, c.sale_values >= :saleValuesStart) " +
            "   and if(:saleValuesEnd is null, true, c.sale_values <= :saleValuesEnd) " +
            "   and if(:effectiveStartDate is null, true, c.effective_start_date >= :effectiveStartDate) " +
            "   and if(:effectiveEndDate is null, true, c.effective_end_date <= :effectiveEndDate) " +
            "   and if(:issueQtyStart is null, true, c.issue_qty >= :issueQtyStart) " +
            "   and if(:issueQtyEnd is null, true, c.issue_qty <= :issueQtyEnd) " +
            "   and if(:useQtyStart is null, true, c.use_qty >= :useQtyStart) " +
            "   and if(:useQtyEnd is null, true, c.use_qty <= :useQtyEnd) " +
            "   and if(:isIssue is null, true, c.is_issue = :isIssue) " +
            "   and if(:isUse is null, true, c.is_use = :isUse) " +
//            "   and if(:fromPurchaseAmount is null, true, c.from_purchase_amount = :fromPurchaseAmount) " +
//            "   and if(:toPurchaseAmount is null, true, c.to_purchase_amount = :toPurchaseAmount) " +
//            "   and if(:brfIntroduction is null, true, c.brf_introduction like %:brfIntroduction%) " +
            "   and if(:couponDescription is null, true, c.coupon_description like %:couponDescription%) " +
            "   and if(:createdBy is null, true, c.created_by = :createdBy) " +
            "   and if(:modifiedBy is null, true, c.modified_by = :modifiedBy) ",
            countQuery = "select c.id " +
                    "from coupon c " +
                    "where " +
                    "   if(:couponId is null, true, c.id = :couponId) " +
                    "   and if(:couponType is null, true, c.coupon_type = :couponType) " +
                    "   and if(:couponCreateCode is null, true, c.coupon_create_code = :couponCreateCode) " +
                    "   and if(:couponName is null, true, c.coupon_name like %:couponName%) " +
                    "   and if(:exposureStartDate is null, true, c.exposure_start_date >= :exposureStartDate) " +
                    "   and if(:exposureEndDate is null, true, c.exposure_end_date <= :exposureEndDate) " +
                    "   and if(:saleValuesStart is null, true, c.sale_values >= :saleValuesStart) " +
                    "   and if(:saleValuesEnd is null, true, c.sale_values <= :saleValuesEnd) " +
                    "   and if(:effectiveStartDate is null, true, c.effective_start_date >= :effectiveStartDate) " +
                    "   and if(:effectiveEndDate is null, true, c.effective_end_date <= :effectiveEndDate) " +
                    "   and if(:issueQtyStart is null, true, c.issue_qty >= :issueQtyStart) " +
                    "   and if(:issueQtyEnd is null, true, c.issue_qty <= :issueQtyEnd) " +
                    "   and if(:useQtyStart is null, true, c.use_qty >= :useQtyStart) " +
                    "   and if(:useQtyEnd is null, true, c.use_qty <= :useQtyEnd) " +
                    "   and if(:isIssue is null, true, c.is_issue = :isIssue) " +
                    "   and if(:isUse is null, true, c.is_use = :isUse) " +
//                    "   and if(:fromPurchaseAmount is null, true, c.from_purchase_amount = :fromPurchaseAmount) " +
//                    "   and if(:toPurchaseAmount is null, true, c.to_purchase_amount = :toPurchaseAmount) " +
//                    "   and if(:brfIntroduction is null, true, c.brf_introduction like %:brfIntroduction%) " +
                    "   and if(:couponDescription is null, true, c.coupon_description like %:couponDescription%) " +
                    "   and if(:createdBy is null, true, c.created_by = :createdBy) " +
                    "   and if(:modifiedBy is null, true, c.modified_by = :modifiedBy) ",
            nativeQuery = true
    )
    Page<CouponManageDtoForPage> getCouponList(
        @Param("couponId") Long couponId,
        @Param("couponType") Integer couponType,
        @Param("couponCreateCode") String couponCreateCode,
        @Param("couponName") String couponName,
        @Param("exposureStartDate") LocalDate exposureStartDate,
        @Param("exposureEndDate") LocalDate exposureEndDate,
        @Param("saleValuesStart") Integer saleValuesStart,
        @Param("saleValuesEnd") Integer saleValuesEnd,
        @Param("effectiveStartDate") LocalDate effectiveStartDate,
        @Param("effectiveEndDate") LocalDate effectiveEndDate,
        @Param("issueQtyStart") Integer issueQtyStart,
        @Param("issueQtyEnd") Integer issueQtyEnd,
        @Param("useQtyStart") Integer useQtyStart,
        @Param("useQtyEnd") Integer useQtyEnd,
        @Param("isIssue") Boolean isIssue,
        @Param("isUse") Boolean isUse,
//        @Param("fromPurchaseAmount") Integer fromPurchaseAmount,
//        @Param("toPurchaseAmount") Integer toPurchaseAmount,
//        @Param("brfIntroduction") String brfIntroduction,
        @Param("couponDescription") String couponDescription,
        @Param("createdBy") Long createdBy,
        @Param("modifiedBy") Long modifiedBy,
        Pageable pageable
    );

    /*발급 쿠폰 검색*/
    @Query(value = "select " +
            "   cm.id couponMemberId, " +
            "   cm.coupon_issue_code couponIssueCode, " +
            "   c.id couponId, " +
            "   c.coupon_name couponName, " +
            "   m.id memberId, " +
            "   m.nick_name nickname, " +
            "   m.member_name memberName, " +
            "   m.areacode areaCode, " +
            "   m.localnumber localNumber, " +
            "   m.city city, " +
            "   m.dong dong, " +
            "   m.gu gu, " +
            "   c.sale_values saleValues, " +
            "   c.coupon_description couponDescription, " +
            "   cm.reg_date regDate, " +
            "   c.effective_start_date effectiveStartDate, " +
            "   c.effective_end_date effectiveEndDate, " +
            "   c.coupon_create_code couponCreateCode, " +
            "   cm.use_date useDate, " +
            "   o.id orderId, " +
            "   o.discount_amount discountAmount, " +
            "   o.total_amount totalAmount, " +
            "   o.payment_amount paymentAmount, " +
            "   o.goods goodsId, " +
            "   o.order_date orderDate, " +
            "   o.order_status orderStatus, " +
            "   o.order_number orderNumber " +
            "from coupon_member cm left join orders o on cm.coupon_orders_id = o.id, coupon c, member m " +
            "where " +
            "   cm.coupon_member_id = m.id " +
            "   and cm.member_coupon_id = c.id " +
            "   and if(:useDateStart is null, true, cm.use_date >= :useDateStart) " +
            "   and if(:useDateEnd is null, true, cm.use_date <= :useDateEnd) " +
            "   and if(:effectiveStartDate is null, true, c.effective_start_date >= :effectiveStartDate) " +
            "   and if(:effectiveEndDate is null, true, c.effective_end_date <= :effectiveEndDate) " +
            "   and if(:exposureStartDate is null, true, c.exposure_start_date >= :exposureStartDate) " +
            "   and if(:exposureEndDate is null, true, c.exposure_end_date <= :exposureEndDate) " +
            "   and if(:couponName is null, true, c.coupon_name like %:couponName%) " +
            "   and if(:areaCode is null, true, m.areacode like %:areaCode%) " +
            "   and if(:localNumber is null, true, m.localnumber like %:localNumber%) " +
            "   and if(:isUse is null, true, cm.is_use = :isUse) ",
            countQuery = "select cm.id " +
                    "from coupon_member cm left join orders o on cm.coupon_orders_id = o.id, coupon c, member m " +
                    "where " +
                    "   cm.coupon_member_id = m.id " +
                    "   and cm.member_coupon_id = c.id " +
                    "   and if(:useDateStart is null, true, cm.use_date >= :useDateStart) " +
                    "   and if(:useDateEnd is null, true, cm.use_date <= :useDateEnd) " +
                    "   and if(:effectiveStartDate is null, true, c.effective_start_date >= :effectiveStartDate) " +
                    "   and if(:effectiveEndDate is null, true, c.effective_end_date <= :effectiveEndDate) " +
                    "   and if(:exposureStartDate is null, true, c.exposure_start_date >= :exposureStartDate) " +
                    "   and if(:exposureEndDate is null, true, c.exposure_end_date <= :exposureEndDate) " +
                    "   and if(:couponName is null, true, c.coupon_name like %:couponName%) " +
                    "   and if(:areaCode is null, true, m.areacode like %:areaCode%) " +
                    "   and if(:localNumber is null, true, m.localnumber like %:localNumber%) " +
                    "   and if(:isUse is null, true, cm.is_use = :isUse) ",
            nativeQuery = true
    )
    Page<CouponMemberManageDtoForPage> getCouponMemberList(
            @Param("useDateStart") LocalDate useDateStart,
            @Param("useDateEnd") LocalDate useDateEnd,
            @Param("effectiveStartDate") LocalDate effectiveStartDate,
            @Param("effectiveEndDate") LocalDate effectiveEndDate,
            @Param("couponName") String couponName,
            @Param("areaCode") String areaCode,
            @Param("localNumber") String localNumber,
            @Param("exposureStartDate") LocalDate exposureStartDate,
            @Param("exposureEndDate") LocalDate exposureEndDate,
            @Param("isUse") Boolean isUse,
            Pageable pageable
    );
}