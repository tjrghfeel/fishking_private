package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.model.admin.company.CompanyManageDtoForPage;
import com.tobe.fishking.v2.model.admin.company.CompanySearchConditionDto;
import com.tobe.fishking.v2.repository.fishking.CompanyRepository;
import com.tobe.fishking.v2.service.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class CompanyManageService {

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    Environment env;

    /*업체 목록 검색 메소드*/
    @Transactional
    public Page<CompanyManageDtoForPage> getCompanyList(CompanySearchConditionDto dto,int page)
            throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if(dto.getMemberName()!=null) {dto.setMemberName(AES.aesEncode(dto.getMemberName(),env.getProperty("encrypKey.key")));}
        Pageable pageable = PageRequest.of(page,10, JpaSort.unsafe(Sort.Direction.DESC,"("+dto.getSort()+")"));
        return companyRepository.findCompanyListByConditions(
                dto.getId(),
                dto.getMemberId(),
                dto.getMemberName(),
                dto.getCompanyName(),
                dto.getSido(),
                dto.getGungu(),
                dto.getTel(),
                dto.getBizNo(),
                dto.getBank(),
                dto.getAccountNum(),
                dto.getHarbor(),
                dto.getOwnerWording(),
                dto.getIsOpen(),
                dto.getSkbAccount(),
                dto.getCompanyAddress(),
                dto.getIsRegistered(),
                dto.getCreatedDayStart(),
                dto.getCreatedDayEnd(),
//                dto.getSort(),
                pageable
        );
    }

}
