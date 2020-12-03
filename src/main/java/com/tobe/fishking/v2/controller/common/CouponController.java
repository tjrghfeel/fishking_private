package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.CouponDTO;
import com.tobe.fishking.v2.model.common.CouponMemberDTO;
import com.tobe.fishking.v2.service.common.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api(tags={"쿠폰"})
@RestController
@RequestMapping(value = "/v1/api")
public class CouponController {

    @Autowired
    CouponService couponService;

    /*다운 가능한 쿠폰 리스트 조회.
    * - 쿠폰 다운받기 창에서 해당유저가 다운받을 수 있는 쿠폰의 목록을 출력해줄때 사용.
    * - 어떤 Coupon이 노출기간이 현재시점이고, 발행가능상태이고, 발행수량이 최대발행수량을 넘지 않았으면,
    *    그 쿠폰들을 남은 기간이 짧은 순으로 정렬하여 Page로 반환.
    * - 반환은 쿠폰의 여러 정보들이 담겨있는 DTO형태로 반환. (CouponDTO 필드 참고) */
    @ApiOperation(value = "다운 가능한 쿠폰리스트 출력")
    @GetMapping("/downloadableCouponList/{page}")
    public Page<CouponDTO> getDownloadableCouponList(
            @RequestParam("memberId") Long memberId,
            @PathVariable("page") int page
    ){
        return couponService.getDownloadableCouponList(memberId, page);
    }

    /*쿠폰 다운 받기.
    * - 다운받은 쿠폰 종류를 가지고 coupon_member를 생성.
    * - 생선된 coupon_member의 id를 반환. */
    @ApiOperation(value = "쿠폰 다운 받기")
    @PostMapping("/downloadCoupon")
    public Long downloadCoupon(
            @RequestParam("memberId") Long memberId,
            @RequestParam("couponId") Long couponId
    ) throws ResourceNotFoundException {
        return couponService.downloadCoupon(memberId, couponId);
    }

    /*사용 가능한 coupon_member 리스트 조회.
    * 사용가능한 쿠폰이면서, 아직 사용하지 않았으면서, 유효기간이 지나지 않은 등록된 쿠폰 리스트를 정렬기준에 맞게 반환. */
    @ApiOperation(value = "사용 가능한 쿠폰 리스트 조회")
    @GetMapping("/usableCouponList/{page}")
    public Page<CouponMemberDTO> getCouponMemberList(
            @RequestParam("memberId") Long memberId,
            @PathVariable("page") int page,
            @RequestParam(value = "sort", required = false, defaultValue = "basic") String sort) throws ResourceNotFoundException {
        return  couponService.getCouponMemberList(memberId, page, sort);
    }



}
