package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.CameraPoint;
import com.tobe.fishking.v2.entity.fishing.CameraPointRealTimeVideo;
import com.tobe.fishking.v2.entity.fishing.RealTimeVideo;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.model.fishing.AddCameraPointDto;
import com.tobe.fishking.v2.model.fishing.AddShipCamera;
import com.tobe.fishking.v2.model.fishing.CameraPointDetailDto;
import com.tobe.fishking.v2.model.fishing.CameraPointDetailDtoForPage;
import com.tobe.fishking.v2.model.smartfishing.CameraLoginDTO;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.ObserverCodeRepository;
import com.tobe.fishking.v2.repository.fishking.CameraPointRealTimeVideoRepository;
import com.tobe.fishking.v2.repository.fishking.CameraPointRepository;
import com.tobe.fishking.v2.repository.fishking.RealTimeVideoRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.service.HttpRequestService;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.smartfishing.FishingShipService;
import com.tobe.fishking.v2.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
    CameraPointRealTimeVideoRepository cameraPointRtVideoRepo;
    @Autowired
    ShipRepository shipRepo;
    @Autowired
    HttpRequestService httpRequestService;
    @Autowired
    FishingShipService fishingShipService;
    @Autowired
    Environment env;

    //카메라 포인트 생성
    @Transactional
    public void addCameraPoint(AddCameraPointDto dto, String token) throws ServiceLogicException, ResourceNotFoundException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnsupportedEncodingException, EmptyListException {
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

        CameraPoint cameraPoint = CameraPoint.builder()
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
        cameraPoint = cameraPointRepo.save(cameraPoint);
//        fileEntity.saveTemporaryFile(cameraPoint.getId());

        //실시간 영상 등록
        //  nhnid로 카메라 목록 가져옴
//        List<Map<String, Object>> cameraList = fishingShipService.getNHNCameraList2(new CameraLoginDTO(dto.getAdtId(), "", null), "");
//        List<AddShipCamera> addShipCameraList = new LinkedList<>();
//        for(int i=0; i<cameraList.size(); i++){
//            AddShipCamera addShipCamera = new AddShipCamera();
//            addShipCamera.setName((String)cameraList.get(i).get("name"));
//            addShipCamera.setSerial((String)cameraList.get(i).get("serial"));
//            addShipCameraList.add(addShipCamera);
//        }


        int cameraNum = 0;
        if (cameraPoint.getAdtId() != null) {//ship으로 수정.
            String cameraToken ;
            String expTime ;

            List<CameraPointRealTimeVideo> preVideos = cameraPointRtVideoRepo.getNHNByNHNId(cameraPoint.getAdtId());
//                List<RealTimeVideo> preVideos = realTimeVideoRepository.getNHNByShipsId(ship.getId());
            if (preVideos.size() > 0) {
                CameraPointRealTimeVideo video = preVideos.get(0);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expTime2 = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(Long.parseLong(video.getExpireTime())),
                        TimeZone.getDefault().toZoneId()
                );
                String expireTime = "";
                if (now.isAfter(expTime2)) {
                    Map<String, String> tokenData = httpRequestService.refreshToken(video.getToken());
                    cameraToken = tokenData.get("token");
                    expTime = tokenData.get("expireTime");
                    cameraPointRtVideoRepo.updateToken(cameraToken, expTime, video.getToken());
                } else {
                    cameraToken = video.getToken();
                    expTime = video.getExpireTime();
                }
            } else {
                Map<String, Object> nhnCameraToken = httpRequestService.getToken(cameraPoint.getAdtId());//company.getNhnId()가 아닌 ship.getNhnId()로 수정.
                cameraToken = ((String) nhnCameraToken.get("token")).replaceAll("\"", "");
                expTime = (String) nhnCameraToken.get("expireTime");
//                token = ((String) httpRequestService.getToken(dto.getId()).get("token")).replaceAll("\"", "");
            }

            for (AddShipCamera addShipCamera : dto.getNhnCameras()) {
                cameraNum += 1;
                CameraPointRealTimeVideo video = CameraPointRealTimeVideo.builder()
                        .rNo(cameraNum)
                        .member(member)
                        .cameraPoint(cameraPoint)
                        .name(addShipCamera.getName())
                        .serial(addShipCamera.getSerial())
                        .token(cameraToken)
                        .expireTime(expTime)
                        .type("toast")
                        .build();
                cameraPointRtVideoRepo.save(video);
            }
        }

        return;
    }
    //카메라 포인트 정보 수정.
    @Transactional
    public void modifyCameraPoint(Long cameraPointId, AddCameraPointDto dto, String token) throws ServiceLogicException, ResourceNotFoundException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnsupportedEncodingException {
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

        int cameraNum = 0;
        List<CameraPointRealTimeVideo> preNhnVideos = cameraPointRtVideoRepo.getNHNByShipsId(cameraPointId);
        cameraPointRtVideoRepo.deleteAll(preNhnVideos);

        if (cameraPoint.getAdtId() != null) {//ship으로 수정.
            String cameraToken ;
            String expTime ;

            List<CameraPointRealTimeVideo> preVideos = cameraPointRtVideoRepo.getNHNByNHNId(cameraPoint.getAdtId());
//                List<RealTimeVideo> preVideos = realTimeVideoRepository.getNHNByShipsId(ship.getId());
            if (preVideos.size() > 0) {
                CameraPointRealTimeVideo video = preVideos.get(0);
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expTime2 = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(Long.parseLong(video.getExpireTime())),
                        TimeZone.getDefault().toZoneId()
                );
                String expireTime = "";
                if (now.isAfter(expTime2)) {
                    Map<String, String> tokenData = httpRequestService.refreshToken(video.getToken());
                    cameraToken = tokenData.get("token");
                    expTime = tokenData.get("expireTime");
                    cameraPointRtVideoRepo.updateToken(cameraToken, expTime, video.getToken());
                } else {
                    cameraToken = video.getToken();
                    expTime = video.getExpireTime();
                }
            } else {
                Map<String, Object> nhnCameraToken = httpRequestService.getToken(cameraPoint.getAdtId());//company.getNhnId()가 아닌 ship.getNhnId()로 수정.
                cameraToken = ((String) nhnCameraToken.get("token")).replaceAll("\"", "");
                expTime = (String) nhnCameraToken.get("expireTime");
//                token = ((String) httpRequestService.getToken(dto.getId()).get("token")).replaceAll("\"", "");
            }

            for (AddShipCamera addShipCamera : dto.getNhnCameras()) {
                cameraNum += 1;
                CameraPointRealTimeVideo video = CameraPointRealTimeVideo.builder()
                        .rNo(cameraNum)
                        .member(member)
                        .cameraPoint(cameraPoint)
                        .name(addShipCamera.getName())
                        .serial(addShipCamera.getSerial())
                        .token(cameraToken)
                        .expireTime(expTime)
                        .type("toast")
                        .build();
                cameraPointRtVideoRepo.save(video);
            }
        }

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
                .imgUrl(env.getProperty("file.downloadUrl")+cameraPoint.getImgUrl())
                .build();

        //실시간 영상 가져오기.
        Long ship_id = 19L; //'카이호'의 id. '지상 카메라'의 카메라 정책 결정 전까지 '지상 카메라'페이지에 임시로 카이호의 실시간 영상 연결.
        String sessionToken = token;
