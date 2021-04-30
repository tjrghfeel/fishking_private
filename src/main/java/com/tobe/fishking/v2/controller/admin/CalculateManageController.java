package com.tobe.fishking.v2.controller.admin;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.CalculateManageDtoForPage;
import com.tobe.fishking.v2.model.admin.CalculateSearchConditionDto;
import com.tobe.fishking.v2.service.admin.CalculateManageService;
import com.tobe.fishking.v2.service.fishking.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Api(tags={"정산관리"})
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/v2/api")
public class CalculateManageController {
    private final OrdersService ordersService;
    private final CalculateManageService paidManageService;

    @ApiOperation(value = "정산 리스트 검색")
    @GetMapping("/manage/calculate/list/{page}")
    @ResponseBody
    public Page<CalculateManageDtoForPage> getCalculateList(
            @RequestHeader("Authorization") String token,
            @Valid CalculateSearchConditionDto dto,
            @PathVariable("page") int page
    ) throws ServiceLogicException {
        return paidManageService.getCalculateList(token, dto, page);
    }

    @ApiOperation(value = "정산 처리")
    @PutMapping("/manage/calculate/{shipId}/{year}/{month}/{isCalculated}")
    @ResponseBody
    public Boolean setIsCalculated(
            @RequestHeader("Authorization") String token,
            @PathVariable("shipId") String shipId,
            @PathVariable("year") String year,
            @PathVariable("month") String month,
            @PathVariable("isCalculated") String isCalculated
    ) throws ServiceLogicException, ResourceNotFoundException {
        //인자 검증
        Long shipIdLong = null;
        Boolean isCalculatedBoolean = null;
        Integer yearInt = null;
        Integer monthInt = null;
        try{
            shipIdLong = Long.parseLong(shipId);
            isCalculatedBoolean = Boolean.parseBoolean(isCalculated);
            yearInt = Integer.parseInt(year);
            monthInt = Integer.parseInt(month);
        }
        catch (Exception e){
            throw new ServiceLogicException("url입력값이 잘못되었습니다.");
        }
        return paidManageService.setIsCalculated(shipIdLong, token, String.format("%02d",yearInt), String.format("%02d",monthInt), isCalculatedBoolean);
    }

    @ApiOperation(value = "정산 리스트 엑셀다운로드")
    @GetMapping("/manage/calculate/list/excel")
    public String getCalculateListExcel(
            @RequestHeader("Authorization") String token,
            @Valid CalculateSearchConditionDto dto
    ) throws ServiceLogicException, IOException {
        return "redirect:/" + paidManageService.getCalculateListExcel(token, dto);
    }

    @ApiOperation(value = "정산 상세 엑셀다운로드")
    @GetMapping("/manage/calculate/detail/excel")
    public String getCalculateDetailExcel(
            @RequestHeader("Authorization") String token,
            @RequestParam Long shipId,
            @RequestParam String year,
            @RequestParam String month
    ) throws ServiceLogicException, IOException {
        return "redirect:/" + paidManageService.getCalculateDetailExcel(token, shipId, year, month);
    }
}
