package com.tobe.fishking.v2.service;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.auth.RegistrationToken;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.enums.fishing.EntityType;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.AddAlertDto;
import com.tobe.fishking.v2.model.common.CouponMemberDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.auth.RegistrationTokenRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.common.AlertsRepository;
import com.tobe.fishking.v2.repository.common.CodeGroupRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import com.tobe.fishking.v2.repository.common.CouponMemberRepository;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.common.AlertService;
import com.tobe.fishking.v2.service.common.PayService;
import com.tobe.fishking.v2.service.common.PopularService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FishkingScheduler {

    private final CouponMemberRepository couponMemberRepository;
    private final AlertService alertService;
    private final AlertsRepository alertsRepository;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PopularService popularService;
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final OrdersRepository ordersRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final CalculateRepository calculateRepository;
    private final PayService payService;
    private final CodeGroupRepository codeGroupRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final RegistrationTokenRepository registrationTokenRepository;
    private final GoodsFishingDateRepository goodsFishingDateRepository;
    private final GoodsRepository goodsRepository;

    /*쿠폰 만료 알림.
    새벽4시마다, 사용기간이 일주일남은 쿠폰들에 대해 alerts를 생성시켜준다. */
    @Scheduled(cron = "0 0 12 * * *")//실서버용
//    @Scheduled(cron = "0 0/1 * * * *")//테스트용
    void checkCouponExpire() throws ResourceNotFoundException, IOException {
        List<CouponMemberDTO> couponList = couponMemberRepository.checkCouponExpire();
        Member manager = memberService.getMemberById(16L);

        for(int i=0; i<couponList.size(); i++){
            CouponMemberDTO dto = couponList.get(i);

            Member receiver = memberRepository.findById(dto.getMember())
                    .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+dto.getMember()));
            //혜택알림 허용 설정되어있으면 푸시알림.
            CodeGroup alertSetCodeGroup = codeGroupRepository.findByCode("alertSet");
            CommonCode benefitAlertSetCommonCode = commonCodeRepository.findByCodeGroupAndCode(alertSetCodeGroup, "benefit");
            if(receiver.hasAlertSetCode(benefitAlertSetCommonCode.getCode())) {
                Set<RegistrationToken> registrationTokenList = receiver.getRegistrationTokenList();
                String alertTitle = "쿠폰 만료 알림";
                String sentence = "보유하고 계신 쿠폰 '" + dto.getCouponName() + "'의 유효기간이 7일 남았습니다." + " 7일 이후에는 자동 소멸됩니다.";

                Alerts alerts = Alerts.builder()
                        .alertType(AlertType.couponExpire)
                        .entityType(EntityType.couponMember)
                        .pid(dto.getId())
                        .content(null)
                        .sentence(sentence)
                        .isRead(false)
                        .isSent(false)
                        .receiver(receiver)
                        .alertTime(LocalDateTime.now())
                        .createdBy(manager)
                        .type("c")
                        .build();

                alerts = alertsRepository.save(alerts);

                for (RegistrationToken item: registrationTokenList) {
                    sendPushAlert(alertTitle, sentence, alerts, item.getToken());
                }
            }
        }
    }

    /*물때 알림. */
