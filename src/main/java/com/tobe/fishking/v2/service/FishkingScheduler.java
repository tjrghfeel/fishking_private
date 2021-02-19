package com.tobe.fishking.v2.service;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.AddAlertDto;
import com.tobe.fishking.v2.model.common.CouponMemberDTO;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.repository.common.AlertsRepository;
import com.tobe.fishking.v2.repository.common.CouponMemberRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.common.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FishkingScheduler {
    @Autowired
    CouponMemberRepository couponMemberRepository;
    @Autowired
    AlertService alertService;
    @Autowired
    AlertsRepository alertsRepository;
    @Autowired
    MemberService memberService;

    /*쿠폰 만료 알림.
    새벽4시마다, 사용기간이 일주일남은 쿠폰들에 대해 alerts를 생성시켜준다. */
    @Scheduled(cron = "0 0 4 * * *")
    void checkCouponExpire() throws ResourceNotFoundException {
        System.out.println("coupon expire scheduler start.");
        List<CouponMemberDTO> couponList = couponMemberRepository.checkCouponExpire();

        for(int i=0; i<couponList.size(); i++){
            CouponMemberDTO dto = couponList.get(i);
            AddAlertDto addAlertDto = AddAlertDto.builder()
                    .memberId(dto.getMember())
                    .alertType("couponExpire")
                    .entityType("couponMember")
                    .pid(dto.getId())
                    .content(dto.getCouponName())
                    .createdBy(16L)
                    .build();
            alertService.addAlert(addAlertDto);
        }
    }

    /*물때 알림. */
    @Scheduled(cron = "0 0 0,3,6,9,12 * * *")
    public void checkTideAlert() throws IOException {
        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime.withMinute(0).withSecond(0).withNano(0);
        List<Alerts> alertsList = alertsRepository.findAllByAlertTypeAndIsSentAndAlertTime(
                AlertType.tide, false, dateTime
        );

        /*알림 전송*/
        for(int i=0; i<alertsList.size(); i++){
            Alerts alerts = alertsList.get(i);

            Member receiver = alerts.getReceiver();
            String registrationToken = receiver.getRegistrationToken();
            String[] alertData = alerts.getContent().split(" ");//index순서대로, 관측소명,물때,몇일전,시간.
            String alertTitle = "물때 알림";
            String alertContent = null;

            /*알림 내용 생성*/
            alertContent = alertData[0] + " " + alertData[1] + " " + alertData[2] + "일 전 " + alertData[3] + "시 알림";

            /*푸쉬알림 보내기. */
            String url = "https://fcm.googleapis.com/fcm/send";
            Map<String,String> parameter = new HashMap<>();
            parameter.put("json",
                    "{ \"notification\": " +
                            "{" +
                            "\"title\": \""+alertTitle+"\", " +
                            "\"body\": \""+alertContent+"\", " +
                            "\"android_channel_id\": \"notification.native_fishking\"" +
                            "}," +
                            "\"to\" : \""+registrationToken+"\"" +
                    "}");
            memberService.sendRequest(url, "JSON", parameter,"key=AAAAlI9VsDY:APA91bGtlb8VOtuRGVFU4jmWrgdDnNN3-qfKBm-5sz2LZ0MqsSvsDBzqHrLPapE2IALudZvlyB-f94xRCrp7vbGcQURaZon368Uey9HQ4_CtTOQQSEa089H_AbmWNVfToR42qA8JGje5");
            alerts.sent();
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
            String registrationToken = receiver.getRegistrationToken();
            String[] alertData = alerts.getContent().split(" ");//index순서대로, 관측소명, 만조/간조, 몇시간전인지.
            String alertTitle = "조위 알림";
            String tideHighLow = (alertData[1].equals("high"))? "만조" : "간조";
            Integer time = Integer.parseInt(alertData[2]);
            String timeString = null;
            if(time<0){timeString = time+"시간 전";}
            else if(time>0){timeString = time+"시간 후";}
            else{timeString="";}
            time = Math.abs(time);
            String alertContent = alertData[0] + " " + " " + tideHighLow + " " + timeString+" 알림" ;

            /*푸쉬알림 보내기. */
            String url = "https://fcm.googleapis.com/fcm/send";
            Map<String,String> parameter = new HashMap<>();
            parameter.put("json",
                    "{ \"notification\": " +
                            "{" +
                            "\"title\": \""+alertTitle+"\", " +
                            "\"body\": \""+alertContent+"\", " +
                            "\"android_channel_id\": \"notification.native_fishking\"" +
                            "}," +
                            "\"to\" : \""+registrationToken+"\"" +
                            "}");
            memberService.sendRequest(url, "JSON", parameter,"key=AAAAlI9VsDY:APA91bGtlb8VOtuRGVFU4jmWrgdDnNN3-qfKBm-5sz2LZ0MqsSvsDBzqHrLPapE2IALudZvlyB-f94xRCrp7vbGcQURaZon368Uey9HQ4_CtTOQQSEa089H_AbmWNVfToR42qA8JGje5");
//            alerts.sent();
//            alertsRepository.save(alerts);
        }
    }

    /*기상 특보? 알림. */

    /**/
}
