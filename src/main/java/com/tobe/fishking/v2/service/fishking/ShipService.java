package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.model.fishing.ShipDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.PopularRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.repository.fishking.specs.ShipSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

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
    public Page<ShipDTO> getShipList(Pageable pageable,
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
            popularRepo.save(new Popular(SearchPublish.TOTAL, (String)searchRequest.get(key), memberRepo.getOne((long)5)));
        }

       // UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        //file 처리
        return ships.map(ShipDTO::of);
    }






}
