package com.tobe.fishking.v2.service.smartfishing;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.common.Event;
import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.fishing.*;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.model.common.ShareStatus;
import com.tobe.fishking.v2.model.fishing.AddEvent;
import com.tobe.fishking.v2.model.fishing.AddShipCamera;
import com.tobe.fishking.v2.model.fishing.AddShipDTO;
import com.tobe.fishking.v2.model.fishing.UpdateShipDTO;
import com.tobe.fishking.v2.model.response.FishingShipResponse;
import com.tobe.fishking.v2.model.response.UpdateShipResponse;
import com.tobe.fishking.v2.model.smartfishing.PlaceDTO;
import com.tobe.fishking.v2.model.smartfishing.PlacePointDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import com.tobe.fishking.v2.repository.common.EventRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.ObserverCodeRepository;
import com.tobe.fishking.v2.repository.fishking.*;
import com.tobe.fishking.v2.service.HttpRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FishingShipService {

    private final ShipRepository shipRepository;
    private final CompanyRepository companyRepository;
    private final HttpRequestService httpRequestService;
    private final RealTimeVideoRepository realTimeVideoRepository;
    private final Environment env;
    private final EventRepository eventRepository;
    private final FileRepository fileRepo;
    private final CommonCodeRepository codeRepository;
    private final ShipSeaRocksRepository shipSeaRocksRepository;
    private final MemberRepository memberRepo;
    private final ObserverCodeRepository observerCodeRepository;
    private final PlacesRepository placesRepository;
    private final PlacePointRepository placePointRepository;

    @Transactional
    public Page<FishingShipResponse> getFishingShips(Long memberId, String keyword, String cameraActive, Integer page) throws EmptyListException {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("shipName").ascending());
        Page<FishingShipResponse> ships = shipRepository.getShipsByCompanyMember2(memberId, keyword, cameraActive, pageable);
        List<FishingShipResponse> contents = ships.getContent();
        if (contents.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        }
        return ships;
    }

    @Transactional
    public List<Map<String, Object>> getGoodsShips(Long memberId) throws EmptyListException {
        List<Map<String, Object>> response = new ArrayList<>();
        List<Tuple> tuples = shipRepository.getGoodsShips(memberId);
        for (Tuple t : tuples) {
            Map<String, Object> r = new HashMap<>();
            r.put("id", t.get(0, Long.class));
            r.put("shipName", t.get(1, String.class));
            response.add(r);
        }
        if (response.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        }
        return response;
    }

    @Transactional
    public List<Map<String, Object>> getNHNCameraList(Member member) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, EmptyListException {
        Company company = companyRepository.findByMember(member);
        List<Map<String, Object>> response =  new ArrayList<>();
        List<RealTimeVideo> videos = realTimeVideoRepository.getNHNByMemberId(member.getId());
        String token = "";
        if (videos.size() > 0) {
            RealTimeVideo video = videos.get(0);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(Long.parseLong(video.getExpireTime())),
                    TimeZone.getDefault().toZoneId()
            );
            String expireTime = "";
            if (now.isAfter(expTime)) {
                Map<String, String> tokenData = httpRequestService.refreshToken(video.getToken());
                token = tokenData.get("token");
                expireTime = tokenData.get("expireTime");
                realTimeVideoRepository.updateToken(token, expireTime, video.getToken());
            } else {
                token = video.getToken();
                expireTime = video.getExpireTime();
            }
        } else {
            token = ((String) httpRequestService.getToken(company.getNhnId()).get("token")).replaceAll("\"", "");
        }

        List<Map<String, Object>> cameras = httpRequestService.getCameraList(token);
        for (Map<String, Object> camera : cameras) {
            Map<String, Object> c = new HashMap<>();
            String serial = camera.get("serialNo").toString();
            String name = camera.get("labelName").toString();
            c.put("serial", serial);
            c.put("name", name);
            response.add(c);
        }
        if (response.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        }
        return response;
    }

    @Transactional
    public List<Map<String, Object>> getADTCameraList(Member member) throws UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, EmptyListException {
        Company company = companyRepository.findByMember(member);
        List<Map<String, Object>> response =  new ArrayList<>();
        List<RealTimeVideo> videos = realTimeVideoRepository.getADTByMemberId(member.getId());
        String token = httpRequestService.loginADT(company.getAdtId(), company.getAdtPw(), member.getId().toString());

        List<Map<String, Object>> cameras = httpRequestService.getADTList(token);
        for (Map<String, Object> camera : cameras) {
            Map<String, Object> c = new HashMap<>();
            c.put("serial", String.valueOf(((Double) camera.get("camId")).intValue()));
            c.put("name", (String) camera.get("camName"));
            response.add(c);
        }
        if (response.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        }
        return response;
    }

    @Transactional
    public Long addShip(UpdateShipDTO addShipDTO,
                        String token
    ) throws ResourceNotFoundException, UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Member member = memberRepo.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+token));
        Company company = companyRepository.findByMember(member);
        List<ObserverCode> codes = observerCodeRepository.findAll();
        Ship ship = addShipDTO.toEntity(member, company, codes, addShipDTO.getPositions());

        List<CommonCode> speciesList = new ArrayList<>();
        for (String species : addShipDTO.getFishSpecies()) {
            speciesList.add(codeRepository.getSpeciesByCode(species));
        }
        ship.setFishSpecies(speciesList);

        List<CommonCode> serviceList = new ArrayList<>();
        for (String service : addShipDTO.getServices()) {
            serviceList.add(codeRepository.getByCode(service));
        }
        ship.setServices(serviceList);

        List<CommonCode> facilityList = new ArrayList<>();
        for (String facility : addShipDTO.getFacilities()) {
            facilityList.add(codeRepository.getByCode(facility));
        }
        ship.setFacilities(facilityList);

        List<CommonCode> deviceList = new ArrayList<>();
        if (addShipDTO.getDevices() != null) {
            for (String device : addShipDTO.getDevices()) {
                deviceList.add(codeRepository.getByCode(device));
            }
        }
        ship.setDevices(deviceList);

        shipRepository.save(ship);

        int cameraNum = 0;
        List<RealTimeVideo> allVideos = realTimeVideoRepository.getNHNByMemberId(member.getId());
        String cameraToken;
        String expTime;
        if (allVideos.isEmpty()) {
            Map<String, Object> nhnCameraToken = httpRequestService.getToken(company.getNhnId());
            cameraToken = ((String) nhnCameraToken.get("token")).replaceAll("\"", "");
            expTime = (String) nhnCameraToken.get("expireTime");
        } else {
            cameraToken = allVideos.get(0).getToken();
            expTime = allVideos.get(0).getExpireTime();
        }

        for (AddShipCamera addShipCamera : addShipDTO.getNhnCameras()) {
            cameraNum += 1;
            RealTimeVideo video = RealTimeVideo.builder()
                    .rNo(cameraNum)
                    .member(member)
                    .ship(ship)
                    .name(addShipCamera.getName())
                    .serial(addShipCamera.getSerial())
                    .token(cameraToken)
                    .expireTime(expTime)
                    .type("toast")
                    .build();
            realTimeVideoRepository.save(video);
        }

        for (AddShipCamera addShipCamera : addShipDTO.getAdtCameras()) {
            cameraNum += 1;
            RealTimeVideo video = RealTimeVideo.builder()
                    .rNo(cameraNum)
                    .member(member)
                    .ship(ship)
                    .name(addShipCamera.getName())
                    .serial(addShipCamera.getSerial())
                    .token("")
                    .expireTime("")
                    .type("caps")
                    .build();
            realTimeVideoRepository.save(video);
        }

        for (AddEvent event : addShipDTO.getEvents()) {
//        for (AddEvent event : events) {
            ShareStatus status = ShareStatus.builder()
                    .viewCount(0)
                    .likeCount(0)
                    .commentCount(0)
                    .shareCount(0)
                    .build();
            Event e = Event.builder()
                    .addEvent(event)
                    .member(member)
                    .ship(ship)
                    .status(status)
                    .build();
            eventRepository.save(e);
            if (event.getImageId() != null) {
                FileEntity file = fileRepo.getOne(event.getImageId());
                file.saveTemporaryFile(e.getId());
                fileRepo.save(file);
            }
        }

        if (ship.getFishingType().equals(FishingType.seaRocks)) {
            for (String position : addShipDTO.getPositions()) {
//            for (String position : positions) {
                Places places = placesRepository.getOne(Long.parseLong(position));
                shipSeaRocksRepository.save(
                        ShipSeaRocks.builder()
                                .ship(ship)
                                .places(places)
                                .build()
                );
            }
        }

        return ship.getId();
    }

    @Transactional
    public void updateShip(Long shipId, UpdateShipDTO updateShipDTO, String token) throws ResourceNotFoundException, UnsupportedEncodingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Member member = memberRepo.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+token));
        Company company = companyRepository.findByMember(member);
        List<ObserverCode> codes = observerCodeRepository.findAll();
        Ship ship = shipRepository.getOne(shipId);

        ship.updateShip(updateShipDTO, codes, member);

        List<CommonCode> speciesList = new ArrayList<>();
        for (String species : updateShipDTO.getFishSpecies()) {
            speciesList.add(codeRepository.getSpeciesByCode(species));
        }
        ship.setFishSpecies(speciesList);

        List<CommonCode> serviceList = new ArrayList<>();
        for (String service : updateShipDTO.getServices()) {
            serviceList.add(codeRepository.getByCode(service));
        }
        ship.setServices(serviceList);

        List<CommonCode> facilityList = new ArrayList<>();
        for (String facility : updateShipDTO.getFacilities()) {
            facilityList.add(codeRepository.getByCode(facility));
        }
        ship.setFacilities(facilityList);

        List<CommonCode> deviceList = new ArrayList<>();
        if (updateShipDTO.getDevices() != null) {
            for (String device : updateShipDTO.getDevices()) {
                deviceList.add(codeRepository.getByCode(device));
            }
        }
        ship.setDevices(deviceList);

        shipRepository.save(ship);


        int cameraNum = 0;
        List<RealTimeVideo> videos = realTimeVideoRepository.getNHNByShipsId(shipId);
        List<RealTimeVideo> allVideos = realTimeVideoRepository.getNHNByMemberId(member.getId());
        String cameraToken;
        String expTime;
        if (allVideos.isEmpty()) {
            Map<String, Object> nhnCameraToken = httpRequestService.getToken(company.getNhnId());
            cameraToken = ((String) nhnCameraToken.get("token")).replaceAll("\"", "");
            expTime = (String) nhnCameraToken.get("expireTime");
        } else {
            cameraToken = allVideos.get(0).getToken();
            expTime = allVideos.get(0).getExpireTime();
        }

        if (videos.isEmpty()) {
            for (AddShipCamera addShipCamera : updateShipDTO.getNhnCameras()) {
                cameraNum += 1;
                RealTimeVideo video = RealTimeVideo.builder()
                        .rNo(cameraNum)
                        .member(member)
                        .ship(ship)
                        .name(addShipCamera.getName())
                        .serial(addShipCamera.getSerial())
                        .token(cameraToken)
                        .expireTime(expTime)
                        .type("toast")
                        .build();
                realTimeVideoRepository.save(video);
            }
        } else {
            String serial = allVideos.get(0).getSerial();
            List<AddShipCamera> newCameras = updateShipDTO.getNhnCameras();
            List<String> newSerials;
            if (newCameras == null) {
                newCameras = new ArrayList<>();
                newSerials = new ArrayList<>();
            } else {
                newSerials = newCameras.stream().map(AddShipCamera::getSerial).collect(Collectors.toList());
            }
            for (RealTimeVideo v : videos) {
                boolean use = newSerials.contains(v.getSerial());
                if (use) {
                    newSerials.remove(v.getSerial());
                } else {
                    v.setNotUse();
                    v.updateToken(cameraToken, expTime);
                    realTimeVideoRepository.save(v);
                }
            }
            for (AddShipCamera c : newCameras) {
                if (newSerials.contains(c.getSerial())) {
                    realTimeVideoRepository.save(
                            RealTimeVideo.builder()
                                    .rNo(cameraNum)
                                    .member(member)
                                    .ship(ship)
                                    .name(c.getName())
                                    .serial(c.getSerial())
                                    .token(cameraToken)
                                    .expireTime(expTime)
                                    .type("toast")
                                    .build()
                    );
                }
            }
        }

        List<RealTimeVideo> adtVideos = realTimeVideoRepository.getADTByShipsId(shipId);
        if (adtVideos.isEmpty()) {
            if (updateShipDTO.getAdtCameras() != null) {
                for (AddShipCamera addShipCamera : updateShipDTO.getAdtCameras()) {
                    cameraNum += 1;
                    RealTimeVideo video = RealTimeVideo.builder()
                            .rNo(cameraNum)
                            .member(member)
                            .ship(ship)
                            .name(addShipCamera.getName())
                            .serial(addShipCamera.getSerial())
                            .token("")
                            .expireTime("")
                            .type("caps")
                            .build();
                    realTimeVideoRepository.save(video);
                }
            }
        } else {
            List<AddShipCamera> newCameras = updateShipDTO.getAdtCameras();
            List<String> newSerials;
            if (newCameras == null) {
                newCameras = new ArrayList<>();
                newSerials = new ArrayList<>();
            } else {
                newSerials = newCameras.stream().map(AddShipCamera::getSerial).collect(Collectors.toList());
            }
            for (RealTimeVideo v : videos) {
                boolean use = newSerials.contains(v.getSerial());
                if (use) {
                    newSerials.remove(v.getSerial());
                } else {
                    v.setNotUse();
                    v.updateToken(cameraToken, expTime);
                    realTimeVideoRepository.save(v);
                }
            }
            for (AddShipCamera c : newCameras) {
                if (newSerials.contains(c.getSerial())) {
                    realTimeVideoRepository.save(
                            RealTimeVideo.builder()
                                    .rNo(cameraNum)
                                    .member(member)
                                    .ship(ship)
                                    .name(c.getName())
                                    .serial(c.getSerial())
                                    .token("")
                                    .expireTime("")
                                    .type("caps")
                                    .build()
                    );
                }
            }
        }

        List<Event> events = eventRepository.getEventByShipActive(shipId);
        if (events.isEmpty()) {
            for (AddEvent event : updateShipDTO.getEvents()) {
                ShareStatus status = ShareStatus.builder()
                        .viewCount(0)
                        .likeCount(0)
                        .commentCount(0)
                        .shareCount(0)
                        .build();
                Event e = Event.builder()
                        .addEvent(event)
                        .member(member)
                        .ship(ship)
                        .status(status)
                        .build();
                eventRepository.save(e);
                if (event.getImageId() != null) {
                    FileEntity file = fileRepo.getOne(event.getImageId());
                    file.saveTemporaryFile(e.getId());
                    fileRepo.save(file);
                }
            }
        } else {
            List<AddEvent> newEvents = updateShipDTO.getEvents().stream().filter(e -> e.getEventId() == null).collect(Collectors.toList());
            List<Long> newEventsId = updateShipDTO.getEvents().stream().map(AddEvent::getEventId).filter(Objects::nonNull).collect(Collectors.toList());
            for (Event e : events) {
                boolean use = newEventsId.contains(e.getId());
                if (!use) {
                    e.setDelete(member);
                    eventRepository.save(e);
                }
            }
            for (AddEvent event : newEvents) {
                ShareStatus status = ShareStatus.builder()
                        .viewCount(0)
                        .likeCount(0)
                        .commentCount(0)
                        .shareCount(0)
                        .build();
                Event e = Event.builder()
                        .addEvent(event)
                        .member(member)
                        .ship(ship)
                        .status(status)
                        .build();
                eventRepository.save(e);
                if (event.getImageId() != null) {
                    FileEntity file = fileRepo.getOne(event.getImageId());
                    file.saveTemporaryFile(e.getId());
                    fileRepo.save(file);
                }
            }
        }

        if (ship.getFishingType().equals(FishingType.seaRocks)) {
            List<Places> oldPlaces = placesRepository.getPlacesByShipId(shipId);
            if (oldPlaces.isEmpty()) {
                for (String position : updateShipDTO.getPositions()) {
                    Places places = placesRepository.getOne(Long.parseLong(position));
                    shipSeaRocksRepository.save(
                            ShipSeaRocks.builder()
                                    .ship(ship)
                                    .places(places)
                                    .build()
                    );
                }
            } else {
                List<String> newPlaces = updateShipDTO.getPositions();
                for (Places p : oldPlaces) {
                    if (newPlaces.contains(String.valueOf(p.getId()))) {
                        newPlaces.remove(String.valueOf(p.getId()));
                    } else {
                        placesRepository.delete(p);
                    }
                }
                for (String position : newPlaces) {
                    Places places = placesRepository.getOne(Long.parseLong(position));
                    shipSeaRocksRepository.save(
                            ShipSeaRocks.builder()
                                    .ship(ship)
                                    .places(places)
                                    .build()
                    );
                }
            }
        }
    }

    @Transactional
    public UpdateShipResponse getShipDetails(Long shipId) {
        Ship ship = shipRepository.getOne(shipId);
        UpdateShipResponse response = new UpdateShipResponse();
        response.setId(ship.getId());
        response.setName(ship.getShipName());
        response.setFishingType(ship.getFishingType().getKey());
        response.setAddress(ship.getAddress());
        response.setSido(ship.getSido());
        response.setSigungu(ship.getSigungu());
        response.setTel(ship.getTel());
        response.setWeight(ship.getWeight());
        response.setBoardingPerson(ship.getBoardingPerson());
        response.setLatitude(ship.getLocation().getLatitude());
        response.setLongitude(ship.getLocation().getLongitude());
        response.setProfileImage(ship.getProfileImage());
        response.setOwnerWordingTitle(ship.getOwnerWordingTitle());
        response.setOwnerWording(ship.getOwnerWording());
        response.setNoticeTitle(ship.getNoticeTitle());
        response.setNotice(ship.getNotice());
        response.setFishSpecies(ship.getFishSpecies().stream().map(CommonCode::getCode).collect(Collectors.toList()));
        response.setFacilities(ship.getFacilities().stream().map(CommonCode::getCode).collect(Collectors.toList()));
        response.setServices(ship.getServices().stream().map(CommonCode::getCode).collect(Collectors.toList()));
        response.setDevices(ship.getDevices().stream().map(CommonCode::getCode).collect(Collectors.toList()));
        response.setRouter(ship.getRouterIMEI() == null ? "" : ship.getRouterIMEI());
        response.setVideoId(ship.getVideoId());
        if (ship.getVideoId() != null) {
            String videoUrl = fileRepo.getOne(ship.getVideoId()).getDownloadUrl();
            response.setVideo(videoUrl);
        }
        if (ship.getFishingType() == FishingType.ship) {
            response.setPositions(Arrays.asList(ship.getPositions().split(",").clone()));
            response.setSeaRocks(null);
        } else {
            List<Map<String, Object>> rockData = new ArrayList<>();
            List<Places> places = placesRepository.getPlacesByShipId(shipId);
            for (Places place : places) {
                Map<String, Object> placeData = new HashMap<>();
                placeData.put("id", place.getId());
                placeData.put("name", place.getPlaceName());
                placeData.put("address", place.getAddress());
                placeData.put("latitude", place.getLocation().getLatitude());
                placeData.put("longitude", place.getLocation().getLatitude());
                List<PlacePoint> points = placePointRepository.getPlacePointByPlace(place);
                List<Map<String, Object>> pointList = new ArrayList<>();
                for (PlacePoint point : points) {
                    Map<String, Object> pointData = new HashMap<>();
                    pointData.put("latitude", point.getLocation().getLatitude());
                    pointData.put("longitude", point.getLocation().getLongitude());
                    pointData.put("id", point.getId());
                    pointList.add(pointData);
                }
                placeData.put("points", pointList);
                rockData.add(placeData);
            }
            response.setSeaRocks(rockData);
            response.setPositions(null);
        }
        List<AddEvent> events = new ArrayList<>();
        List<Event> eventList = eventRepository.getEventByShipActive(shipId);
        for (Event e : eventList) {
            AddEvent addEvent = new AddEvent();
            addEvent.setEventId(e.getId());
            addEvent.setTitle(e.getTitle());
            addEvent.setContents(e.getContents());
            addEvent.setStartDate(e.getStartDay());
            addEvent.setEndDate(e.getEndDay());
            List<FileEntity> files = fileRepo.findByPidAndFilePublishAndFileTypeAndIsDelete(e.getId(), FilePublish.event, FileType.image, false);
            if (files.isEmpty()) {
                addEvent.setImageId(null);
                addEvent.setImageUrl(null);
            } else {
                String path = env.getProperty("file.downloadUrl");
                addEvent.setImageId(files.get(0).getId());
                addEvent.setImageUrl(path + "/" + files.get(0).getFileUrl() + "/" + files.get(0).getStoredFile());
            }
            events.add(addEvent);
        }
        response.setEvents(events);
        List<RealTimeVideo> videos = realTimeVideoRepository.getNHNByShipsId(shipId);
        List<AddShipCamera> cameras = new ArrayList<>();
        for (RealTimeVideo v : videos) {
            AddShipCamera c = new AddShipCamera();
            c.setSerial(v.getSerial());
            c.setName(v.getName());
            cameras.add(c);
        }
        response.setNhnCameras(cameras);
        return response;
    }

    @Transactional
    public void changeCameraStatus(Long cameraId, Boolean isUse) {
        RealTimeVideo realTimeVideo = realTimeVideoRepository.getOne(cameraId);
        if (isUse) {
            realTimeVideo.setUse();
        } else {
            realTimeVideo.setNotUse();
        }
        realTimeVideoRepository.save(realTimeVideo);
    }

    @Transactional
    public Map<String, Object> getShipCamera(Long shipId) {
        Ship ship = shipRepository.getOne(shipId);
        List<RealTimeVideo> videos = realTimeVideoRepository.getRealTimeVideoByShipsId(shipId);
        List<Map<String, Object>> videoRes = new ArrayList<>();
        for (RealTimeVideo v : videos) {
            Map<String, Object> r = new HashMap<>();
            r.put("image", "/resource" + ship.getProfileImage());
            r.put("cameraId", v.getId());
            r.put("name", v.getName());
            r.put("isUse", v.getIsUse());
            videoRes.add(r);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("shipId", ship.getId());
        result.put("shipName", ship.getShipName());
        result.put("count", videoRes.size());
        result.put("content", videoRes);
        return result;
    }

    @Transactional
    public List<Map<String, Object>> getShipsCameraTab(Long memberId, String keyword, Boolean hasVideo) {
        List<Tuple> ships = shipRepository.getShipsCameraTab(memberId, keyword, hasVideo);

        List<Map<String, Object>> shipsRes = new ArrayList<>();
        for (Tuple s : ships) {
            Map<String, Object> r = new HashMap<>();
            String profile = s.get(3, String.class);
            if (profile == null) {
                profile = "/ship/defaultship.png";
            } else {
                profile = "/resource/" + profile.split("/")[1] + "/thumb_" + profile.split("/")[2];
            }

            r.put("shipId", s.get(0, Long.class));
            r.put("shipName", s.get(1, String.class));
            r.put("hasVideo", s.get(2, Boolean.class));
            r.put("profileImage", profile);
            r.put("takes", s.get(4, Long.class));
            shipsRes.add(r);
        }
        return shipsRes;
    }
}
