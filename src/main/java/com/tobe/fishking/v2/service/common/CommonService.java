package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.CodeGroupWriteDTO;
import com.tobe.fishking.v2.model.CommonCodeDTO;
import com.tobe.fishking.v2.model.CommonCodeWriteDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.CodeGroupRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonService {
    @Autowired
    CommonCodeRepository commonCodeRepository;
    @Autowired
    CodeGroupRepository codeGroupRepository;
    @Autowired
    MemberRepository memberRepository;

    /*CodeGroup 하나 추가 메소드.*/
    @Transactional
    public Long writeCodeGroup(CodeGroupWriteDTO codeGroupWriteDTO) throws ResourceNotFoundException {
        Member member = memberRepository.findById(codeGroupWriteDTO.getCreatedBy())
                .orElseThrow(()->new ResourceNotFoundException("Member not found for this id :: "+codeGroupWriteDTO.getCreatedBy()));

        CodeGroup codeGroup = CodeGroup.builder()
                .code(codeGroupWriteDTO.getCode())
                .name(codeGroupWriteDTO.getName())
                .description(codeGroupWriteDTO.getDescription())
                .remark(codeGroupWriteDTO.getRemark())
                .createdBy(member)
                .modifiedBy(member)
                .build();

        return codeGroupRepository.save(codeGroup).getId();
    }
    /*CodeGroup 수정 메소드*/
    @Transactional
    public Long updateCodeGroup(CodeGroupWriteDTO codeGroupWriteDTO) throws ResourceNotFoundException {
        CodeGroup codeGroup = codeGroupRepository.findById(codeGroupWriteDTO.getId())
                .orElseThrow(()->new ResourceNotFoundException("CodeGroup not found for this id :: " + codeGroupWriteDTO.getId()));
        Member member = memberRepository.findById(codeGroupWriteDTO.getModifiedBy())
                .orElseThrow(()->new ResourceNotFoundException("Member not found for this id :: "+codeGroupWriteDTO.getModifiedBy()));

        codeGroup.updateCodeGroup(codeGroupWriteDTO, member);

        return codeGroup.getId();
    }

    /*CommonCode를 하나 추가해주는 메소드.
    * 새로 만든 CommonCode Entity의 id를 반환. */
    @Transactional
    public String writeCommonCode(CommonCodeWriteDTO commonCodeWriteDTO) throws ResourceNotFoundException {
        Member member = memberRepository.findById(commonCodeWriteDTO.getCreatedBy())
                .orElseThrow(()->new ResourceNotFoundException("Member not found for this id :: "+commonCodeWriteDTO.getCreatedBy()));
        CodeGroup codeGroup = codeGroupRepository.findById(commonCodeWriteDTO.getCodeGroup())
                .orElseThrow(()->new ResourceNotFoundException("CodeGroup not found for this id :: "+commonCodeWriteDTO.getId()));

        //CommdonCode 엔터티 생성.
        CommonCode commonCode = CommonCode.builder()
                //not null필드.
                .retValue1(commonCodeWriteDTO.getRetValue1())
                .createdBy(member)
                .modifiedBy(member)
                //nullable 필드.
                .code(commonCodeWriteDTO.getCode())
                .codeGroup(codeGroup)
                .codeName(commonCodeWriteDTO.getCodeName())
                .aliasName(commonCodeWriteDTO.getAliasName())
                .extraValue1(commonCodeWriteDTO.getExtraValue1())
                .remark(commonCodeWriteDTO.getRemark())
                //not null & default값 존재 필드.
                .iLevel(commonCodeWriteDTO.getLevel())
                .isActive(commonCodeWriteDTO.isActive())
                .orderBy(commonCodeWriteDTO.getOrderBy())
                .build();

        commonCode = commonCodeRepository.save(commonCode);

        return commonCode.getCode();
    }
    /*Common Code 수정 메소드*/
    @Transactional
    public String updateCommonCode(CommonCodeWriteDTO commonCodeWriteDTO) throws ResourceNotFoundException {
        CommonCode commonCode = commonCodeRepository.findById(commonCodeWriteDTO.getId())
                .orElseThrow(()->new ResourceNotFoundException("CommonCode not found for this id ::"+commonCodeWriteDTO.getId()));
        Member member = memberRepository.findById(commonCodeWriteDTO.getModifiedBy())
                .orElseThrow(()->new ResourceNotFoundException("Member not found for this id :: "+commonCodeWriteDTO.getModifiedBy()));
        CodeGroup codeGroup = codeGroupRepository.findById(commonCodeWriteDTO.getCodeGroup())
                .orElseThrow(()->new ResourceNotFoundException("CodeGroup not found for this id ::"+commonCodeWriteDTO.getId()));

        commonCode.updateCommonCode(commonCodeWriteDTO,member,codeGroup);

        return commonCode.getCode();
    }


    @Transactional
    public List<CommonCodeDTO> getCommonCodeDTOList(Long codeGroupId) throws ResourceNotFoundException {
        List<CommonCodeDTO> commonCodeDTOList = new ArrayList<CommonCodeDTO>();

        CodeGroup codeGroup = codeGroupRepository.findById(codeGroupId)
                .orElseThrow(()->new ResourceNotFoundException("codeGroup not found for this id :: "+codeGroupId));

        List<CommonCode> commonCodeList = commonCodeRepository.findAllByCodeGroup(codeGroup);
        for(int i=0; i<commonCodeList.size(); i++){
            CommonCode commonCode = commonCodeList.get(i);
            CommonCodeDTO commonCodeDTO = new CommonCodeDTO();
            commonCodeDTO.setCode(commonCode.getCode());
            commonCodeDTO.setCodeGroup(commonCode.getCodeGroup());
            commonCodeDTO.setCodeName(commonCode.getCodeName());
            commonCodeDTO.setRemark(commonCode.getRemark());

            commonCodeDTOList.add(commonCodeDTO);
        }

        return commonCodeDTOList;
    }


}
