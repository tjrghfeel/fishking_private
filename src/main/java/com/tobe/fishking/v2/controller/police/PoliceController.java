package com.tobe.fishking.v2.controller.police;

import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.police.PoliceGoodsResponse;
import com.tobe.fishking.v2.service.ResponseService;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.GoodsService;
import com.tobe.fishking.v2.service.fishking.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"해경앱"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class PoliceController {

    private final GoodsService goodsService;
    private final OrdersService ordersService;
    private final ResponseService responseService;
    private final MemberService memberService;

    @ApiOperation(value = "선박현황 -대시보드 ", notes = "선박수, 업체수 조회한다.   " +
            "\n {" +
            "\n     runningToday: 오늘 예정" +
            "\n     runningNow: 현재 출조" +
            "\n     total: 금일 예약 인원" +
            "\n     waitRider: 승선 대기 인원" +
            "\n     waitRiderPercentage: 승선 대기 인원 퍼센티지" +
            "\n     realRider: 승선완료 인원" +
            "\n     realRiderPercentage: 승선완료 인원 퍼센티지" +
            "\n }")
    @GetMapping(value = "/police/dashboard")
    public HashMap<String, Object> getDashboard(@RequestHeader(name = "Authorization") String token) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkPoliceAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        var marineCount  = new HashMap<String, Object>();

        marineCount.put("runningToday", goodsService.getTodayRunGoods());
        marineCount.put("runningNow", goodsService.getNowRunGoods());

        Long realRider = goodsService.getRealRidePersonnel();
        Long waitRider = goodsService.getWaitRidePersonnel();
        Double realRiderPercentage = realRider * 100.0 / (waitRider + realRider);
        Double waitRiderPercentage = waitRider * 100.0 / (waitRider + realRider);

        marineCount.put("total", realRider + waitRider);
        marineCount.put("waitRider", waitRider);
        marineCount.put("waitRiderPercentage", waitRiderPercentage);
        marineCount.put("realRider", realRider);
        marineCount.put("realRiderPercentage", realRiderPercentage);

        return marineCount;

    }

    @ApiOperation(value = " 출항정보 ", notes = "출항현황 Map.   " +
            "\n [ {" +
            "\n     shipId: 선박id" +
            "\n     goodsId: 상품id" +
            "\n     shipName: 선박명" +
            "\n     runningTime: 운항시간" +
            "\n     status: 상태" +
            "\n     hasCamera: 카메라 유무" +
            "\n     maxPersonnel: 최대인원" +
            "\n     ridePersonnel: 탑승인원" +
            "\n     latitude: 현재 위치의 위도" +
            "\n     longitude: 현재 위치의 경도" +
            "\n }, ... ]" +
            "\n 리스트에 있는 선박들의 위경도값으로 지도에 포인트 찍어주세요")
    @GetMapping(value = "/police/ships/map")
    public List<PoliceGoodsResponse> getShipDepartInfoForMap(@RequestHeader(name = "Authorization") String token) throws EmptyListException, NotAuthException, ResourceNotFoundException {
        if (!memberService.checkPoliceAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        List<PoliceGoodsResponse> responses = goodsService.getPoliceAllGoods();
        if (responses.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return responses;
        }
    }

    @ApiOperation(value = " 선박정보 ", notes = "선박정보 탭 리스트 입니다. 선상정보 -> 선박정보 로 변경해주세요" +
            "\n [ {" +
            "\n     shipId: 선박id" +
            "\n     goodsId: 상품id" +
            "\n     shipName: 선박명" +
            "\n     runningTime: 운항시간" +
            "\n     status: 상태" +
            "\n     hasCamera: 카메라 유무" +
            "\n     maxPersonnel: 최대인원" +
            "\n     ridePersonnel: 탑승인원" +
            "\n     latitude: 현재 위치의 위도" +
            "\n     longitude: 현재 위치의 경도" +
            "\n }, ... ]" +
            "\n 카메라 없을 경우 CCTV 보기 버튼 안보이게 해주세요" +
            "\n 보기 버튼 누를 경우 고객앱에 어복라이브 선박 상세 페이지와 같게 보이면 됩니다. API도 거기와 같은 API를 사용하시면 됩니다." +
            "\n 선박명과 인원 사이에 운항시간 넣어주세")
    @GetMapping(value = "/police/ships/{page}")
    public Page<PoliceGoodsResponse> getShipDepartInfo(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Integer page) throws EmptyListException, NotAuthException, ResourceNotFoundException {
        if (!memberService.checkPoliceAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Page<PoliceGoodsResponse> responses = goodsService.getPoliceAllGoods(page);
        if (responses.isEmpty()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return responses;
        }
    }

    @ApiOperation(value = " 선박정보 -명단리스트 ", notes = "승선확인 -명단리스트. " +
            "\n {" +
            "\n     shipName: 선박명" +
            "\n     date: 날짜" +
            "\n     ridersCount: 인원수" +
            "\n     riders: [{" +
            "\n         name: 이름" +
            "\n         birthdate: 생년월일" +
            "\n         phone: 연락처" +
            "\n         fingerPrint: 지문인식" +
            "\n     }, ... ]" +
            "\n 성별, 지역은 빼주세요")
    @GetMapping(value = "/police/ride/{goodsId}")
    public Map<String, Object> getBoardingListForGoods(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long goodsId) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkPoliceAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        if (goodsId == null ) return null;
        return goodsService.getRideData(goodsId);
    }
}
