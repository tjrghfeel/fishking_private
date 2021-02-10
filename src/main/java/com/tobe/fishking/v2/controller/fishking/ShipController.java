package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.ShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ResponseHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/v2/api")
@Api(tags = {"선상 및 갯바위"})
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;
    private final MemberService memberService;

    @ApiOperation(value = "배 리스트", notes = "배 리스트. 필수 아닌 값은 빈 문자열 또는 빈 리스트로 보내면 됩니다. speciesList, servicesList, facilitiesList, genresList는 무시하시면 됩니다.")
    @GetMapping("/ships/{page}")
    @ResponseBody
    public Page<ShipListResponse> getShips(ShipSearchDTO shipSearchDTO,
                                           @ApiParam(value = "페이지. 0부터 시작", required = true, defaultValue = "0", example = "0") @PathVariable int page,
                                           @RequestParam(value = "species[]", required = false) String[] species,
                                           @RequestParam(value = "services[]", required = false) String[] services,
                                           @RequestParam(value = "facilities[]", required = false) String[] facilities,
                                           @RequestParam(value = "genres[]", required = false) String[] genres) {
        if (species != null) {
            if (species.length != 0 ) {
                shipSearchDTO.setSpeciesList(Arrays.asList(species.clone()));
            }
        }
        if (services != null) {
            if (services.length != 0) {
                shipSearchDTO.setServicesList(Arrays.asList(services.clone()));
            }
        }
        if (facilities != null) {
            if (facilities.length != 0) {
                shipSearchDTO.setFacilitiesList(Arrays.asList(facilities.clone()));
            }
        }
        if (genres != null) {
            if (genres.length != 0) {
                shipSearchDTO.setGenresList(Arrays.asList(genres.clone()));
            }
        }
        return shipService.getShips(shipSearchDTO, page);
    }

    @ApiOperation(value = "배 정보", notes = "배 정보. ")
    @GetMapping("/ship/{ship_id}")
    @ResponseBody
    public ShipResponse shipDetail(
            @RequestHeader(name = "Authorization") String sessionToken,
            @ApiParam(value = "배 id", required = true, example = "0") @PathVariable Long ship_id) {
        return shipService.getShipDetail(ship_id, sessionToken);
    }

    @ApiOperation(value = "배의 상품 리스트", notes = "배의 상품 리스트 ")
    @GetMapping("/ship/{ship_id}/goods")
    @ResponseBody
    public List<GoodsResponse> shipGoods(
            @RequestHeader(name = "Authorization") String sessionToken,
            @ApiParam(value = "배 id", required = true, example = "0") @PathVariable Long ship_id) {
        return shipService.getShipGoods(ship_id);
    }

    @ApiOperation(value = "해당 상품 정보 ", notes = "해당 상품 정보")
    @GetMapping("/goods/{goods_id}")
    @ResponseBody
    public GoodsResponse getGoodsDetail(@PathVariable Long goods_id) {
        return shipService.getGoodsDetail(goods_id);
    }

    @ApiOperation(value = "해당 상품 날짜 승선위치 ", notes = "해당 상품의 날짜 승선위치. date: yyyy-MM-dd")
    @GetMapping("/goods/{goods_id}/position")
    @ResponseBody
    public Map<String, Object> getGoodsDatePositions(@PathVariable Long goods_id, @RequestParam String date) {
        return shipService.getGoodsDatePositions(goods_id, date);
    }

//    @ApiOperation(value = "배 정보", notes = "배 정보. ")
//    @GetMapping("/ship/{ship_id}")
//    public ShipResponse shipDetail(@ApiParam(value = "배 id", required = true, example = "0") @PathVariable Long ship_id) {
//        return shipService.getShipDetail(ship_id);
//    }

    @ApiOperation(value = "예약", notes = "예약정보 전화번호는 '-' 포함. positionsList 는 무시하시면 됩니다.")
    @PostMapping("/ship/reserve")
    public String Reserve(
//            @RequestHeader(name = "Authorization") String sessionToken,
            ReserveDTO reserveDTO,
            @RequestParam(value = "positions[]") Integer[] positions,
            @RequestParam(value = "personsName[]") String[] personsName,
            @RequestParam(value = "personsPhone[]") String[] personsPhone,
            @RequestParam(value = "personsBirthdate[]") String[] personsBirthdate,
            @RequestParam(value = "token", required = false) String token,
            Model model) {
//        Member member = memberService.getMemberBySessionToken(sessionToken);
        if (positions != null) {
            if (positions.length != 0) {
                reserveDTO.setPositionsList(Arrays.asList(positions.clone()));
            }
        }
        OrderResponse response = shipService.reserve(reserveDTO, token, personsName, personsPhone, personsBirthdate);
        model.addAttribute("pay", response);
        return "pay_request";
    }

//    @ApiOperation(value = "거리계산")
//    @GetMapping("/calc")
//    public String calcDistance() {
//        shipService.calcDistance();
//        return "true";
//    }
}
