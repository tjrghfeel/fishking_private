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
import com.tobe.fishking.v2.service.common.CommonService;
import com.tobe.fishking.v2.utils.DateUtils;
import com.tobe.fishking.v2.utils.HolidayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
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
    @Autowired
    CommonService commonService;
    @Autowired
    CameraPointRepository harborRepo;


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

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<TidalLevelResponse> tideList = tidalLevelRepository.findAllByDateAndCode(DateUtils.getDateFromString(date), observer.getCode());
        if(tideList.size() > 0 && tideList.size() < 4){
            for(; tideList.size() != 4;){ tideList.add(new TidalLevelResponse(null, null, null));}
        }

        //물때 계산.
        Map<String, Object> tide = commonService.findTideTime(date);

        result = TodayTideDto.builder()
                .observerId(observerId)
                .observerName(observer.getName())
                .isAlerted(isAlerted)
                .date(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .tideList(tideList)
                .tide((String)tide.get("tideTime"))
                .build();

        /*알림 리스트*/
        ArrayList<String> tidalAlertTimeList = new ArrayList<>();

        List<Alerts> preAlertList = alertsRepository.findAllByAlertTimeGreaterThanAndReceiverAndAlertTypeAndPidAndIsSent(
                LocalDateTime.now(), member, AlertType.tideLevel, observerId,false);
        for(int i=0; i<preAlertList.size(); i++){
            Alerts alert = preAlertList.get(i);
            List<CommonCode> alertSet = alert.getAlert_sets();
            tidalAlertTimeList.add(alertSet.get(0).getCode());
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

//        LocalDateTime currentTime = LocalDateTime.now();
//        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        int temp = currentTime.getHour();
//        if(temp >= 2 && temp < 5){temp = 20; currentDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));}
//        else if(temp >= 5 && temp < 8){temp = 23; currentDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));}
//        else if(temp >= 8&& temp < 11){temp = 2;}
//        else if(temp >= 11&& temp < 14){temp = 5;}
//        else if(temp >= 14&& temp < 17){temp = 8;}
//        else if(temp >= 17&& temp < 20){temp = 11;}
//        else if(temp >= 20&& temp < 23){temp = 14;}
//        else if(temp >= 23){temp = 17;}
//        else if(temp < 2){temp = 17; currentDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));}

        LocalDateTime currentTime = LocalDateTime.now();
        int currentHour = currentTime.getHour();
        String baseTime = null;
        String baseDate = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        if(currentHour >= 0 && currentHour < 3){ baseTime = "2000"; baseDate = currentTime.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));}
        else if(currentHour >= 3 && currentHour < 6){ baseTime = "2300"; baseDate = currentTime.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));}
        else if(currentHour >= 6 && currentHour < 9){ baseTime = "0200"; }
        else if(currentHour >= 9 && currentHour < 12){ baseTime = "0500"; }
        else if(currentHour >= 12 && currentHour < 15){ baseTime = "0800"; }
        else if(currentHour >= 15 && currentHour < 18){ baseTime = "1100"; }
        else if(currentHour >= 18 && currentHour < 21){ baseTime = "1400"; }
        else if(currentHour >= 21){ baseTime = "1700"; }

