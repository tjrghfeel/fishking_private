package com.tobe.fishking.v2.controller.smartfishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.ErrorCodes;
import com.tobe.fishking.v2.exception.ApiException;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.AddGoods;
import com.tobe.fishking.v2.model.response.FishingShipResponse;
import com.tobe.fishking.v2.model.response.UpdateGoodsResponse;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.GoodsService;
import com.tobe.fishking.v2.service.fishking.ShipService;
import com.tobe.fishking.v2.service.smartfishing.FishingShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"스마트출조 선박 및 상품"})
@RequiredArgsConstructor
public class ShipsGoodsController {

    private final MemberService memberService;
    private final ShipService shipService;
    private final FishingShipService fishingShipService;
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
        return fishingShipService.getFishingShips(memberId, keyword, cameraActive, page);
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

    @ApiOperation(value = "상품등록 - 선박 리스트", notes= "상품등록에 필요한 선박 데이터 {" +
            "\n id: 선박 id" +
            "\n shipName: 선박명" +
            "\n ")
    @GetMapping("/goods/ships")
    public List<Map<String, Object>> getGoodsShips(@RequestHeader(name = "Authorization") String token) throws ResourceNotFoundException, NotAuthException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnsupportedEncodingException, EmptyListException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        return fishingShipService.getGoodsShips(memberId);
    }

    @ApiOperation(value = "상품등록", notes = "상품등록")
    @PostMapping("/goods/add")
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

    @ApiOperation(value = "상품수정", notes = "상품수정")
    @PutMapping("/goods/update/{goodsId}")
    public Map<String, Object> updateGoods(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody AddGoods addGoods,
            @PathVariable Long goodsId) throws ResourceNotFoundException {
        Map<String, Object> result = new HashMap<>();
        Member member = memberService.getMemberBySessionToken(token);
        try {
            Boolean success = shipService.updateGoods(goodsId, addGoods, member);
            result.put("result", "success");
            result.put("id", goodsId);
            return result;
        } catch (Exception e) {
            throw new ApiException(ErrorCodes.DB_INSERT_ERROR, "상품 업데이트에 실패했습니다.");
        }
    }

    @ApiOperation(value = "상품정보 ", notes= "상품정보 {" +
            "\n shipId: 선박 id" +
            "\n shipName: 선박명" +
            "\n name: 상품명" +
            "\n amount: 상품 가격" +
            "\n minPersonnel: 최소인원" +
            "\n maxPersonnel: 최대인원" +
            "\n fishingStartTime: 운항시작시간" +
            "\n fishingEndTime: 운항종료시간" +
            "\n isUse: 상태 (판매 true, 대기 false)" +
            "\n species: 어종리스트 [어종, ... ]" +
            "\n fishingDates: 조업일 리스트 [조업일, ... ]" +
            "\n reserveType: 예약 확정방법 approval: 승인예약 auto: 자동예약 " +
            "\n positionSelect: 위치선정여부" +
            "\n extraRun: 추가운행 여부" +
            "\n extraPersonnel: 추가운행 최소 인원" +
            "\n extraShipNumber: 추가운행 최대 선박 수 " +
            "\n ")
    @GetMapping("/goods/detail/{goodsId}")
    public UpdateGoodsResponse getGoodsData(@RequestHeader(name = "Authorization") String token,
                                            @PathVariable Long goodsId) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        return goodsService.getGoodsData(goodsId);
    }

    @ApiOperation(value = "선박등록 - 카메라 리스트", notes= "선박등록에 필요한 카메라리스트 데이터 {" +
            "\n serial: 시리얼번호" +
            "\n name: 카메라 이름" +
            "\n ")
    @GetMapping("/ship/cameras")
    public List<Map<String, Object>> getCameras(@RequestHeader(name = "Authorization") String token) throws ResourceNotFoundException, NotAuthException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnsupportedEncodingException, EmptyListException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        return fishingShipService.getCameraList(member);
    }
}
