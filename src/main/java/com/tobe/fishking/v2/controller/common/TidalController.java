package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.model.common.ObserverCodeResponse;
import com.tobe.fishking.v2.model.response.TidalLevelResponse;
import com.tobe.fishking.v2.service.common.CommonService;
import com.tobe.fishking.v2.service.fishking.MyMenuService;
import com.tobe.fishking.v2.service.fishking.ShipService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/v2/api")
@RequiredArgsConstructor
public class TidalController {

    private final CommonService commonService;
    private final MyMenuService myMenuService;
    private final ShipService shipService;

    @ApiOperation(value = "날짜, 관측소 코드로 조위 데이터 ", notes = "date: yyyy-MM-dd" +
            "\n [{" +
            "\n     dateTime: 날짜" +
            "\n     level: 조위" +
            "\n     peak: 고조/저조" +
            "\n }, ... ]")
    @GetMapping("/tidalPeak")
    public List<TidalLevelResponse> findAllByDateAndCode(
            @RequestParam("date") String date,
            @RequestParam("code") String code) {
        return commonService.findAllByDateAndCode(date, code);
    }

    @ApiOperation(value = "물때 ", notes = "date: yyyy-MM-dd" +
            "\n date: 날짜" +
            "\n tideTime: 물때 (ex 11물)" +
            "- weather : ArrayList<String> / 날씨. 문자열배열형태로서, 날씨를 나타내는 문자열과 날씨 이미지url이 순서대로 나온다. " +
            "현재를 기준으로 3일후부터 10일까지의 데이터만존재. 없을시 null. 3~7일까지는 오전,오후날씨. 이후로는 하루평균날씨.\n" +
            "ex) 오전/오후 둘다 오는 경우 : [구름많음, localhost:8083/files/common/weather_clouds.svg, 구름많음, localhost:8083/files/common/weather_clouds.svg]\n" +
            "   하루평균만 오는 경우 : [구름많음, localhost:8083/files/common/weather_clouds.svg]\n " +
            "   빈 문자열인 경우에는 빈 값 보여주세요 가져올 수 있는 데이터가 없는 경우입니다")
    @GetMapping("/tideTime")
    public Map<String, Object> getTideTimeFromDate(
            @RequestParam(value = "shipId", required = false) Long shipId,
            @RequestParam("date") String date) throws IOException, ParseException {
        Map<String, Object> result = commonService.findTideTime(date);
        result.put("date", date);
        if (shipId == null) {
            result.put("weather", "");
        } else {
            try {
                ArrayList<String> weather = myMenuService.getWeather(shipService.getObserverCodeFromShip(shipId), date);
                result.put("weather",  weather == null ? null : weather );
            } catch (Exception e) {
                result.put("weather", "");
            }
        }
         return result;
    }

    @ApiOperation(value = "관측소코드 리스트", notes = "관측소 코드 얻기" +
            "\n id: 관측소 id" +
            "\n code: 관측소코드" +
            "\n name: 관측소이름" +
            "\n latitude: 관측소 위도" +
            "\n longitude: 관측소 경도" +
            "\n seaDirection: 서해, 동해, 남해" +
            "\n xgrid: xgrid 값" +
            "\n ygrid: ygrid 값" +
            "")
    @GetMapping("/observers")
    public List<ObserverCodeResponse> getObserver() {
        return commonService.getAllObserverCode();
    }
}