//        temp = (temp < 0)? temp + 24 : temp;
//        String baseTime = String.format("%02d",temp);
        String fcstTime = String.format("%02d",(currentTime.getHour()/3)*3) + "00";

        //동네예보 api 호출.
        try{
            String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst?" +
                    "serviceKey=EU1VFa5ptjvV1eaOpB9bnBKBxJxBGaZ%2BqthSuo3%2FZxGfQ%2BrHHiKxf%2Bt1a13VCLBfj1eBv%2BwElgABiGOyIIWDpA%3D%3D" +//
                    "&pageNo=1" +
                    "&numOfRows=100" +
                    "&dataType=JSON" +
                    "&base_date=" + baseDate +
                    "&base_time=" + baseTime +
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
                if(sky != null && pty != null) { break; }
            }

            //api에서받아온 날씨 코드값에 따라 날씨 데이터 설정.
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
        }
        catch (Exception e){
            e.printStackTrace();
            result.setWeather(null);
            return result;
        }

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
        for (String time : alertTimeList) {
            if (time.contains("high")) {
                highTideAlert.add(time);
            } else if (time.contains("low")) {
                lowTideAlert.add(time);
            }
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

        for (CommonCode commonCode : lowTideAlertCodeList) {
            Integer time = Integer.parseInt(commonCode.getExtraValue1());
            LocalDateTime alertTime = list.get(0).getDateTime();
            alertTime = alertTime.plusHours(time);
            for (int j = 1; (alertTime.compareTo(LocalDateTime.now()) < 0); j++) {
                if (list.size() < j) {
                    throw new RuntimeException("알림을 설정할 수 없습니다.");
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
                    .content(observer.getName() + " low " + time)
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
        if(tideList.size() > 0 && tideList.size() < 4){
            for(; tideList.size() != 4;){ tideList.add(new TidalLevelResponse(null, null, null));}
        }

        /*알림 리스트*/
        ArrayList<String> alertTideList = new ArrayList<String>();
        ArrayList<String> alertDayList = new ArrayList<String>();
        ArrayList<String> alertTimeList = new ArrayList<String>();

        List<Alerts> preAlertList = alertsRepository.findAllByAlertTimeGreaterThanAndReceiverAndAlertTypeAndPidAndIsSent(
                LocalDateTime.now(), member, AlertType.tide, observerId,false);
        for (Alerts alert : preAlertList) {
            List<CommonCode> commonCodeList = alert.getAlert_sets();
            for (CommonCode commonCode : commonCodeList) {
                switch (commonCode.getCodeGroup().getCode()) {
                    case "tideAlertTide8": {
                        boolean exist = false;
                        for (String s : alertTideList) {
                            if (s.equals(commonCode.getCode())) {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            alertTideList.add(commonCode.getCode());
                        }
                        break;
                    }
                    case "tideAlertDay": {
                        boolean exist = false;
                        for (String s : alertDayList) {
                            if (s.equals(commonCode.getCode())) {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            alertDayList.add(commonCode.getCode());
                        }
                        break;
                    }
                    case "tideAlertTime": {
                        boolean exist = false;
                        for (String s : alertTimeList) {
                            if (s.equals(commonCode.getCode())) {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            alertTimeList.add(commonCode.getCode());
                        }
                        break;
                    }
                }
            }
        }

        //물때 계산.
        Map<String, Object> tide = commonService.findTideTime(dateString);

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
                .tide((String)tide.get("tideTime"))
                .build();

        /*날씨*/
        try {
            ArrayList<String> w = getWeather(observer,dateString);
            result.setWeather(w);
        } catch (Exception e) {
            result.setWeather(null);
        }

        return result;
    }

    /*중기예보로 날짜별 날씨 반환.*/
    public ArrayList<String> getWeather(ObserverCode observer, String dateString) throws IOException, ParseException {
        ArrayList<String> result = new ArrayList<>();

        LocalDateTime currentTime = LocalDateTime.now();
        int time = currentTime.getHour();
        String tmFc = null;
        if(time < 6){
            currentTime = currentTime.minusDays(1);
            tmFc = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "1800";
        }
        else if(time < 18 && time >= 6){tmFc = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "0600";}
        else{tmFc = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "1800";}

        try{
            String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidSeaFcst?" +
                    "serviceKey=EU1VFa5ptjvV1eaOpB9bnBKBxJxBGaZ%2BqthSuo3%2FZxGfQ%2BrHHiKxf%2Bt1a13VCLBfj1eBv%2BwElgABiGOyIIWDpA%3D%3D" +//
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
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
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

    //선박 위치에 해당하는 현재 날씨 데이터 가져오기.
    @Transactional
    public Map<String, Object> getTimelyWeather(Float lon, Float lat) throws ResourceNotFoundException {
        Map<String, Object> weather = new HashMap<>();
        ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        Map<String, Float> xyGrid = transLatLonToXY(0, lon, lat);
        //위경도->x,y격자 좌표 변환.

//        transLatLonToXY(1, Float.parseFloat("53"),
//                Float.parseFloat("127"));

        Integer sky = null;//하늘상태
        Integer pty = null;//강수형태
        Integer pop = null;//강수확률
        Integer reh = null;//습도
        Double tmp = null;//기온
        Double tmn = null;//일 최저기온
        Double tmx = null; //일 최고기온
        Double uuu = null;//풍속(동남)
        Double vvv = null;//풍속(남북)
        Double wav = null;//파고
        Integer vec = null;//풍향
        Double wsd = null; //풍속

        String todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        LocalDateTime currentTime = LocalDateTime.now();
        int currentHour = currentTime.getHour();
        String baseTime = null;
        String baseDate = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        if(currentHour >= 0 && currentHour < 3){ baseTime = "2000"; baseDate = currentTime.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));}
        else if(currentHour >= 3 && currentHour < 6){ baseTime = "2300"; baseDate = currentTime.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));}
        else if(currentHour >= 6 && currentHour < 9){ baseTime = "0200"; }
        else if(currentHour >= 9 && currentHour < 12){ baseTime = "0500"; }
        else if(currentHour >= 12 && currentHour < 15){ baseTime = "0800"; }
        else if(currentHour >= 15 && currentHour < 18){ baseTime = "1100"; }
        else if(currentHour >= 18 && currentHour < 21){ baseTime = "1400"; }
        else if(currentHour >= 21){ baseTime = "1700"; }

//        temp = (temp < 0)? temp + 24 : temp;
//        String baseTime = String.format("%02d",temp);
        String fcstTime = String.format("%02d",(currentTime.getHour()/3)*3) + "00";
        String fcstNextTime = String.format("%02d",(currentTime.plusHours(3).getHour()/3)*3) + "00";

        //동네예보 api 호출.
        try{
            String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst?" +
                    "serviceKey=EU1VFa5ptjvV1eaOpB9bnBKBxJxBGaZ%2BqthSuo3%2FZxGfQ%2BrHHiKxf%2Bt1a13VCLBfj1eBv%2BwElgABiGOyIIWDpA%3D%3D" +//
                    "&pageNo=1" +
                    "&numOfRows=120" +
                    "&dataType=XML" +
                    "&base_date=" + baseDate +
                    "&base_time=" + baseTime +
                    "&nx=" + Math.round(xyGrid.get("x")) +
                    "&ny=" + Math.round(xyGrid.get("y"));
            String response = memberService.sendRequest(url,"GET",new HashMap<String,String>(),"");
//            System.out.println("result>>> "+response);

            //xml데이터 파싱.
            InputSource is = new InputSource(new StringReader(new String(response)));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("item");

            //기존 json으로 받아올때의 코드.
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> tempResponse1 = mapper.readValue(response, Map.class);
//            Map<String,Object> tempResponse2 = (Map<String,Object>)tempResponse1.get("response");
//            Map<String,Object> tempResponse3 = (Map<String,Object>)tempResponse2.get("body");
//            Map<String,Object> tempResponse4 = (Map<String,Object>)tempResponse3.get("items");
//            ArrayList<Map<String,Object>> dataList = (ArrayList<Map<String,Object>>)tempResponse4.get("item");

            //시간대에 따라 날씨데이터 저장.
            for(int x=1; x<9; x++){
                fcstTime = String.format("%02d",(currentTime.plusHours(3*x).getHour()/3)*3) + "00";
                String fcstDate = currentTime.plusHours(3*x).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                fcstNextTime = String.format("%02d",(currentTime.plusHours(3*(x+1)).getHour()/3)*3) + "00";
//                result.add(getWeatherFromApiResult(dataList, fcstTime,fcstNextTime, fcstDate));
                result.add(getWeatherFromApiResultFromXml(response, fcstTime, fcstNextTime, fcstDate));
            }
            weather.put("weatherByTime", result);

            for(int i=0; i<nodeList.getLength(); i++){
//                Map<String,Object> item = dataList.get(i); //기존 json형태로 받아올때의 코드.
                Element item = (Element)nodeList.item(i);
                if( (item.getElementsByTagName("fcstTime").item(0).getTextContent().equals(fcstTime)
                        || item.getElementsByTagName("fcstTime").item(0).getTextContent().equals(fcstNextTime))
                        && (item.getElementsByTagName("fcstDate").item(0).getTextContent().equals(todayDate)) ){
                    String category = (String)item.getElementsByTagName("category").item(0).getTextContent();
                    switch (category){
                        case "SKY":
                            if(sky == null){ sky = Integer.parseInt((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                            break;
                        case "PTY":
                            if(pty == null){ pty = Integer.parseInt((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                            break;
                        case "POP":
                            if(pop == null){ pop = Integer.parseInt((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                            break;
                        case "REH":
                            if(reh == null){ reh = Integer.parseInt((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                            break;
                        case "TMP": case "T3H": case "T1H":
                            if(tmp == null){ tmp = Double.parseDouble((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                            break;
                        case "TMN":
                            if(tmn == null){ tmn = Double.parseDouble((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                            break;
                        case "TMX":
                            if(tmx == null){ tmx = Double.parseDouble((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                            break;
                        case "VEC":
                            if(vec == null){ vec = Integer.parseInt((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                            break;
                        case "WSD":
                            if(wsd == null){ wsd = Double.parseDouble((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                            break;
                    }
                }
            }

            //api에서받아온 날씨 코드값에 따라 날씨 데이터 설정.
            CodeGroup codeGroup = codeGroupRepository.findByCode("etcImg");
            String imgUrl=null;

            if(sky ==1){//맑음
//                weather.put("weather", "맑음");
                switch (pty){
                    case 0:
                        weather.put("weather", "맑음");break;
                    case 1: case 5:
                        weather.put("weather", "구름많고 비");break;
                    case 2: case 6:
                        weather.put("weather", "구름많고 비/눈");break;
                    case 3: case 7:
                        weather.put("weather", "구름많고 눈");break;
                    case 4:
                        weather.put("weather", "구름많고 소나기");break;
                    default: weather.put("weather", "맑음");
                }
            }
            else if(sky == 3){//구름많음
                switch (pty){
                    case 0:
                        weather.put("weather", "구름많음");break;
                    case 1: case 5:
                        weather.put("weather", "구름많고 비");break;
                    case 2: case 6:
                        weather.put("weather", "구름많고 비/눈");break;
                    case 3: case 7:
                        weather.put("weather", "구름많고 눈");break;
                    case 4:
                        weather.put("weather", "구름많고 소나기");break;
                    default: weather.put("weather","구름많음");
                }
            }
            else if(sky == 4){//흐림
                switch (pty){
                    case 0:
                        weather.put("weather", "흐림");break;
                    case 1: case 5:
                        weather.put("weather", "흐리고 비");break;
                    case 2: case 6:
                        weather.put("weather", "흐리고 비/눈");break;
                    case 3: case 7:
                        weather.put("weather", "흐리고 눈");break;
                    case 4:
                        weather.put("weather", "흐리고 소나기");break;
                    default: weather.put("weather","흐림");
                }
            }
            else{
                weather.put("weather", null);
            }

            if(weather.get("weather")!=null) {
                imgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, (String)weather.get("weather")).getExtraValue1();
//                imgUrl = env.getProperty("file.downloadUrl") + imgUrl;
                imgUrl = "https://fishkingapp.com/resource"+imgUrl;
                weather.put("weatherImg", imgUrl);
            }

            //강수 확률
            weather.put("rainProbability", pop);
            //습도
            weather.put("humidity", reh);
            //온도
            weather.put("tmp", tmp);
            //일 최저기온
            weather.put("tmpMin", tmn);
            //일 최고기온
            weather.put("tmpMax", tmx);
            //풍향
            if(vec != null){
                int windDirection = (int)((vec + 22.5 * 0.5) / 22.5);
                String[] windDirectionString = new String[]{
                        "북", "북북동", "북동", "동북동", "동", "동남동", "남동", "남남동", "남", "남남서", "남서", "서남서", "서",
                        "서북서","북서","북북서","북"
                };
                weather.put("windDirection", windDirectionString[windDirection]);
            }
            else{ weather.put("windDirection", null);}
            //풍속
            weather.put("windSpeed", wsd);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return weather;
    }

    //api로부터 반환받은 배열으로부터 해당 시간대에 대한 날씨 정보 추출.
    private Map<String, Object> getWeatherFromApiResult(ArrayList<Map<String,Object>> dataList,
                                                        String fcstTime, String fcstNextTime, String fcstDate){
        Map<String, Object> weather = new HashMap<>();
        weather.put("date", fcstDate);
        weather.put("time", fcstTime);

        Integer sky = null;//하늘상태
        Integer pty = null;//강수형태
        Integer pop = null;//강수확률
        Integer reh = null;//습도
        Double tmp = null;//기온
        Double tmn = null;//일 최저기온
        Double tmx = null; //일 최고기온
        Double uuu = null;//풍속(동남)
        Double vvv = null;//풍속(남북)
        Double wav = null;//파고
        Integer vec = null;//풍향
        Double wsd = null; //풍속

        for(int i=0; i<dataList.size(); i++){
            Map<String,Object> item = dataList.get(i);
            if( (item.get("fcstTime").equals(fcstTime) || item.get("fcstTime").equals(fcstNextTime))
                    && (item.get("fcstDate").equals(fcstDate)) ){
                String category = (String)item.get("category");
                switch (category){
                    case "SKY":
                        if(sky == null){ sky = Integer.parseInt((String)item.get("fcstValue")); }
                        break;
                    case "PTY":
                        if(pty == null){ pty = Integer.parseInt((String)item.get("fcstValue")); }
                        break;
                    case "POP":
                        if(pop == null){ pop = Integer.parseInt((String)item.get("fcstValue")); }
                        break;
                    case "REH":
                        if(reh == null){ reh = Integer.parseInt((String)item.get("fcstValue")); }
                        break;
                    case "TMP": case "T3H": case "T1H":
                        if(tmp == null){ tmp = Double.parseDouble((String)item.get("fcstValue")); }
                        break;
                    case "TMN":
                        if(tmn == null){ tmn = Double.parseDouble((String)item.get("fcstValue")); }
                        break;
                    case "TMX":
                        if(tmx == null){ tmx = Double.parseDouble((String)item.get("fcstValue")); }
                        break;
                    case "VEC":
                        if(vec == null){ vec = Integer.parseInt((String)item.get("fcstValue")); }
                        break;
                    case "WSD":
                        if(wsd == null){ wsd = Double.parseDouble((String)item.get("fcstValue")); }
                        break;
                }
            }
        }

        //api에서받아온 날씨 코드값에 따라 날씨 데이터 설정.
        CodeGroup codeGroup = codeGroupRepository.findByCode("etcImg");
        String imgUrl=null;

        if(sky ==1){//맑음
//                weather.put("weather", "맑음");
            switch (pty){
                case 0:
                    weather.put("weather", "맑음");break;
                case 1: case 5:
                    weather.put("weather", "구름많고 비");break;
                case 2: case 6:
                    weather.put("weather", "구름많고 비/눈");break;
                case 3: case 7:
                    weather.put("weather", "구름많고 눈");break;
                case 4:
                    weather.put("weather", "구름많고 소나기");break;
                default: weather.put("weather", "맑음");
            }
        }
        else if(sky == 3){//구름많음
            switch (pty){
                case 0:
                    weather.put("weather", "구름많음");break;
                case 1: case 5:
                    weather.put("weather", "구름많고 비");break;
                case 2: case 6:
                    weather.put("weather", "구름많고 비/눈");break;
                case 3: case 7:
                    weather.put("weather", "구름많고 눈");break;
                case 4:
                    weather.put("weather", "구름많고 소나기");break;
                default: weather.put("weather","구름많음");
            }
        }
        else if(sky == 4){//흐림
            switch (pty){
                case 0:
                    weather.put("weather", "흐림");break;
                case 1: case 5:
                    weather.put("weather", "흐리고 비");break;
                case 2: case 6:
                    weather.put("weather", "흐리고 비/눈");break;
                case 3: case 7:
                    weather.put("weather", "흐리고 눈");break;
                case 4:
                    weather.put("weather", "흐리고 소나기");break;
                default: weather.put("weather","흐림");
            }
        }
        else{
            weather.put("weather", null);
        }

        if(weather.get("weather")!=null) {
            imgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, (String)weather.get("weather")).getExtraValue1();
//                imgUrl = env.getProperty("file.downloadUrl") + imgUrl;
            imgUrl = "https://fishkingapp.com/resource"+imgUrl;
            weather.put("weatherImg", imgUrl);
        }

        //강수 확률
        weather.put("rainProbability", pop);
        //습도
        weather.put("humidity", reh);
        //온도
        weather.put("tmp", tmp);
        //일 최저기온
        weather.put("tmpMin", tmn);
        //일 최고기온
        weather.put("tmpMax", tmx);
        //풍향
        if(vec != null){
            int windDirection = (int)((vec + 22.5 * 0.5) / 22.5);
            String[] windDirectionString = new String[]{
                    "북", "북북동", "북동", "동북동", "동", "동남동", "남동", "남남동", "남", "남남서", "남서", "서남서", "서",
                    "서북서","북서","북북서","북"
            };
            weather.put("windDirection", windDirectionString[windDirection]);
        }
        else{ weather.put("windDirection", null);}
        //풍속
        weather.put("windSpeed", wsd);

        return weather;
    }
    //api로부터 반환받은 배열으로부터 해당 시간대에 대한 날씨 정보 추출. XML파일 파싱.
    private Map<String, Object> getWeatherFromApiResultFromXml(String dataList,
                                                        String fcstTime, String fcstNextTime, String fcstDate)
            throws ParserConfigurationException, IOException, SAXException {

        InputSource is = new InputSource(new StringReader(new String(dataList)));
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("item");

//        Node node = nodeList.item(0);
//        node = (Element)node;
//        Node x1 = ((Element) node).getElementsByTagName("fcstTime").item(0);
//        System.out.println(x1.getTextContent());
//        Node x2 = ((Element) node).getElementsByTagName("category").item(0);
//        System.out.println(x2.getTextContent());

        Map<String, Object> weather = new HashMap<>();
        weather.put("date", fcstDate);
        weather.put("time", fcstTime);

        Integer sky = null;//하늘상태
        Integer pty = null;//강수형태
        Integer pop = null;//강수확률
        Integer reh = null;//습도
        Double tmp = null;//기온
        Double tmn = null;//일 최저기온
        Double tmx = null; //일 최고기온
        Double uuu = null;//풍속(동남)
        Double vvv = null;//풍속(남북)
        Double wav = null;//파고
        Integer vec = null;//풍향
        Double wsd = null; //풍속

//        for(int i=0; i<dataList.size(); i++){
        for(int itemIdx=0; itemIdx< nodeList.getLength(); itemIdx++){

            Element item = (Element)nodeList.item(itemIdx);

//            Map<String,Object> item = dataList.get(i);
            if( (item.getElementsByTagName("fcstTime").item(0).getTextContent().equals(fcstTime)
                    || item.getElementsByTagName("fcstTime").item(0).getTextContent().equals(fcstNextTime))
                    && (item.getElementsByTagName("fcstDate").item(0).getTextContent().equals(fcstDate)) ){
                String category = (String)item.getElementsByTagName("category").item(0).getTextContent();
                switch (category){
                    case "SKY":
                        if(sky == null){ sky = Integer.parseInt((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                        break;
                    case "PTY":
                        if(pty == null){ pty = Integer.parseInt((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                        break;
                    case "POP":
                        if(pop == null){ pop = Integer.parseInt((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                        break;
                    case "REH":
                        if(reh == null){ reh = Integer.parseInt((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                        break;
                    case "TMP": case "T3H": case "T1H":
                        if(tmp == null){ tmp = Double.parseDouble((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                        break;
                    case "TMN":
                        if(tmn == null){ tmn = Double.parseDouble((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                        break;
                    case "TMX":
                        if(tmx == null){ tmx = Double.parseDouble((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                        break;
                    case "VEC":
                        if(vec == null){ vec = Integer.parseInt((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                        break;
                    case "WSD":
                        if(wsd == null){ wsd = Double.parseDouble((String)item.getElementsByTagName("fcstValue").item(0).getTextContent()); }
                        break;
                }
            }
        }

        //api에서받아온 날씨 코드값에 따라 날씨 데이터 설정.
        CodeGroup codeGroup = codeGroupRepository.findByCode("etcImg");
        String imgUrl=null;

        if(sky ==1){//맑음
//                weather.put("weather", "맑음");
            switch (pty){
                case 0:
                    weather.put("weather", "맑음");break;
                case 1: case 5:
                    weather.put("weather", "구름많고 비");break;
                case 2: case 6:
                    weather.put("weather", "구름많고 비/눈");break;
                case 3: case 7:
                    weather.put("weather", "구름많고 눈");break;
                case 4:
                    weather.put("weather", "구름많고 소나기");break;
                default: weather.put("weather", "맑음");
            }
        }
        else if(sky == 3){//구름많음
            switch (pty){
                case 0:
                    weather.put("weather", "구름많음");break;
                case 1: case 5:
                    weather.put("weather", "구름많고 비");break;
                case 2: case 6:
                    weather.put("weather", "구름많고 비/눈");break;
                case 3: case 7:
                    weather.put("weather", "구름많고 눈");break;
                case 4:
                    weather.put("weather", "구름많고 소나기");break;
                default: weather.put("weather","구름많음");
            }
        }
        else if(sky == 4){//흐림
            switch (pty){
                case 0:
                    weather.put("weather", "흐림");break;
                case 1: case 5:
                    weather.put("weather", "흐리고 비");break;
                case 2: case 6:
                    weather.put("weather", "흐리고 비/눈");break;
                case 3: case 7:
                    weather.put("weather", "흐리고 눈");break;
                case 4:
                    weather.put("weather", "흐리고 소나기");break;
                default: weather.put("weather","흐림");
            }
        }
        else{
            weather.put("weather", null);
        }

        if(weather.get("weather")!=null) {
            imgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, (String)weather.get("weather")).getExtraValue1();
//                imgUrl = env.getProperty("file.downloadUrl") + imgUrl;
            imgUrl = "https://fishkingapp.com/resource"+imgUrl;
            weather.put("weatherImg", imgUrl);
        }

        //강수 확률
        weather.put("rainProbability", pop);
        //습도
        weather.put("humidity", reh);
        //온도
        weather.put("tmp", tmp);
        //일 최저기온
        weather.put("tmpMin", tmn);
        //일 최고기온
        weather.put("tmpMax", tmx);
        //풍향
        if(vec != null){
            int windDirection = (int)((vec + 22.5 * 0.5) / 22.5);
            String[] windDirectionString = new String[]{
                    "북", "북북동", "북동", "동북동", "동", "동남동", "남동", "남남동", "남", "남남서", "남서", "서남서", "서",
                    "서북서","북서","북북서","북"
            };
            weather.put("windDirection", windDirectionString[windDirection]);
        }
        else{ weather.put("windDirection", null);}
        //풍속
        weather.put("windSpeed", wsd);

        return weather;
    }

    //항구 주간 날씨
    @Transactional
    public ArrayList<Map<String, Object>> getDailyWeather(String address) throws ResourceNotFoundException {
        ArrayList<Map<String, Object>> result = new ArrayList<>();

        Map<String, String> fcstRegionCode = transRegionCodeFromAddress(address);
//        Map<String, String> fcstRegionCode = transRegionCodeFromAddress(address);
        String tmpRegionCode = fcstRegionCode.get("tmpFcstRegionCode");
        String weatherRegionCode = fcstRegionCode.get("landFcstRegionCode");

        //주간 최저,최고 기온 조회.
        LocalDateTime currentTime = LocalDateTime.now();
        int time = currentTime.getHour();
        String tmFc = null;
        if(time < 6){
            currentTime = currentTime.minusDays(1);
            tmFc = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "1800";
        }
        else if(time < 18 && time >= 6){tmFc = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "0600";}
        else{tmFc = currentTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "1800";}

        try{
            String tmpUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa?" +
                    "serviceKey=EU1VFa5ptjvV1eaOpB9bnBKBxJxBGaZ%2BqthSuo3%2FZxGfQ%2BrHHiKxf%2Bt1a13VCLBfj1eBv%2BwElgABiGOyIIWDpA%3D%3D" +//
                    "&pageNo=1" +
                    "&numOfRows=50" +
                    "&dataType=JSON" +
                    "&regId=" + tmpRegionCode +
                    "&tmFc=" + tmFc;
            String tmpRes = memberService.sendRequest(tmpUrl,"GET",new HashMap<String,String>(),"");
            System.out.println("result>>> "+tmpRes);
            ObjectMapper mapper1 = new ObjectMapper();
            Map<String, Object> tmpRes1 = mapper1.readValue(tmpRes, Map.class);
            Map<String,Object> tmpRes2 = (Map<String,Object>)tmpRes1.get("response");
            Map<String,Object> tmpRes3 = (Map<String,Object>)tmpRes2.get("body");
            Map<String,Object> tmpRes4 = (Map<String,Object>)tmpRes3.get("items");
            ArrayList<Map<String,Object>> tmpDataList = (ArrayList<Map<String,Object>>)tmpRes4.get("item");
            Map<String,Object> tmpData = tmpDataList.get(0);

            for(int afterDayCount = 3; afterDayCount<=10; afterDayCount++){
                Map<String, Object> tmp = new HashMap<>();
                tmp.put("date", LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd(EEE)", Locale.KOREAN)));
                //해당 날 이후의 예보 데이터가 있으면,
                if(tmpData.containsKey("taMin"+afterDayCount)){ tmp.put("tmpMin", tmpData.get("taMin"+afterDayCount)); }
                else{tmp.put("tmpMin", "");}
                if(tmpData.containsKey("taMax"+afterDayCount)){ tmp.put("tmpMax", tmpData.get("taMax"+afterDayCount)); }
                else{tmp.put("tmpMax", "");}
                result.add(tmp);
            }

            //주간 날씨 예보 조회.
            String weatherUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst?" +
                    "serviceKey=EU1VFa5ptjvV1eaOpB9bnBKBxJxBGaZ%2BqthSuo3%2FZxGfQ%2BrHHiKxf%2Bt1a13VCLBfj1eBv%2BwElgABiGOyIIWDpA%3D%3D" +//
                    "&pageNo=1" +
                    "&numOfRows=50" +
                    "&dataType=JSON" +
                    "&regId=" + weatherRegionCode +
                    "&tmFc=" + tmFc;
            String weatherRes = memberService.sendRequest(weatherUrl,"GET",new HashMap<String,String>(),"");
            System.out.println("result>>> "+weatherRes);
            ObjectMapper mapper2 = new ObjectMapper();
            Map<String, Object> weatherRes1 = mapper2.readValue(weatherRes, Map.class);
            Map<String,Object> weatherRes2 = (Map<String,Object>)weatherRes1.get("response");
            Map<String,Object> weatherRes3 = (Map<String,Object>)weatherRes2.get("body");
            Map<String,Object> weatherRes4 = (Map<String,Object>)weatherRes3.get("items");
            ArrayList<Map<String,Object>> weatherDataList = (ArrayList<Map<String,Object>>)weatherRes4.get("item");
            Map<String,Object> weatherData = weatherDataList.get(0);

            CodeGroup codeGroup = codeGroupRepository.findByCode("etcImg");

            for(int afterDayCount = 3; afterDayCount<=10; afterDayCount++){
                Map<String, Object> resultMap = result.get(afterDayCount-3);
                //8일 이전 예보인 경우, 날씨 오전, 오후로 나뉨.
                if(afterDayCount<8){
                    //해당 날 오전 날씨 데이터가 있으면,
                    if(weatherData.containsKey("wf"+afterDayCount+"Am")){
                        String tempWeather = (String)weatherData.get("wf"+afterDayCount+"Am");
                        resultMap.put("weatherAm", tempWeather);
                        String imgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, tempWeather).getExtraValue1();
                        imgUrl = env.getProperty("file.downloadUrl") + imgUrl;
                        resultMap.put("weatherAmImg", imgUrl);
                    }
                    else{
                        resultMap.put("weatherAm", "");
                        resultMap.put("weatherAmImg", "");
                    }
                    //해당 날 오후 날씨 데이터가 있으면,
                    if(weatherData.containsKey("wf"+afterDayCount+"Pm")){
                        String tempWeather = (String)weatherData.get("wf"+afterDayCount+"Pm");
                        resultMap.put("weatherPm", tempWeather);
                        String imgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, tempWeather).getExtraValue1();
                        imgUrl = env.getProperty("file.downloadUrl") + imgUrl;
                        resultMap.put("weatherPmImg",imgUrl);
                    }
                    else{
                        resultMap.put("weatherPm", "");
                        resultMap.put("weatherPmImg", "");
                    }
                }
                //8일 이후 예보인 경우, 오전, 오후로 나뉘지 않음.
                else{
                    if(weatherData.containsKey("wf"+afterDayCount)){
                        String tempWeather = (String)weatherData.get("wf"+afterDayCount);
                        resultMap.put("weather", tempWeather);
                        String imgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, tempWeather).getExtraValue1();
                        imgUrl = env.getProperty("file.downloadUrl") + imgUrl;
                        resultMap.put("weatherImg",imgUrl);
                    }
                    else{
                        resultMap.put("weather", "");
                        resultMap.put("weatherImg", "");
                    }
                }

            }


//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            long today = (sdf.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))).getTime();
//            long inputDay = (sdf.parse(DateUtils.getDateFromString(dateString).format(DateTimeFormatter.ofPattern("yyyyMMdd")))).getTime();
//            long dayDiff = (inputDay - today) / (24*60*60*1000);
//
//            CodeGroup codeGroup = codeGroupRepository.findByCode("etcImg");
//
//            if(dayDiff < 3 || dayDiff > 10){result=null;}
//            else if(dayDiff <8){
//                String morningWeather = (String)data.get("wf"+dayDiff+"Am");
//                String afternoonWeather = (String)data.get("wf"+dayDiff+"Pm");
//                result.add(morningWeather);
//                String morningWeatherImgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, morningWeather).getExtraValue1();
//                morningWeatherImgUrl = env.getProperty("file.downloadUrl") + morningWeatherImgUrl;
//                result.add(morningWeatherImgUrl);
//
//                result.add(afternoonWeather);
//                String afternoonWeatherImgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, afternoonWeather).getExtraValue1();
//                afternoonWeatherImgUrl = env.getProperty("file.downloadUrl") + afternoonWeatherImgUrl;
//                result.add(afternoonWeatherImgUrl);
//            }
//            else{
//                String weather = data.get("wf"+dayDiff) +"";
//                result.add(weather);
//                String weatherImgUrl = commonCodeRepository.findByCodeGroupAndCode(codeGroup, weather).getExtraValue1();
//                weatherImgUrl = env.getProperty("file.downloadUrl") + weatherImgUrl;
//                result.add(weatherImgUrl);
//            }
            return result;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //위,경도로부터 기상청 단기예보 api에 필요한 x,y격자 좌표 변환.
    //mode인자 0이면, 위경도=>좌표. 1이면, 좌표=>위경도.

    int NX = 149;/* X축 격자점 수 */
    int NY = 253;/* Y축 격자점 수 */

    class lamc_parameter{
        float Re; /* 사용할 지구반경 [ km ] */
        float grid; /* 격자간격 [ km ] */
        float slat1; /* 표준위도 [degree] */
        float slat2; /* 표준위도 [degree] */
        float olon; /* 기준점의 경도 [degree] */
        float olat; /* 기준점의 위도 [degree] */
        float xo; /* 기준점의 X좌표 [격자거리] */
        float yo; /* 기준점의 Y좌표 [격자거리] */
        int first; /* 시작여부 (0 = 시작) */
    }

    public Map<String, Float> transLatLonToXY(int mode, Float lonOrX, Float latOrY){
        Map<String, Float> result = new HashMap<>();
        int NX = 149;/* X축 격자점 수 */
        int NY = 253;/* Y축 격자점 수 */

        MyMenuService.lamc_parameter map = new MyMenuService.lamc_parameter();

        Float lon= null, lat= null, x= null, y = null;
//
        if (mode == 1) {
            x = lonOrX;
            y = latOrY;

        }
        else if (mode == 0){
            lon = lonOrX;
            lat = latOrY;
        }

        // 동네예보 지도 정보

        map.Re = (float)6371.00877; // 지도반경
        map.grid = (float)5.0; // 격자간격 (km)
        map.slat1 = (float)30.0; // 표준위도 1
        map.slat2 = (float)60.0; // 표준위도 2
        map.olon = (float)126.0; // 기준점 경도
        map.olat = (float)38.0; // 기준점 위도
        map.xo = 210/map.grid; // 기준점 X좌표
        map.yo = 675/map.grid; // 기준점 Y좌표
        map.first = 0;

        // 동네예보

        result = map_conv(lon, lat, x, y, mode, map);

        return result;
    }

    /*============================================================================*
     * 좌표변환
     *============================================================================*/
    private HashMap<String,Float> map_conv (
        Float lon, // 경도(degree)
        Float lat, // 위도(degree)
        Float x, // X격자 (grid)
        Float y, // Y격자 (grid)
        int code, // 0 (격자->위경도), 1 (위경도->격자)
        lamc_parameter map // 지도정보
    ) {
        HashMap<String,Float> result = new HashMap<>();
        Float lon1=null, lat1=null, x1=null, y1=null;

        // 위경도 -> (X,Y)

        if (code == 0) {
            lon1 = lon;
            lat1 = lat;
            HashMap<String,Float> transValue = lamcproj(lon1, lat1, x1, y1, 0, map);
            x = (float)(int)(transValue.get("x") + 1.5);
            y = (float)(int)(transValue.get("y") + 1.5);
            result.put("x", x);
            result.put("y",y);
        }

        // (X,Y) -> 위경도

        if (code == 1) {
            x1 = x - 1;
            y1 = y - 1;
            HashMap<String,Float> transValue = lamcproj(lon1, lat1, x1, y1, 1, map);
            lon = transValue.get("lon");
            lat = transValue.get("lat");
            result.put("lon", lon);
            result.put("lat", lat);
        }
        return result;
    }

    /***************************************************************************
    *
    * [ Lambert Conformal Conic Projection ]
    *
    * olon, lat : (longitude,latitude) at earth [degree]
    * o x, y : (x,y) cordinate in map [grid]
    * o code = 0 : (lon,lat) --> (x,y)
    * 1 : (x,y) --> (lon,lat)
    *
    ***************************************************************************/
    static double PI, DEGRAD, RADDEG;
    static double re, olon, olat, sn, sf, ro;

    private HashMap<String, Float> lamcproj(Float lon, Float lat, Float x, Float y, int code, lamc_parameter map)
    {
        HashMap<String, Float> result = new HashMap<>();
        double slat1, slat2, alon, alat, xn, yn, ra, theta;

        if ((map).first == 0) {
            PI = Math.asin(1.0)*2.0;
            DEGRAD = PI/180.0;
            RADDEG = 180.0/PI;

            re = (map).Re/(map).grid;
            slat1 = (map).slat1 * DEGRAD;
            slat2 = (map).slat2 * DEGRAD;
            olon = (map).olon * DEGRAD;
            olat = (map).olat * DEGRAD;

            sn = Math.tan(PI*0.25 + slat2*0.5)/Math.tan(PI*0.25 + slat1*0.5);
            sn = Math.log(Math.cos(slat1)/Math.cos(slat2))/Math.log(sn);
            sf = Math.tan(PI*0.25 + slat1*0.5);
            sf = Math.pow(sf,sn)*Math.cos(slat1)/sn;
            ro = Math.tan(PI*0.25 + olat*0.5);
            ro = re*sf/Math.pow(ro,sn);
            (map).first = 1;
        }

        if (code == 0) {
            ra = Math.tan(PI*0.25+(lat)*DEGRAD*0.5);
            ra = re*sf/Math.pow(ra,sn);
            theta = (lon)*DEGRAD - olon;
            if (theta > PI) theta -= 2.0*PI;
            if (theta < -PI) theta += 2.0*PI;
            theta *= sn;
            x = (float)(ra*Math.sin(theta)) + (map).xo;
            y = (float)(ro - ra*Math.cos(theta)) + (map).yo;
            result.put("x",x);
            result.put("y",y);
        }
        else {
            xn = x - (map).xo;
            yn = ro - y + (map).yo;
            ra = Math.sqrt(xn*xn+yn*yn);
            if (sn< 0.0) ra = -ra;
            alat = Math.pow((re*sf/ra),(1.0/sn));
            alat = 2.0*Math.atan(alat) - PI*0.5;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0) {
                    theta = PI*0.5;
                    if(xn< 0.0 ) theta = -theta;
                }
                else
                    theta = Math.atan2(xn,yn);
            }
            alon = theta/sn + olon;
            lat = (float)(alat*RADDEG);
            lon = (float)(alon*RADDEG);
            result.put("lon",lon);
            result.put("lat",lat);
        }
        return result;
    }

    //주소로부터 기상청 예보 구역 코드 계산.
    public Map<String, String> transRegionCodeFromAddress(String address){
        Map<String, String> result = new HashMap<>();

        String district1 = address.split(" ")[0];//주소의 첫 부분 : 행정구역1단계.
        String district2 = address.split(" ")[1];//주소의 두 번째 부분 : 행정구역2단계.

        CodeGroup tmpFcstRegionCodeGroup = codeGroupRepository.findByCode("tmpFcstRegion");
        CodeGroup landFcstRegionCodeGroup = codeGroupRepository.findByCode("landFcstRegion");

        String parsedDistrict1ForTmp = "";
        String parsedDistrict2 = "";
        String parsedDistrict1ForLand = "";

        if(district1.contains("서울")){
            parsedDistrict1ForTmp = "서울·인천·경기도";
            parsedDistrict2 = "서울";
            parsedDistrict1ForLand = "11B00000";
        }
        else if(district1.contains("인천")){
            parsedDistrict1ForTmp = "서울·인천·경기도";
            parsedDistrict1ForLand = "11B00000";
            if(district2.contains("강화")){
                parsedDistrict2 = "강화";
            }
            else if(address.contains("백령면")){
                parsedDistrict2 = "백령도";
            }
            else{
                parsedDistrict2 = "인천";
            }
        }
        else if(district1.contains("경기")){
            parsedDistrict1ForTmp = "서울·인천·경기도";
            parsedDistrict1ForLand = "11B00000";
            List<CommonCode> district2CommonCodeList =
                    commonCodeRepository.findAllByExtraValue1AndCodeGroup("서울·인천·경기도", tmpFcstRegionCodeGroup);

            for(int i=0; i<district2CommonCodeList.size(); i++){
                CommonCode tmpFcstRegionCode = district2CommonCodeList.get(i);
                if(district2.contains(tmpFcstRegionCode.getCode())){
                    parsedDistrict2 = tmpFcstRegionCode.getCode();
                    break;
                }
            }
        }
        //부산,울산,경남
        else if(district1.contains("부산")){
            parsedDistrict1ForTmp = "부산.울산.경상남도";
            parsedDistrict2 = "부산";
            parsedDistrict1ForLand = "11H20000";
        }
        else if(district1.contains("울산")){
            parsedDistrict1ForTmp = "부산.울산.경상남도";
            parsedDistrict2 = "울산";
            parsedDistrict1ForLand = "11H20000";
        }
        else if(district1.contains("경남")){
            parsedDistrict1ForTmp = "부산.울산.경상남도";
            parsedDistrict1ForLand = "11H20000";
            List<CommonCode> district2CommonCodeList =
                    commonCodeRepository.findAllByExtraValue1AndCodeGroup("부산.울산.경상남도", tmpFcstRegionCodeGroup);

            for(int i=0; i<district2CommonCodeList.size(); i++){
                CommonCode tmpFcstRegionCode = district2CommonCodeList.get(i);
                if(district2.contains(tmpFcstRegionCode.getCode())){
                    parsedDistrict2 = tmpFcstRegionCode.getCode();
                    break;
                }
            }
        }
        //대구,경북
        else if(district1.contains("대구")){
            parsedDistrict1ForTmp = "대구.경상북도";
            parsedDistrict2 = "대구";
            parsedDistrict1ForLand = "11H10000";
        }
        else if(district1.contains("경북")){
            parsedDistrict1ForTmp = "대구.경상북도";
            parsedDistrict1ForLand = "11H10000";

            if(address.contains("독도리") || (address.contains("울릉읍") && address.contains("독도"))){
                parsedDistrict2 = "독도";
            }
            else if(address.contains("울릉군")){
                parsedDistrict2 = "울릉도";
            }

            else{
                List<CommonCode> district2CommonCodeList =
                        commonCodeRepository.findAllByExtraValue1AndCodeGroup("대구.경상북도", tmpFcstRegionCodeGroup);

                for(int i=0; i<district2CommonCodeList.size(); i++){
                    CommonCode tmpFcstRegionCode = district2CommonCodeList.get(i);
                    if(district2.contains(tmpFcstRegionCode.getCode())){
                        parsedDistrict2 = tmpFcstRegionCode.getCode();
                        break;
                    }
                }
            }
        }
        //광주, 전남
        else if(district1.contains("광주")){
            parsedDistrict1ForTmp = "광주.전라남도";
            parsedDistrict2 = "광주";
            parsedDistrict1ForLand = "11F20000";
        }
        else if(district1.contains("전남")){
            parsedDistrict1ForTmp = "광주.전라남도";
            parsedDistrict1ForLand = "11F20000";

            if(address.contains("흑산면")){
                parsedDistrict2 = "흑산도";
            }
            else if(address.contains("완도군")){parsedDistrict2 = "완도";}
            else{
                List<CommonCode> district2CommonCodeList =
                        commonCodeRepository.findAllByExtraValue1AndCodeGroup("광주.전라남도", tmpFcstRegionCodeGroup);

                for(int i=0; i<district2CommonCodeList.size(); i++){
                    CommonCode tmpFcstRegionCode = district2CommonCodeList.get(i);
                    if(district2.contains(tmpFcstRegionCode.getCode())){
                        parsedDistrict2 = tmpFcstRegionCode.getCode();
                        break;
                    }
                }
            }
        }
        //전북
        else if(district1.contains("전북")){
            parsedDistrict1ForTmp = "전라북도";
            parsedDistrict1ForLand = "11F10000";

            List<CommonCode> district2CommonCodeList =
                    commonCodeRepository.findAllByExtraValue1AndCodeGroup("전라북도", tmpFcstRegionCodeGroup);

            for(int i=0; i<district2CommonCodeList.size(); i++){
                CommonCode tmpFcstRegionCode = district2CommonCodeList.get(i);
                if(district2.contains(tmpFcstRegionCode.getCode())){
                    parsedDistrict2 = tmpFcstRegionCode.getCode();
                    break;
                }
            }
        }
        //대전,세종,충남
        else if(district1.contains("대전")){
            parsedDistrict1ForTmp = "대전.세종.충청남도";
            parsedDistrict2 = "대전";
            parsedDistrict1ForLand = "11C20000";
        }
        else if(district1.contains("세종특별자치시")){
            parsedDistrict1ForTmp = "대전.세종.충청남도";
            parsedDistrict2 = "세종";
            parsedDistrict1ForLand = "11C20000";
        }
        else if(district1.contains("충남")){
            parsedDistrict1ForTmp = "대전.세종.충청남도";
            parsedDistrict1ForLand = "11C20000";

            List<CommonCode> district2CommonCodeList =
                    commonCodeRepository.findAllByExtraValue1AndCodeGroup("대전.세종.충청남도", tmpFcstRegionCodeGroup);

            for(int i=0; i<district2CommonCodeList.size(); i++){
                CommonCode tmpFcstRegionCode = district2CommonCodeList.get(i);
                if(district2.contains(tmpFcstRegionCode.getCode())){
                    parsedDistrict2 = tmpFcstRegionCode.getCode();
                    break;
                }
            }
        }
        //충북
        else if(district1.contains("충북")){
            parsedDistrict1ForTmp = "충청북도";
            parsedDistrict1ForLand = "11C10000";

            List<CommonCode> district2CommonCodeList =
                    commonCodeRepository.findAllByExtraValue1AndCodeGroup("충청북도", tmpFcstRegionCodeGroup);

            for(int i=0; i<district2CommonCodeList.size(); i++){
                CommonCode tmpFcstRegionCode = district2CommonCodeList.get(i);
                if(district2.contains(tmpFcstRegionCode.getCode())){
                    parsedDistrict2 = tmpFcstRegionCode.getCode();
                    break;
                }
            }
        }
        //강원
        else if(district1.contains("강원")){
            parsedDistrict1ForTmp = "강원도";

            List<CommonCode> district2CommonCodeList =
                    commonCodeRepository.findAllByExtraValue1AndCodeGroup("강원도", tmpFcstRegionCodeGroup);

            for(int i=0; i<district2CommonCodeList.size(); i++){
                CommonCode tmpFcstRegionCode = district2CommonCodeList.get(i);
                if(district2.contains(tmpFcstRegionCode.getCode())){
                    parsedDistrict2 = tmpFcstRegionCode.getCode();
                    parsedDistrict1ForLand = tmpFcstRegionCode.getRemark().substring(0,4)+"0000";
                    break;
                }
            }
        }
        //제주도
        else if(district1.contains("제주특별자치도")){
            parsedDistrict1ForTmp = "제주도";
            parsedDistrict1ForLand = "11G00000";

            if(address.contains("추자면")){
                parsedDistrict2 = "추자도";
            }
            List<CommonCode> district2CommonCodeList =
                    commonCodeRepository.findAllByExtraValue1AndCodeGroup("제주도", tmpFcstRegionCodeGroup);

            for(int i=0; i<district2CommonCodeList.size(); i++){
                CommonCode tmpFcstRegionCode = district2CommonCodeList.get(i);
                if(district2.contains(tmpFcstRegionCode.getCode())){
                    parsedDistrict2 = tmpFcstRegionCode.getCode(); break;
                }
            }
        }

        CommonCode tmpFcstRegionCommonCode =
                commonCodeRepository.findAllByExtraValue1AndCodeGroupAndCode
                        (parsedDistrict1ForTmp, tmpFcstRegionCodeGroup, parsedDistrict2).get(0);
        String tmpCode = tmpFcstRegionCommonCode.getRemark();

        result.put("tmpFcstRegionCode", tmpCode);
        result.put("landFcstRegionCode", parsedDistrict1ForLand);
        return result;
    }

    @Transactional
    public String getSeaCode(String shipId){
        String observerCode = shipRepository.findById(Long.parseLong(shipId)).get().getObserverCode();
        ObserverCode observer = observerCodeRepository.getObserverCodeByCode(observerCode);
        return observer.getForecastCode();
    }


}
