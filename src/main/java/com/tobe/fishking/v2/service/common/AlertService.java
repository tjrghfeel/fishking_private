package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Alerts;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.fishing.EntityType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.AddAlertDto;
import com.tobe.fishking.v2.model.common.AlertListForPage;
import com.tobe.fishking.v2.model.common.DeleteAlertDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.AlertsRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Long addAlert(AddAlertDto dto) throws ResourceNotFoundException {
        Member receiver = memberRepo.findById(dto.getMemberId())
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+dto.getMemberId()));
        Member createdBy = memberRepo.findById(dto.getCreatedBy())
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+dto.getCreatedBy()));

        Alerts alerts = Alerts.builder()
                .alertType(AlertType.valueOf(dto.getAlertType()))
                .entityType(EntityType.valueOf(dto.getEntityType()))
                .pid(dto.getPid())
                .isRead(false)
                .content(dto.getContent())
                .receiver(receiver)
                .createdBy(createdBy)
                .build();
        return alertsRepo.save(alerts).getId();
    }

    /*알람 삭제*/
    @Transactional
    public boolean deleteAlert(DeleteAlertDto dto) throws ResourceNotFoundException {
        Alerts alerts = alertsRepo.findById(dto.getAlertId())
                .orElseThrow(()->new ResourceNotFoundException("alerts not found for this id :: "+dto.getAlertId()));

        alertsRepo.delete(alerts);
        return true;
    }

    /*회원의 알람 리스트 반환
    * - 세션토큰에 해당하는 회원의 현재 알림 리스트 Page형태로 반환
    * - AlertType의 메세지에다가 Alert entity의 content필드의 내용을 붙여서 완전한 알림메세지로 만들어 반환
    * - AlertType에 맞는 알람이미지url을 반환.
    * - */
    @Transactional
    public Page<AlertListForPage> getAlertList(String token) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(0,50);
        return alertsRepo.findAllByMember(token,pageable);
    }
}
