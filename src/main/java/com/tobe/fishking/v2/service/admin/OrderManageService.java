package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.fishing.OrderStatus;
import com.tobe.fishking.v2.enums.fishing.PayMethod;
import com.tobe.fishking.v2.model.admin.OrderManageDtoForPage;
import com.tobe.fishking.v2.model.admin.OrderSearchConditionDto;
import com.tobe.fishking.v2.repository.fishking.OrdersRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderManageService {

    private final OrdersRepository ordersRepository;
    private final MemberService memberService;

    @Transactional
    public Page<OrderManageDtoForPage> getOrderList(OrderSearchConditionDto dto, String token, int page){
        Member member = memberService.getMemberBySessionToken(token);
        if(member.getRoles() != Role.admin){ throw new RuntimeException("권한이 없습니다."); }

        //인자로 넘길 조건들 데이터 타입변환
        String orderDateStart=null;
        String orderDateEnd=null;
        String fishingDateStart=null;
        String fishingDateEnd=null;
        Integer payMethod = null;
        Integer orderStatus = null;

        if(dto.getOrderDateStart()!=null){orderDateStart = dto.getOrderDateStart().toString();}
        if(dto.getOrderDateEnd()!=null){orderDateEnd = dto.getOrderDateEnd().toString(); }
        if(dto.getFishingDateStart() != null){ fishingDateStart = dto.getFishingDateStart().toString(); }
        if(dto.getFishingDateEnd() != null) { fishingDateEnd = dto.getFishingDateEnd().toString(); }
        if(dto.getPayMethod() != null){ payMethod = PayMethod.valueOf(dto.getPayMethod()).ordinal(); }
        if(dto.getOrderStatus() != null){ orderStatus = OrderStatus.valueOf(dto.getOrderStatus()).ordinal(); }

        Pageable pageable = PageRequest.of(page, dto.getPageCount());

        return ordersRepository.getOrderList(
                dto.getOrderId(),
                dto.getOrderDetailId(),
                orderDateStart,
                orderDateEnd,
                fishingDateStart,
                fishingDateEnd,
                dto.getTotalAmountStart(),
                dto.getTotalAmountEnd(),
                dto.getDiscountAmountStart(),
                dto.getDiscountAmountEnd(),
                dto.getPaymentAmountStart(),
                dto.getPaymentAmountEnd(),
                dto.getIsPay(),
                payMethod,
                orderStatus,
                dto.getGoodsName(),
                dto.getMemberName(),
                dto.getMemberAreaCode(),
                dto.getMemberLocalNumber(),
                dto.getShipName(),
                pageable
        );
    }

}
