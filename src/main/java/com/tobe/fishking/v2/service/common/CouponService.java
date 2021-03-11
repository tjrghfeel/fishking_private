package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Coupon;
import com.tobe.fishking.v2.entity.fishing.CouponMember;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.CouponDTO;
import com.tobe.fishking.v2.model.common.CouponDownloadDto;
import com.tobe.fishking.v2.model.common.CouponMemberDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.CouponMemberRepository;
import com.tobe.fishking.v2.repository.common.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        //!!!!!!!!!!!!쿠폰 번호 생성. (일단은 랜덤숫자 넣음. 번호어떻게만들지 정해지고 수정필요.
        String couponCodeOfCouponMember = coupon.getCouponCode() + String.format("%03d",(int)(Math.random()*1000));

        CouponMember couponMember = CouponMember.builder()
                .coupon(coupon)
                .couponCode(couponCodeOfCouponMember)
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
            CouponMember couponMember = CouponMember.builder()
                    .coupon(coupon)
                    .couponCode(coupon.getCouponCode() + String.format("%03d",(int)(Math.random()*1000)))
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
}
