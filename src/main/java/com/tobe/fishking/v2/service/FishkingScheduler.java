package com.tobe.fishking.v2.service;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.enums.fishing.EntityType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.AddAlertDto;
import com.tobe.fishking.v2.model.common.CouponMemberDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.board.PostRepository;
import com.tobe.fishking.v2.repository.common.AlertsRepository;
import com.tobe.fishking.v2.repository.common.CouponMemberRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.common.AlertService;
import com.tobe.fishking.v2.service.common.PopularService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            String registrationToken = receiver.getRegistrationToken();
            String alertTitle = "쿠폰 만료 알림";
            String sentence = "보유하고 계신 쿠폰 '"+dto.getCouponName()+"'의 유효기간이 7일 남았습니다."+" 7일 이후에는 자동 소멸됩니다.";

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
                    .build();

            alerts = alertsRepository.save(alerts);

            sendPushAlert(alertTitle,sentence,alerts,registrationToken);
        }
    }

    /*물때 알림. */
    @Scheduled(cron = "0 0/1 * * * *")
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
            String registrationToken = receiver.getRegistrationToken();
//            String[] alertData = alerts.getContent().split(" ");//index순서대로, 관측소명,물때,몇일전,시간.
            String alertTitle = "["+alerts.getAlertType().getValue()+"]";

            /*알림 내용 생성*/
//            if(alertData[1].equals("15")){alertData[1] = "조금";}
//            else{alertData[1] += "물";}

            /*푸쉬알림 보내기. */
            sendPushAlert(alertTitle,alerts.getSentence(),alerts, registrationToken);
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
//            String[] alertData = alerts.getContent().split(" ");//index순서대로, 관측소명, 만조/간조, 몇시간전인지.
            String alertTitle = "["+alerts.getAlertType().getValue()+"]";
//            String tideHighLow = (alertData[1].equals("high"))? "만조" : "간조";
//            Integer time = Integer.parseInt(alertData[2]);
//            String timeString = null;
//            if(time<0){timeString = Math.abs(time)+"시간 전";}
//            else if(time>0){timeString = Math.abs(time)+"시간 후";}
//            else{timeString="";}

            /*푸쉬알림 보내기. */
            sendPushAlert(alertTitle,alerts.getSentence(),alerts,registrationToken);
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

    @Scheduled(cron = "0 0 12 * * *")
    void checkNoticeDate(){
        Board board = boardRepository.findBoardByFilePublish(FilePublish.notice);
        List<Post> noticeList = postRepository.findAllByBoardAndChannelType(board, ChannelType.important);

        for(int i=0; i<noticeList.size(); i++){
            Post notice = noticeList.get(i);
            if(LocalDate.now().isAfter(notice.getNoticeEndDate())){//공지마지막날이 지났으면,
                notice.setChannelType(ChannelType.general);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void updatePopularKeyword() {
        popularService.updatePopularKeyword();
    }
}
