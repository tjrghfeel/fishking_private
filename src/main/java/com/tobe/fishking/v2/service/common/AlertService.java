package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.AlertsRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class AlertService {

    @Autowired
    MemberRepository memberRepo;
    @Autowired
    CommonCodeRepository commonCodeRepo;
    @Autowired
    AlertsRepository alertsRepo;

    /*알람 추가.
    * 해당 회원의 기존의 Alerts를 삭제하고 다시 생성. */
    @Transactional
    public Long addAlert(Long memberId, Long[] commonCodeIdList) throws ResourceNotFoundException {
        List<CommonCode> commonCodeList = new LinkedList<CommonCode>();
        Member member = memberRepo.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        /*기존의 Alerts엔터티를 삭제. */
        Alerts preAlerts = alertsRepo.findByCreatedBy(member);
        if(preAlerts !=null){  alertsRepo.delete(preAlerts); }

        /*commonCodeIdList를 반복하며 해당id의 CommonCode 엔터티를 commonCodeLIst에 저장해준다. */
        for(int i=0; i<commonCodeIdList.length; i++){
            Long commonCodeId = commonCodeIdList[i];
            CommonCode commonCode = commonCodeRepo.findById(commonCodeId)
                .orElseThrow(()->new ResourceNotFoundException("commonCode not found for this id ::"+commonCodeId));

            commonCodeList.add(commonCode);
        }



        /*Alert 엔터티 생성 및 저장. */
        Alerts newAlerts = Alerts.builder()
                .alert_sets(commonCodeList)
                .createdBy(member)
                .build();
        newAlerts = alertsRepo.save(newAlerts);

        return newAlerts.getId();
    }

}
