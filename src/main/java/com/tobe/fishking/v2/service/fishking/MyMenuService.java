package com.tobe.fishking.v2.service.fishking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.*;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.fishing.EntityType;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.enums.fishing.SeaDirection;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.TideException;
import com.tobe.fishking.v2.model.common.ReviewDto;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.model.response.TidalLevelResponse;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.utils.DateUtils;
import com.tobe.fishking.v2.utils.HolidayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    @Autowired
    CodeGroupRepository codeGroupRepository;
    @Autowired
    CommonCodeRepository commonCodeRepository;

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
        Integer alertCount = alertsRepository.countByMember(member.getId(), LocalDateTime.now());

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
//        return fishingDiaryRepository.findByMember(member, member,pageable);
        return fishingDiaryRepository.getFishingDiaryListOrderByCreatedDate(
                FilePublish.fishingBlog.ordinal(), null, null, null, null, null, member.getId(),
                true, null, null, null, null, null, null, null,null,
                null,false, pageable
        );
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
        return fishingDiaryRepository.getMyScrapList(member.getId(),pageable);
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
                .sigungu(ship.getAddress())
                .latitude(ship.getLocation().getLatitude())
                .longitude(ship.getLocation().getLongitude())
//                .distance(ship.getDistance())
                .fishingDate(orders.getFishingDate().replace("-", ""))
                //.fishSpecies()
