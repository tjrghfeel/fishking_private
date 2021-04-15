package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.SeaDirection;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.ShipManageDtoForPage;
import com.tobe.fishking.v2.model.admin.ShipSearchConditionDto;
import com.tobe.fishking.v2.repository.common.CodeGroupRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipManageService {
    private final ShipRepository shipRepository;
    private final CodeGroupRepository codeGroupRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final MemberService memberService;

    //선박 검색
    @Transactional
    public Page<ShipManageDtoForPage> getShipList(ShipSearchConditionDto dto, String token, int page){

        //fishingType설정
        Integer fishingType = null;
        if(dto.getFishingType() != null){
            fishingType = FishingType.valueOf(dto.getFishingType()).ordinal();
        }
        //seaDirection 설정
        Integer seaDirection = null;
        if(dto.getSeaDirection() != null){
            seaDirection = SeaDirection.valueOf(dto.getSeaDirection()).ordinal();
        }

        //어종 common code id설정
        Boolean searchFishSpecies = false;
        Long[] fishSpeciesIdList = new Long[0];
        if(dto.getFishSpecies().length > 0) {
            List<CommonCode> speciesList = commonCodeRepository.findFishSpeciesCodeByCodeList(dto.getFishSpecies());
             fishSpeciesIdList = new Long[speciesList.size()];
            for (int i = 0; i < speciesList.size(); i++) {
                fishSpeciesIdList[i] = speciesList.get(i).getId();
            }
            searchFishSpecies = true;
        }

        //service
        Boolean searchServices = false;
        Long[] services = new Long[0];
        CodeGroup servicesCodeGroup = codeGroupRepository.findByCode("service");
        if(dto.getServices().length > 0) {
            List<CommonCode> servicesList = commonCodeRepository.findCommonCodesByCodeGroupAndCodes(servicesCodeGroup, Arrays.asList(dto.getServices()));
            services = new Long[servicesList.size()];
            for (int i = 0; i < servicesList.size(); i++) {
                services[i] = servicesList.get(i).getId();
            }
            searchServices = true;
        }

        //facilities
        Boolean searchFacilities = false;
        Long[] facilities = new Long[0];
        CodeGroup facilitiesCodeGroup = codeGroupRepository.findByCode("convenient");
        if(dto.getFacilities().length > 0) {
            List<CommonCode> facilitiesList = commonCodeRepository.findCommonCodesByCodeGroupAndCodes(facilitiesCodeGroup, Arrays.asList(dto.getFacilities()));
            facilities = new Long[facilitiesList.size()];
            for (int i = 0; i < facilitiesList.size(); i++) {
                facilities[i] = facilitiesList.get(i).getId();
            }
            searchFacilities = true;
        }

        //devices
        Boolean searchDevices = false;
        Long[] devices = new Long[0];
        CodeGroup devicesCodeGroup = codeGroupRepository.findByCode("device");
        if(dto.getDevices().length > 0) {
            List<CommonCode> devicesList = commonCodeRepository.findCommonCodesByCodeGroupAndCodes(devicesCodeGroup, Arrays.asList(dto.getDevices()));
            devices = new Long[devicesList.size()];
            for (int i = 0; i < devicesList.size(); i++) {
                devices[i] = devicesList.get(i).getId();
            }
            searchDevices = true;
        }

        Pageable pageable = PageRequest.of(page, dto.getPageCount(), JpaSort.unsafe(Sort.Direction.DESC,"("+dto.getSort()+")"));

        return shipRepository.getShipList(
                dto.getShipName(),
                fishingType, dto.getAddress(), dto.getCompanyPhoneNumber(), seaDirection, searchFishSpecies, fishSpeciesIdList,
                searchServices, services, searchFacilities, facilities, searchDevices, devices,
                dto.getIsLive(), dto.getCompanyName(), dto.getTotalAvgByReview(), dto.getIsActive(), dto.getDepartStatus(),
                pageable
        );
    }

    //선박 활성화/비활성화
    @Transactional
    public Boolean setIsActive(Long shipId, String inputIsActive, String token) throws ServiceLogicException {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){throw new ServiceLogicException("관리자 권한이 없습니다.");}

        Ship ship = shipRepository.findById(shipId)
                .orElseThrow(()->new ServiceLogicException("해당 선박이 존재하지 않습니다."));

        Boolean isActive = (inputIsActive.equals("true"))? true:false;
        ship.setIsActive(isActive);
        return true;
    }
}
