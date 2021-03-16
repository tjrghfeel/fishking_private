package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
import com.tobe.fishking.v2.entity.fishing.CouponMember;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.common.CouponType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.CouponMemberRepository;
import com.tobe.fishking.v2.repository.common.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService {


    @Autowired
    CouponRepository couponRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CouponMemberRepository couponMemberRepository;

    /*다운 가능한 쿠폰 리스트 조회.*/
    @Transactional
    public Page<CouponDTO> getDownloadableCouponList(String token, int page) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        Pageable pageable = PageRequest.of(page, 10);
        return couponRepository.findCouponList(member.getId(), /*LocalDateTime.now(),*/ pageable);
    }

    /*쿠폰 다운받기
     * - couponId로 CouponMember를 생성한다. */
    @Transactional
    public Long downloadCoupon(String sessionToken, CouponDownloadDto couponId) throws ResourceNotFoundException {
        Coupon coupon = couponRepository.findById(couponId.getCouponId())
                .orElseThrow(()->new ResourceNotFoundException("coupon not found for this id ::"+couponId));
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        /*이미 다운받았는지 확인*/
        boolean isDownloaded = couponMemberRepository.existsByMemberAndCoupon(member, coupon);
        if(isDownloaded == true){ return -1L;}

        /*couponIssueCode 생성*/
        CouponMember lastCouponMember = couponMemberRepository.findTopByCouponOrderByCreatedDateDesc(coupon);
        String lastCouponIssueCode = lastCouponMember.getCouponIssueCode();
        int lastCouponIssueCodeNum = Integer.parseInt(lastCouponIssueCode.substring(8));
        String newCouponIssueCode = coupon.getCouponCreateCode()+ (lastCouponIssueCodeNum+1);

        CouponMember couponMember = CouponMember.builder()
                .coupon(coupon)
                .couponIssueCode(newCouponIssueCode)
                .member(member)
                .isUse(false)
                .regDate(LocalDateTime.now())
                .createdBy(member)
                .modifiedBy(member)
                .build();
        couponMember = couponMemberRepository.save(couponMember);

        return couponMember.getId();
    }


    /*coupon_member 리스트 조회
     * 사용가능한 쿠폰이면서, 아직 사용하지 않았으면서, 유효기간이 지나지 않은 등록된 쿠폰 리스트를 정렬기준에 맞게 반환.*/
    @Transactional
    public Page<CouponMemberDTO> getCouponMemberList(String sessionToken, int page, String sort) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));

        Pageable pageable = PageRequest.of(page, 10);
        //정렬 기준에 따라 다른 repository메소드 호출.
        if(sort.equals("popular")) {
            return couponMemberRepository.findCouponMemberListOrderByPopular(member, false, LocalDateTime.now(), pageable);
        }
        else if(sort.equals("latest")){
            //!!!!!쿠폰 정렬기준이 '최신순'일 경우. (최신순이 뭔지 잘모르겟음)
        }
        //sort가 'basic'인 경우.
        return couponMemberRepository.findCouponMemberListOrderByBasic(member, false, LocalDateTime.now(), pageable);
    }

    @Transactional
    public List<CouponMemberDTO> getAllCouponMemberList(String sessionToken) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));
        return couponMemberRepository.findAllCouponMemberListOrderByPopular(member, false, LocalDateTime.now());
    }

    /*모든 쿠폰 다운로드
     * - */
    @Transactional
    public Boolean downloadAllCoupon(String sessionToken) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken :: "+sessionToken));
        //회원이 다운가능한 모든 coupon의 id리스트를 받아옴.
        List<Long> downloadableCouponList = couponRepository.findDownloadableCouponList(member.getId(),LocalDateTime.now());
        List<CouponMember> couponMemberList = new ArrayList<>();//새로생성될 couponMember가 저장될 리스트.

        /*모든 다운가능한 coupon들에 대해서, 쿠폰을 다운(couponeMember를 생성)하고 couponMemberList에 저장시켜줌.  */
        for(int i=0; i<downloadableCouponList.size(); i++){
            Long couponId = downloadableCouponList.get(i);
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(()->new ResourceNotFoundException("coupon not found for this id :: "+couponId));

            /*couponIssueCode 생성*/
            CouponMember lastCouponMember = couponMemberRepository.findTopByCouponOrderByCreatedDateDesc(coupon);
            String lastCouponIssueCode = lastCouponMember.getCouponIssueCode();
            int lastCouponIssueCodeNum = Integer.parseInt(lastCouponIssueCode.substring(8));
            String newCouponIssueCode = coupon.getCouponCreateCode()+ (lastCouponIssueCodeNum+1);

            CouponMember couponMember = CouponMember.builder()
                    .coupon(coupon)
                    .couponIssueCode(newCouponIssueCode)
                    .member(member)
                    .isUse(false)
                    .regDate(LocalDateTime.now())
                    .useDate(null)
                    .orders(null)
                    .createdBy(member)
                    .modifiedBy(member)
                    .build();
            couponMemberList.add(couponMember);
        }
        /*생성된 couponMember들을 저장. */
        couponMemberRepository.saveAll(couponMemberList);
        return true;
    }

    /*쿠폰 생성*/
    @Transactional
    public Long makeCoupon(String token, String name, Integer saleValue, String description, Integer maxIssueCount,
                           LocalDate issueStartDate, LocalDate issueEndDate, LocalDate effectiveStartDate, LocalDate effectiveEndDate) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member"));

        /*member의 권한확인*/
        if(member.getRoles() != Role.admin){throw new RuntimeException("쿠폰 생성의 권한이 없습니다.");}

        /*coupon create code 생성. 형식은 yyMM(*4자리일련번호*) */
        String couponCreateCode = null;
        LocalDate today = LocalDate.now();
        String yyMM = today.format(DateTimeFormatter.ofPattern("yyMM"));
        /*coupon create code에 들어갈 일련번호 생성*/
        int maxOfMonth = YearMonth.from(LocalDateTime.now()).lengthOfMonth();
        LocalDateTime start = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = LocalDateTime.now().withDayOfMonth(maxOfMonth).withHour(0).withMinute(0).withSecond(0).withNano(0);
        Coupon lastCreatedCoupon = couponRepository.findTop1ByCreatedDateGreaterThanAndCreatedDateLessThanOrderByCreatedDateDesc(
                start, end
        );
        if(lastCreatedCoupon != null) {
            int sequenceNum = Integer.parseInt(lastCreatedCoupon.getCouponCreateCode().substring(4)) + 1;
            couponCreateCode = yyMM + String.format("%04d",sequenceNum);
        }
        else{ couponCreateCode = yyMM + "0001";}

        /*쿠폰 생성*/
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.amount)
                .couponCreateCode(couponCreateCode)
                .couponName(name)
                .exposureStartDate(issueStartDate)
                .exposureEndDate(issueEndDate)
                .saleValues(saleValue)
                .maxIssueCount(maxIssueCount)
                .effectiveStartDate(effectiveStartDate)
                .effectiveEndDate(effectiveEndDate)
                .issueQty(0)
                .useQty(0)
                .isIssue(true)
                .isUse(true)
                .fromPurchaseAmount(0)
                .toPurchaseAmount(1000000)
