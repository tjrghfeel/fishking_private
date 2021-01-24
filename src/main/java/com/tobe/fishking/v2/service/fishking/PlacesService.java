package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.fishing.Places;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.fishing.PlacesDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.PopularRepository;
import com.tobe.fishking.v2.repository.fishking.PlacesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlacesService {

    private final UploadService uploadService;
    private final MemberRepository memberRepo;
    private final FileRepository fileRepo;
    private final PlacesRepository placesRepo;
    private final PopularRepository popularRepo;


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


}
