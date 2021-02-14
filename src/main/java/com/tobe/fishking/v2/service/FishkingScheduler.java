package com.tobe.fishking.v2.service;

import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.AddAlertDto;
import com.tobe.fishking.v2.model.common.CouponMemberDTO;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.repository.common.AlertsRepository;
import com.tobe.fishking.v2.repository.common.CouponMemberRepository;
import com.tobe.fishking.v2.service.common.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class FishkingScheduler {
    @Autowired
    CouponMemberRepository couponMemberRepository;
    @Autowired
    AlertService alertService;
    @Autowired
    AlertsRepository alertsRepository;

    /*새벽4시마다, 사용기간이 일주일남은 쿠폰들에 대해 alerts를 생성시켜준다. */
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

    @Scheduled(cron = "0 0 0,3,6,9,12 * * *")
    public void checkTideAlert(){
        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime.withMinute(0).withSecond(0);
        List<Alerts> alertsList = alertsRepository.findAllByAlertTypeAndIsSentAndAlertTime(
                AlertType.tide, false, dateTime
        );

        /*알림 전송*/


    }
}
