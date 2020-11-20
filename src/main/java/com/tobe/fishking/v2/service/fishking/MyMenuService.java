package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.MyMenuPageDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.CouponMemberRepository;
import com.tobe.fishking.v2.repository.common.CouponRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.fishking.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class MyMenuService {
    @Autowired
    FileRepository fileRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrdersRepository ordersRepository;
    @Autowired
    CouponMemberRepository couponMemberRepository;

    /*마이메뉴 페이지 조회 처리 메소드
    * - member의 프사, nickname, 예약건수, 쿠폰 수를 dto에 담아서 반환. */
    @Transactional
    public MyMenuPageDTO getMyMenuPage(Long memberId) throws ResourceNotFoundException {
        MyMenuPageDTO myMenuPageDTO = null;

        /*repository로부터 값 가져옴. */
            //프사가져옴.
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));
            String profileImage = member.getProfileImage();
            //nickName 가져옴
            String nickName = member.getNickName();
            //예약건수 가져옴.
            Integer bookingCount = ordersRepository.countByOrderStatusAndCreatedBy(OrderStatus.payment, member);
            //쿠폰 수 가져옴.
            Integer couponCount = couponMemberRepository.countByMemberAndIsUseAndDays(member,false, LocalDateTime.now());

        /*dto에 값 넣어줌. */
        myMenuPageDTO = MyMenuPageDTO.builder()
                .profileImage(profileImage)
                .nickName(nickName)
                .bookingCount(bookingCount)
                .couponCount(couponCount)
                .build();

        return myMenuPageDTO;
    }



    /*내글관리 - 게시글 */
   /* @Transactional
    public ??? getMyFishingDiary(){

        
        
        *//*repository로부터 Page형태로 데이터받아옴. *//*


    }*/


}
