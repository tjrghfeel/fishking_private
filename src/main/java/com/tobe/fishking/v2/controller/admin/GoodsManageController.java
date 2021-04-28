package com.tobe.fishking.v2.controller.admin;

import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.GoodsManageDtoForPage;
import com.tobe.fishking.v2.model.admin.GoodsSearchConditionDto;
import com.tobe.fishking.v2.service.admin.GoodsManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags={"상품 관리"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class GoodsManageController {
    private final GoodsManageService goodsManageService;

    //상품 리스트 검색
    @ApiOperation(value = "상품 리스트 검색",notes = "")
    @GetMapping("/manage/goods/list/{page}")
    public Page<GoodsManageDtoForPage> getGoodsList(
        @Valid GoodsSearchConditionDto dto,
        @RequestHeader("Authorization") String token,
        @PathVariable("page") int page
    ){
        return goodsManageService.getGoodsList(dto,token,page);
    }

    //상품 활성화 수정
    @PutMapping("/manage/goods/isActive/{goodsId}/{isActive}")
    public Boolean setIsActive(
            @PathVariable("goodsId") Long goodsId,
            @PathVariable("isActive") String isActive,
            @RequestHeader("Authorization") String token
    ) throws ServiceLogicException {
        if(!isActive.equals("true") && !isActive.equals("false")){
            throw new ServiceLogicException("isActive의 값으로는 'true' 또는 'false'만 가능합니다.");
        }
        return goodsManageService.setIsActive(goodsId, isActive, token);
    }
}
