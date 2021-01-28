package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.fishing.ShipDTO;
import com.tobe.fishking.v2.model.fishing.ShipListResponse;
import com.tobe.fishking.v2.model.fishing.ShipSearchDTO;
import com.tobe.fishking.v2.service.fishking.ShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"선상 및 갯바위"})
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;

    @ApiOperation(value = "배 리스트", notes = "배 리스트. 필수 아닌 값은 빈 문자열 또는 빈 리스트로 보내면 됩니다 ")
    @GetMapping("/ships")
    public Page<ShipListResponse> getShips(ShipSearchDTO shipSearchDTO) {
        return shipService.getShips(shipSearchDTO);
    }
}