//        List<RealTimeVideo> videos = realTimeVideoRepository.getRealTimeVideoByShipsId(ship_id);
        List<CameraPointRealTimeVideo> videos = cameraPointRtVideoRepo.findAllByCameraPoint(cameraPoint);
        if (videos.size() > 0) {
            CameraPointRealTimeVideo video = videos.get(0);
            if (video.getType().equals("toast")) {
                try {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime expTime = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(Long.parseLong(video.getExpireTime())),
                            TimeZone.getDefault().toZoneId()
                    );
//                LocalDateTime expTime = LocalDateTime.of(2100, 12, 31, 12, 12);
                    String cameraApiToken = "";
                    if (now.isAfter(expTime)) {
//                        Map<String, String> tokenData = httpRequestService.refreshToken(video.getToken());
//                        Company company = companyRepository.getCompanyByShip(response.getId());
                        Ship ship = shipRepo.findById(ship_id)
                                .orElseThrow(()->new ResourceNotFoundException("ship not found for this id :: "+ship_id));
                        Map<String, Object> tokenData = httpRequestService.getToken(cameraPoint.getAdtId());
                        cameraApiToken = ((String) tokenData.get("token")).replaceAll("\"", "");
                        String expireTime = (String) tokenData.get("expireTime");
                        cameraPointRtVideoRepo.updateToken(cameraApiToken, expireTime, video.getToken());
                    } else {
                        cameraApiToken = video.getToken();
                    }
                    if (!cameraApiToken.equals("")) {
                        List<Map<String, Object>> cameras = httpRequestService.getCameraList(cameraApiToken);
                        for (Map<String, Object> camera : cameras) {
                            String serial = camera.get("serialNo").toString();
                            if (serial.equals(video.getSerial())) {
                                String type = camera.get("recordType").toString();
                                String streamStatus = camera.get("streamStatus").toString();
                                String controlStatus = camera.get("controlStatus").toString();
                                String liveUrl = "";
                                if (type.equals("24h")) {
                                    if (streamStatus.equals("on")) {
                                        liveUrl = httpRequestService.getPlayUrl(cameraApiToken, serial);
                                    }
                                } else {
                                    if (controlStatus.equals("on")) {
                                        liveUrl = httpRequestService.getPlayUrl(cameraApiToken, serial);
                                    }
                                }
                                result.setLiveVideo(liveUrl);
                            }
                        }
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            } else {
//                Company company = companyRepository.getCompanyByShip(ship_id);
                Ship ship = shipRepo.findById(ship_id)
                        .orElseThrow(()->new ResourceNotFoundException("ship not found for this id :: "+ship_id));
                String cameraApiToken = null;
                try {
                    if (sessionToken.equals("")) {
                        sessionToken = LocalTime.now().toString();
                    }
                    cameraApiToken = httpRequestService.loginADT(ship.getSkbId(), HashUtil.sha256(ship.getSkbPw()), sessionToken).replaceAll("\"", "");
                    String videoUrl = httpRequestService.getADTCameraLive(video.getSerial(), cameraApiToken);
                    if (videoUrl != null) {
                        result.setLiveVideo(videoUrl);
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                }
            }
        }

        //로그인 계정에 따른 처리.
        if(!token.equals("") && token != null){
            Member member = memberService.getMemberBySessionToken(token);
            //접속 회원이 관리자가 아니고, 해당 포인트가 비활성화나 삭제 되었다면, null반환.
            if( (cameraPoint.getIsDeleted()==true || cameraPoint.getIsActive()==false) && member.getRoles()!=Role.admin){
                return null;
            }
            //관리자가 조회한 경우, 추가 정보 반환
            else if(member.getRoles() == Role.admin){
                result.setAdtId(cameraPoint.getAdtId());
                result.setAdtPw(cameraPoint.getAdtPw());
                result.setIsDeleted(cameraPoint.getIsDeleted());
                result.setIsActive(cameraPoint.getIsActive());
            }
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
