package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.GoodsDTO;
import com.tobe.fishking.v2.repository.fishing.GoodsRepository;
import com.tobe.fishking.v2.service.fishing.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(tags = {"상품"})
@Slf4j
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



    @ApiOperation(value = "상품 목록 조회 ", notes = "상품 목록을 조회합니다. ex) /goods?page=0&siez=5&total_elements=100")
    @GetMapping("/goods")
    public ResponseEntity<Page<GoodsDTO>> getGoods(Pageable pageable,
                                                   @RequestParam(required = false, name = "total_elements")Integer totalElements){
        Page<GoodsDTO> goodss = goodsService.getGoods(pageable, totalElements);
        return ResponseEntity.ok(goodss);
    }

/*    @GetMapping("/goods/search")
    public ResponseEntity<?> searchAll(GoodsDTO search, PageRequest pageRequest) throws Exception {
        ResponseEntity<?> result;
        try {
            result = new ResponseEntity<>(goodsService.searchAll(search,pageRequest.of()),HttpStatus.OK) ;
        }
        catch(Exception e) {
            log.debug(e.getMessage());
            result = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST) ;
        }
        return result;
    }*/


}