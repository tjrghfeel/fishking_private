package com.tobe.fishking.v2.service.fishking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.common.TidalLevel;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.fishing.EntityType;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.enums.fishing.SeaDirection;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.ReviewDto;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.utils.HolidayUtil;
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

    HolidayUtil holidayUtil;

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
                .shipId(ship.getId())
                .goodsId(goods.getId())
                .shipName(ship.getShipName())
                .orderStatus(orders.getOrderStatus().getValue())
//                .fishingType(goods.getFishingType().getValue())
                .fishingType(ship.getFishingType().getValue())
                .sigungu(ship.getSigungu())
//                .distance(ship.getDistance())
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
    @Transactional
    public List<ObserverDtoList> getSearchPointList(String token, AlertType alertType) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));

        return observerCodeRepository.getObserverList(member.getId(), alertType);
    }

    /*오늘의 물때정보 반환*/
    @Transactional
    public TodayTideDto getTodayTide(Long observerId, String token) throws IOException, ResourceNotFoundException {
        TodayTideDto result = null;

        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        ObserverCode observer = observerCodeRepository.findById(observerId)
                .orElseThrow(()->new ResourceNotFoundException("observer not found for this id :: "+observerId));
        Boolean isAlerted = alertsRepository.existsByReceiverAndPidAndEntityTypeAndAlertType(
                member, observerId, EntityType.observerCode, AlertType.tide);
        /*tideTimeList*/
        ArrayList<String> tideTimeList = new ArrayList<>();
        ArrayList<String> tideLevelList = new ArrayList<>();
        List<TidalLevel> tideList = tidalLevelRepository.findAllByDateAndIsHighWaterAndIsLowWaters(LocalDate.now(), observer);
        for(int i=0; i<tideList.size(); i++){
            String tideTime = tideList.get(i).getDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String tideLevel = tideList.get(i).getLevel().toString();
            tideTimeList.add(tideTime);
            tideLevelList.add(tideLevel);
        }

        result = TodayTideDto.builder()
                .observerId(observerId)
                .observerName(observer.getName())
                .isAlerted(isAlerted)
                .date(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .tideTimeList(tideTimeList)
                .tideLevelList(tideLevelList)
                .build();

        /*알림 리스트*/
        Boolean[] alertList = new Boolean[10];

        List<Alerts> preAlertList = alertsRepository.findAllByReceiverAndAlertTypeAndPidAndIsSent(member, AlertType.tideLevel, observerId,false);
        for(int i=0; i<preAlertList.size(); i++){
            String content = preAlertList.get(i).getContent();
            String[] contentToken = content.split(" ");
            String highLow = contentToken[1];
            Integer time = Integer.parseInt(contentToken[2]);

            if(highLow.equals("high")){
                switch (time){
                    case -2: result.setHighWaterBefore2(true);break;
                    case -1: result.setHighWaterBefore1(true);break;
                    case 0: result.setHighWater(true);break;
                    case 1: result.setHighWaterAfter1(true);break;
                    case 2: result.setHighWaterAfter2(true);break;
                }
            }
            else if(highLow.equals("low")){
                switch (time){
                    case -2: result.setLowWaterBefore2(true);break;
                    case -1: result.setLowWaterBefore1(true);break;
                    case 0: result.setLowWater(true);break;
                    case 1: result.setLowWaterAfter1(true);break;
                    case 2: result.setLowWaterAfter2(true);break;
                }
            }
        }

        /*오늘의 날씨 */
        String weather = null;
        Integer sky = null;
        Integer pty = null;
        String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        String url = "http://www.khoa.go.kr/oceangrid/grid/api/fcIndexOfType/search.do?" +
//                "ServiceKey=ezm3hM52KibRN1SR6Rg3vA==" +
//                "&Type=SF" +
//                "&ResultType=json";

        LocalDateTime currentTime = LocalDateTime.now();
        int temp = (currentTime.getHour()/3)*3 - 4;
        temp = (temp < 0)? temp + 24 : temp;
        String baseTime = String.format("%02d",temp);
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String fcstTime = String.format("%02d",(currentTime.getHour()/3)*3) + "00";

        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst?" +
                "serviceKey=Cnd72OYCx%2BsOLJ1xCdGngFgZUPJBj3ULqLX%2Fj%2BKW2JOtoAxQLjZ4wU%2Fc8hUf4DL7mAHx0USlJ9K0K1tUd6QP%2BA%3D%3D" +
                "&pageNo=1" +
                "&numOfRows=100" +
                "&dataType=JSON" +
                "&base_date=" + currentDate +
                "&base_time=" + baseTime + "00" +
                "&nx=" + observer.getXGrid() +
                "&ny=" + observer.getYGrid();
        String response = memberService.sendRequest(url,"GET",new HashMap<String,String>(),"");
        System.out.println("result>>> "+response);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> tempResponse1 = mapper.readValue(response, Map.class);
        Map<String,Object> tempResponse2 = (Map<String,Object>)tempResponse1.get("response");
        Map<String,Object> tempResponse3 = (Map<String,Object>)tempResponse2.get("body");
        Map<String,Object> tempResponse4 = (Map<String,Object>)tempResponse3.get("items");
        ArrayList<Map<String,Object>> dataList = (ArrayList<Map<String,Object>>)tempResponse4.get("item");
        for(int i=0; i<dataList.size(); i++){
            Map<String,Object> item = dataList.get(i);
            if(item.get("fcstTime").equals(fcstTime) ){
                if(item.get("category").equals("SKY")){ sky = Integer.parseInt((String)item.get("fcstValue"));}
                else if(item.get("category").equals("PTY")){ pty = Integer.parseInt((String)item.get("fcstValue"));}
            }
            else{break;}
        }

        if(pty == 0) {
            switch (sky){
                case 1: result.setWeather("맑음");break;
                case 3: result.setWeather("구름많음");break;
                case 4: result.setWeather("흐림");break;
            }
        }
        else if(pty !=0){
            switch(pty){
                case 1: result.setWeather("비");break;
                case 2: result.setWeather("비/눈");break;
                case 3: result.setWeather("눈");break;
                case 4: result.setWeather("소나기");break;
                case 5: result.setWeather("빗방울");break;
                case 6: result.setWeather("빗방울/눈날림");break;
                case 7: result.setWeather("눈날림");break;
            }
        }
        return result;
    }

    /*오늘의 물때정보 알람 설정(조위알람) */
    @Transactional
    public Boolean addTideLevelAlert(
            Integer[] highTideAlert, Integer[] lowTideAlert, Long observerId, String token
    ) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        ObserverCode observer = observerCodeRepository.findById(observerId)
                .orElseThrow(()->new ResourceNotFoundException("observer not found for this id :: "+observerId));

        for(int i=0; i<highTideAlert.length; i++){
            List<TidalLevel> list = tidalLevelRepository.findTop2ByDateTimeGreaterThanAndPeakAndObserverCodeOrderByDateTimeAsc(
                    LocalDateTime.now(), "high", observer
            );

            LocalDateTime alertTime = list.get(0).getDateTime();
            alertTime = alertTime.minusHours(highTideAlert[i]);
            if(alertTime.compareTo(LocalDateTime.now())<0){
                alertTime = list.get(1).getDateTime();
                alertTime = alertTime.minusHours(highTideAlert[i]);
            }

            Alerts alerts = Alerts.builder()
                    .alertType(AlertType.tideLevel)
                    .entityType(EntityType.observerCode)
                    .pid(observerId)
                    .content(observer.getName()+" high "+highTideAlert[i])
                    .isRead(false)
                    .isSent(false)
                    .receiver(member)
                    .alertTime(alertTime)
                    .createdBy(member)
                    .build();
            alerts = alertsRepository.save(alerts);
        }
        for(int i=0; i<lowTideAlert.length;  i++){
            List<TidalLevel> list = tidalLevelRepository.findTop2ByDateTimeGreaterThanAndPeakAndObserverCodeOrderByDateTimeAsc(
                    LocalDateTime.now(), "low", observer
            );

            LocalDateTime alertTime = list.get(0).getDateTime();
            alertTime = alertTime.minusHours(lowTideAlert[i]);
            if(alertTime.compareTo(LocalDateTime.now())<0){
                alertTime = list.get(1).getDateTime();
                alertTime = alertTime.minusHours(lowTideAlert[i]);
            }

            Alerts alerts = Alerts.builder()
                    .alertType(AlertType.tideLevel)
                    .entityType(EntityType.observerCode)
                    .pid(observerId)
                    .content(observer.getName()+" low "+lowTideAlert[i])
                    .isRead(false)
                    .isSent(false)
                    .receiver(member)
                    .alertTime(alertTime)
                    .createdBy(member)
                    .build();
            alerts = alertsRepository.save(alerts);
        }

        return true;
    }

    /*날짜별 물때정보 출력*/
    @Transactional
    public TideByDateDto getTideByDate(
            Long observerId, String dateString, String token
    ) throws ResourceNotFoundException {
        TideByDateDto result = null;

        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        ObserverCode observer = observerCodeRepository.findById(observerId)
                .orElseThrow(()->new ResourceNotFoundException("observer not found for this id :: "+observerId));
        Boolean isAlerted = alertsRepository.existsByReceiverAndPidAndEntityTypeAndAlertType(
                member, observerId, EntityType.observerCode, AlertType.tide);
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        /*tideTimeList*/
        ArrayList<String> tideTimeList = new ArrayList<>();
        ArrayList<String> tideLevelList = new ArrayList<>();
        List<TidalLevel> tideList = tidalLevelRepository.findAllByDateAndIsHighWaterAndIsLowWaters(date, observer);
        for(int i=0; i<tideList.size(); i++){
            String tideTime = tideList.get(i).getDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String tideLevel = tideList.get(i).getLevel().toString();
            tideTimeList.add(tideTime);
            tideLevelList.add(tideLevel);
        }
        /*알림 리스트*/
        Boolean[] alertTideList = new Boolean[15];
        Boolean[] alertDayList = new Boolean[7];
        Boolean[] alertTimeList = new Boolean[5];

        List<Alerts> preAlertList = alertsRepository.findAllByReceiverAndAlertTypeAndPidAndIsSent(member, AlertType.tide, observerId,false);
        for(int i=0; i<preAlertList.size(); i++){
            String content = preAlertList.get(i).getContent();
            String[] contentToken = content.split(" ");
            Integer[] data = new Integer[]{Integer.parseInt(contentToken[1]), Integer.parseInt(contentToken[2]),Integer.parseInt(contentToken[3])};
            alertTideList[data[0]-1] = true;
            alertDayList[data[1]-1] = true;
            switch (data[2]){
                case 0: alertTimeList[0] = true; break;
                case 3: alertTimeList[1] = true; break;
                case 6: alertTimeList[2] = true;break;
                case 9: alertTimeList[3] = true; break;
                case 12: alertTimeList[4] = true; break;
            }
        }


        result = TideByDateDto.builder()
                .observerId(observerId)
                .observerName(observer.getName())
                .isAlerted(isAlerted)
                .date(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .tideTimeList(tideTimeList)
                .tideLevelList(tideLevelList)
                .alertTideList(alertTideList)
                .alertDayList(alertDayList)
                .alertTimeList(alertTimeList)
                .build();
        return result;
    }

    /*물때 알림 추가*/
    @Transactional
    public Long addTideAlert(Long observerId, Integer[] tideList, Integer[] dayList, Integer[] timeList, String token ) throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        ObserverCode observer = observerCodeRepository.findById(observerId)
                .orElseThrow(()->new ResourceNotFoundException("observer code not found for this id :: "+observerId));
        SeaDirection seaDirection = observer.getSeaDirection();

        /*기존 알림들 삭제*/
        List<Alerts> preAlertList = alertsRepository.findAllByReceiverAndAlertTypeAndPidAndIsSent(member, AlertType.tide, observerId,false);
        alertsRepository.deleteAll(preAlertList);

        /*알림 추가*/
        String todaySolar = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String todayLunar = holidayUtil.converSolarToLunar(todaySolar);

        for(int i=0; i<tideList.length; i++){
            /*물때 계산*/
            Integer afterDays = null; //알람 요청한 물때가 현재로부터 몇일 후인지.
            if(seaDirection == SeaDirection.west){
                Integer lunarDay = Integer.parseInt(todayLunar.substring(8));
                Integer tide = (lunarDay+6)%15;
                afterDays = tideList[i] - tide;
                if(afterDays<0) afterDays += 15;
            }
            else if(seaDirection == SeaDirection.east || seaDirection == SeaDirection.south){
                Integer lunarDay = Integer.parseInt(todayLunar.substring(8));
                Integer tide = (lunarDay+6)%15 +1;
                afterDays = tideList[i] - tide;
                if(afterDays<0) afterDays += 15;
            }

            for(int j=0; j<dayList.length; j++){

                afterDays = afterDays - dayList[j];
                if(afterDays < 0) afterDays += 15;//알림 시점이 지났으면 다음 물때에 대해 알림을 설정해준다.

                LocalDateTime today = LocalDateTime.now();
                today = today.plusDays(afterDays);

                for(int l=0; l<timeList.length; l++){
                    today = today.withHour(timeList[l]).withMinute(0).withSecond(0);

                    Alerts alerts = Alerts.builder()
                            .alertType(AlertType.tide)
                            .content(observer.getName()+" "+tideList[i]+" "+dayList[j]+" "+timeList[l])
                            .isRead(false)
                            .receiver(member)
                            .alertTime(today)
                            .entityType(EntityType.observerCode)
                            .pid(observerId)
                            .isSent(false)
                            .createdBy(member)
                            .build();
                    alerts = alertsRepository.save(alerts);
                    alerts.getAlertTime();
                }
            }
        }


        return 1L;

    }

}
