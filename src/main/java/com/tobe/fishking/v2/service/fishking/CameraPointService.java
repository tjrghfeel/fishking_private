package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.CameraPoint;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.model.fishing.AddCameraPointDto;
import com.tobe.fishking.v2.model.fishing.CameraPointDetailDto;
import com.tobe.fishking.v2.model.fishing.CameraPointDetailDtoForPage;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.ObserverCodeRepository;
import com.tobe.fishking.v2.repository.fishking.CameraPointRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CameraPointService {

    @Autowired
    CameraPointRepository cameraPointRepo;
    @Autowired
    ObserverCodeRepository observerCodeRepo;
    @Autowired
    MemberService memberService;
    @Autowired
    FileRepository fileRepo;
    @Autowired
    Environment env;

    //카메라 포인트 생성
    @Transactional
    public void addCameraPoint(AddCameraPointDto dto, String token) throws ServiceLogicException, ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);
        if (member.getRoles() != Role.admin){throw new ServiceLogicException("권한이 없습니다."); }

        Location location = Location.builder().latitude(dto.getLat()).longitude(dto.getLon()).build();

//        List<ObserverCode> codes = observerCodeRepo.findAll();
//        ObserverCode code = codes.stream()
//                .sorted(Comparator.comparing(e -> e.distanceFrom(location)))
//                .collect(Collectors.toList())
//                .get(0);
        if(dto.getImgFileId()== null){
            throw new ServiceLogicException("이미지 한 장은 필수입니다.");
        }
        FileEntity fileEntity = fileRepo.findById(dto.getImgFileId())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id ::"+dto.getImgFileId()));
        String imgFileUlr = "/"+fileEntity.getFileUrl()+"/"+fileEntity.getStoredFile();

        CameraPoint harbor = CameraPoint.builder()
                .name(dto.getName())
                .sido(dto.getSido())
                .gungu(dto.getGungu())
                .address(dto.getAddress())
                .location(location)
                .adtId(dto.getAdtId())
                .adtPw(dto.getAdtPw())
//                .observerCode(code.getCode())
                .isDeleted(false)
                .isActive(true)
                .imgUrl(imgFileUlr)
                .createdBy(member)
                .modifiedBy(member)
                .build();
        harbor = cameraPointRepo.save(harbor);
//        fileEntity.saveTemporaryFile(harbor.getId());

        return;
    }
    //카메라 포인트 정보 수정.
    @Transactional
    public void modifyCameraPoint(Long cameraPointId, AddCameraPointDto dto, String token) throws ServiceLogicException, ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);
        if (member.getRoles() != Role.admin){throw new ServiceLogicException("권한이 없습니다."); }

        CameraPoint cameraPoint = cameraPointRepo.findById(cameraPointId)
                .orElseThrow(()->new ResourceNotFoundException("cameraPoint not found for this id ::"+cameraPointId));
        String imgFileUrl = cameraPoint.getImgUrl();
        if(dto.getImgFileId() != null){
            FileEntity fileEntity = fileRepo.findById(dto.getImgFileId())
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id ::"+dto.getImgFileId()));
            imgFileUrl = "/"+fileEntity.getFileUrl()+"/"+fileEntity.getStoredFile();
        }
//        Location location = Location.builder().latitude(dto.getLat()).longitude(dto.getLog()).build();
//
//        List<ObserverCode> codes = observerCodeRepo.findAll();
//        ObserverCode code = codes.stream()
//                .sorted(Comparator.comparing(e -> e.distanceFrom(location)))
//                .collect(Collectors.toList())
//                .get(0);

        cameraPoint.modifyInfo(
                dto.getName(), dto.getSido(), dto.getGungu(), dto.getAddress(), dto.getLat(), dto.getLon(),
                dto.getAdtId(), dto.getAdtPw(), imgFileUrl, member
        );

        cameraPointRepo.save(cameraPoint);

        return;
    }

    @Transactional
    public CameraPointDetailDto getCameraPointDetail(Long cameraPointId, String token) throws ResourceNotFoundException {
        CameraPoint cameraPoint = cameraPointRepo.findById(cameraPointId)
                .orElseThrow(()->new ResourceNotFoundException("harbor not found for this id ::"+cameraPointId));

        Member member = memberService.getMemberBySessionToken(token);
        //접속 회원이 관리자가 아니고, 해당 포인트가 비활성화나 삭제 되었다면, null반환.
        if( (cameraPoint.getIsDeleted()==true || cameraPoint.getIsActive()==false) && member.getRoles()!=Role.admin){
            return null;
        }

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
                .imgUrl(env.getProperty("file.downloadUrl")+cameraPoint.getImgUrl())
                .build();

        //관리자가 조회한 경우, 추가 정보 반환
        if(member.getRoles() == Role.admin){
            result.setAdtId(cameraPoint.getAdtId());
            result.setAdtPw(cameraPoint.getAdtPw());
            result.setIsDeleted(cameraPoint.getIsDeleted());
            result.setIsActive(cameraPoint.getIsActive());
        }

        return result;
    }

    //카메라 포인트 리스트 조회(관리자용)
    @Transactional
    public Page<CameraPointDetailDtoForPage> getCameraPointList(String token, int page, int pageCount) throws ServiceLogicException {
        Member member = memberService.getMemberBySessionToken(token);

        Pageable pageable = PageRequest.of(page, pageCount);
        Page<CameraPointDetailDtoForPage> result = null;

        //관리자인 경우
        if (member.getRoles() != Role.admin){
//            result = cameraPointRepo.getAllByPage(pageable);
        }
        else{
            result = cameraPointRepo.getAllByPageForManager(pageable);
        }

        return result;
    }
    //카메라 포인트 리스트 조회(고객앱 메인용)
    @Transactional
    public List<CameraPointDetailDtoForPage> getCameraPointList(){
        List<CameraPointDetailDtoForPage> result = cameraPointRepo.getAllForMainPage();
        return result;
    }

    //카메라 포인트 삭제
    @Transactional
    public Boolean deleteCameraPoint(Long cameraPointId, String token) throws ServiceLogicException {
        Member member = memberService.getMemberBySessionToken(token);
        if(member.getRoles() != Role.admin){
            throw new ServiceLogicException("권한이 없습니다.");
        }

        CameraPoint cameraPoint = cameraPointRepo.getOne(cameraPointId);
        cameraPoint.setIsDelete();
        cameraPointRepo.save(cameraPoint);
        return true;
    }

    //카메라 포인트 활성화/비활성화
    @Transactional
    public Boolean setIsActive(Long cameraPointId, Boolean isActive, String token) throws ServiceLogicException {
        Member member = memberService.getMemberBySessionToken(token);
        if(member.getRoles() != Role.admin){
            throw new ServiceLogicException("권한이 없습니다.");
        }

        CameraPoint cameraPoint = cameraPointRepo.getOne(cameraPointId);
        cameraPoint.setIsActive(isActive);
        cameraPointRepo.save(cameraPoint);
        return true;
    }
}
