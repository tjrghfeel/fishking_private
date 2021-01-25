package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.model.fishing.ShipSearchDTO;
import com.tobe.fishking.v2.service.fishking.ShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"선상 및 갯바위"})
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;

    @ApiOperation(value = "통합 검색 Main Load", notes = "통합 검색 Main Load")
    @GetMapping("/ships")
    public Page<Ship> getShips(ShipSearchDTO shipSearchDTO) {
        return shipService.getShips(shipSearchDTO);
    }
}
