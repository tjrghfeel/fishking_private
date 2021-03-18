package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.enums.common.CouponType;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.*;
import com.tobe.fishking.v2.service.common.CouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags={"쿠폰"})
@RestController
@RequestMapping(value = "/v2/api")
public class CouponController {

    @Autowired
    CouponService couponService;

    /*다운 가능한 쿠폰 리스트 조회.
     * - 쿠폰 다운받기 창에서 해당유저가 다운받을 수 있는 쿠폰의 목록을 출력해줄때 사용.
     * - 어떤 Coupon이 노출기간이 현재시점이고, 발행가능상태이고, 발행수량이 최대발행수량을 넘지 않았으면,
     *    그 쿠폰들을 남은 기간이 짧은 순으로 정렬하여 Page로 반환.
     * - 반환은 쿠폰의 여러 정보들이 담겨있는 DTO형태로 반환. (CouponDTO 필드 참고) */
    @ApiOperation(value = "다운 가능한 쿠폰리스트 출력",notes = "현재 로그인된 회원이 다운받을 수 있는 쿠폰목록을 출력. \n" +
            "- 필드 )\n" +
            "   id : 쿠폰id \n" +
            "   couponType : 쿠폰 유형(정액/정률) / amount(\"정액\"), rate(\"정률\")\n" +
            "   couponCode : 쿠폰 코드\n" +
            "   couponName : 쿠폰 이름\n" +
            "   exposureEndDate : 노출 종료일\n" +
            "   saleValues : 할인률\n" +
            "   effectiveStartDate : 유효기간 시작일 \n" +
            "   effectiveEndDate : 유효기간 종료일\n" +
            "   isUsable : 쿠폰이 현재 사용가능상태인지 불가한상태인지\n" +
            "   brfIntroduction : 쿠폰 간략 소개\n" +
            "   couponDescription : 쿠폰 설명\n" +
            "   couponImage : 쿠폰 이미지 download url")
    @GetMapping("/downloadableCouponList/{page}")
    public Page<CouponDTO> getDownloadableCouponList(
            @RequestHeader("Authorization") String token,
            @PathVariable("page") int page
    ) throws ResourceNotFoundException, EmptyListException {
//        return couponService.getDownloadableCouponList(token, page);
        Page<CouponDTO> coupons = couponService.getDownloadableCouponList(token, page);
        if (coupons.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return coupons;
        }
    }

    /*쿠폰 다운 받기.
     * - 다운받은 쿠폰 종류를 가지고 coupon_member를 생성.
     * - 생선된 coupon_member의 id를 반환. */
    @ApiOperation(value = "쿠폰 다운 받기", notes = "" +
            "- 쿠폰하나를 다운받아 내 쿠폰함에 넣는다 \n" +
            "- 필드 )\n" +
            "   couponId : 다운받을 쿠폰id ")
    @PostMapping("/downloadCoupon")
    public Long downloadCoupon(
            @RequestHeader("Authorization") String token,
            @RequestBody CouponDownloadDto couponId
    ) throws ResourceNotFoundException {
        return couponService.downloadCoupon(token, couponId);
    }

