package com.tobe.fishking.v2.controller.smartfishing;

import com.tobe.fishking.v2.enums.ErrorCodes;
import com.tobe.fishking.v2.exception.ApiException;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.AddGoods;
import com.tobe.fishking.v2.model.response.FishingShipResponse;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.GoodsService;
import com.tobe.fishking.v2.service.fishking.ShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"스마트출조 선박 및 상품"})
@RequiredArgsConstructor
public class ShipsGoodsController {

    private final MemberService memberService;
    private final ShipService shipService;
    private final GoodsService goodsService;

    @ApiOperation(value = "선박 리스트", notes = "선박 탭의 상 리스트. " +
            "\n keywordType: 검색어타입 (선박명: shipName) 한 종류 입니다." +
            "\n keyword: 검색어" +
            "\n status: cameraActive (유: true, 무: false)" +
            "\n {" +
            "\n content:[ {" +
            "\n     id: 상품 id" +
            "\n     shipName: 선박명" +
            "\n     fishingType: 낚시 타입" +
            "\n     createDate: 등록일" +
            "\n     hasCamera: 카메라유무" +
            "\n     species: [어종, ...]" +
            "\n     speciesCount: 어종 총 수" +
            "\n totalElements: 총 데이터 수" +
            "\n totalPages: 총 페이지 수" +
            "\n last: 마지막페이지 여부" +
            "\n first: 첫페이지 여부" +
            "\n }" +
            "\n 상품리스트는 항상 null 입니다. 탭 이름 선상 -> 선박 입니다.")
    @GetMapping("/smartfishing/ship/{page}")
    public Page<FishingShipResponse> getShips(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable Integer page,
                                              @RequestParam(defaultValue = "") String keyword,
                                              @RequestParam(defaultValue = "") String cameraActive) throws NotAuthException, ResourceNotFoundException, EmptyListException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        return shipService.getFishingShips(memberId, keyword, cameraActive, page);
    }

    @ApiOperation(value = "상품 리스트", notes = "상품 탭의 상 리스트. " +
            "\n keywordType: 검색어타입 (상품명: goodsName, 선박명: shipName) 종류 입니다." +
            "\n keyword: 검색어" +
            "\n status: 상태 (판매: active, 판매중지: inactive)" +
            "\n {" +
            "\n content:[ {" +
            "\n     id: 상품 id" +
            "\n     shipName: 선박명" +
            "\n     fishingType: 낚시 타입" +
            "\n     createDate: 등록일" +
            "\n     hasCamera: 카메라유무" +
            "\n     species: [어종, ...]" +
            "\n     speciesCount: 어종 총 수" +
            "\n     goodsList: 상품리스트 [ {" +
            "\n         id: 상품 id" +
            "\n         name: 상품명" +
            "\n         fishingStartTime: 출조 시작 시간" +
            "\n         minPersonnel: 최소인원" +
            "\n         maxPersonnel: 최대인원" +
            "\n         amount: 가격" +
            "\n         status: 상태, (판매 / 판매중지) }, ... ]" +
            "\n totalElements: 총 데이터 수" +
            "\n totalPages: 총 페이지 수" +
            "\n last: 마지막페이지 여부" +
            "\n first: 첫페이지 여부" +
            "\n }" +
            "\n 카메라유무, 등록일, 낚시타입은 상품리스트에는 사용하지 않습니다.")
    @GetMapping("/smartfishing/goods/{page}")
    public Page<FishingShipResponse> getGoods(@RequestHeader(name = "Authorization") String token,
                                              @PathVariable Integer page,
                                              @RequestParam(defaultValue = "") String keywordType,
                                              @RequestParam(defaultValue = "") String keyword,
                                              @RequestParam(defaultValue = "") String status) throws NotAuthException, ResourceNotFoundException, EmptyListException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        return goodsService.getGoods(memberId, keywordType, keyword, status, page);
    }

    @ApiOperation(value = "상품등록", notes = "상품등록")
    @PutMapping("/goods/add")
    public Map<String, Object> addGoods(
            @RequestHeader(name = "Authorization") String token,
            AddGoods addGoods) throws ResourceNotFoundException {
        Map<String, Object> result = new HashMap<>();
        try {
            Long goodsId = shipService.addGood(addGoods, token);
            result.put("result", "success");
            result.put("id", goodsId);
            return result;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ErrorCodes.DB_INSERT_ERROR, "상품 등록에 실패했습니다.");
        }
    }

}