//    @Scheduled(cron = "0 0/1 * * * *")
    @Scheduled(cron = "0 0 0,3,6,9,12 * * *")
    public void checkTideAlert() throws IOException {
        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime/*.withMinute(0)*/.withSecond(0).withNano(0);
        List<Alerts> alertsList = alertsRepository.findAllByAlertTypeAndIsSentAndAlertTime(
                AlertType.tide, false, dateTime
        );

        /*알림 전송*/
        for(int i=0; i<alertsList.size(); i++){
            Alerts alerts = alertsList.get(i);

            Member receiver = alerts.getReceiver();
            CodeGroup alertSetCodeGroup = codeGroupRepository.findByCode("alertSet");
            CommonCode tideAlertSetCommonCode = commonCodeRepository.findByCodeGroupAndCode(alertSetCodeGroup, "tide");
            if(receiver.hasAlertSetCode(tideAlertSetCommonCode.getCode())) {
                Set<RegistrationToken> registrationTokenList = receiver.getRegistrationTokenList();
//            String[] alertData = alerts.getContent().split(" ");//index순서대로, 관측소명,물때,몇일전,시간.
                String alertTitle = "[" + alerts.getAlertType().getValue() + "]";

                /*알림 내용 생성*/
//            if(alertData[1].equals("15")){alertData[1] = "조금";}
//            else{alertData[1] += "물";}

                /*푸쉬알림 보내기. */
                for (RegistrationToken item: registrationTokenList) {
                    sendPushAlert(alertTitle, alerts.getSentence(), alerts, item.getToken());
                }
            }
        }

    }

    /*조위 알림*/
    @Scheduled(cron = "0 0/1 * * * *")
    public void checkTideLevelAlert() throws IOException {
        System.out.println("checkTideLevelAlert()");
        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime.withSecond(0).withNano(0);
        List<Alerts> alertsList = alertsRepository.findAllByAlertTypeAndIsSentAndAlertTime(
                AlertType.tideLevel, false, dateTime
        );

        /*알림 전송*/
        for(int i=0; i<alertsList.size(); i++){
            Alerts alerts = alertsList.get(i);

            Member receiver = alerts.getReceiver();
            CodeGroup alertSetCodeGroup = codeGroupRepository.findByCode("alertSet");
            CommonCode tideAlertSetCommonCode = commonCodeRepository.findByCodeGroupAndCode(alertSetCodeGroup, "tide");
            if(receiver.hasAlertSetCode(tideAlertSetCommonCode.getCode())) {
                Set<RegistrationToken> registrationTokenList = receiver.getRegistrationTokenList();
//            String[] alertData = alerts.getContent().split(" ");//index순서대로, 관측소명, 만조/간조, 몇시간전인지.
                String alertTitle = "" + alerts.getAlertType().getValue() + "";
//            String tideHighLow = (alertData[1].equals("high"))? "만조" : "간조";
//            Integer time = Integer.parseInt(alertData[2]);
//            String timeString = null;
//            if(time<0){timeString = Math.abs(time)+"시간 전";}
//            else if(time>0){timeString = Math.abs(time)+"시간 후";}
//            else{timeString="";}

                /*푸쉬알림 보내기. */
                for (RegistrationToken item: registrationTokenList) {
                    sendPushAlert(alertTitle, alerts.getSentence(), alerts, item.getToken());
                }
            }
        }
    }

    /*기상 특보? 알림. */

    /*푸쉬알림 보내기*/
    public void sendPushAlert(String alertTitle, String alertContent, Alerts alerts, String registrationToken) throws IOException {
        String url = "https://fcm.googleapis.com/fcm/send";
        Map<String,String> parameter = new HashMap<>();
        parameter.put("json",
                "{ \"notification\": " +
                        "{" +
                        "\"title\": \"["+alertTitle+"]\", " +
                        "\"body\": \""+alertContent+"\", " +
                        "\"android_channel_id\": \"notification.native_fishking\"" +
                        "}," +
                        "\"to\" : \""+registrationToken+"\"" +
                        "}");
        memberService.sendRequest(url, "JSON", parameter,"key=AAAAlI9VsDY:APA91bGtlb8VOtuRGVFU4jmWrgdDnNN3-qfKBm-5sz2LZ0MqsSvsDBzqHrLPapE2IALudZvlyB-f94xRCrp7vbGcQURaZon368Uey9HQ4_CtTOQQSEa089H_AbmWNVfToR42qA8JGje5");
        alerts.sent();
        alertsRepository.save(alerts);
    }

    @Scheduled(cron = "0 0 0 * * *")
    void checkNoticeDate(){
        Board board = boardRepository.findBoardByFilePublish(FilePublish.notice);
        List<Post> noticeList = postRepository.findAllByBoardAndChannelType(board, ChannelType.important);

        for (Post notice : noticeList) {
            if (notice.getNoticeEndDate() != null) {
                if (LocalDate.now().isAfter(notice.getNoticeEndDate())) {//공지마지막날이 지났으면,
                    notice.setChannelType(ChannelType.general);
                    postRepository.save(notice);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void updatePopularKeyword() {
        popularService.updatePopularKeyword();
    }

    @Scheduled(cron = "0 10 * * * ?")
    @Transactional
    public void confirm() {
        String ip = InetAddress.getLoopbackAddress().getHostAddress();
        Member manager = memberService.getMemberById(16L);
        LocalDateTime targetDate = LocalDateTime.now().plusHours(12L);

        String date = targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = targetDate.format(DateTimeFormatter.ofPattern("HH"));

        List<Goods> goodsList = goodsRepository.getNeedConfirm(date, time);

        if (ip.contains("204")) {
            if (goodsList.size() >= 2) {
                goodsList = goodsList.subList(0, goodsList.size()/2);
            }
        } else {
            if (goodsList.size() >= 2) {
                goodsList = goodsList.subList(goodsList.size() / 2, goodsList.size());
            } else {
                goodsList = new ArrayList<>();
            }
        }

        for (Goods goods : goodsList) {
            try {
                Ship ship = goods.getShip();
                GoodsFishingDate goodsFishingDate = goodsFishingDateRepository.findByGoodsIdAndDateString(goods.getId(), date);

                int confirmCount = 0;
                int cancelCount = 0;
                List<OrderDetails> waitTempList = new ArrayList<>();

                int maxPersonnel = goods.getMaxPersonnel();
                int extraShipCount = goods.getExtraShipNumber();
                int extraMinPersonnel = goods.getExtraPersonnel();
                List<Map<String, Object>> extraData = new ArrayList<>();
                while (extraShipCount > 1) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("remains", maxPersonnel);
                    m.put("board", new ArrayList<OrderDetails>());
                    extraData.add(m);
                    extraShipCount -= 1;
                }

                List<OrderDetails> orderDetailsList = orderDetailsRepository.getByGoodsAndDate(goods, date);
                if (orderDetailsList.isEmpty()) continue;
                for (OrderDetails details : orderDetailsList) {
                    Thread.sleep(800);
                    Orders orders = details.getOrders();
                    if (orders.getOrderStatus().equals(OrderStatus.bookConfirm)) {
                        if (goodsFishingDate.getReservedNumber() < goods.getMinPersonnel()) {
                            orders.changeStatus(OrderStatus.bookCancel);
                            ordersRepository.save(orders);
                            payService.cancelOrder(orders.getId(), "autoCancel");
                            cancelCount += 1;
                        } else {
                            orders.changeStatus(OrderStatus.bookFix);
                            ordersRepository.save(orders);
                            confirmOrder(goods, orders, manager, date);
                            confirmCount += 1;
                        }
                    } else if (orders.getOrderStatus().equals(OrderStatus.waitBook) || orders.getOrderStatus().equals(OrderStatus.bookRunning)) {
                        if (goods.getExtraRun()) {
                            if (details.getPersonnel() > maxPersonnel) {
                                orders.changeStatus(OrderStatus.bookCancel);
                                ordersRepository.save(orders);
                                // 취소에서 메세지도 같이 보냄
                                payService.cancelOrder(orders.getId(), "autoCancel");
                                cancelCount += 1;
                            } else {
                                boolean added = false;
                                for (Map<String, Object> e : extraData) {
                                    int remains = (int) e.get("remains");
                                    if (details.getPersonnel() <= remains) {
                                        remains -= details.getPersonnel();
                                        e.put("remains", remains);
                                        ((List<OrderDetails>) e.get("board")).add(details);
                                        added = true;
                                        break;
                                    }
                                }
                                if (!added) {
                                    orders.changeStatus(OrderStatus.bookCancel);
                                    ordersRepository.save(orders);
                                    // 취소에서 메세지도 같이 보냄
                                    payService.cancelOrder(orders.getId(), "autoCancel");
                                    cancelCount += 1;
                                }
                            }
                        } else {
                            orders.changeStatus(OrderStatus.bookCancel);
                            ordersRepository.save(orders);
                            // 취소에서 메세지도 같이 보냄
                            payService.cancelOrder(orders.getId(), "autoCancel");
                            cancelCount += 1;
                        }
                    }
                    ordersRepository.save(orders);
                }
                for (Map<String, Object> ex : extraData) {
                    int remains = (int) ex.get("remains");
                    List<OrderDetails> dl = (List<OrderDetails>) ex.get("board");
                    if (remains > maxPersonnel - extraMinPersonnel) {
                        for (OrderDetails od : dl) {
                            Orders o = od.getOrders();
                            o.changeStatus(OrderStatus.bookCancel);
                            ordersRepository.save(o);
                            // 취소에서 메세지도 같이 보냄
                            payService.cancelOrder(o.getId(), "autoCancel");
                            cancelCount += 1;
                        }
                    } else {
                        for (OrderDetails od : dl) {
                            Orders o = od.getOrders();
                            o.changeStatus(OrderStatus.bookFix);
                            ordersRepository.save(o);
                            confirmOrder(goods, o, manager, date);
                            confirmCount += 1;
                            goodsFishingDate.addReservedNumber(od.getPersonnel());
                            goodsFishingDate.addWaitNumber(-1 * od.getPersonnel());
                            od.setExtraRun(manager);
                            orderDetailsRepository.save(od);
                        }
                    }
                }
                goodsFishingDateRepository.save(goodsFishingDate);
                String title = "시스템 예약확정";
                String sentence = ship.getShipName() + "의 " + date + " "
                        + time + ":" + goods.getFishingStartTime().substring(2,4)
                        + goods.getName() + "상품의 \n"
                        + "시스템예약확정시점이 도래하여 예약확정 " + confirmCount + "건,\n"
                        + "예약취소 " + cancelCount + "건이 발생하였습니다.";
                AlertType type = AlertType.systemConfirm;
                Member receiver = ship.getCompany().getMember();
                List<RegistrationToken> registrationTokenList = registrationTokenRepository.findAllByCompanyMember(receiver);
                Alerts alerts = Alerts.builder()
                        .alertType(type)
                        .entityType(EntityType.orders)
                        .pid(goods.getId())
                        .content(null)
                        .sentence(sentence)
                        .isRead(false)
                        .isSent(false)
                        .receiver(receiver)
                        .alertTime(LocalDateTime.now())
                        .createdBy(manager)
                        .type("f")
                        .build();
                alerts = alertsRepository.save(alerts);
                for (RegistrationToken item: registrationTokenList) sendPushAlert(title, sentence, alerts, item.getToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void confirmOrder(Goods goods, Orders orders, Member manager, String date) throws IOException {
        AlertType type = AlertType.reservationComplete;
        Member receiver = orders.getCreatedBy();
        Set<RegistrationToken> registrationTokenList = receiver.getRegistrationTokenList();
        String alertTitle = "예약 확정 알림";
        String sentence = receiver.getMemberName() + "님 \n"
                + goods.getShip().getShipName() + "의 \n"
                + date + " " + goods.getFishingStartTime().substring(0,2) + ":" + goods.getFishingStartTime().substring(2) + "의 출조상품이\n"
                + type.getMessage();

        Alerts alerts = Alerts.builder()
                .alertType(type)
                .entityType(EntityType.orders)
                .pid(orders.getId())
                .content(null)
                .sentence(sentence)
                .isRead(false)
                .isSent(false)
                .receiver(receiver)
                .alertTime(LocalDateTime.now())
                .createdBy(manager)
                .build();
        alerts = alertsRepository.save(alerts);
        for(RegistrationToken item: registrationTokenList){
            sendPushAlert(alertTitle, sentence, alerts, item.getToken());
        }
    }

}
