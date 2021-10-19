package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.fishing.Harbor;
import com.tobe.fishking.v2.entity.fishing.HarborDetailDto;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.model.fishing.AddHarborDto;
import com.tobe.fishking.v2.repository.common.ObserverCodeRepository;
import com.tobe.fishking.v2.repository.fishking.HarborRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HarborService {

    @Autowired
    HarborRepository harborRepo;
    @Autowired
    ObserverCodeRepository observerCodeRepo;
    @Autowired
    MemberService memberService;

    @Transactional
    public void addHarbor(AddHarborDto dto, String token) throws ServiceLogicException {
        Member member = memberService.getMemberBySessionToken(token);
        if (member.getRoles() != Role.admin){throw new ServiceLogicException("권한이 없습니다."); }


        Location location = Location.builder().latitude(dto.getLat()).longitude(dto.getLog()).build();

        List<ObserverCode> codes = observerCodeRepo.findAll();
        ObserverCode code = codes.stream()
                .sorted(Comparator.comparing(e -> e.distanceFrom(location)))
                .collect(Collectors.toList())
                .get(0);

        Harbor harbor = Harbor.builder()
                .name(dto.getName())
                .sido(dto.getSido())
                .gungu(dto.getGungu())
                .address(dto.getAddress())
                .location(location)
                .adtId(dto.getAdtId())
                .adtPw(dto.getAdtPw())
                .observerCode(code.getCode())
                .createdBy(member)
                .modifiedBy(member)
                .build();
        harborRepo.save(harbor);

        //기존 항구 목록 생성.
    }

    @Transactional
    public HarborDetailDto getHarborDetail(Long harborId, String token) throws ResourceNotFoundException {
        Harbor harbor = harborRepo.findById(harborId)
                .orElseThrow(()->new ResourceNotFoundException("harbor not found for this id ::"+harborId));
        ObserverCode observer = observerCodeRepo.getObserverCodeByCode(harbor.getObserverCode());

        HarborDetailDto result = HarborDetailDto.builder()
                .id(harbor.getId())
                .name(harbor.getName())
                .sido(harbor.getSido())
                .gungu(harbor.getGungu())
                .address(harbor.getAddress())
                .lat(harbor.getLocation().getLatitude())
                .lon(harbor.getLocation().getLongitude())
                .observerCode(harbor.getObserverCode())
                .observerId(observer.getId())
                .adtId(null)
                .adtPw(null)
                .build();

        //관리자가 조회한 경우, 카메라 id,pw 반환
        Member member = memberService.getMemberBySessionToken(token);
        if(member.getRoles() == Role.admin){
            result.setAdtId(harbor.getAdtId());
            result.setAdtPw(harbor.getAdtPw());
        }

        return result;
    }
}
