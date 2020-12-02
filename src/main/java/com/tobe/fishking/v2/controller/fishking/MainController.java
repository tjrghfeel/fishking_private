package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.model.common.FilesDTO;
import com.tobe.fishking.v2.model.common.MapInfoDTO;
import com.tobe.fishking.v2.model.fishing.GoodsDTO;
import com.tobe.fishking.v2.model.response.ListResult;
import com.tobe.fishking.v2.service.ResponseService;
import com.tobe.fishking.v2.service.common.CommonService;
import com.tobe.fishking.v2.service.common.PopularService;
import com.tobe.fishking.v2.service.fishking.FishingDiaryService;
import com.tobe.fishking.v2.service.fishking.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@Api(tags = {"메인"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/api")
public class MainController {

    private final GoodsService goodsService;
    private final CommonService commonService;
    private final PopularService popularService;
    private final ResponseService responseService;
    private final FishingDiaryService fishingDiaryService;


    @ApiOperation(value = "통합 검색 Main Load", notes = "통합 검색 Main Load")
    @GetMapping("/getMain")
    public Map<String, Object> getMain(Pageable pageable,
                                       @RequestParam(required = false, name = "total_elements") Integer totalElements) {

        Map<String, Object> map = null;

        List<Object> popularList = popularService.getPopularList(SearchPublish.TOTAL);

        map.put("popularList", popularList);

        Page<GoodsDTO> recommendGoods = goodsService.getGoodsByRecommend(pageable, totalElements);

        map.put("recommendGoods", recommendGoods);

        return map;
    }


    @ApiOperation(value = "통합 검색 ", notes = "통합 검색")
    @GetMapping("/getGoodsListByAll")
    public Map<String, Object> getGoodsListByAll(Pageable pageable,
                                                 @RequestParam(required = false) Map<String, Object> searchRequest,
                                                 @RequestParam(required = false, name = "total_elements") Integer totalElements) {

        Map<String, Object> map = null;

        Page<GoodsDTO> goods = goodsService.getGoodsList(pageable, searchRequest, totalElements);
        map.put("goods", goods);


        Page<FilesDTO> files = commonService.getFilesList(pageable, searchRequest, totalElements);
        map.put("files", files);


        List<Object> goosByFishSpeciesCnt = goodsService.getCountTotalGoodsByFishSpecies();

        map.put("fishspeciescnt", goosByFishSpeciesCnt);

        List<Object> goosByRegionCnt = goodsService.getCountTotalGoodsByRegion();
        map.put("regioncnt", goosByRegionCnt);

        return map;
    }

    /*  jkkim 수정 지우질 말것 */
    @ApiOperation(value = "위경도 리스트", notes = "조항일지/포인트 조행기 위경도  리스트를 조회한다.")
    @GetMapping(value = "/getLatitudeAndLongitudeList")
    public ListResult<MapInfoDTO> getLatitudeAndLongitudeList() {
        return responseService.getListResult(fishingDiaryService.getLatitudeAndLongitudeList());
    }


}