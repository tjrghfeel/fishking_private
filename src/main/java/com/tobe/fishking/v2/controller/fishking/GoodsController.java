package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.repository.fishing.GoodsRepository;
import com.tobe.fishking.v2.service.fishing.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(tags = {"상품"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/api")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;


    @ApiOperation(value = "상품 - 어종별 count 조회", notes = "상품 -어종별 Count 을 조회합니다.")
    @GetMapping("/goodsbyfishspecies")
    public List<Object[]> getCountTotalGoodsByFishSpecies() {
        return goodsService.getCountTotalGoodsByFishSpecies();
    }
    /*client
    List<Object[]> results = ......
    for (Object[] result : results) {
    String name = (String) result[0];
    int count = ((Number) result[1]).intValue();
    }

     */

    @ApiOperation(value = "상품- 지역별  count ", notes = "상품 목록을 조회합니다.")
    @GetMapping("/goodsbyregion")
    public List<Object[]> getCountTotalGoodsByRegion() {
        return goodsService.getCountTotalGoodsByRegion();
    }





}