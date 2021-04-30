package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.AddAlertDto;
import com.tobe.fishking.v2.model.common.AlertListForPage;
import com.tobe.fishking.v2.model.common.DeleteAlertDto;
import com.tobe.fishking.v2.service.common.AlertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = {"알람"})
@RequestMapping(value = "/v2/api")
public class AlertController {

    @Autowired
    AlertService alertService;

    /*알람 추가. */
//    @ApiOperation(value = "알람 추가", notes = "" +
//            "- 관리자쪽에서 회원에게 알림 추가하는 api\n" +
//            "- 요청 필드 )\n" +
//            "   memberId : Long / 알람이 추가될 회원id\n" +
//            "   alertType : String / 알람 종류 / couponExpire(\"쿠폰 만료 알림\"), reservationComplete(\"예약완료\")\n" +
//            "   entityType : String / 알림과 관련있는 entity의 종류\n" +
//            "   pid : Long / 알림과 관련있는 entity의 id\n" +
//            "   createdBy : Long / 현재알림을 생성하고있는 회원 id\n" +
//            "   alertTime : String / 알림")
//    @PostMapping("/alert")
//    public Long addAlert(@RequestBody AddAlertDto dto) throws ResourceNotFoundException {
//        return alertService.addAlert(dto);
//    }

    /*알림 삭제*/
    @ApiOperation(value = "알림 삭제",notes = "" +
            "- 해당 알림을 삭제. \n" +
            "- 요청 필드 ) \n" +
            "   alertId : 삭제할 알림의 id")
    @DeleteMapping("/alert")
    public boolean deleteAlert(@RequestBody DeleteAlertDto dto) throws ResourceNotFoundException {
        return alertService.deleteAlert(dto);
    }

    /*현재 회원의 알림 리스트 출력*/
    @ApiOperation(value="현재 회원의 알림 리스트 출력",notes = "" +
            "- 알림리스트를 시간순으로 반환\n" +
            "- 응답 필드 ) \n" +
            "   alertId : 알림 id\n" +
            "   alertType : 알림 종류\n" +
            "   createdDate : 알림 생성일자\n" +
            "   content : 알림 메세지 내용\n" +
            "   iconDownloadUrl : 알림 아이콘 download url")
    @GetMapping("/alert/alertList")
    public Page<AlertListForPage> getAlertList(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "type", required = false) String type) throws ResourceNotFoundException {
        return alertService.getAlertList(token, type);
    }

    /*현재 회원의 알림 수 출력*/
    @ApiOperation(value="현재 회원의 알림 수 출력",notes = "" +
            "   현재 회원의 알림 수 출력")
    @GetMapping("/alert/alertCount")
    public int getAlertCount(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "type", required = false) String type) throws ResourceNotFoundException {
        return alertService.getAlertCount(token, type);
    }

}
