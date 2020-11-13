/*
package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.model.fishing.GoodsDTO;
import com.tobe.fishking.v2.service.fishking.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Api(tags = {"상품"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/api")
public class GoodsController {
    private final GoodsService service;

    */
/*@ApiOperation(value = "상품리스트 조회", notes = "상품리스트를  조회합니다. ")
    @GetMapping("/goodses")
    public ResponseEntity<Page<GoodsDTO>> getGoods(Pageable pageable,
                                                   @RequestParam(required = false, name = "total_elements")Integer totalElements){
        Page<GoodsDTO> goods = service.getGoodsList(pageable, totalElements);
        return ResponseEntity.ok(goods);
    }*//*


*/
/*
    @ApiOperation(value = "상품상세 조회", notes = "상품상세를  조회합니다. ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "상품 고유키", required = true, dataType = "string", paramType = "path", defaultValue = "")
    })

    @RequestMapping(value = "/view/detail")
    public String viewGooldsDetail(Model model) {
        model.addAttribute("template", "fragments/content/product/detail");
        return "index";
    }
*//*



    
}*/
