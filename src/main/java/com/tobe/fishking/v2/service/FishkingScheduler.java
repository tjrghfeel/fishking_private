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

//    private final CouponMemberRepository couponMemberRepository;
//    private final AlertService alertService;
    private final AlertsRepository alertsRepository;
    private final MemberService memberService;
//    private final MemberRepository memberRepository;
//    private final PopularService popularService;
//    private final PostRepository postRepository;
//    private final BoardRepository boardRepository;
//    private final OrdersRepository ordersRepository;
//    private final OrderDetailsRepository orderDetailsRepository;
//    private final CalculateRepository calculateRepository;
//    private final PayService payService;
//    private final CodeGroupRepository codeGroupRepository;
//    private final CommonCodeRepository commonCodeRepository;
//    private final RegistrationTokenRepository registrationTokenRepository;
//    private final GoodsFishingDateRepository goodsFishingDateRepository;
//    private final GoodsRepository goodsRepository;
//
//    /*?????? ?????? ??????.
//    ??????4?????????, ??????????????? ??????????????? ???????????? ?????? alerts??? ??????????????????. */
//    @Scheduled(cron = "0 0 12 * * *")//????????????
////    @Scheduled(cron = "0 0/1 * * * *")//????????????
//    void checkCouponExpire() throws ResourceNotFoundException, IOException {
//        List<CouponMemberDTO> couponList = couponMemberRepository.checkCouponExpire();
//        Member manager = memberService.getMemberById(16L);
//
//        for(int i=0; i<couponList.size(); i++){
//            CouponMemberDTO dto = couponList.get(i);
//
//            Member receiver = memberRepository.findById(dto.getMember())
//                    .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+dto.getMember()));
//            //???????????? ?????? ????????????????????? ????????????.
//            CodeGroup alertSetCodeGroup = codeGroupRepository.findByCode("alertSet");
//            CommonCode benefitAlertSetCommonCode = commonCodeRepository.findByCodeGroupAndCode(alertSetCodeGroup, "benefit");
//            if(receiver.hasAlertSetCode(benefitAlertSetCommonCode.getCode())) {
//                Set<RegistrationToken> registrationTokenList = receiver.getRegistrationTokenList();
//                String alertTitle = "?????? ?????? ??????";
//                String sentence = "???????????? ?????? ?????? '" + dto.getCouponName() + "'??? ??????????????? 7??? ???????????????." + " 7??? ???????????? ?????? ???????????????.";
//
//                Alerts alerts = Alerts.builder()
//                        .alertType(AlertType.couponExpire)
//                        .entityType(EntityType.couponMember)
//                        .pid(dto.getId())
//                        .content(null)
//                        .sentence(sentence)
//                        .isRead(false)
//                        .isSent(false)
//                        .receiver(receiver)
//                        .alertTime(LocalDateTime.now())
//                        .createdBy(manager)
//                        .type("c")
//                        .build();
//
//                alerts = alertsRepository.save(alerts);
//
//                for (RegistrationToken item: registrationTokenList) {
//                    sendPushAlert(alertTitle, sentence, alerts, item.getToken());
//                }
//            }
//        }
//    }
//
//    /*?????? ??????. */
////    @Scheduled(cron = "0 0/1 * * * *")
//    @Scheduled(cron = "0 0 0,3,6,9,12 * * *")
//    public void checkTideAlert() throws IOException {
//        LocalDateTime dateTime = LocalDateTime.now();
//        dateTime = dateTime/*.withMinute(0)*/.withSecond(0).withNano(0);
//        List<Alerts> alertsList = alertsRepository.findAllByAlertTypeAndIsSentAndAlertTime(
//                AlertType.tide, false, dateTime
//        );
//
//        /*?????? ??????*/
//        for(int i=0; i<alertsList.size(); i++){
//            Alerts alerts = alertsList.get(i);
//
//            Member receiver = alerts.getReceiver();
//            CodeGroup alertSetCodeGroup = codeGroupRepository.findByCode("alertSet");
//            CommonCode tideAlertSetCommonCode = commonCodeRepository.findByCodeGroupAndCode(alertSetCodeGroup, "tide");
//            if(receiver.hasAlertSetCode(tideAlertSetCommonCode.getCode())) {
//                Set<RegistrationToken> registrationTokenList = receiver.getRegistrationTokenList();
////            String[] alertData = alerts.getContent().split(" ");//index????????????, ????????????,??????,?????????,??????.
//                String alertTitle = "[" + alerts.getAlertType().getValue() + "]";
//
//                /*?????? ?????? ??????*/
////            if(alertData[1].equals("15")){alertData[1] = "??????";}
////            else{alertData[1] += "???";}
//
//                /*???????????? ?????????. */
//                for (RegistrationToken item: registrationTokenList) {
//                    sendPushAlert(alertTitle, alerts.getSentence(), alerts, item.getToken());
//                }
//            }
//        }
//
//    }
//
//    /*?????? ??????*/
//    @Scheduled(cron = "0 0/1 * * * *")
//    public void checkTideLevelAlert() throws IOException {
//        System.out.println("checkTideLevelAlert()");
//        LocalDateTime dateTime = LocalDateTime.now();
//        dateTime = dateTime.withSecond(0).withNano(0);
//        List<Alerts> alertsList = alertsRepository.findAllByAlertTypeAndIsSentAndAlertTime(
//                AlertType.tideLevel, false, dateTime
//        );
//
//        /*?????? ??????*/
//        for(int i=0; i<alertsList.size(); i++){
//            Alerts alerts = alertsList.get(i);
//
//            Member receiver = alerts.getReceiver();
//            CodeGroup alertSetCodeGroup = codeGroupRepository.findByCode("alertSet");
//            CommonCode tideAlertSetCommonCode = commonCodeRepository.findByCodeGroupAndCode(alertSetCodeGroup, "tide");
//            if(receiver.hasAlertSetCode(tideAlertSetCommonCode.getCode())) {
//                Set<RegistrationToken> registrationTokenList = receiver.getRegistrationTokenList();
////            String[] alertData = alerts.getContent().split(" ");//index????????????, ????????????, ??????/??????, ??????????????????.
//                String alertTitle = "" + alerts.getAlertType().getValue() + "";
////            String tideHighLow = (alertData[1].equals("high"))? "??????" : "??????";
////            Integer time = Integer.parseInt(alertData[2]);
////            String timeString = null;
////            if(time<0){timeString = Math.abs(time)+"?????? ???";}
////            else if(time>0){timeString = Math.abs(time)+"?????? ???";}
////            else{timeString="";}
//
//                /*???????????? ?????????. */
//                for (RegistrationToken item: registrationTokenList) {
//                    sendPushAlert(alertTitle, alerts.getSentence(), alerts, item.getToken());
//                }
//            }
//        }
//    }
//
//    /*?????? ??????? ??????. */
//
    /*???????????? ?????????*/
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
//
//    @Scheduled(cron = "0 0 0 * * *")
//    void checkNoticeDate(){
//        Board board = boardRepository.findBoardByFilePublish(FilePublish.notice);
//        List<Post> noticeList = postRepository.findAllByBoardAndChannelType(board, ChannelType.important);
//
//        for (Post notice : noticeList) {
//            if (notice.getNoticeEndDate() != null) {
//                if (LocalDate.now().isAfter(notice.getNoticeEndDate())) {//????????????????????? ????????????,
//                    notice.setChannelType(ChannelType.general);
//                    postRepository.save(notice);
//                }
//            }
//        }
//    }
//
//    @Scheduled(cron = "0 0 1 * * ?")
//    public void updatePopularKeyword() {
//        popularService.updatePopularKeyword();
//    }

