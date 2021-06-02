package com.tobe.fishking.v2.controller.smartfishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.ErrorCodes;
import com.tobe.fishking.v2.exception.ApiException;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.AddGoods;
import com.tobe.fishking.v2.model.fishing.UpdateGoods;
import com.tobe.fishking.v2.model.fishing.UpdateShipDTO;
import com.tobe.fishking.v2.model.response.FishingShipResponse;
import com.tobe.fishking.v2.model.response.UpdateGoodsResponse;
import com.tobe.fishking.v2.model.response.UpdateShipResponse;
import com.tobe.fishking.v2.model.smartfishing.PlaceDTO;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.GoodsService;
import com.tobe.fishking.v2.service.fishking.PlacesService;
import com.tobe.fishking.v2.service.fishking.ShipService;
import com.tobe.fishking.v2.service.smartfishing.FishingShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    private final PlacesService placesService;

    @ApiOperation(value = "선박 리스트", notes = "선박 탭의 상 리스트. " +
            "\n keywordType: 검색어타입 (선박명: shipName) 한 종류 입니다." +
            "\n keyword: 검색어" +
            "\n status: cameraActive (유: true, 무: false)" +
            "\n {" +
            "\n content:[ {" +
            "\n     id: 상품 id" +
            "\n     shipName: 선박명" +
            "\n     profileImage: 선박이미지" +
            "\n     address: 선박주소" +
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
            "\n     profileImage: 선박이미지" +
            "\n     address: 선박주소" +
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
//            @RequestParam(name = "species") List<String> species,
//            @RequestParam(name = "fishingDates") List<String> fishingDates,
            @RequestBody AddGoods addGoods) throws ResourceNotFoundException {
        Map<String, Object> result = new HashMap<>();
        try {
            Long goodsId = goodsService.addGood(addGoods, token);
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
            @RequestBody UpdateGoods updateGoods,
            @PathVariable Long goodsId) throws ResourceNotFoundException {
        Map<String, Object> result = new HashMap<>();
        Member member = memberService.getMemberBySessionToken(token);
        try {
            Boolean success = goodsService.updateGoods(goodsId, updateGoods, member);
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
            "\n nhn: [{" +
            "\n serial: 시리얼번호" +
            "\n name: 카메라 이름" +
            "\n }, .. ]," +
            "\n adt: [{" +
            "\n }, .. ]," +
            "")
    @GetMapping("/ship/cameras")
    public Map<String, Object> getCameras(@RequestHeader(name = "Authorization") String token) throws ResourceNotFoundException, NotAuthException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnsupportedEncodingException, EmptyListException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        Map<String, Object> result = new HashMap<>();
        result.put("nhn", fishingShipService.getNHNCameraList(member));
        result.put("adt", fishingShipService.getADTCameraList(member));
        return result;
    }

    @ApiOperation(value = "선박등록", notes = "선박등록 아래는 요청데이터의 일부입니" +
            "\n name: 선박명 " +
            "\n fishingType: 선박 구분 (ship: 선상, seaRocks: 갯바위) " +
            "\n address: 선박 탑승 주소. 필수로 보내주세요. 검색 후 기본주소 + 상세주소 보내주시면 됩니다." +
            "\n sido: 시/도 " +
            "\n sigungu: 시/군/구 " +
            "\n weight: 무게 (3, 5, 9 t 선택) " +
            "\n boardingPerson: 탑승인원 " +
            "\n latitude: 주소 위도 " +
            "\n longitude: 주소 경도 " +
            "\n profileImage: 사진등록 (등록 후 응답의 downloadUrl 을 보내주세요 )" +
            "\n videoId: 녹화영상 등록. 등록한 영상 id." +
            "\n ownerWordingTitle: 한마디 제목 " +
            "\n ownerWording: 한마디 내용 " +
            "\n noticeTitle: 공지 제목 " +
            "\n notice: 공지 내용 " +
            "\n fishSpecies[] 어종 리스트" +
            "\n services[] 서비스 리스트" +
            "\n facilities[] 편의시설 리스트" +
            "\n devices[] 장비 리스트" +
            "\n events[] 이벤트 리스트 [{eventId: 이벤트 id 신규는 null, title: 제목, startDate: 시작일, endDate: 종료일, contents: 내용, imageId: 이미지 아이디, imageUrl: 이미지 주소, 신규는 null}, ... ]" +
            "\n positions[] 사용할 위치 리스트 (예: [1,2,3,4]), 갯바위 타입의 경우에는 갯바위 id 리스트" +
            "\n adtCameras[] adt 카메라리스트 [{serial: 시리얼번호, name: 카메라명}, .. ] " +
            "\n nhnCameras[] nhn 카메라리스트 [{serial: 시리얼번호, name: 카메라명}, .. ] " +
            "\n router: LTE 라우터 IMEI" +
            "\n 메인화면 노출은 빠집니다" +
            "\n 녹화영상은 파일 업로드 시 filePublish: ship 으로 이미지와 같은 방법으로 업로드 하시면 됩니다. " +
            "\n 주소 아래의 좌표는 위경도로 변경해주세요. 사용자 입력이 아닌 주소 검색결과값 넣어주시면 됩니다" +
            "\n devices는 /v2/api/commonCode/153 으로 나오는 코드 리스트중 선택 한 장비의 코드 리스트입니다  보내주세요." +
            "\n")
    @PostMapping("/ship/add")
    public Map<String, Object> addShip(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody UpdateShipDTO addShipDTO) throws ResourceNotFoundException {
        Map<String, Object> result = new HashMap<>();
//        Long shipId = fishingShipService.addShip(addShipDTO, token);
//        result.put("result", "success");
//        result.put("id", shipId);
//        return result;
        try {
//            Long shipId = fishingShipService.addShip(addShipDTO, token, fishSpecies, services, facilities, devices, events, positions, adtCameras, nhnCameras);
            if (addShipDTO.getFishingType().equals("all")) {
                String shipName = addShipDTO.getName();
                addShipDTO.setFishingType("ship");
                addShipDTO.setName(shipName + " (선상)");
                Long shipId_ship = fishingShipService.addShip(addShipDTO, token);
                addShipDTO.setFishingType("seaRocks");
                addShipDTO.setName(shipName + " (갯바위)");
                Long shipId_seaRocks = fishingShipService.addShip(addShipDTO, token);
                result.put("id", shipId_ship);
            } else {
                Long shipId = fishingShipService.addShip(addShipDTO, token);
                result.put("id", shipId);
            }
            result.put("result", "success");
            return result;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ErrorCodes.DB_INSERT_ERROR, "선박 등록에 실패했습니다.");
        }
    }

    @ApiOperation(value = "선박수정", notes = "선박수정")
    @PutMapping("/ship/update/{shipId}")
    public Map<String, Object> updateShip(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody UpdateShipDTO updateShipDTO,
            @PathVariable Long shipId) throws ResourceNotFoundException {
        Map<String, Object> result = new HashMap<>();
        try {
            fishingShipService.updateShip(shipId, updateShipDTO, token);
            result.put("result", "success");
            result.put("id", shipId);
            return result;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
//            e.printStackTrace();
//            return result;
            throw new ApiException(ErrorCodes.DB_INSERT_ERROR, "선박 수정에 실패했습니다.");
        }
    }

    @ApiOperation(value = "선박정보 ", notes= "선박정보 {" +
            "\n id: 선박 id" +
            "\n name: 선박명" +
            "\n fishingType: 타입 (ship, seaRocks)" +
            "\n address: 주소" +
            "\n sido: 시/도 (경기도)" +
            "\n sigungu: 시/군/구 (고양시)" +
            "\n tel: 전화번호" +
            "\n weight: 선박크기 (3, 5, 9)" +
            "\n boardingPerson: 탑승인원" +
            "\n latitude: 위도" +
            "\n longitude: 경도" +
            "\n profileImage: 선박 이미지" +
            "\n videoId: 비디오 id" +
            "\n video: 비디오 주소" +
            "\n fishSpecies: 어종 코드 리스트 [어종 code, ...] " +
            "\n services: 서비스 코드 리스트 [서비스 code, ...] " +
            "\n facilities: 편의시설 코드 리스트 [편의시설 code, ...] " +
            "\n devices: 보유장비 코드 리스트 [보유장비 code, ...] " +
            "\n events: [{eventId: 이벤트 id, title: 제목, startDate: 시작일, endDate: 종료일, contents: 내용, imageId: 이미지 id, imageUrl: 이미지 주소}, ... ]" +
            "\n positions: 사용하는 위치, 갯바위 타입의 경우 null" +
            "\n seaRocks: 갯바위 리스트. 선상 타입의 경우 null [{" +
            "\n     id: 갯바위 id" +
            "\n     name: 갯바위 명" +
            "\n     address: 갯바위의 주소" +
            "\n     latitude: 갯바위의 위도" +
            "\n     longitude: 갯바위의 경도" +
            "\n     points: 해당 갯바위의 포인트 리스트 [{ " +
            "\n         latitude: 포인트의 위도" +
            "\n         longitude: 포인트의 경도" +
            "\n         id: 포인트 id " +
            "\n     }, ... ]" +
            "\n ownerWordingTitle: 한마디 제목 " +
            "\n ownerWording: 한마디 내용 " +
            "\n noticeTitle: 공지사항 제목 " +
            "\n notice: 공지사항 내용 " +
            "\n adtCameras: ADT 캡스 리스트 [{serial: 시리얼넘버, name: 카메라명}, ... ]" +
            "\n nhnCameras: NHN 토스트캠 리스트 [{serial: 시리얼넘버, name: 카메라명}, ... ]" +
            "\n router: LTE 라우터 IMEI" +
            "\n ")
    @GetMapping("/ship/detail/{shipId}")
    public UpdateShipResponse getShipDetails(@RequestHeader(name = "Authorization") String token,
                                             @PathVariable Long shipId) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        return fishingShipService.getShipDetails(shipId);
    }

    @ApiOperation(value = "갯바위 검색 ", notes = "주소로 갯바위를 검색합니다"  +
            "\n data: 갯바위 포인트 [{" +
            "\n     id: 갯바위 id" +
            "\n     name: 갯바위 명" +
            "\n }, ... ]" +
            "\n 결과값이 없는 경우 body 가 비어있고 status 가 204인 응답이 전달됩니다. ")
    @GetMapping("/searocks")
    public Map<String, Object> searchSeaRock(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(required = false, defaultValue = "") String sido,
            @RequestParam(required = false, defaultValue = "") String sigungu,
            @RequestParam(required = false, defaultValue = "") String dong) throws EmptyListException {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> rocks = placesService.searchSeaRock(sido, sigungu, dong, token);
        if (rocks.size() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            response.put("data", rocks);
        }
        return response;
    }

    @ApiOperation(value = "갯바위 리스트 ", notes = "id로 갯바위 정보를 얻습니다"  +
            "\n data: 갯바위 포인트 [{" +
            "\n     id: 갯바위 id" +
            "\n     name: 갯바위 명" +
            "\n     address: 갯바위의 주소" +
            "\n     latitude: 갯바위의 위도" +
            "\n     longitude: 갯바위의 경도" +
            "\n     points: 해당 갯바위의 포인트 리스트 [{ " +
            "\n         latitude: 포인트의 위도" +
            "\n         longitude: 포인트의 경도" +
            "\n         id: 포인트 id " +
            "\n     }, ... ]" +
            "\n }, ... ]" +
            "seaRockId%5B%5D=5&seaRockId%5B%5D=3 와 같이 urlencoding 해서 보내주세요")
    @GetMapping("/searocks/id")
    public Map<String, Object> getSeaRocks(
            @RequestParam(value = "seaRockId[]", required = false) Long[] seaRockId) throws EmptyListException {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> rocks = placesService.getSeaRocks(seaRockId);
        if (rocks.size() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            response.put("data", rocks);
        }
        return response;
    }

    @ApiOperation(value = "갯바위 등록 ", notes = "갯바위 등록")
    @PostMapping("/searocks/add")
    public Map<String, Object> addSeaRock(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody PlaceDTO placeDTO) throws ResourceNotFoundException {
        Map<String, Object> result = new HashMap<>();
        try {
            Long placeId = placesService.addSeaRock(placeDTO, token);

            switch (placeId.intValue()) {
                case -1:
                    result.put("result", "fail");
                    result.put("id", placeId);
                    result.put("message", "위도값이 없습니다");
                    break;
                case -2:
                    result.put("result", "fail");
                    result.put("id", placeId);
                    result.put("message", "경도값이 없습니다");
                    break;
                case -3:
                    result.put("result", "fail");
                    result.put("id", placeId);
                    result.put("message", "명칭값이 없습니다");
                    break;
                case -4:
                    result.put("result", "fail");
                    result.put("id", placeId);
                    result.put("message", "시/도 값이 없습니다");
                    break;
                case -5:
                    result.put("result", "fail");
                    result.put("id", placeId);
                    result.put("message", "시/군/구 값이 없습니다");
                    break;
                case -6:
                    result.put("result", "fail");
                    result.put("id", placeId);
                    result.put("message", "주소값이 없습니다");
                    break;
                case -7:
                    result.put("result", "fail");
                    result.put("id", placeId);
                    result.put("message", "공개여부값이 없습니다");
                    break;
                default:
                    result.put("result", "success");
                    result.put("id", placeId);
            }
            return result;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(ErrorCodes.DB_INSERT_ERROR, "갯바위 등록에 실패했습니다.");
        }
    }
}
