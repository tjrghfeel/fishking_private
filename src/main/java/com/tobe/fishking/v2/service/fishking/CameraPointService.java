package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.CameraPoint;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.model.fishing.AddCameraPointDto;
import com.tobe.fishking.v2.model.fishing.CameraPointDetailDto;
import com.tobe.fishking.v2.model.fishing.CameraPointDetailDtoForPage;
import com.tobe.fishking.v2.repository.common.ObserverCodeRepository;
import com.tobe.fishking.v2.repository.fishking.CameraPointRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CameraPointService {

    @Autowired
    CameraPointRepository cameraPointRepo;
    @Autowired
    ObserverCodeRepository observerCodeRepo;
    @Autowired
    MemberService memberService;

    //카메라 포인트 생성
    @Transactional
    public void addCameraPoint(AddCameraPointDto dto, String token) throws ServiceLogicException {
        Member member = memberService.getMemberBySessionToken(token);
        if (member.getRoles() != Role.admin){throw new ServiceLogicException("권한이 없습니다."); }


        Location location = Location.builder().latitude(dto.getLat()).longitude(dto.getLon()).build();

//        List<ObserverCode> codes = observerCodeRepo.findAll();
//        ObserverCode code = codes.stream()
//                .sorted(Comparator.comparing(e -> e.distanceFrom(location)))
//                .collect(Collectors.toList())
//                .get(0);

        CameraPoint harbor = CameraPoint.builder()
                .name(dto.getName())
                .sido(dto.getSido())
                .gungu(dto.getGungu())
                .address(dto.getAddress())
                .location(location)
                .adtId(dto.getAdtId())
                .adtPw(dto.getAdtPw())
//                .observerCode(code.getCode())
                .createdBy(member)
                .modifiedBy(member)
                .build();
        cameraPointRepo.save(harbor);

        //기존 항구 목록 생성.
    }
    //카메라 포인트 정보 수정.
    @Transactional
    public void modifyCameraPoint(Long cameraPointId, AddCameraPointDto dto, String token) throws ServiceLogicException, ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);
        if (member.getRoles() != Role.admin){throw new ServiceLogicException("권한이 없습니다."); }

        CameraPoint cameraPoint = cameraPointRepo.findById(cameraPointId)
                .orElseThrow(()->new ResourceNotFoundException("cameraPoint not found for this id ::"+cameraPointId));

//        Location location = Location.builder().latitude(dto.getLat()).longitude(dto.getLog()).build();
//
//        List<ObserverCode> codes = observerCodeRepo.findAll();
//        ObserverCode code = codes.stream()
//                .sorted(Comparator.comparing(e -> e.distanceFrom(location)))
//                .collect(Collectors.toList())
//                .get(0);

        cameraPoint.modify(
                dto.getName(), dto.getSido(), dto.getGungu(), dto.getAddress(), dto.getLat(), dto.getLon(),
                dto.getAdtId(), dto.getAdtPw(), member
        );

        cameraPointRepo.save(cameraPoint);

        return;
    }

    @Transactional
    public CameraPointDetailDto getCameraPointDetail(Long cameraPointId, String token) throws ResourceNotFoundException {
        CameraPoint cameraPoint = cameraPointRepo.findById(cameraPointId)
                .orElseThrow(()->new ResourceNotFoundException("harbor not found for this id ::"+cameraPointId));

        CameraPointDetailDto result = CameraPointDetailDto.builder()
                .id(cameraPoint.getId())
                .name(cameraPoint.getName())
                .sido(cameraPoint.getSido())
                .gungu(cameraPoint.getGungu())
                .address(cameraPoint.getAddress())
                .lat(cameraPoint.getLocation().getLatitude())
                .lon(cameraPoint.getLocation().getLongitude())
                .adtId(null)
                .adtPw(null)
                .build();

        //관리자가 조회한 경우, 카메라 id,pw 반환
        Member member = memberService.getMemberBySessionToken(token);
        if(member.getRoles() == Role.admin){
            result.setAdtId(cameraPoint.getAdtId());
            result.setAdtPw(cameraPoint.getAdtPw());
        }

        return result;
    }

    //카메라 포인트 리스트 조회
    @Transactional
    public Page<CameraPointDetailDtoForPage> getCameraPointList(String token, int page, int pageCount) throws ServiceLogicException {
        Member member = memberService.getMemberBySessionToken(token);
        if (member.getRoles() != Role.admin){throw new ServiceLogicException("권한이 없습니다."); }

        Pageable pageable = PageRequest.of(page, pageCount);
        Page<CameraPointDetailDtoForPage> result = cameraPointRepo.getAllByPage(pageable);

        return result;
    }
}