//    @Scheduled(cron = "0 10 * * * ?")
//    @Transactional
//    public void confirmOrder() throws IOException {
//        Member manager = memberService.getMemberById(16L);
//        LocalDateTime targetDate = LocalDateTime.now().plusHours(12L);
//
//        String now = targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        String time = targetDate.format(DateTimeFormatter.ofPattern("HH"));
//
//        List<Orders> confirm = ordersRepository.getOrderByStatusForScheduler(now, time, OrderStatus.bookConfirm);
//        List<Orders> wait = ordersRepository.getOrderByStatusForScheduler(now, time, OrderStatus.waitBook);
//        List<Orders> running = ordersRepository.getOrderByStatusForScheduler(now, time, OrderStatus.bookRunning);
//        wait.addAll(running);
//
//        List<Orders> copied = new ArrayList<>(confirm);
//        copied.addAll(wait);
//        copied.addAll(running);
//        List<Goods> goodsList = copied.stream().map(Orders::getGoods).distinct().collect(Collectors.toList());
//        List<Map<String, Object>> goodsExtraRunData = new ArrayList<>();
//        int[] confirmCounts = new int[goodsList.size()];
//        int[] cancelCounts = new int[goodsList.size()];
//
////        for (Goods g : goodsList) {
////            Map<String, Object> data = new HashMap<>();
////            data.put("run", g.getExtraRun());
////            data.put("personnel", g.getExtraPersonnel());
////            data.put("ships", g.getExtraShipNumber());
////            data.put("adds", 0);
////            goodsExtraRunData.add(data);
////        }
////
////        for (Orders w : wait) {
////            int gIdx = goodsList.indexOf(w.getGoods());
////            Map<String, Object> extraData = goodsExtraRunData.get(gIdx);
////            Boolean run = (Boolean) extraData.get("run");
////            if (run) {
////                int personnel = (int) extraData.get("personnel");
////                int ships = (int) extraData.get("ships");
////                int adds = (int) extraData.get("adds");
////                OrderDetails details = orderDetailsRepository.findByOrders(w);
////                adds += details.getPersonnel();
////                if (adds < (ships * personnel)) {
////                    confirm.add(w);
////                    wait.remove(w);
////                    GoodsFishingDate goodsFishingDate = goodsFishingDateRepository.findByGoodsIdAndDateString(w.getGoods().getId(), w.getFishingDate());
////                    goodsFishingDate.addReservedNumber(details.getPersonnel());
////                    goodsFishingDate.addWaitNumber(-1 * details.getPersonnel());
////                    goodsFishingDateRepository.save(goodsFishingDate);
////                }
////                extraData.put("adds", adds);
////            }
////        }
//
//        wait.addAll(running);
//        for (Orders o : confirm) {
//            int goodsIdx = goodsList.indexOf(o.getGoods());
//            confirmCounts[goodsIdx] += 1;
//
//            o.changeStatus(OrderStatus.bookFix);
//            ordersRepository.save(o);
//
//            AlertType type = AlertType.reservationComplete;
//            Member receiver = o.getCreatedBy();
//            Goods goods = o.getGoods();
//            GoodsFishingDate goodsFishingDate = goodsFishingDateRepository.findByGoodsIdAndDateString(goods.getId(), now);
//            if (goodsFishingDate.getReservedNumber() < goods.getMinPersonnel()) {
//                wait.add(o);
//                continue;
//            }
//            Set<RegistrationToken> registrationTokenList = receiver.getRegistrationTokenList();
//            String alertTitle = "?????? ?????? ??????";
//            String sentence = receiver.getMemberName() + "??? \n"
//                    + goods.getShip().getShipName() + "??? \n"
//                    + now + " " + goods.getFishingStartTime().substring(0,2) + ":" + goods.getFishingStartTime().substring(2) + "??? ???????????????\n"
//                    + type.getMessage();
//
//            Alerts alerts = Alerts.builder()
//                    .alertType(type)
//                    .entityType(EntityType.orders)
//                    .pid(o.getId())
//                    .content(null)
//                    .sentence(sentence)
//                    .isRead(false)
//                    .isSent(false)
//                    .receiver(receiver)
//                    .alertTime(LocalDateTime.now())
//                    .createdBy(manager)
//                    .build();
//            alerts = alertsRepository.save(alerts);
//            for(RegistrationToken item: registrationTokenList){
//                sendPushAlert(alertTitle, sentence, alerts, item.getToken());
//            }
//        }
//
//        for (Orders o : wait) {
//            int goodsIdx = goodsList.indexOf(o.getGoods());
//            cancelCounts[goodsIdx] += 1;
//
//            try {
//                Thread.sleep(500);
//                payService.cancelOrder(o.getId(), " ");
//
////                AlertType type = AlertType.reservationCancel;
////                Member receiver = o.getCreatedBy();
////                Goods goods = o.getGoods();
////                Set<RegistrationToken> registrationTokenList = receiver.getRegistrationTokenList();
////                String alertTitle = "?????? ?????? ??????";
////                String sentence = receiver.getMemberName() + "??? \n"
////                        + goods.getShip().getShipName() + "??? \n"
////                        + now + " " + goods.getFishingStartTime().substring(0,2) + ":" + goods.getFishingStartTime().substring(2) + "??? ???????????????\n"
////                        + type.getMessage();
//
////                Alerts alerts = Alerts.builder()
////                        .alertType(type)
////                        .entityType(EntityType.orders)
////                        .pid(o.getId())
////                        .content(null)
////                        .sentence(sentence)
////                        .isRead(false)
////                        .isSent(false)
////                        .receiver(receiver)
////                        .alertTime(LocalDateTime.now())
////                        .createdBy(manager)
////                        .build();
////                alerts = alertsRepository.save(alerts);
////                for(RegistrationToken item: registrationTokenList){
////                    sendPushAlert(alertTitle, sentence, alerts, item.getToken());
////                }
//            } catch (Exception e) {
//                continue;
//            }
//        }
//
//        for (Goods g : goodsList) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            int idx = goodsList.indexOf(g);
//            String title = "????????? ????????????";
//            String sentence = g.getShip().getShipName() + "??? " + now + " "
//                    + g.getFishingStartTime().substring(0,2) + ":" + g.getFishingStartTime().substring(2,4)
//                    + g.getName() + "????????? \n"
//                    + "?????????????????????????????? ???????????? ???????????? " + confirmCounts[idx] + "???,\n"
//                    + "???????????? " + cancelCounts[idx] + "?????? ?????????????????????.";
//            AlertType type = AlertType.systemConfirm;
//            Member receiver = g.getShip().getCompany().getMember();
//            List<RegistrationToken> registrationTokenList = registrationTokenRepository.findAllByCompanyMember(receiver);
//            Alerts alerts = Alerts.builder()
//                    .alertType(type)
//                    .entityType(EntityType.orders)
//                    .pid(g.getId())
//                    .content(null)
//                    .sentence(sentence)
//                    .isRead(false)
//                    .isSent(false)
//                    .receiver(receiver)
//                    .alertTime(LocalDateTime.now())
//                    .createdBy(manager)
//                    .type("f")
//                    .build();
//            alerts = alertsRepository.save(alerts);
//            for (RegistrationToken item: registrationTokenList) sendPushAlert(title, sentence, alerts, item.getToken());
//        }
//    }