//                .meridiem(goods.getMeridiem().getValue())
                .shipStartTime(goods.getFishingStartTime())
                .goodsPrice(goods.getTotalAmount())
                .personnel(orderDetails.getPersonnel())
                .ordersNum(orders.getOrderNumber())
                .memberName(member.getMemberName())
                .areaCode(member.getPhoneNumber().getAreaCode())
                .localNumber(member.getPhoneNumber().getLocalNumber())
                .orderDate(orders.getOrderDate().replaceAll("-", ""))
                //.paymentGroup(orders.getPaymentGroup())
                .totalAmount(orderDetails.getTotalAmount())
                .discountAmount(orders.getDiscountAmount())
                .paymentAmount(orders.getPaymentAmount())
                .goodsName(goods.getName())
                .tradeNumber(orders.getTradeNumber())
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
    public List<ObserverDtoList> getSearchPointList(String token, String searchKey, AlertType alertType) throws ResourceNotFoundException {
        Long memberId = null;
        if(token != null) {
            Member member = memberRepository.findBySessionToken(token)
                    .orElseThrow(() -> new ResourceNotFoundException("member not found for this token :: " + token));
            memberId = member.getId();
        }
        return observerCodeRepository.getObserverList(memberId, searchKey, alertType.ordinal(), LocalDateTime.now());
    }

    /*오늘의 물때정보 반환*/
    @Transactional
    public TodayTideDto getTodayTide(Long observerId, String token) throws IOException, ResourceNotFoundException {
        TodayTideDto result = null;
        Member member = null;
        if(token !=null) {
            member = memberRepository.findBySessionToken(token)
                    .orElseThrow(() -> new ResourceNotFoundException("member not found for this token :: " + token));
        }
        ObserverCode observer = observerCodeRepository.findById(observerId)
                .orElseThrow(()->new ResourceNotFoundException("observer not found for this id :: "+observerId));
        Boolean isAlerted = alertsRepository.existsByAlertTimeGreaterThanAndReceiverAndPidAndEntityTypeAndAlertTypeAndIsSent(
                LocalDateTime.now(), member, observerId, EntityType.observerCode, AlertType.tideLevel, false);
        /*tideTimeList*/
//        ArrayList<String> tideTimeList = new ArrayList<>();
//        ArrayList<String> tideLevelList = new ArrayList<>();
//        List<TidalLevel> tideList = tidalLevelRepository.findAllByDateAndIsHighWaterAndIsLowWaters(LocalDate.now(), observer);
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<TidalLevelResponse> tideList = tidalLevelRepository.findAllByDateAndCode(DateUtils.getDateFromString(date), observer.getCode());
//        for(int i=0; i<tideList.size(); i++){
//            String tideTime = tideList.get(i).getDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
//            String tideLevel = tideList.get(i).getLevel().toString();
//            tideTimeList.add(tideTime);
//            tideLevelList.add(tideLevel);
//        }

        result = TodayTideDto.builder()
                .observerId(observerId)
                .observerName(observer.getName())
                .isAlerted(isAlerted)
                .date(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .tideList(tideList)
//                .tideTimeList(tideTimeList)
//                .tideLevelList(tideLevelList)
                .build();

        /*알림 리스트*/
        ArrayList<String> tidalAlertTimeList = new ArrayList<>();

        List<Alerts> preAlertList = alertsRepository.findAllByAlertTimeGreaterThanAndReceiverAndAlertTypeAndPidAndIsSent(
                LocalDateTime.now(), member, AlertType.tideLevel, observerId,false);
        for(int i=0; i<preAlertList.size(); i++){
            Alerts alert = preAlertList.get(i);
            List<CommonCode> alertSet = alert.getAlert_sets();
            tidalAlertTimeList.add(alertSet.get(0).getCode());

//            String content = preAlertList.get(i).getContent();
//            String[] contentToken = content.split(" ");
//            String highLow = contentToken[1];
//            Integer time = Integer.parseInt(contentToken[2]);
//
//            if(highLow.equals("high")){
//                switch (time){
//                    case -2: result.setHighWaterBefore2(true);break;
//                    case -1: result.setHighWaterBefore1(true);break;
//                    case 0: result.setHighWater(true);break;
//                    case 1: result.setHighWaterAfter1(true);break;
//                    case 2: result.setHighWaterAfter2(true);break;
//                }
//            }
//            else if(highLow.equals("low")){
//                switch (time){
//                    case -2: result.setLowWaterBefore2(true);break;
//                    case -1: result.setLowWaterBefore1(true);break;
//                    case 0: result.setLowWater(true);break;
//                    case 1: result.setLowWaterAfter1(true);break;
//                    case 2: result.setLowWaterAfter2(true);break;
//                }
//            }
        }
        result.setTidalAlertTimeList(tidalAlertTimeList);

        /*오늘의 날씨 */
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

        CodeGroup codeGroup = codeGroupRepository.findByCode("etcImg");
        ArrayList<String> weather = new ArrayList<>();
        String imgUrl=null;

        if(sky ==1){//맑음
            weather.add("맑음");
        }
        else if(sky == 3){//구름많음
            switch (pty){
                case 0:
                    weather.add("구름많음");break;
                case 1: case 5:
                    weather.add("구름많고 비");break;
                case 2: case 6:
                    weather.add("구름많고 비/눈");break;
                case 3: case 7:
                    weather.add("구름많고 눈");break;
                case 4:
                    weather.add("구름많고 소나기");break;
                default: weather = null;
            }
        }
        else if(sky == 4){//흐림
            switch (pty){
                case 0:
                    weather.add("흐림");break;
                case 1: case 5:
                    weather.add("흐리고 비");break;
                case 2: case 6:
                    weather.add("흐리고 비/눈");break;
                case 3: case 7:
                    weather.add("흐리고 눈");break;
                case 4:
                    weather.add("흐리고 소나기");break;
                default: weather = null;
            }
        }
        else{
            weather = null;
        }
        if(weather != null) {
            imgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, weather.get(0)).getExtraValue1();
            imgUrl = env.getProperty("file.downloadUrl") + imgUrl;
            weather.add(imgUrl);
        }
        result.setWeather(weather);

        return result;
    }

    /*오늘의 물때정보 알람 설정(조위알람) */
    @Transactional
    public Boolean addTideLevelAlert(
            ArrayList<String> alertTimeList, Long observerId, String token
    ) throws ResourceNotFoundException, TideException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        ObserverCode observer = observerCodeRepository.findById(observerId)
                .orElseThrow(()->new ResourceNotFoundException("observer not found for this id :: "+observerId));

        /*간조,만조 알람 구분*/
        ArrayList<String> highTideAlert = new ArrayList<>();
        ArrayList<String> lowTideAlert = new ArrayList<>();
        for(int i=0; i<alertTimeList.size(); i++){
            String time = alertTimeList.get(i);
            if(time.contains("high")){  highTideAlert.add(time); }
            else if(time.contains("low")){lowTideAlert.add(time);}
        }

        CodeGroup codeGroup = codeGroupRepository.findByCode("tidalLevelAlert");
        List<CommonCode> highTideAlertCodeList = commonCodeRepository.findCommonCodesByCodeGroupAndCodes(codeGroup,highTideAlert);
        List<CommonCode> lowTideAlertCodeList = commonCodeRepository.findCommonCodesByCodeGroupAndCodes(codeGroup,lowTideAlert);

        List<Alerts> preAlertList = alertsRepository.findAllByAlertTimeGreaterThanAndReceiverAndAlertTypeAndPidAndIsSent(
                LocalDateTime.now(), member,AlertType.tideLevel,observerId,false);
        alertsRepository.deleteAll(preAlertList);

        List<TidalLevel> list = tidalLevelRepository.findTop6ByDateTimeGreaterThanAndPeakAndObserverCodeOrderByDateTimeAsc(
                LocalDateTime.now().minusDays(1), "high", observer
        );
        if(list.isEmpty()){throw new TideException("조위데이터가 없습니다.");}

        for (CommonCode commonCode : highTideAlertCodeList) {
            Integer time = Integer.parseInt(commonCode.getExtraValue1());
            LocalDateTime alertTime = list.get(0).getDateTime();
            alertTime = alertTime.plusHours(time);
            for (int j = 1; (alertTime.compareTo(LocalDateTime.now()) < 0); j++) {
                if (list.size() < j) {
                    throw new TideException("알림을 설정할 수 없습니다.");
                }
                alertTime = list.get(j).getDateTime();
                alertTime = alertTime.plusHours(time);
            }
            List<CommonCode> alertSet = new ArrayList<>();
            alertSet.add(commonCode);

            String sentence = "\'" + observer.getName() + "\' \'" + commonCode.getCodeName() + "\'입니다.";

            Alerts alerts = Alerts.builder()
                    .alert_sets(alertSet)
                    .alertType(AlertType.tideLevel)
                    .entityType(EntityType.observerCode)
                    .pid(observerId)
                    .content(observer.getName() + " high " + time)
                    .sentence(sentence)
                    .isRead(false)
                    .isSent(false)
                    .receiver(member)
                    .alertTime(alertTime)
                    .createdBy(member)
                    .type("c")
                    .build();
            alerts = alertsRepository.save(alerts);
        }

        list = tidalLevelRepository.findTop6ByDateTimeGreaterThanAndPeakAndObserverCodeOrderByDateTimeAsc(
                LocalDateTime.now().minusDays(1), "low", observer
        );
        if(list.size() < 0){throw new RuntimeException("조위데이터가 없습니다.");}

        for(int i=0; i<lowTideAlertCodeList.size();  i++){
            CommonCode commonCode = lowTideAlertCodeList.get(i);
            Integer time = Integer.parseInt(commonCode.getExtraValue1());
            LocalDateTime alertTime = list.get(0).getDateTime();
            alertTime = alertTime.plusHours(time);
            for(int j=1; (alertTime.compareTo(LocalDateTime.now()) < 0); j++){
                if(list.size() < j){ throw new RuntimeException("알림을 설정할 수 없습니다.");}
                alertTime = list.get(j).getDateTime();
                alertTime = alertTime.plusHours(time);
            }
            List<CommonCode> alertSet = new ArrayList<>();
            alertSet.add(commonCode);

            String sentence = "\'"+observer.getName() + "\' \'" + commonCode.getCodeName() + "\'입니다.";

            Alerts alerts = Alerts.builder()
                    .alert_sets(alertSet)
                    .alertType(AlertType.tideLevel)
                    .entityType(EntityType.observerCode)
                    .pid(observerId)
                    .content(observer.getName()+" low "+time)
                    .sentence(sentence)
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
    ) throws ResourceNotFoundException, IOException, ParseException {
        TideByDateDto result = null;
        Member member = null;
        if(token != null) {
            member = memberRepository.findBySessionToken(token)
                    .orElseThrow(() -> new ResourceNotFoundException("member not found for this token :: " + token));
        }
        ObserverCode observer = observerCodeRepository.findById(observerId)
                .orElseThrow(()->new ResourceNotFoundException("observer not found for this id :: "+observerId));
        Boolean isAlerted = alertsRepository.existsByAlertTimeGreaterThanAndReceiverAndPidAndEntityTypeAndAlertTypeAndIsSent(
                LocalDateTime.now(), member, observerId, EntityType.observerCode, AlertType.tide,false);
//        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        /*tideTimeList*/
//        ArrayList<String> tideTimeList = new ArrayList<>();
//        ArrayList<String> tideLevelList = new ArrayList<>();
//        List<TidalLevel> tideList = tidalLevelRepository.findAllByDateAndIsHighWaterAndIsLowWaters(date, observer);
//        for(int i=0; i<tideList.size(); i++){
//            String tideTime = tideList.get(i).getDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
//            String tideLevel = tideList.get(i).getLevel().toString();
//            tideTimeList.add(tideTime);
//            tideLevelList.add(tideLevel);
//        }
        List<TidalLevelResponse> tideList = tidalLevelRepository.findAllByDateAndCode(DateUtils.getDateFromString(dateString), observer.getCode());

        /*알림 리스트*/
        ArrayList<String> alertTideList = new ArrayList<String>();
        ArrayList<String> alertDayList = new ArrayList<String>();
        ArrayList<String> alertTimeList = new ArrayList<String>();

        List<Alerts> preAlertList = alertsRepository.findAllByAlertTimeGreaterThanAndReceiverAndAlertTypeAndPidAndIsSent(
                LocalDateTime.now(), member, AlertType.tide, observerId,false);
        for(int i=0; i<preAlertList.size(); i++){
            Alerts alert = preAlertList.get(i);
            List<CommonCode> commonCodeList = alert.getAlert_sets();
            for(int j=0; j<commonCodeList.size(); j++){
                CommonCode commonCode = commonCodeList.get(j);
                if(commonCode.getCodeGroup().getCode().equals("tideAlertTide8")) {
                    boolean exist = false;
                    for(int l=0; l<alertTideList.size(); l++){
                        if(alertTideList.get(l).equals(commonCode.getCode())){ exist = true; }
                    }
                    if(exist ==false){alertTideList.add(commonCode.getCode());}
                }
                else if(commonCode.getCodeGroup().getCode().equals("tideAlertDay")) {
                    boolean exist = false;
                    for(int l=0; l<alertDayList.size(); l++){
                        if(alertDayList.get(l).equals(commonCode.getCode())){ exist = true; }
                    }
                    if(exist ==false){alertDayList.add(commonCode.getCode());}
                }
                else if(commonCode.getCodeGroup().getCode().equals("tideAlertTime")){
                    boolean exist = false;
                    for(int l=0; l<alertTimeList.size(); l++){
                        if(alertTimeList.get(l).equals(commonCode.getCode())){ exist = true; }
                    }
                    if(exist ==false){alertTimeList.add(commonCode.getCode());}
                }
            }
        }

        result = TideByDateDto.builder()
                .observerId(observerId)
                .observerName(observer.getName())
                .isAlerted(isAlerted)
                .date(dateString)
                .tideList(tideList)
//                .tideTimeList(tideTimeList)
//                .tideLevelList(tideLevelList)
                .alertTideList(alertTideList)
                .alertDayList(alertDayList)
                .alertTimeList(alertTimeList)
                .build();

        /*날씨*/

        result.setWeather(getWeather(observer,dateString));

        return result;
    }

    /*중기예보로 날짜별 날씨 반환.*/
    public ArrayList<String> getWeather(ObserverCode observer, String dateString) throws IOException, ParseException {
        ArrayList<String> result = new ArrayList<>();

        LocalDateTime currentTime = LocalDateTime.now();
        int time = currentTime.getHour();
        String tmFc = null;
        if(time < 6){
            currentTime.minusDays(1);
            tmFc = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "1800";
        }
        else if(time < 18 || time > 6){tmFc = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "0600";}
        else{tmFc = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "1800";}


        String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidSeaFcst?" +
                "serviceKey=Cnd72OYCx%2BsOLJ1xCdGngFgZUPJBj3ULqLX%2Fj%2BKW2JOtoAxQLjZ4wU%2Fc8hUf4DL7mAHx0USlJ9K0K1tUd6QP%2BA%3D%3D" +
                "&pageNo=1" +
                "&numOfRows=50" +
                "&dataType=JSON" +
                "&regId=" + observer.getForecastCode() +
                "&tmFc=" + tmFc;
        String response = memberService.sendRequest(url,"GET",new HashMap<String,String>(),"");
        System.out.println("result>>> "+response);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> tempResponse1 = mapper.readValue(response, Map.class);
        Map<String,Object> tempResponse2 = (Map<String,Object>)tempResponse1.get("response");
        Map<String,Object> tempResponse3 = (Map<String,Object>)tempResponse2.get("body");
        Map<String,Object> tempResponse4 = (Map<String,Object>)tempResponse3.get("items");
        ArrayList<Map<String,Object>> dataList = (ArrayList<Map<String,Object>>)tempResponse4.get("item");
        Map<String,Object> data = dataList.get(0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        long today = (sdf.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))).getTime();
        long inputDay = (sdf.parse(DateUtils.getDateFromString(dateString).format(DateTimeFormatter.ofPattern("yyyyMMdd")))).getTime();
        long dayDiff = (inputDay - today) / (24*60*60*1000);

        CodeGroup codeGroup = codeGroupRepository.findByCode("etcImg");

        if(dayDiff < 3 || dayDiff > 10){result=null;}
        else if(dayDiff <8){
            String morningWeather = (String)data.get("wf"+dayDiff+"Am");
            String afternoonWeather = (String)data.get("wf"+dayDiff+"Pm");
            result.add(morningWeather);
            String morningWeatherImgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, morningWeather).getExtraValue1();
            morningWeatherImgUrl = env.getProperty("file.downloadUrl") + morningWeatherImgUrl;
            result.add(morningWeatherImgUrl);

            result.add(afternoonWeather);
            String afternoonWeatherImgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, afternoonWeather).getExtraValue1();
            afternoonWeatherImgUrl = env.getProperty("file.downloadUrl") + afternoonWeatherImgUrl;
            result.add(afternoonWeatherImgUrl);
        }
        else{
            String weather = data.get("wf"+dayDiff) +"";
            result.add(weather);
            String weatherImgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, weather).getExtraValue1();
            weatherImgUrl = env.getProperty("file.downloadUrl") + weatherImgUrl;
            result.add(weatherImgUrl);
        }
        return result;
    }

    /*물때 알림 추가*/
    @Transactional
    public Boolean addTideAlert(Long observerId, ArrayList<String> tideList, ArrayList<String> dayList, ArrayList<String> timeList, String token )
            throws ResourceNotFoundException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        ObserverCode observer = observerCodeRepository.findById(observerId)
                .orElseThrow(()->new ResourceNotFoundException("observer code not found for this id :: "+observerId));
        SeaDirection seaDirection = observer.getSeaDirection();

        /*알림 common code들 가져옴*/
        CodeGroup codeGroup = codeGroupRepository.findByCode("tideAlertTide8");
        List<CommonCode> tideCodeList = commonCodeRepository.findCommonCodesByCodeGroupAndCodes(codeGroup, tideList);
        codeGroup = codeGroupRepository.findByCode("tideAlertDay");
        List<CommonCode> dayCodeList = commonCodeRepository.findCommonCodesByCodeGroupAndCodes(codeGroup, dayList);
        codeGroup = codeGroupRepository.findByCode("tideAlertTime");
        List<CommonCode> timeCodeList = commonCodeRepository.findCommonCodesByCodeGroupAndCodes(codeGroup, timeList);

        /*기존 알림들 삭제*/
        List<Alerts> preAlertList = alertsRepository.findAllByAlertTimeGreaterThanAndReceiverAndAlertTypeAndPidAndIsSent(
                LocalDateTime.now(), member, AlertType.tide, observerId,false);
        alertsRepository.deleteAll(preAlertList);

        /*알림 추가*/
        String todaySolar = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String todayLunar = holidayUtil.convertSolarToLunar(todaySolar);

        for(int i=0; i<tideCodeList.size(); i++){
            /*물때 계산*/
            CommonCode tideCode = tideCodeList.get(i);
            Integer tide = Integer.parseInt(tideCode.getExtraValue1());
            Integer tideDayDiff = null; //알람 요청한 물때가 현재로부터 몇일 후인지.
            Integer lunarDay = Integer.parseInt(todayLunar.substring(8));
            Integer todayTide = (lunarDay+6)%15 +1;
            tideDayDiff = tide - todayTide;
            if(tideDayDiff<0) tideDayDiff += 15;

            for(int j=0; j<dayCodeList.size(); j++){
                CommonCode dayCode = dayCodeList.get(j);
                Integer day = Integer.parseInt(dayCode.getExtraValue1());
                Integer afterDays = tideDayDiff - day;
                if(afterDays < 0) afterDays += 15;//알림 시점이 지났으면 다음 물때에 대해 알림을 설정해준다.

                LocalDateTime today = LocalDateTime.now();

                for(int l=0; l<timeCodeList.size(); l++){
                    CommonCode timeCode = timeCodeList.get(l);
                    Integer time = Integer.parseInt(timeCode.getExtraValue1());
                    LocalDateTime alertTime = today.plusDays(afterDays);
                    alertTime = alertTime.withHour(time).withMinute(0).withSecond(0);
                    if(today.compareTo(alertTime)>0){ alertTime = alertTime.plusDays(15); }/*today.plusDays()하기때문에 보통의 경우 today가 alertTime보다 이후가 될일은 없다.
                                                            근데, 오늘당일이어서 plusDays로 더한날짜가 0이고 설정된시간이 이미지난시간일경우 today가
                                                            이후가 되고, 이경우 다음 물때주기에 대해 알림을 설정하는게 아닌 그냥 무시하겠다는의미. 너무복잡해져서. */
                    List<CommonCode> alertSet = new ArrayList<>();
                    alertSet.add(tideCode);
                    alertSet.add(dayCode);
                    alertSet.add(timeCode);

                    String alertSentence = "\'"+observer.getName() + "\' \'" + tideCode.getCodeName() + "\' \'" + dayCode.getCodeName() +
                            "\' \'" + timeCode.getCodeName() + "\' 알람입니다.";

                    Alerts alerts = Alerts.builder()
                            .alert_sets(alertSet)
                            .alertType(AlertType.tide)
                            .content(observer.getName()+" "+tide+" "+day+" "+time)
                            .sentence(alertSentence)
                            .isRead(false)
                            .receiver(member)
                            .alertTime(alertTime)
                            .entityType(EntityType.observerCode)
                            .pid(observerId)
                            .isSent(false)
                            .createdBy(member)
                            .type("c")
                            .build();
                    alerts = alertsRepository.save(alerts);
                    alerts.getAlertTime();
                }
            }
        }
        return true;
    }

    /*실시간 조항 리스트 가져오기*/
    @Transactional
    public Page<LiveShipDtoForPage> getLiveShipList(int page){
        Pageable pageable = PageRequest.of(page, 20);
        return shipRepository.getLiveShipList(pageable);
    }


}