    /*사용 가능한 coupon_member 리스트 조회.
     * 사용가능한 쿠폰이면서, 아직 사용하지 않았으면서, 유효기간이 지나지 않은 등록된 쿠폰 리스트를 정렬기준에 맞게 반환. */
    @ApiOperation(value = "사용 가능한 쿠폰 리스트 조회",notes = "현재 로그인한 회원이 다운받은 쿠폰들 중에서 사용가능한 쿠폰목록 반환. \n" +
            "- 다운가능한 쿠폰을 다운받으면 '내 쿠폰함'에 들어가며, '내 쿠폰'이 되어 사용이 가능해진다. " +
            "- 필드 )\n" +
            "   id : '내 쿠폰' id \n" +
            "   coupon : 쿠폰id \n" +
            "   couponType : 쿠폰 유형(정액/정률) / amount(\"정액\"), rate(\"정률\")\n" +
            "   couponCode : 쿠폰 코드\n" +
            "   member : '내 쿠폰'의 소유자 회원id\n" +
            "   isUse : '내 쿠폰' 사용여부\n" +
            "   regDate : '내 쿠폰' 다운받은 시기\n" +
            "   useDate : '내 쿠폰' 사용한 시기\n" +
            "   orders : '내 쿠폰'을 사용한 결제건의 id \n" +
            "   createdBy : '내 쿠폰'다운받은 사람\n" +
            "   modifiedBy : '내 쿠폰' 수정한 사람\n" +
            "   couponName : 쿠폰 이름\n" +
            "   saleValues : 할인률\n" +
            "   effectiveStartDate : 유효기간 시작일 \n" +
            "   effectiveEndDate : 유효기간 종료일\n" +
            "   isIssue : 쿠폰이 현재 발행 중/ 발행 중지 인지\n" +
            "   isUsable : 쿠폰이 현재 사용가능상태인지 불가한상태인지\n" +
            "   brfIntroduction : 쿠폰 간략 소개\n" +
            "   couponDescription : 쿠폰 설명\n")
    @GetMapping("/usableCouponList/{page}")
    public Page<CouponMemberDTO> getCouponMemberList(
            @RequestHeader("Authorization") String token,
            @PathVariable("page") int page,
            @RequestParam(value = "sort", required = false, defaultValue = "basic") String sort) throws ResourceNotFoundException, EmptyListException {
//        return  couponService.getCouponMemberList(token, page, sort);
        Page<CouponMemberDTO> coupons = couponService.getCouponMemberList(token, page, sort);
        if (coupons.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return coupons;
        }
    }

    @ApiOperation(value = "사용 가능한 쿠폰 리스트 조회",notes = "현재 로그인한 회원이 다운받은 쿠폰들 중에서 사용가능한 전체 쿠폰목록 반환. \n" +
            "- 다운가능한 쿠폰을 다운받으면 '내 쿠폰함'에 들어가며, '내 쿠폰'이 되어 사용이 가능해진다. " +
            "- 필드 )\n" +
            "   coupons: [{ \n" +
            "   id : '내 쿠폰' id \n" +
            "   coupon : 쿠폰id \n" +
            "   couponType : 쿠폰 유형(정액/정률) / amount(\"정액\"), rate(\"정률\")\n" +
            "   couponCode : 쿠폰 코드\n" +
            "   member : '내 쿠폰'의 소유자 회원id\n" +
            "   isUse : '내 쿠폰' 사용여부\n" +
            "   regDate : '내 쿠폰' 다운받은 시기\n" +
            "   useDate : '내 쿠폰' 사용한 시기\n" +
            "   orders : '내 쿠폰'을 사용한 결제건의 id \n" +
            "   createdBy : '내 쿠폰'다운받은 사람\n" +
            "   modifiedBy : '내 쿠폰' 수정한 사람\n" +
            "   couponName : 쿠폰 이름\n" +
            "   saleValues : 할인률\n" +
            "   effectiveStartDate : 유효기간 시작일\n" +
            "   effectiveEndDate : 유효기간 종료일\n" +
            "   isIssue : 쿠폰이 현재 발행 중/ 발행 중지 인지\n" +
            "   isUsable : 쿠폰이 현재 사용가능상태인지 불가한상태인지\n" +
            "   brfIntroduction : 쿠폰 간략 소개\n" +
            "   couponDescription : 쿠폰 설명\n" +
            "}, ... ] \n" +
            "size: 쿠폰 갯수")
    @GetMapping("/usableCoupons")
    public Map<String, Object> getAllCouponMemberList(
            @RequestHeader("Authorization") String token) throws ResourceNotFoundException, EmptyListException {
        Map<String, Object> result = new HashMap<>();
        List<CouponMemberDTO> coupons = couponService.getAllCouponMemberList(token);
        if (coupons.size() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            result.put("coupons", coupons);
            result.put("size", coupons.size());
            return result;
        }
    }

    /*쿠폰 전체 다운받기
     * - 현재 회원이 다운받을 수 있는 쿠폰들을 모두 다운받아 coupon_member를 만들어준다. */
    @ApiOperation(value = "쿠폰 전체 다운받기",notes = "" +
            "- 다운가능한 쿠폰 목록의 쿠폰들을 모두 다운받는다. \n" +
            "")
    @PostMapping("/downloadAllCoupon")
    public Boolean downloadAllCoupon(@RequestHeader("Authorization") String sessionToken) throws ResourceNotFoundException {
        return couponService.downloadAllCoupon(sessionToken);
    }