//    @Scheduled(cron = "0 10 * * * ?")
//    @Transactional
//    public void confirm() {
//        String ip = InetAddress.getLoopbackAddress().getHostAddress();
//        Member manager = memberService.getMemberById(16L);
//        LocalDateTime targetDate = LocalDateTime.now().plusHours(12L);
//
//        String date = targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        String time = targetDate.format(DateTimeFormatter.ofPattern("HH"));
//
//        List<Goods> goodsList = goodsRepository.getNeedConfirm(date, time);
//
//        if (ip.contains("204")) {
//            if (goodsList.size() >= 2) {
//                goodsList = goodsList.subList(0, goodsList.size()/2);
//            }
//        } else {
//            if (goodsList.size() >= 2) {
//                goodsList = goodsList.subList(goodsList.size() / 2, goodsList.size());
//            } else {
//                goodsList = new ArrayList<>();
//            }
//        }
//
//        for (Goods goods : goodsList) {
//            try {
//                Ship ship = goods.getShip();
//                GoodsFishingDate goodsFishingDate = goodsFishingDateRepository.findByGoodsIdAndDateString(goods.getId(), date);
//
//                int confirmCount = 0;
//                int cancelCount = 0;
//                List<OrderDetails> waitTempList = new ArrayList<>();
//
//                int maxPersonnel = goods.getMaxPersonnel();
//                int extraShipCount = goods.getExtraShipNumber();
//                int extraMinPersonnel = goods.getExtraPersonnel();
//                List<Map<String, Object>> extraData = new ArrayList<>();
//                while (extraShipCount > 1) {
//                    Map<String, Object> m = new HashMap<>();
//                    m.put("remains", maxPersonnel);
//                    m.put("board", new ArrayList<OrderDetails>());
//                    extraData.add(m);
//                    extraShipCount -= 1;
//                }
//
//                List<OrderDetails> orderDetailsList = orderDetailsRepository.getByGoodsAndDate(goods, date);
//                if (orderDetailsList.isEmpty()) continue;
//                for (OrderDetails details : orderDetailsList) {
//                    Thread.sleep(800);
//                    Orders orders = details.getOrders();
//                    if (orders.getOrderStatus().equals(OrderStatus.bookConfirm)) {
//                        if (goodsFishingDate.getReservedNumber() < goods.getMinPersonnel()) {
//                            orders.changeStatus(OrderStatus.bookCancel);
//                            ordersRepository.save(orders);
//                            payService.cancelOrder(orders.getId(), "autoCancel");
//                            cancelCount += 1;
//                        } else {
//                            orders.changeStatus(OrderStatus.bookFix);
//                            ordersRepository.save(orders);
//                            confirmOrder(goods, orders, manager, date);
//                            confirmCount += 1;
//                        }
//                    } else if (orders.getOrderStatus().equals(OrderStatus.waitBook) || orders.getOrderStatus().equals(OrderStatus.bookRunning)) {
//                        if (goods.getExtraRun()) {
//                            if (details.getPersonnel() > maxPersonnel) {
//                                orders.changeStatus(OrderStatus.bookCancel);
//                                ordersRepository.save(orders);
//                                // ???????????? ???????????? ?????? ??????
//                                payService.cancelOrder(orders.getId(), "autoCancel");
//                                cancelCount += 1;
//                            } else {
//                                boolean added = false;
//                                for (Map<String, Object> e : extraData) {
//                                    int remains = (int) e.get("remains");
//                                    if (details.getPersonnel() <= remains) {
//                                        remains -= details.getPersonnel();
//                                        e.put("remains", remains);
//                                        ((List<OrderDetails>) e.get("board")).add(details);
//                                        added = true;
//                                        break;
//                                    }
//                                }
//                                if (!added) {
//                                    orders.changeStatus(OrderStatus.bookCancel);
//                                    ordersRepository.save(orders);
//                                    // ???????????? ???????????? ?????? ??????
//                                    payService.cancelOrder(orders.getId(), "autoCancel");
//                                    cancelCount += 1;
//                                }
//                            }
//                        } else {
//                            orders.changeStatus(OrderStatus.bookCancel);
//                            ordersRepository.save(orders);
//                            // ???????????? ???????????? ?????? ??????
//                            payService.cancelOrder(orders.getId(), "autoCancel");
//                            cancelCount += 1;
//                        }
//                    }
//                    ordersRepository.save(orders);
//                }
//                for (Map<String, Object> ex : extraData) {
//                    int remains = (int) ex.get("remains");
//                    List<OrderDetails> dl = (List<OrderDetails>) ex.get("board");
//                    if (remains > maxPersonnel - extraMinPersonnel) {
//                        for (OrderDetails od : dl) {
//                            Orders o = od.getOrders();
//                            o.changeStatus(OrderStatus.bookCancel);
//                            ordersRepository.save(o);
//                            // ???????????? ???????????? ?????? ??????
//                            payService.cancelOrder(o.getId(), "autoCancel");
//                            cancelCount += 1;
//                        }
//                    } else {
//                        for (OrderDetails od : dl) {
//                            Orders o = od.getOrders();
//                            o.changeStatus(OrderStatus.bookFix);
//                            ordersRepository.save(o);
//                            confirmOrder(goods, o, manager, date);
//                            confirmCount += 1;
//                            goodsFishingDate.addReservedNumber(od.getPersonnel());
//                            goodsFishingDate.addWaitNumber(-1 * od.getPersonnel());
//                            od.setExtraRun(manager);
//                            orderDetailsRepository.save(od);
//                        }
//                    }
//                }
//                goodsFishingDateRepository.save(goodsFishingDate);
//                String title = "????????? ????????????";
//                String sentence = ship.getShipName() + "??? " + date + " "
//                        + time + ":" + goods.getFishingStartTime().substring(2,4)
//                        + goods.getName() + "????????? \n"
//                        + "?????????????????????????????? ???????????? ???????????? " + confirmCount + "???,\n"
//                        + "???????????? " + cancelCount + "?????? ?????????????????????.";
//                AlertType type = AlertType.systemConfirm;
//                Member receiver = ship.getCompany().getMember();
//                List<RegistrationToken> registrationTokenList = registrationTokenRepository.findAllByCompanyMember(receiver);
//                Alerts alerts = Alerts.builder()
//                        .alertType(type)
//                        .entityType(EntityType.orders)
//                        .pid(goods.getId())
//                        .content(null)
//                        .sentence(sentence)
//                        .isRead(false)
//                        .isSent(false)
//                        .receiver(receiver)
//                        .alertTime(LocalDateTime.now())
//                        .createdBy(manager)
//                        .type("f")
//                        .build();
//                alerts = alertsRepository.save(alerts);
//                for (RegistrationToken item: registrationTokenList) sendPushAlert(title, sentence, alerts, item.getToken());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    private void confirmOrder(Goods goods, Orders orders, Member manager, String date) throws IOException {
//        AlertType type = AlertType.reservationComplete;
//        Member receiver = orders.getCreatedBy();
//        Set<RegistrationToken> registrationTokenList = receiver.getRegistrationTokenList();
//        String alertTitle = "?????? ?????? ??????";
//        String sentence = receiver.getMemberName() + "??? \n"
//                + goods.getShip().getShipName() + "??? \n"
//                + date + " " + goods.getFishingStartTime().substring(0,2) + ":" + goods.getFishingStartTime().substring(2) + "??? ???????????????\n"
//                + type.getMessage();
//
//        Alerts alerts = Alerts.builder()
//                .alertType(type)
//                .entityType(EntityType.orders)
//                .pid(orders.getId())
//                .content(null)
//                .sentence(sentence)
//                .isRead(false)
//                .isSent(false)
//                .receiver(receiver)
//                .alertTime(LocalDateTime.now())
//                .createdBy(manager)
//                .build();
//        alerts = alertsRepository.save(alerts);
//        for(RegistrationToken item: registrationTokenList){
//            sendPushAlert(alertTitle, sentence, alerts, item.getToken());
//        }
//    }

}
