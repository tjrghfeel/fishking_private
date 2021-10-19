package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.entity.common.ObserverCode;
import com.tobe.fishking.v2.entity.fishing.Harbor;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.ObserverCodeResponse;
import com.tobe.fishking.v2.model.response.TidalLevelResponse;
import com.tobe.fishking.v2.repository.common.ObserverCodeRepository;
import com.tobe.fishking.v2.repository.fishking.HarborRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.service.common.CommonService;
import com.tobe.fishking.v2.service.fishking.MyMenuService;
import com.tobe.fishking.v2.service.fishking.ShipService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    private final HarborRepository harborRepo;
    private final ObserverCodeRepository observerCodeRepo;
    private final ShipRepository shipRepo;


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

    //항구와 연결된 관측소 조위 모든 데이터 리스트 조회
    @ApiOperation(value = "항구와 연결된 관측소 조위 모든 데이터 조회")
    @GetMapping("/allTideList/harbor/{harborId}")
    public List<TidalLevelResponse> getAllTideListForHarbor(@PathVariable("harborId") Long harborId) throws ResourceNotFoundException {
        Harbor harbor = harborRepo.findById(harborId)
                .orElseThrow(()->new ResourceNotFoundException("harbor not found for this id ::"+harborId));
        String observerCode = harbor.getObserverCode();
        ObserverCode observer = observerCodeRepo.getObserverCodeByCode(observerCode);
        Long observerId = observer.getId();

        return commonService.findAllByDateAndCode2(observerId);
    }
    //선박 상세 > 해상 예보, 조위 모든 데이터 리스트 조회
    @ApiOperation(value = "선박 상세 > 해상 예보. 조위 모든 데이터 조회")
    @GetMapping("/allTideList/ship/{shipId}")
    public List<TidalLevelResponse> getAllTideListForShip(@PathVariable("shipId") Long shipId) throws ResourceNotFoundException {
        Ship ship = shipRepo.findById(shipId)
                .orElseThrow(()->new ResourceNotFoundException("ship not found for this id ::"+shipId));
        ObserverCode observer = observerCodeRepo.getObserverCodeByCode(ship.getObserverCode());
        return commonService.findAllByDateAndCode2(observer.getId());
    }
}
