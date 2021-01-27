package com.tobe.fishking.v2.service.fishking;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.fishing.ShipDTO;
import com.tobe.fishking.v2.model.fishing.ShipListForWriteFishingDiary;
import com.tobe.fishking.v2.model.fishing.ShipListResponse;
import com.tobe.fishking.v2.model.fishing.ShipSearchDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.PopularRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.repository.fishking.specs.ShipSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipService {

    private final UploadService uploadService;
    private final MemberRepository memberRepo;
    private final FileRepository fileRepo;
    private final ShipRepository shipRepo;
    private final PopularRepository popularRepo;


    /*
     *//*업체요청시 등록한 증빙서류File에 대한 downloadUrl을 받아오기위해 FileEntity들을 가져와줌. *//*
    FileEntity ship = fileRepository.findById(ship.getId())
            .orElseThrow(()->new ResourceNotFoundException("files not found for this id ::"+company.getBizNoFileId()));

    */

    //검색 --  name으로 검색
    public Page<ShipDTO.ShipDTOResp> getShipList(Pageable pageable,
                                                 @RequestParam(required = false) Map<String, Object> searchRequest,   ///total를 제외한 모든 것 조회
                                                 Integer totalElement) {

        Map<ShipSpecs.SearchKey, Object> searchKeys = new HashMap<>();

        for (String key : searchRequest.keySet()) {
            searchKeys.put(ShipSpecs.SearchKey.valueOf(key.toUpperCase()), searchRequest.get(key));
        }

        Page<Ship> ships = searchKeys.isEmpty()
                ? shipRepo.findAll(pageable, totalElement)
                : shipRepo.findAll(ShipSpecs.searchWith(searchKeys), pageable, totalElement);

        //member 가져오기 jkkim
        for (String key : searchRequest.keySet()) {
            popularRepo.save(new Popular(SearchPublish.TOTAL, (String) searchRequest.get(key), memberRepo.getOne((long) 5)));
        }

        // UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        //file 처리
        return ships.map(ShipDTO.ShipDTOResp::of);
    }

    /*글쓰기시, ship선택버튼에 있는 ship검색 메소드 */
    @Transactional
    public Page<ShipListForWriteFishingDiary> searchShipForWriteFishingDiary(String keyword,/* String sortBy,*/ int page) {

//        if(sortBy.equals("name")){
        Pageable pageable = PageRequest.of(page, 10/*, JpaSort.unsafe(Sort.Direction.DESC,"("+sortBy+")")*/);
        return shipRepo.findBySearchKeyword(keyword, pageable);
        /*}
        //!!!!!정렬기준이 거리순일 경우, gps api를 이용해 처리해야할듯?
        else{
            Pageable pageable = PageRequest.of(page, 10, JpaSort.unsafe(Sort.Direction.DESC,"("+sortBy+")"));
            return shipRepo.findBySearchKeyword(keyword,pageable);
        }*/
    }


    /*지도에 선상 위치 및 정보를 위한 Method*/
    public List<ShipDTO.ShipDTOResp> getShipListsForMap(FilePublish filePublish) {

        if (FilePublish.ship != filePublish) return null;

        FishingType fishingType = filePublish.name().equals("ship") ? FishingType.ship : FishingType.seaRocks;

        List<Ship> shipEntityList = shipRepo.findAllShipAndLocation();

        List<ShipDTO.ShipDTOResp> shipDTORespList = shipEntityList.stream().map(ShipDTO.ShipDTOResp::of).collect(Collectors.toList());  //O

        //대표이미지
        for (int i = 0; i < shipDTORespList.size(); i++) {

            ShipDTO.ShipDTOResp entity = (ShipDTO.ShipDTOResp) shipDTORespList.get(i);

            FileEntity shipFile = fileRepo.findTop1ByPidAndFilePublishAndIsRepresent(entity.getId(), FilePublish.ship, true);


            if (shipFile != null) entity.setShipImageFileUrl(shipFile.getDownloadThumbnailUrl());
            else entity.setShipImageFileUrl("https://");

            // entity.setFishSpeciesCount(entity.getFishSpecies() == null? 0:entity.getFishSpecies().size());
            shipDTORespList.set(i, entity);

        }

        return shipDTORespList;


    }

    /* 선상, 갯바위 리스트 */
    public Page<ShipListResponse> getShips(ShipSearchDTO shipSearchDTO) {
        Pageable pageable = PageRequest.of(shipSearchDTO.getPageNumber()-1, shipSearchDTO.getSize(), Sort.by(shipSearchDTO.getOrderBy()));
        return shipRepo.searchAll(shipSearchDTO, pageable);
    }
}
