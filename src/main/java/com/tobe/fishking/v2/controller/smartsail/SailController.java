package com.tobe.fishking.v2.controller.smartsail;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.smartsail.AddRiderDTO;
import com.tobe.fishking.v2.model.smartsail.RiderGoodsListResponse;
import com.tobe.fishking.v2.model.smartsail.RiderSearchDTO;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.smartsail.BoardingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"스마트승선 승선관리 및 대시보드"})
@RequiredArgsConstructor
public class SailController {

    private final MemberService memberService;
    private final BoardingService boardingService;

    @ApiOperation(value = "대시보드", notes= "대시보드 orderBy: 정렬, 최신순:new, 선박명순: shipName, 승선자명순: username " +
            "\n 응답데이터 : {" +
            "\n status: 금일 승선 현황 {" +
            "\n     waitCount: 승선대기" +
            "\n     confirmCount: 승선확인" +
            "\n     failCount: 확인실패" +
            "\n     cancelCount: 승선취소" +
            "\n     confirmPercentage: 확인비율" +
            "\n     failPercentage: 실패비율" +
            "\n }" +
            "\n boardingPeople: 금일 승선 대기자 [ {" +
            "\n     riderId: 승선자 id" +
            "\n     username: 승선자명" +
            "\n     shipName: 선박명" +
            "\n     goodsName: 상품명" +
            "\n     phone: 연락처" +
            "\n     emergencyPhone: 비상연락처" +
            "\n     visitCount: 이용횟수" +
            "\n     fingerType: 지문의 손가락" +
            "\n }, ... ]" +
            "\n } " +
            "\n 현황: " +
            "\n 왼쪽의 표는 확인, 실패의 % 입니다" +
            "\n 대기자 리스트:" +
            "\n 선상명 -> 선박명 으로 변경해주세요" +
            "\n 연락처 아래 비상연락처 보여주세요" +
            "\n 프로필사진 없이 이름만 가운데로 보여주세요" +
            "\n 방문횟수는 어복황제 이용 횟수 로 변경해주세요")
    @GetMapping("/sail/dashboard")
    public Map<String, Object> dashboard(@RequestHeader(name = "Authorization") String token,
                                         @RequestParam(defaultValue = "new") String orderBy) throws ResourceNotFoundException, NotAuthException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        Map<String, Object> response = new HashMap<>();
        response.put("boardingPeople", boardingService.getTodayBoarding(member, orderBy));
        response.put("boardingPeopleComplete", boardingService.getTodayBoardingComplete(member, orderBy));
        response.put("status", boardingService.getDashboard(member));
        return response;
    }

    @ApiOperation(value = "지문등록", notes= "지문등록  " +
            "\n {" +
            "\n     username: 등록자 이름" +
            "\n     phone: 등록자 연락처" +
            "\n     fingerprint: 지문데이터" +
            "\n     fingerTypeNum: 손가락 숫자 (오른쪽 엄지~새끼 : 1~5, 왼쪽 엄지~새끼 : 6~10)" +
            "\n }")
    @PostMapping(value = "/sail/fingerprint/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> addFingerprint(@RequestHeader(name = "Authorization") String token,
                                              @RequestBody Map<String, Object> body) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        Map<String, Object> response = new HashMap<>();
        try {
            boardingService.updateFingerprint(member, body);
            response.put("status", "success");
            response.put("message", "등록되었습니다.");
        } catch (Exception e) {
//            response.put("status", "fail");
//            response.put("message", "등록에 실패했습니다.");
            throw new RuntimeException("지문등록에 실패했습니다.");
        }
        return response;
    }

    @ApiOperation(value = "지문확인", notes= "지문확인  " +
            "\n {" +
            "\n     riderId: 승선자 id" +
            "\n     username: 등록자 이름" +
            "\n     phone: 등록자 연락처" +
            "\n     fingerprint: 지문데이터" +
            "\n }")
    @PostMapping(value = "/sail/fingerprint/check", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> checkFingerprint(@RequestHeader(name = "Authorization") String token,
                                                @RequestBody Map<String, Object> body) throws ResourceNotFoundException, NotAuthException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean checked = boardingService.checkFingerprint(member, body);
            if (checked) {
                response.put("status", "success");
                response.put("message", "확인되었습니다.");
            } else {
//                response.put("status", "fail");
//                response.put("message", "일치하지 않습니다.");
                throw new RuntimeException("지문등록에 실패했습니다.");
            }
        } catch (Exception e) {
//            response.put("status", "fail");
//            response.put("message", "확인에 실패했습니다.");
            throw new RuntimeException("지문등록에 실패했습니다.");
        }
        return response;
    }

    @ApiOperation(value = "승선관리 탭", notes= "승선관리 탭 리스트" +
            "\n [{" +
            "\n     orderId: 주문 id" +
            "\n     shipName: 선박명" +
            "\n     profileImage: 프로필이미지" +
            "\n     goodsName: 상품명" +
            "\n     status: 상태" +
            "\n     dateInterval: 오늘로부터 일 수" +
            "\n     date: 날짜" +
            "\n     orderNumber: 주문번호" +
            "\n     orderName: 주문자명" +
            "\n     orderEmail: 주문자 이메일" +
            "\n     personnel: 인원수" +
            "\n }, ... ]" +
            "\n 주소 거리 빼시고 해당 자리에 상품명 보여주세요" +
            "\n 모든 데이터에대해서 예약번호 아래 예약자 정보 보여주세요")
    @GetMapping("/sail/riders/{page}")
    public Page<RiderGoodsListResponse> searchRiders(@RequestHeader(name = "Authorization") String token,
                             @PathVariable Integer page,
                             RiderSearchDTO riderSearchDTO) throws NotAuthException, ResourceNotFoundException, EmptyListException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        Page<RiderGoodsListResponse> riders = boardingService.searchRider(member, riderSearchDTO, page);
        if (!riders.hasContent()) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return riders;
        }
    }

    @ApiOperation(value = "승선자 추가", notes= "승선자 추가  " +
            "\n {" +
            "\n     orderId: 주문 아이디" +
            "\n     name: 승선자명" +
            "\n     birthDate: 생년월일 yyyy-MM-dd" +
            "\n     phone: 연락처" +
            "\n     emergencyPhone: 비상연락처" +
            "\n }" +
            "\n 성별, 거주지역, 지문입력은 빼주세요" +
            "\n 비상연락처 입력이 필요합니다")
    @PostMapping("/sail/riders/add")
    public Map<String, Object> addRider(@RequestHeader(name = "Authorization") String token,
                                        @RequestBody AddRiderDTO body) throws ResourceNotFoundException, NotAuthException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        Map<String, Object> response = new HashMap<>();
        try {
            boardingService.addRider(member, body);
            response.put("status", "success");
            response.put("message", "등록되었습니다.");
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "등록에 실패했습니다.");
        }
        return response;
    }

    @ApiOperation(value = "승선자 삭제", notes= "승선자 삭제  " +
            "\n {" +
            "\n     riderId: 승선자 id" +
            "\n }")
    @PostMapping("/sail/riders/del")
    public Map<String, Object> delRider(@RequestHeader(name = "Authorization") String token,
                                        @RequestBody Map<String, Object> body) throws ResourceNotFoundException, NotAuthException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        Map<String, Object> response = new HashMap<>();
        try {
            boardingService.delRider(member, Long.parseLong(body.get("riderId").toString()));
            response.put("status", "success");
            response.put("message", "삭제되었습니다.");
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "삭제에 실패했습니다.");
        }
        return response;
    }

    @ApiOperation(value = "승선자리스트 상세", notes= "승선자리스트 상세  " +
            "\n {" +
            "\n     orderId: 주문 id" +
            "\n     date: 날짜" +
            "\n     personnel: 승선자 수" +
            "\n     shipName: 선박명" +
            "\n     shipProfileImage: 선박프로필사진" +
            "\n     riders: 승선자 리스트 [{" +
            "\n         riderId: 승선자 id" +
            "\n         name: 이름" +
            "\n         phone: 연락처" +
            "\n         emergencyPhone: 비상연락처" +
            "\n         isRide: 상태" +
            "\n     }, ... ]" +
            "\n }" +
            "\n 연락처 아래에 비상연락처 추가해주세요" +
            "")
    @GetMapping("/sail/riders/detail/{orderId}")
    public Map<String, Object> getRiderDetails(@RequestHeader(name = "Authorization") String token,
                                               @PathVariable Long orderId) throws ResourceNotFoundException, NotAuthException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        return boardingService.detailRiders(orderId);
    }

}
