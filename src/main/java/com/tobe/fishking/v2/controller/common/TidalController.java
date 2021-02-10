package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.model.response.TidalLevelResponse;
import com.tobe.fishking.v2.service.common.CommonService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v2/api")
@RequiredArgsConstructor
public class TidalController {

    private final CommonService commonService;

    @ApiOperation(value = "날짜, 관측소 코드로 조위 데이터 ", notes = "date: yyyy-MM-dd")
    @GetMapping("/tidalPeak")
    public List<TidalLevelResponse> findAllByDateAndCode(
            @RequestParam("date") String date,
            @RequestParam("code") String code) {
        return commonService.findAllByDateAndCode(date, code);
    }
}
