package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.OrderDetails;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.AdType;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.exception.CNotOwnerException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.FilesDTO;
import com.tobe.fishking.v2.model.common.MapInfoDTO;
import com.tobe.fishking.v2.model.fishing.GoodsDTO;
import com.tobe.fishking.v2.model.fishing.OrdersInfoDTO;
import com.tobe.fishking.v2.model.fishing.RiderShipDTO;
import com.tobe.fishking.v2.model.response.ListResult;
import com.tobe.fishking.v2.model.response.SingleResult;
import com.tobe.fishking.v2.service.ResponseService;
import com.tobe.fishking.v2.service.common.CommonService;
import com.tobe.fishking.v2.service.common.PopularService;
import com.tobe.fishking.v2.service.fishking.*;
import com.tobe.fishking.v2.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Api(tags = {"메인"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class MainController {

    private final GoodsService goodsService;
    private final CompanyService companyService;
    private final ShipService shipService;
    private final OrdersService ordersService;
    //private final OrderDetailsService ordersDetailService;



    private final CommonService commonService;
    private final PopularService popularService;
    private final ResponseService responseService;
    private final FishingDiaryService fishingDiaryService;

    private final PlacesService placesService;


    @ApiOperation(value = "통합 검색 Main Load", notes = "통합 검색 Main Load")
    @GetMapping("/getMain")
    public Map<String, Object> getMain(Pageable pageable,
                                       @RequestParam(required = false, name = "total_elements") Integer totalElements) {

        Map<String, Object> map = null;

        List<Object> popularList = popularService.getPopularList(SearchPublish.TOTAL);

        map.put("popularList", popularList);

        Page<GoodsDTO.GoodsDTOResp> recommendGoods = goodsService.getGoodsByRecommend(pageable, totalElements);

        map.put("recommendGoods", recommendGoods);

        return map;
    }


//    @ApiOperation(value = "통합 검색 ", notes = "통합 검색")
//    @GetMapping("/getGoodsListByAll")
//    public Map<String, Object> getGoodsListByAll(Pageable pageable,
//                                                 @RequestParam(required = false) Map<String, Object> searchRequest,
//                                                 @RequestParam(required = false, name = "total_elements") Integer totalElements) {
//
//        Map<String, Object> map = null;
//
//        Page<GoodsDTO.GoodsDTOResp> goods = goodsService.getGoodsList(pageable, searchRequest, totalElements);
//        map.put("goods", goods);
//
//
//        Page<FilesDTO> files = commonService.getFilesList(pageable, searchRequest, totalElements);
//        map.put("files", files);
//
//
//        List<Object> goosByFishSpeciesCnt = goodsService.getCountTotalGoodsByFishSpecies();
//
//        map.put("fishspeciescnt", goosByFishSpeciesCnt);
//
//        List<Object> goosByRegionCnt = goodsService.getCountTotalGoodsByRegion();
//        map.put("regioncnt", goosByRegionCnt);
//
//        return map;
//    }


    @ApiOperation(value = "통합 검색 인기검색어, 어복황제 추천 AD", notes = "인기검색어, 추천AD" +
            "1\n { " +
            "\n popularKeyword: [ keyword, keyword, .... , keyword ]  // 인덱스 순서로 1 2 3 .. 순입니다. String 리스트입니다." +
            "\n ad: [{ " +
            "\n id: 상품 id" +
            "\n shipImageFileUrl: 선박 이미지 주소 " +
            "\n shipName: 선박명" +
            "\n sido: 시도" +
            "\n sigungu: 시군구" +
            "\n distance: 거리" +
            "\n location: {" +
            "\n     latitude: 위도" +
            "\n     longitude: 경도" +
            "\n } " +
            "\n address: 주소" +
            "\n fishSpecies: [{" +
            "\n     id: id" +
            "\n     codeGroup: " +
            "\n     codeGroupName: 코드 그룹 명" +
            "\n     code: 코드" +
            "\n     codeName: 코드명" +
            "\n     extraValue1: 대체값" +
            "\n     remark: 주석" +
            "\n }]" +
            "\n fishSpeciesCount: 대상 어종 수" +
            "\n lowPrice: 상품 중 가장 낮은 가격" +
            "\n }, ... ] 상품 광고의 하트는 빼주세요" +
            "")
    @GetMapping("/search/keywords")
    public Map<String, Object> getSearchPageData(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
        result.put("popularKeyword", popularService.getPopularKeyword());
        result.put("ad", commonService.getAdList(AdType.SEARCH_AD));
        return result;
    }

    /*  jkkim 수정 지우질 말것 */
/*    @ApiOperation(value = "위경도 리스트", notes = "조항일지/포인트 조행기 위경도  리스트를 조회한다.")
    @GetMapping(value = "/getLatitudeAndLongitudeList")
    public ListResult<MapInfoDTO> getLatitudeAndLongitudeList() {
        return responseService.getListResult(fishingDiaryService.getLatitudeAndLongitudeList());
    }*/

    /*  jkkim 수정 지우질 말것 */

    @ApiOperation(value = "지도리스트", notes = "선상, 포인트, 조행기 조회한다." +
            "                   FishingType : ship, fishingBlog,  seaRocks만 조회한다.   ")
    @GetMapping(value = "/getListForMap")
    public SingleResult<Map<String, Object>> getListForMap(FilePublish filePublish)
    {

        Map<String, Object> map = new HashMap<>();

        if (!filePublish.name().equals("ship") && (!filePublish.name().equals("seaRocks")) && (!filePublish.name().equals("fishingBlog")))
            throw new CNotOwnerException();

        if (!filePublish.name().equals("fishingBlog") ) // 조행기가 아니면
        {
        /*    //갯바위
            if (filePublish.name().equals("seaRocks")) {
                map.put(filePublish.name(), responseService.getListResult(placesService.getPlacesListsForMap(filePublish)));
            } else{*/
            //선상
                //map.put(filePublish.name(),  responseService.getListResult(goodsService.getGoodListsForMap(filePublish)));
                map.put(filePublish.name(), responseService.getListResult(shipService.getShipListsForMap(filePublish)));
          //  }
        }
        else
            map.put(filePublish.name(), responseService.getListResult(fishingDiaryService.getFishingDiaryListsForMap(filePublish)));

        return responseService.getSingleResult(map);


    }


    @ApiOperation(value = "선박현황 -대시보드 ", notes = "선박수, 업체수 조회한다.   ")
    @GetMapping(value = "/getMaininfoByMarine")
    public HashMap<String, Long> getMaininfoByMarine(){

        var marineCount  = new HashMap<String, Long>();

        marineCount.put("company", companyService.findAllByIsRegistered());
        marineCount.put("ship", shipService.findAllShipCount());

        RiderShipDTO.RiderShipDTOResp _riderShipDTOResp  =  ordersService.findAllByDateAndRider(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), OrderStatus.bookConfirm);


        if (_riderShipDTOResp != null) {
            marineCount.put("riderPersonalByOrder", _riderShipDTOResp.getPersonnelCountByOrder().longValue());
            marineCount.put("realRider", _riderShipDTOResp.getRiderCountByOrder());
        }
        else {
            marineCount.put("riderPersonalByOrder", 0L);
            marineCount.put("realRider", 0L);
        }

        return marineCount;

    }

    @ApiOperation(value = " 출항현황 ", notes = "출항현황 Map.   ")
    @GetMapping(value = "/getShipDepartInfoByMarine")
    public ListResult<OrdersInfoDTO.ShipByOrdersDTOResp> getShipDepartInfoByMarine(){
        return responseService.getListResult(ordersService.findAllByFishingDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
    }


    @ApiOperation(value = " 승선확인 -명단리스트 ", notes = "승선확인 -명단리스트.   ")
    @GetMapping(value = "/getBoardingListForGoodsByMarine")
    public ListResult<RiderShipDTO.BoardingListByOrdersDTOResp> getBoardingListForGoodsByMarine(Long goodsId){
        if (goodsId == null ) return null;
        return responseService.getListResult(ordersService.findAllByBoardingList(goodsId));
    }

    @ApiOperation(value = "메인화면 데이터 ", notes = "메인화면 데이터 " +
            "\n live: [{ " +
            "\n id: 상품 id" +
            "\n shipImageFileUrl: 선박 이미지 주소 " +
            "\n shipName: 선박명" +
            "\n sido: 시도" +
            "\n sigungu: 시군구" +
            "\n distance: 거리" +
            "\n location: {" +
            "\n     latitude: 위도" +
            "\n     longitude: 경도" +
            "\n } " +
            "\n address: 주소" +
            "\n fishSpecies: [{" +
            "\n     id: id" +
            "\n     codeGroup: " +
            "\n     codeGroupName: 코드 그룹 명" +
            "\n     code: 코드" +
            "\n     codeName: 코드명" +
            "\n     extraValue1: 대체값" +
            "\n     remark: 주석" +
            "\n }]" +
            "\n fishSpeciesCount: 대상 어종 수" +
            "\n lowPrice: 상품 중 가장 낮은 가격" +
            "\n }, ... ] " +
            "\n ship: live 와 같음" +
            "\n ad: live 와 같음" +
            "\n direction: [{ " +
            "\n     code: 코드 " +
            "\n     codeName: 이름 " +
            "\n     count: 포함 수" +
            "\n }, ... ] " +
            "\n species: direction 과 같음" +
            "")
    @GetMapping(value = "/main")
    public Map<String, Object> getMainScreenData(@RequestHeader(name = "Authorization") String token){
        Map<String, Object> result;
        result = commonService.getMainScreenData();
        return result;
    }
}