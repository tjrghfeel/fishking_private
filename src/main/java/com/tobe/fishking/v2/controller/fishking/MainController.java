package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.AdType;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.exception.CNotOwnerException;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.fishing.GoodsDTO;
import com.tobe.fishking.v2.model.fishing.OrdersInfoDTO;
import com.tobe.fishking.v2.model.fishing.RiderShipDTO;
import com.tobe.fishking.v2.model.fishing.ShipListResponse;
import com.tobe.fishking.v2.model.response.ListResult;
import com.tobe.fishking.v2.model.response.SingleResult;
import com.tobe.fishking.v2.service.ResponseService;
import com.tobe.fishking.v2.service.common.CommonService;
import com.tobe.fishking.v2.service.common.PopularService;
import com.tobe.fishking.v2.service.fishking.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(tags = {"메인"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class MainController {

    private Environment environment;

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
            "\n { " +
            "\n popularKeyword: [ keyword1, keyword2, ... , keyword10 ]  // 인덱스 순서로 1 2 3 .. 순입니다." +
            "\n keywords: [ {keyword: 키워드 1, isNew: 신규여부}, ... , {keyword: 키워드 10, isNew: 신규여부} ]  // 인덱스 순서로 1 2 3 .. 순입니다." +
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
            "\n 키워드 리스트가 추가되었습니다. new 표시를 위해 String list 에서 Object list 로 리턴됩니다." +
            "\n 해당 작업이 끝나면 기존의 popularKeyword 키값은 삭제됩니다. " +
            "")
    @GetMapping("/search/keywords")
    public Map<String, Object> getSearchPageData(@RequestHeader("Authorization") String token) {
        Map<String, Object> result = new HashMap<>();
//        result.put("popularKeyword", popularService.getPopularKeywordString());
        result.put("keywords", popularService.getPopularKeyword());
        result.put("ad", commonService.getAdList(AdType.SEARCH_AD));
        return result;
    }

    @ApiOperation(value = "통합검색 결과", notes = "통합검색 결과 " +
            "\n {" +
            "\n ship: 업체 { " +
            "\n  content: [{ " +
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
            "\n sold: 결제 수 " +
            "\n }, ... ] }," +
            "\n , ... " +
            "\n , totalElements: 총 개수" +
            "\n diary: {" +
            "\n     content: [{" +
            "\n         id: 조황일지 id" +
            "\n         title: 제목" +
            "\n         contents: 내용" +
            "\n         imageUrl: 이미지 " +
            "\n         nickName: 작성자 닉네임" +
            "\n         profileImageUrl: 작성자 프로필사진" +
            "\n         createdDate: 작성일" +
            "\n         loves: 좋아요 수 " +
            "\n         comments: 댓글 수 " +
            "\n         fishingType: 선박 / 갯바위" +
            "\n         species: 어종" +
            "\n     }, ... ]," +
            "\n     , ..." +
            "\n     , totalElements: 총 개수" +
            "\n live: 어복TV, 내용은 ship 과 같음. 왼쪽에 선박 사진, 오른쪽에는 선박명 보여주시면 됩니다. (live 있는 선박리스트입니다) " +
            "\n blog: 조행기, 내용은 diary 와 같음." +
            "\n keyword: 검색 키워드 ")
    @GetMapping("/search/all")
    public Map<String, Object> getSearchResultAll(@RequestHeader("Authorization") String token,
                                                  @RequestParam String keyword,
                                                  @RequestParam(required = false, defaultValue = "37.5642135") Double latitude,
                                                  @RequestParam(required = false, defaultValue = "127.0016985") Double longitude) {
        commonService.addSearchKeys(token, keyword, SearchPublish.TOTAL);
        return commonService.searchTotal(keyword, latitude, longitude);
    }

    @ApiOperation(value = "전체검색 업체 결과", notes = "통합검색 업체 결과 " +
            "\n {" +
            "\n ship: 업체 { " +
            "\n  content: [{ " +
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
            "\n sold: 결제 수 " +
            "\n type: 선상 or 갯바위 " +
            "\n }, ... ] }," +
            "\n , ... " +
            "\n     , totalPages: 총 페이지 수" +
            "\n     , totalElements: 총 개수" +
            "\n     , last: 마지막 페이지 여부" +
            "\n     , first: 첫 페이지 여부" +
            "\n keyword: 검색 키워드 " +
            "\n 정렬에 추천순 -> 좋아요순 입니다. " +
            "\n order 값으로는 " +
            "\n 거리 순: distance, 명칭 순: name " +
            "\n 최신순은 order 없는 경우 거리 순 입니다." +
            "\n " +
            "\n 요청의 type 키는 기본의 검색에는 필요없는 값입니다 (현재 그대로 보내주시면 됩니다) " +
            "\n 메인의 지역별 조황과 어종별 조황의 각 항목을 클릭 하면 " +
            "\n search-boat.html 페이지로 이동 후 " +
            "\n 지역별의 경우 type=direction, 어종별의 경우 type=species 로 설정 후 " +
            "\n keyword 에 해당 code 를 넣어주시고 결과를 리스팅 해주시면 됩니다. " +
            "\n (ex. 서해남부 : keyword=northWest&type=direction , 우럭 : keyword=rockcod&type=species )")
    @GetMapping("/search/ship/{page}")
    public Map<String, Object> getSearchResultShip(@RequestHeader("Authorization") String token,
                                                   @RequestParam String keyword,
                                                   @RequestParam(required = false) String type,
                                                   @RequestParam(defaultValue = "distance") String order,
                                                   @RequestParam(required = false, defaultValue = "37.5642135") Double latitude,
                                                   @RequestParam(required = false, defaultValue = "127.0016985") Double longitude,
                                                   @PathVariable Integer page) throws EmptyListException {
        if (type == null) {
            commonService.addSearchKeys(token, keyword, SearchPublish.COMPANY);
            return commonService.searchShip(keyword, page, order, latitude, longitude);
        } else {
            return commonService.searchShipWithType(keyword, page, order, type, latitude, longitude);
        }
    }

    @ApiOperation(value = "전체검색 라이브 결과", notes = "전체검색 라이브 결과 " +
            "\n {" +
            "\n live: 업체 { " +
            "\n  content: [{ " +
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
            "\n sold: 결제 수 " +
            "\n }, ... ] }," +
            "\n , ... " +
            "\n     , totalPages: 총 페이지 수" +
            "\n     , totalElements: 총 개수" +
            "\n     , last: 마지막 페이지 여부" +
            "\n     , first: 첫 페이지 여부" +
            "\n keyword: 검색 키워드 " +
            "\n order 값으로는 " +
            "\n 인기 순: loves " +
            "\n 최신순은 order 없는 경우 최신 순 입니다." +
            "\n 현재 인기순서 정하는 기준이 확실하지 않습니다. 두 경우 모두 최신순으로 전달됩니다.")
    @GetMapping("/search/live/{page}")
    public Map<String, Object> getSearchResultLive(@RequestHeader("Authorization") String token,
                                                   @RequestParam String keyword,
                                                   @RequestParam(defaultValue = "") String order,
                                                   @RequestParam(required = false, defaultValue = "37.5642135") Double latitude,
                                                   @RequestParam(required = false, defaultValue = "127.0016985") Double longitude,
                                                   @PathVariable Integer page) throws EmptyListException {
        commonService.addSearchKeys(token, keyword, SearchPublish.TV);
        return commonService.searchLive(keyword, page, order, latitude, longitude);
    }

    @ApiOperation(value = "전체검색 조황일지 결과", notes = "전체검색 조황일지 결과 " +
            "\n {" +
            "\n diary: {" +
            "\n     content: [{" +
            "\n         id: 조황일지 id" +
            "\n         title: 제목" +
            "\n         contents: 내용" +
            "\n         imageUrl: 이미지 " +
            "\n         nickName: 작성자 닉네임" +
            "\n         profileImageUrl: 작성자 프로필사진" +
            "\n         createdDate: 작성일" +
            "\n         loves: 좋아요 수 " +
            "\n         comments: 댓글 수 " +
            "\n         fishingType: 선박 / 갯바위" +
            "\n         species: 어종" +
            "\n     }, ... ]," +
            "\n     , ..." +
            "\n     , totalPages: 총 페이지 수" +
            "\n     , totalElements: 총 개수" +
            "\n     , last: 마지막 페이지 여부" +
            "\n     , first: 첫 페이지 여부" +
            "\n keyword: 검색 키워드 " +
            "\n 정렬에 추천순 -> 좋아요순 입니다. " +
            "\n order 값으로는 " +
            "\n 좋아요 순: loves, 댓글 순: comments " +
            "\n 최신순은 order 없는 경우 최신 순 입니다.")
    @GetMapping("/search/diary/{page}")
    public Map<String, Object> getSearchResultDiary(@RequestHeader("Authorization") String token,
                                                    @RequestParam String keyword,
                                                    @RequestParam(defaultValue = "") String order,
                                                    @RequestParam(required = false, defaultValue = "37.5642135") Double latitude,
                                                    @RequestParam(required = false, defaultValue = "127.0016985") Double longitude,
                                                    @PathVariable Integer page) throws EmptyListException {
        commonService.addSearchKeys(token, keyword, SearchPublish.FISHINGDIARY);
        return commonService.searchDiary(keyword, page, order);
    }

    @ApiOperation(value = "전체검색 조행기 결과", notes = "전체검색 조행기 결과 " +
            "\n {" +
            "\n blog: {" +
            "\n     content: [{" +
            "\n         id: 조황일지 id" +
            "\n         title: 제목" +
            "\n         contents: 내용" +
            "\n         imageUrl: 이미지 " +
            "\n         nickName: 작성자 닉네임" +
            "\n         profileImageUrl: 작성자 프로필사진" +
            "\n         createdDate: 작성일" +
            "\n         loves: 좋아요 수 " +
            "\n         comments: 댓글 수 " +
            "\n         fishingType: 선박 / 갯바위" +
            "\n         species: 어종" +
            "\n     }, ... ]," +
            "\n     , ..." +
            "\n     , totalPages: 총 페이지 수" +
            "\n     , totalElements: 총 개수" +
            "\n     , last: 마지막 페이지 여부" +
            "\n     , first: 첫 페이지 여부" +
            "\n keyword: 검색 키워드" +
            "\n 정렬에 추천순 -> 좋아요순 입니다. " +
            "\n order 값으로는 " +
            "\n 좋아요 순: loves, 댓글 순: comments " +
            "\n 최신순은 order 없는 경우 최신 순 입니다.")
    @GetMapping("/search/blog/{page}")
    public Map<String, Object> getSearchResultBlog(@RequestHeader("Authorization") String token,
                                                   @RequestParam String keyword,
                                                   @RequestParam(defaultValue = "") String order,
                                                   @RequestParam(required = false, defaultValue = "37.5642135") Double latitude,
                                                   @RequestParam(required = false, defaultValue = "127.0016985") Double longitude,
                                                   @PathVariable Integer page) throws EmptyListException {
        commonService.addSearchKeys(token, keyword, SearchPublish.FISHINGDIARY2);
        return commonService.searchBlog(keyword, page, order);
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

    @ApiOperation(value = "메인화면 데이터 ", notes = "메인화면 데이터 " +
            "\n live: 실시간 조황 [{ " +
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
            "\n ship: 출조 정보, 내용 live 와 같음" +
            "\n ad: 추천 업체, 내용 live 와 같음" +
            "\n direction: 지역별 조황 [{ " +
            "\n     code: 코드 " +
            "\n     codeName: 이름 " +
            "\n     count: 포함 수" +
            "\n     img: 어종 이미지" +
            "\n }, ... ] " +
            "\n species: 어종별 조황, 내용 direction 과 같음" +
            "\n fishingDiaries: 조황 일지 [{" +
            "\n     id: 조황일지 id" +
            "\n     title: 제목" +
            "\n     imageUrl: 이미지 주소" +
            "\n     sido: 시도 " +
            "\n     sigungu: 시군구 " +
            "\n     species: 어종 " +
            "\n }, ... ] " +
            "\n 조황일지 클릭 시 해당 조황일지 상세로, 더보기 누를 시 어복스토리 > 조황일지로 이동" +
            "\n 실시간 조황 -> 어복TV로 " +
            "\n 출조 정보, 추천 업체 -> 해당 선박 상세로, 더보기 시 선상 리스트로 " +
            "\n 어종별 조황 클릭시 선상 리스트에서 해당 어종 검색 결과로 이동 " +
            "")
    @GetMapping(value = "/main")
    public Map<String, Object> getMainScreenData(@RequestHeader(name = "Authorization") String token)  {
        Map<String, Object> result;
        result = commonService.getMainScreenData();
        return result;
    }

    @GetMapping(value = "/main/new")
    public List<ShipListResponse> newHome(@RequestHeader(name = "Authorization") String token) {
        return shipService.getNewHomeData();
    }

    @GetMapping("envTest")
    public String envTest() {
//        System.out.println(System.getProperty("com.sun.management.jmxremote"));
        String ip = InetAddress.getLoopbackAddress().getHostAddress();
        System.out.println(ip);
        return ip;
    }

}