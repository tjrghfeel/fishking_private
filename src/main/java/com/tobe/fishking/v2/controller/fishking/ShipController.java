package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.ReserveDTO;
import com.tobe.fishking.v2.model.fishing.ShipListResponse;
import com.tobe.fishking.v2.model.fishing.ShipResponse;
import com.tobe.fishking.v2.model.fishing.ShipSearchDTO;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.ShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ResponseHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"선상 및 갯바위"})
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;
    private final MemberService memberService;

    @ApiOperation(value = "배 리스트", notes = "배 리스트. 필수 아닌 값은 빈 문자열 또는 빈 리스트로 보내면 됩니다 ")
    @GetMapping("/ships/{page}")
    public Page<ShipListResponse> getShips(ShipSearchDTO shipSearchDTO,
                                           @ApiParam(value = "페이지. 0부터 시작", required = true, defaultValue = "0", example = "0") @PathVariable int page) {
        return shipService.getShips(shipSearchDTO, page);
    }

    @ApiOperation(value = "배 정보", notes = "배 정보. ")
    @GetMapping("/ship/{ship_id}")
    public ShipResponse shipDetail(
            @RequestHeader(name = "Authorization") String sessionToken,
            @ApiParam(value = "배 id", required = true, example = "0") @PathVariable Long ship_id) {
        return shipService.getShipDetail(ship_id, sessionToken);
    }

//    @ApiOperation(value = "배 정보", notes = "배 정보. ")
//    @GetMapping("/ship/{ship_id}")
//    public ShipResponse shipDetail(@ApiParam(value = "배 id", required = true, example = "0") @PathVariable Long ship_id) {
//        return shipService.getShipDetail(ship_id);
//    }

    @ApiOperation(value = "예약", notes = "예약정보 전화번호는 '-' 포함")
    @PostMapping("/ship/reserve")
    public Long Reserve(
            @RequestHeader(name = "Authorization") String sessionToken,
            ReserveDTO reserveDTO) {
        Member member = memberService.getMemberBySessionToken(sessionToken);
        return shipService.reserve(reserveDTO, member);
    }

//    @ApiOperation(value = "거리계산")
//    @GetMapping("/calc")
//    public String calcDistance() {
//        shipService.calcDistance();
//        return "true";
//    }
}
