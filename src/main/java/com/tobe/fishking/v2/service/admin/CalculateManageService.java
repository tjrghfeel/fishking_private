package com.tobe.fishking.v2.service.admin;

import com.querydsl.core.Tuple;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Calculate;
import com.tobe.fishking.v2.entity.fishing.Company;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.CalculateManageDtoForPage;
import com.tobe.fishking.v2.model.admin.CalculateSearchConditionDto;
import com.tobe.fishking.v2.model.smartfishing.CalculateDetailResponse;
import com.tobe.fishking.v2.repository.fishking.CalculateRepository;
import com.tobe.fishking.v2.repository.fishking.CompanyRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.utils.DateUtils;
import com.tobe.fishking.v2.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalculateManageService {
    private final ShipRepository shipRepository;
    private final CalculateRepository calculateRepository;
    private final MemberService memberService;
    private final CompanyRepository companyRepository;

    @Transactional
    public Page<CalculateManageDtoForPage> getCalculateList(String token, CalculateSearchConditionDto dto, int page) throws ServiceLogicException {

//        List<Tuple> ships = shipRepository.getGoodsShips(memberId);
//        result = calculateRepository.searchCalculateForManage(memberId, "", year, month, isCalculate, pageable);
//        List<CalculateResponse> calcs = calculateRepository.searchCalculate(memberId, shipName, year, month, isCalculate);
//        List<Long> ids = calcs.stream().map(CalculateResponse::getShipId).collect(Collectors.toList());
//        for (Tuple tuple : ships) {
//            Long id = tuple.get(0, Long.class);
//            String name = tuple.get(1, String.class);
//            if (!ids.contains(id)) {
//                calcs.add(new CalculateResponse(id, name, 0L, 0L));
//            }
//        }
//        Map<String, Object> response = new HashMap<>();
//        response.put("year", year);
//        response.put("month", month);
//        response.put("content", calcs);
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles()!= Role.admin){throw new ServiceLogicException("권한이 없습니다.");}

        String yearStart = null;
        String monthStart = null;
        String yearEnd = null;
        String monthEnd = null;
        if(dto.getDateStart() != null){
            yearStart = dto.getDateStart().split("-")[0];
            monthStart = dto.getDateStart().split("-")[1];
        }
        if(dto.getDateEnd() != null){
            yearEnd = dto.getDateEnd().split("-")[0];
            monthEnd = dto.getDateEnd().split("-")[1];
        }

        Pageable pageable = PageRequest.of(page, dto.getPageCount());
        return calculateRepository.getCalculateList(
            dto.getShipName(), dto.getCompanyName(), yearStart, yearEnd, monthStart,monthEnd,
                dto.getTotalAmountStart(), dto.getTotalAmountEnd(), dto.getIsCalculated(), pageable
        );
    }

    //정산 처리
    @Transactional
    public Boolean setIsCalculated(Long shipId, String token, String year, String month, Boolean isCalculated) throws ServiceLogicException, ResourceNotFoundException {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){throw new ServiceLogicException("권한이 없습니다.");}
        Ship ship = shipRepository.findById(shipId)
                .orElseThrow(()->new ResourceNotFoundException("ship not found for this id :: "+shipId));

        List<Calculate> list = calculateRepository.findAllByShipAndYearAndMonth(ship, year, month);

        if(isCalculated){
            for(Calculate item : list){
                item.setCalculate(manager);
            }
        }
        else{
            for(Calculate item : list){
                item.setUnCalculate(manager);
            }
        }

        calculateRepository.saveAll(list);

        return true;
    }

    @Transactional
    public String getCalculateListExcel(String token, CalculateSearchConditionDto dto) throws ServiceLogicException, IOException {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles()!= Role.admin){throw new ServiceLogicException("권한이 없습니다.");}
        Company company = companyRepository.findByMember(manager);
//        String now = DateUtils.getDateTimeInFormat(LocalDateTime.now());
        String now  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        now = now.replace(" ","");

        String yearStart = null;
        String monthStart = null;
        String yearEnd = null;
        String monthEnd = null;
        if(dto.getDateStart() != null){
            yearStart = dto.getDateStart().split("-")[0];
            monthStart = dto.getDateStart().split("-")[1];
        }
        if(dto.getDateEnd() != null){
            yearEnd = dto.getDateEnd().split("-")[0];
            monthEnd = dto.getDateEnd().split("-")[1];
        }

        List<Map<String, String>> data = calculateRepository.getCalculateListForExcel(
                dto.getShipName(), dto.getCompanyName(), yearStart, yearEnd, monthStart,monthEnd,
                dto.getTotalAmountStart(), dto.getTotalAmountEnd(), dto.getIsCalculated()
        );
        String[] headers = {"업체명", "선박명", "정산월", "예약금액", "취소금액", "정산금액", "정산여부"};
        String[] headersEn = {"companyName", "shipName", "yearmonth", "amount", "cancelAmount", "totalAmount", "isCalculated"};

        String fileName = ExcelUtil.getExcelFromList(data, headers, headersEn, company.getCompanyName()+"_"+now) + ".xlsx";
        return "/resource/manage" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
//        return "C:"+ File.separator+File.separator+"Users"+File.separator+"kai"+File.separator+"Desktop"+File.separator+""+fileName;
    }

    @Transactional
    public String getCalculateDetailExcel(String token,
                                          Long shipId,
                                          String year,
                                          String month) throws ServiceLogicException, IOException {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles()!= Role.admin){throw new ServiceLogicException("권한이 없습니다.");}
        Ship ship = shipRepository.getOne(shipId);
//        String now = DateUtils.getDateTimeInFormat(LocalDateTime.now());
        String now  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        List<Map<String, String>> calcs = calculateRepository.calculateDetailForExcel(shipId, year, month);

        String[] headers = {"결제일", "주문자", "주문상품", "인원", "결제금액 (원)"};
        String[] headersEn = {"orderDate", "memberName", "name", "personnel", "payAmount"};

        String fileName = ExcelUtil.getExcelFromList(calcs, headers, headersEn, ship.getShipName() + "_"+ year + "-" + month + "_" + now) + ".xlsx";
        return "/resource/manage" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
//        return "C:"+ File.separator+File.separator+"Users"+File.separator+"kai"+File.separator+"Desktop"+File.separator+""+fileName;
    }
}
