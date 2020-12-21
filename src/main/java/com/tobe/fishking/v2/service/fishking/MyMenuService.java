package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.ReviewDto;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.CouponMemberRepository;
import com.tobe.fishking.v2.repository.common.CouponRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.ReviewRepository;
import com.tobe.fishking.v2.repository.fishking.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    FishingDiaryRepository fishingDiaryRepository;
    @Autowired
    FishingDiaryCommentRepository fishingDiaryCommentRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    OrderDetailsRepository orderDetailsRepository;
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    ShipRepository shipRepository;
    @Autowired
    Environment env;


    /*마이메뉴 페이지 조회 처리 메소드
    * - member의 프사, nickname, 예약건수, 쿠폰 수를 dto에 담아서 반환. */
    @Transactional
    public MyMenuPageDTO getMyMenuPage(String sessionToken) throws ResourceNotFoundException {
        MyMenuPageDTO myMenuPageDTO = null;

        /*repository로부터 값 가져옴. */
            //프사가져옴.
            Member member = memberRepository.findBySessionToken(sessionToken)
                    .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));
            String profileImage = env.getProperty("file.downloadUrl")+member.getProfileImage();
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
    @Transactional
    public Page<FishingDiaryDtoForPage> getMyFishingDiary(String sessionToken, int page) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));
        Pageable pageable = PageRequest.of(page,10);
        return fishingDiaryRepository.findByMember(member,pageable);
    }

    /*내글관리 - 댓글
     * - 댓글을 단 fishingDiary가 선상인지 갯바위인지, fishingDiary 제목, 댓글 내용, 시간이 담긴 DTO를 Page로 반환. */
    @Transactional
    public Page<FishingDiaryCommentDtoForPage> getMyFishingDiaryComment(String sessionToken, int page) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));
        Pageable pageable = PageRequest.of(page, 10);
        return fishingDiaryCommentRepository.findByMember(member, pageable);
    }

    /*내글관리 - 스크랩*/
    @Transactional
    public Page<FishingDiaryDtoForPage> getMyFishingDiaryScrap(String sessionToken, int page) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for thid sessionToken ::"+sessionToken));
        Pageable pageable = PageRequest.of(page,10);
        return fishingDiaryRepository.findByScrapMembers(member,pageable);
    }

    /*내글관리 - 리뷰*/
    @Transactional
    public Page<ReviewDto> getMyReview(String sessionToken, int page) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken :: "+sessionToken));

        Pageable pageable = PageRequest.of(page,10);
        return reviewRepository.findMyReviewList(member, pageable);
    }

    /*내 예약 리스트 조회*/
    @Transactional
    public Page<OrdersDtoForPage> getMyOrdersList(String sessionToken, int page, String sort) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));
        Pageable pageable = PageRequest.of(page,10);
        if(sort.equals("none")){ return ordersRepository.findByCreatedByOrderByOrderStatus(member, pageable);}
        return ordersRepository.findByCreatedByAndOrderStatus(member, OrderStatus.valueOf(sort).ordinal(), pageable);
    }

    /*예약 상세보기 */
    @Transactional(readOnly = true)
    public OrdersDetailDto getOrdersDetail(Long orderId) throws ResourceNotFoundException {
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("orders not found for this id ::"+orderId));
        OrderDetails orderDetails = orderDetailsRepository.findByOrders(orders);
        Goods goods = goodsRepository.findById(orderDetails.getGoods().getId())
                .orElseThrow(()->new ResourceNotFoundException("goods not found for this id ::"+orderDetails.getGoods().getId()));
        Member member = memberRepository.findById(orders.getCreatedBy().getId())
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+orders.getCreatedBy().getId()));
        Ship ship = shipRepository.findById(goods.getShip().getId())
                .orElseThrow(()->new ResourceNotFoundException("ship not found for this id ::"+goods.getShip().getId()));


        /*dto 생성.*/
        OrdersDetailDto ordersDetailDto = OrdersDetailDto.builder()
                .id(orders.getId())
                .shipName(ship.getShipName())
                .orderStatus(orders.getOrderStatus().getValue())
                .fishingType(goods.getFishingType().getValue())
                .sigungu(ship.getSigungu())
                .distance(ship.getDistance())
                .fishingDate(goods.getFishingDate())
                //.fishSpecies()
                .meridiem(goods.getMeridiem().getValue())
                .shipStartTime(goods.getShipStartTime())
                .goodsPrice(goods.getTotalAmount())
                .personnel(orderDetails.getPersonnel())
                .ordersNum(orders.getOrdersNum())
                .nickName(member.getNickName())
                .areaCode(member.getPhoneNumber().getAreaCode())
                .localNumber(member.getPhoneNumber().getLocalNumber())
                .orderDate(orders.getOrderDate())
                //.paymentGroup(orders.getPaymentGroup())
                .totalAmount(orders.getTotalAmount())
                .discountAmount(orders.getDiscountAmount())
                .paymentAmount(orders.getPaymentAmount())
                .build();

        List<CommonCode> list = goods.getFishSpecies();
        if(list.size()!=0) {
            String fishSpecies = list.get(0).getCodeName();
            for (int i = 1; i < list.size(); i++) {
                fishSpecies += "," + list.get(i).getCodeName();
            }
            ordersDetailDto.setFishSpecies(fishSpecies);
        }

        return ordersDetailDto;
    }

}
