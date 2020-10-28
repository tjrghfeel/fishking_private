package com.tobe.fishking.v2.repository.common;


import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("select count(a) from Coupon a where a.createdDate >= :from and a.createdDate < :to")
    int countByRegistDate(LocalDateTime from, LocalDateTime to);

    @Query("select a from Coupon a where a.couponCode = :couponCode")
    Optional<Coupon> findByCouponCode(String couponCode);

    @Query("select a from Coupon_member a where  a.member = :member and a.isUse = true")
    List<Coupon> findAllMember(Member member);

    @Query("select a from Coupon_member a " +
            "where a.member = :member and a.isUse = true " +
            "and a.coupon.exposureStartDate >= :today " +
            "and a.coupon.couponName like concat('%', :searchText, '%')")
    Page<Coupon> findAvailableByMember(Member member, LocalDate today, String searchText, Pageable pageable);

    @Query("select a from Coupon_member a where a.member = :member and a.isUse = false and a.coupon.exposureEndDate >= :today")
    Page<Coupon> findUsedByMember(Member member, LocalDate today, Pageable pageable);


    @Query("select count(a) from Coupon_member a where a.member = :member and a.isUse = true and a.coupon.exposureEndDate >= :today ")
    Integer getCountByMember(Member member, LocalDate today);
}