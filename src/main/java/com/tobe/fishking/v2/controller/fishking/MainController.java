package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.enums.common.OperatorType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.FilesDTO;
import com.tobe.fishking.v2.model.fishing.GoodsDTO;
import com.tobe.fishking.v2.service.common.CommonService;
import com.tobe.fishking.v2.service.fishking.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Api(tags = {"메인"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/api")
public class MainController {
    private final GoodsService service;
    private final CommonService commonService;



    @ApiOperation(value = "상품검색  조회", notes = "상품검색  조회합니다. --검색조건  or  ")
    @GetMapping("/goodesList")
    public ResponseEntity<Page<GoodsDTO>> getGoodsListByAll2(Pageable pageable,
                                                            @RequestParam(required = false) Map<String, Object> searchRequest,
                                                            @RequestParam(required = false, name = "total_elements") Integer totalElements){

        Page<GoodsDTO> goods = service.getGoodsList(pageable, OperatorType.or, searchRequest, totalElements);


        return ResponseEntity.ok(goods);
    }


    @ApiOperation(value = "통합 검색 ", notes = "통합 검색")
    @GetMapping("/saarchAll")
    public Map<String,Object> getGoodsListByAll(Pageable pageable,
                                                 @RequestParam(required = false) Map<String, Object> searchRequest,
                                                 @RequestParam(required = false, name = "total_elements") Integer totalElements ){

        Map<String,Object> map = null;

        Page<GoodsDTO> goods = service.getGoodsList(pageable, OperatorType.or, searchRequest, totalElements);
        map.put("goods", goods);


        Page<FilesDTO> files = commonService.getFilesList(pageable,  searchRequest, totalElements);
        map.put("files", files);

        // goosByRegionCnt = service.getCountTotalGoodsByRegion();



        return map;
    }



}