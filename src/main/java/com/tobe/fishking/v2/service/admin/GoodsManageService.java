package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.model.admin.GoodsManageDtoForPage;
import com.tobe.fishking.v2.model.admin.GoodsSearchConditionDto;
import com.tobe.fishking.v2.repository.fishking.GoodsRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GoodsManageService {
    private final GoodsRepository goodsRepository;
    private final MemberService memberService;

    //상품검색
    @Transactional
    public Page<GoodsManageDtoForPage> getGoodsList(GoodsSearchConditionDto dto, String token, int page ){
        Member member = memberService.getMemberBySessionToken(token);

        if(member.getRoles() != Role.admin){throw new RuntimeException("조회 권한이 없습니다.");}

        //조건들 설정
        Boolean isSpeciesList = false;
        if(dto.getSpeciesList().length!=0){isSpeciesList = true;}

        Pageable pageable = PageRequest.of(page,dto.getPageCount());
        return goodsRepository.getGoodsList(
                dto.getGoodsId(), dto.getGoodsName(), dto.getPriceStart(), dto.getPriceEnd(), dto.getShipName(),
                dto.getFishingDateStart(), dto.getFishingDateEnd(), isSpeciesList, dto.getSpeciesList(), pageable
        );
    }
}