//                .brfIntroduction()
                .couponDescription(description)
                .createdBy(member)
                .modifiedBy(member)
                .build();
        couponRepository.save(coupon);

        return coupon.getId();
    }

    /*쿠폰 리스트 검색*/
    @Transactional
    public Page<CouponManageDtoForPage> getCouponList(int page, String token, CouponSearchConditionDto dto) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));

        /*권한 확인.*/
        if(member.getRoles() != Role.admin){throw new RuntimeException("쿠폰 조회 권한이 없습니다.");}

        /*조건 형식변환*/
        Integer couponType = null;
        if(dto.getCouponType()!=null){ couponType = CouponType.valueOf(dto.getCouponType()).ordinal();}

        Pageable pageable = PageRequest.of(page, 30, JpaSort.unsafe(Sort.Direction.DESC,"("+dto.getSort()+")"));
        Page<CouponManageDtoForPage> result = couponRepository.getCouponList(
            dto.getCouponId(), couponType, dto.getCouponCreateCode(), dto.getCouponName(), dto.getExposureStartDate(),
                dto.getExposureEndDate(), dto.getSaleValuesStart(), dto.getSaleValuesEnd(), dto.getEffectiveStartDate(), dto.getEffectiveEndDate(),
                dto.getIssueQtyStart(), dto.getIssueQtyEnd(), dto.getUseQtyStart(), dto.getUseQtyEnd(), dto.getIsIssue(),
                dto.getIsUse(), dto.getCouponDescription(), dto.getCreatedBy(), dto.getModifiedBy(), pageable
        );

        return result;
    }

}
