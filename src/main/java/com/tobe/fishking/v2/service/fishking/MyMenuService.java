package com.tobe.fishking.v2.service.fishking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.ReviewDto;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.service.auth.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    AlertsRepository alertsRepository;
    @Autowired
    ObserverCodeRepository observerCodeRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    TidalLevelRepository tidalLevelRepository;

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
            Integer bookingCount = ordersRepository.countCurrentMyOrders( member.getId());
            //쿠폰 수 가져옴.
            Integer couponCount = couponMemberRepository.countByMemberAndIsUseAndDays(member,false, LocalDateTime.now());
            //알림 개수 가져옴.
            Integer alertCount = alertsRepository.countBySessionToken(sessionToken);

        /*dto에 값 넣어줌. */
        myMenuPageDTO = MyMenuPageDTO.builder()
                .profileImage(profileImage)
                .nickName(nickName)
                .bookingCount(bookingCount)
                .couponCount(couponCount)
                .alertCount(alertCount)
                .build();

        return myMenuPageDTO;
    }

    /*내글관리 - 게시글 */
    @Transactional
    public Page<FishingDiaryDtoForPage> getMyFishingDiary(String sessionToken, int page) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+sessionToken));
        Pageable pageable = PageRequest.of(page,10);
        return fishingDiaryRepository.findByMember(member, member,pageable);
    }

    /*내글관리 - 댓글
     * - 댓글을 단 fishingDiary가 선상인지 갯바위인지, fishingDiary 제목, 댓글 내용, 시간이 담긴 DTO를 Page로 반환. */
    @Transactional
    public Page<MyFishingDiaryCommentDtoForPage> getMyFishingDiaryComment(String sessionToken, int page) throws ResourceNotFoundException {
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
//                .fishingType(goods.getFishingType().getValue())
                .fishingType(ship.getFishingType().getValue())
                .sigungu(ship.getSigungu())
                .distance(ship.getDistance())
                .fishingDate(goods.getFishingDate())
                //.fishSpecies()
                .meridiem(goods.getMeridiem().getValue())
                .shipStartTime(goods.getShipStartTime())
                .goodsPrice(goods.getTotalAmount())
                .personnel(orderDetails.getPersonnel())
                .ordersNum(orders.getOrdersNum())
                .memberName(member.getMemberName())
                .areaCode(member.getPhoneNumber().getAreaCode())
                .localNumber(member.getPhoneNumber().getLocalNumber())
                .orderDate(orders.getOrderDate())
                //.paymentGroup(orders.getPaymentGroup())
                .totalAmount(orderDetails.getTotalAmount())
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

    /*관측지점 목록 반환*/
    /*@Transactional
    public List<ObserverDtoList> getSearchPointList(String token) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));

        return observerCodeRepository.getObserverList(member.getId(), );
    }*/

    /*오늘의 물때정보 반환*/
    /*@Transactional
    public TodayTideDto getTodayTide(Long observerId, String token) throws IOException, ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        ObserverCode observer = observerCodeRepository.findById(observerId)
                .orElseThrow(()->new ResourceNotFoundException("observer code not found for this id :: "+observerId));

        String weather = null;
        String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst" +
                "?serviceKey=Cnd72OYCx%2BsOLJ1xCdGngFgZUPJBj3ULqLX%2Fj%2BKW2JOtoAxQLjZ4wU%2Fc8hUf4DL7mAHx0USlJ9K0K1tUd6QP%2BA%3D%3D" +
                "&numOfRows=50&pageNo=1" +
                "&dataType=JSON" +
                "&base_date="+todayDate+"" +
                "&base_time=0500" +
                "&nx=" +observer.getXGrid()+
                "&ny="+observer.getYGrid();
        String response = memberService.sendRequest(url,"GET",new HashMap<String,String>(),"");
        System.out.println("result>>> "+response);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> tempResponse1 = mapper.readValue(response, Map.class);
        Map<String,Object> tempResponse2 = (Map<String,Object>)tempResponse1.get("response");
        Map<String,Object> tempResponse3 = (Map<String,Object>)tempResponse2.get("body");
        Map<String,Object> tempResponse4 = (Map<String,Object>)tempResponse3.get("items");
        ArrayList<Map<String,Object>> tempResponse5 = (ArrayList<Map<String,Object>>)tempResponse4.get("item");

//        Map<String,Object> tempResponse5 = (Map<String,Object>)tempResponse4.get("response");


//        String plid = (String)userInfo.get("plid");


        String[] tideTimeList =null;
        String[] tideLevelList =null;

        String[] alertKeyList = null;

        TodayTideDto result = TodayTideDto.builder()
                .observerId(observerId)
                .observerName(observer.getName())
//                .isAlerted()
//                .weather()
                .build();
        return result;


    }*/

    /*오늘의 물때정보 알람 설정(조위알람) */
    /*@Transactional
    public Long addTideLevelAlert(
            AlertType alertType,
            String token
    ){
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));



        Alerts alerts = Alerts.builder()
                .alertType()
                .content()
                .isRead()
                .receiver()
                .alertTime()
                .createdBy()
                .build();
    }*/

    /*물때 알림 추가*/
    /*@Transactional
    public Long addTideAlert(Long observerId, Integer[] tide, Integer[] day, Integer[] time ){


    }*/

    /*만조,간조 설정*/
//    @Transactional
//    public void setHighAndLowWater(LocalDate date){
//        List<TidalLevel> tidalLevelList = tidalLevelRepository.findAllByDate(date);
//        TidalLevel preLevel = null;
//        TidalLevel currentLevel = null;
//
//        Boolean status = null;//true : 조위 증가 상태, false : 조위 감소 상태
//        for(int i=0; i<tidalLevelList.size(); i++){
//            if(preLevel.getLevel() < currentLevel.getLevel() && status == false){ currentLevel.setLowWater();}
//            else if(preLevel.getLevel() > currentLevel.getLevel() && status == true){currentLevel.setHighWater();}
//            else{status = status;}
////            if()
//        }
//
//        /*for(int i=0; i<tidalLevelList.size()-2; i++){
//            preLevel = tidalLevelList.get(i);
//            currentLevel = tidalLevelList.get(i+1);
//            nextLevel = tidalLevelList.get(i+2);
//
//            if(preLevel.getLevel() < currentLevel.getLevel() && currentLevel.getLevel() >= nextLevel.getLevel()){
//                currentLevel.setHighWater();
//            }
//            else if(preLevel.getLevel() > currentLevel.getLevel() && currentLevel.getLevel() < nextLevel.getLevel()){
//                currentLevel.setLowWater();
//            }
//        }*/
//
//    }
}
