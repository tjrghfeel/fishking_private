package com.tobe.fishking.v2.controller.admin;

import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.SeaDirection;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.ShipManageDtoForPage;
import com.tobe.fishking.v2.model.admin.ShipSearchConditionDto;
import com.tobe.fishking.v2.service.admin.ShipManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags={"선박 관리"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class ShipManageController {
    private final ShipManageService shipManageService;

    //조건 검색
    @ApiOperation(value = "선박 검색")
    @GetMapping("/manage/ship/list/{page}")
    public Page<ShipManageDtoForPage> getShipList(
            @RequestHeader("Authorization") String token,
            @PathVariable("page") int page,
            @Valid ShipSearchConditionDto dto
    ) throws ServiceLogicException {
        try{
            if(dto.getFishingType() != null){
                FishingType.valueOf(dto.getFishingType());}
            if(dto.getSeaDirection() != null){
                SeaDirection.valueOf(dto.getSeaDirection());}
        }catch (Exception e){throw new ServiceLogicException("fishingType, seaDirection의 값이 잘못되었습니다.");}

        return shipManageService.getShipList(dto, token,page);
    }

    //선박 활성화/비활성화
    @PutMapping("/manage/ship/isActive/{shipId}/{isActive}")
    public Boolean setIsActive(
            @PathVariable("shipId") Long shipId,
            @PathVariable("isActive") String isActive,
            @RequestHeader("Authorization") String token
    ) throws ServiceLogicException {
        if(!isActive.equals("true") && !isActive.equals("false")){
            throw new ServiceLogicException("isActive의 값으로는 'true' 또는 'false'만 가능합니다.");
        }
        return shipManageService.setIsActive(shipId, isActive, token);
    }
}