    /*관리자 항목들*/

    /*쿠폰 생성*/
    @ApiOperation(value = "쿠폰 생성",notes = "" +
            "요청필드 ) \n" +
            "- name : String / 필수 / 쿠폰명\n" +
            "- saleValue : Integer / 필수 / 쿠폰 할인 금액\n" +
            "- description : String / 필수 / 쿠폰 설명\n" +
            "- maxIssueCount : Integer / 필수 / 최대 발행 수량\n" +
            "- issueStartDate : yyyy-MM-dd / 필수 / 발행 시작일\n" +
            "- issueEndDate : yyyy-MM-dd / 필수 / 발행 종료일\n" +
            "- effectiveStartDate : yyyy-MM-dd / 필수 / 유효일 시작\n" +
            "- effectiveEndDate : yyyy-MM-dd / 필수 / 유효일 종료\n" +
            "응답 필드 ) 생성된 쿠폰의 id\n")
    @PostMapping("/manage/coupon")
    public Long makeCoupon(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CouponMakeDto dto
    ) throws ResourceNotFoundException {

        /*날짜 검증. 시작일이 종료일 이전인지 검사. */
        if(dto.getEffectiveStartDate().isAfter(dto.getEffectiveEndDate())){
            throw new RuntimeException("유효일 시작이 유효일 종료보다 늦습니다.");
        }
        if(dto.getIssueStartDate().isAfter(dto.getIssueEndDate())){
            throw new RuntimeException("발행 시작일이 발행 종료일보다 늦습니다.");
        }

        return couponService.makeCoupon(token, dto.getName(), dto.getSaleValue(), dto.getDescription(), dto.getMaxIssueCount(),
                dto.getIssueStartDate(), dto.getIssueEndDate(), dto.getEffectiveStartDate(), dto.getEffectiveEndDate());
    }

    /*쿠폰 수정*/
    @ApiOperation(value = "쿠폰 수정",notes = "")
    @PutMapping("/manage/coupon")
    public Boolean modifyCoupon(
            @RequestBody @Valid ModifyCouponDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        try{ if(dto.getCouponType()!=null){CouponType.valueOf(dto.getCouponType()); }}
        catch (Exception e){ throw new RuntimeException("couponType의 값으로는 'amount' 또는 'rate'만 가능합니다.");  }

        return couponService.modifyCoupon(dto,token);
    }

    /*쿠폰 리스트 검색 (couponMember아님)*/
    @ApiOperation(value = "쿠폰 리스트 검색",notes = "" +
            "요청 필드 ) \n" +
            "- sort : String / 정렬기준(내림차순). 조건 인자명 중 하나를 넘기면 되나, saleValuesStart, saleValueEnd의 경우엔 saleValues로." +
            "   issueQty와 useQty도 동일. ")
    @GetMapping("manage/coupon/list/{page}")
    public Page<CouponManageDtoForPage> getCouponList(
            @PathVariable("page") int page,
            @RequestHeader("Authorization") String token,
            CouponSearchConditionDto dto
    ) throws ResourceNotFoundException {
        try{ if(dto.getCouponType()!=null){CouponType.valueOf(dto.getCouponType()); }}
        catch (Exception e){ throw new RuntimeException("couponType의 값으로는 'amount' 또는 'rate'만 가능합니다.");  }

        return couponService.getCouponList(page, token, dto);
    }

    /*발급 쿠폰 검색*/
    @ApiOperation(value = "발급 쿠폰 검색",notes = "")
    @GetMapping("manage/couponMember/list/{page}")
    public Page<CouponMemberManageDtoForPage> getCouponMemberList(
            @PathVariable("page") int page,
            @RequestHeader("Authorization") String token,
            CouponMemberSearchConditionDto dto
    ) throws ResourceNotFoundException {
        return couponService.getCouponMemberManageList(page, token, dto);
    }

    /*쿠폰 디테일*/
    @ApiOperation(value = "쿠폰 디테일")
    @GetMapping("manage/coupon/detail")
    public CouponDetail getCouponDetail(@Param("couponId") Long couponId) throws ResourceNotFoundException {
        return couponService.getCouponDetail(couponId);
    }
}
