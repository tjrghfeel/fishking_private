package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.PlacePoint;
import com.tobe.fishking.v2.entity.fishing.Places;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.Location;
import com.tobe.fishking.v2.model.fishing.PlacesDTO;
import com.tobe.fishking.v2.model.smartfishing.PlaceDTO;
import com.tobe.fishking.v2.model.smartfishing.PlacePointDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.PopularRepository;
import com.tobe.fishking.v2.repository.fishking.PlacePointRepository;
import com.tobe.fishking.v2.repository.fishking.PlacesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlacesService {

    private final UploadService uploadService;
    private final MemberRepository memberRepo;
    private final FileRepository fileRepo;
    private final PlacesRepository placesRepo;
    private final PopularRepository popularRepo;
    private final PlacePointRepository placePointRepository;


    private static ModelMapper modelMapper = new ModelMapper();


    public List<PlacesDTO.PlacesDTOResp> getPlacesListsForMap(FilePublish filePublish) {

        if (filePublish.ordinal() != FilePublish.seaRocks.ordinal()) return null;

        List<Places> placesEntityList = placesRepo.findAllPlacesAndLocation();

        List<PlacesDTO.PlacesDTOResp> placesDTORespList = placesEntityList.stream().map(PlacesDTO.PlacesDTOResp::of).collect(Collectors.toList());  //O

        //대표이미지
        for (int i = 0; i < placesDTORespList.size(); i++) {

            PlacesDTO.PlacesDTOResp entity = (PlacesDTO.PlacesDTOResp) placesDTORespList.get(i);

            FileEntity placesFile = fileRepo.findTop1ByPidAndFilePublishAndIsRepresent(entity.getId(), FilePublish.seaRocks, true);

            //Image경로가 다운로드 Url, display하는 url인지
            if (placesFile != null) entity.setPlacesImageFileUrl(placesFile.getDownloadThumbnailUrl());
            else entity.setPlacesImageFileUrl("https://");

            // entity.setFishSpeciesCount(entity.getFishSpecies() == null? 0:entity.getFishSpecies().size());
            placesDTORespList.set(i, entity);

        }

        return placesDTORespList;


    }

    @Transactional
    public List<Map<String, Object>> searchSeaRock(String sido, String sigungu, String dong, String token) {
        Member member = memberRepo.findBySessionToken(token).orElseThrow(
                EntityNotFoundException::new
        );
        List<Map<String, Object>> rockData = new ArrayList<>();
        List<Places> places = placesRepo.getPlacesByAddress(sido, sigungu, dong, member);
        for (Places place : places) {
            Map<String, Object> placeData = new HashMap<>();
            placeData.put("id", place.getId());
            placeData.put("name", place.getPlaceName());
            rockData.add(placeData);
        }
        return rockData;
    }

    @Transactional
    public List<Map<String, Object>> getSeaRocks(Long[] seaRockId) {
        List<Map<String, Object>> rockData = new ArrayList<>();
        List<Places> places = placesRepo.getPlacesInId(seaRockId);
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
        return rockData;
    }

    @Transactional
    public Long addSeaRock(PlaceDTO dto, String token) throws ResourceNotFoundException {
        Member member = memberRepo.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+token));
        Places places = Places.builder()
                .placeName(dto.getName())
                .sido(dto.getSido())
                .sigungu(dto.getSigungu())
                .dong(dto.getDong())
                .address(dto.getAddress())
                .location(new Location(dto.getLatitude(), dto.getLongitude()))
                .averageDepth(dto.getAverageDepth())
                .floorMaterial(dto.getFloorMaterial())
                .tideTime(dto.getTideTime())
                .introduce(dto.getIntroduce())
                .open(dto.getIsOpen())
                .createdBy(member)
                .modifiedBy(member)
                .build();
        placesRepo.save(places);

        int number = 0;
        for (PlacePointDTO pointDTO : dto.getPoints()) {
            number += 1;
            placePointRepository.save(
                    PlacePoint.builder()
                            .place(places)
                            .location(new Location(pointDTO.getLatitude(), pointDTO.getLongitude()))
                            .number(number)
                            .member(member)
                            .build()
            );
        }
        return places.getId();
    }

}
