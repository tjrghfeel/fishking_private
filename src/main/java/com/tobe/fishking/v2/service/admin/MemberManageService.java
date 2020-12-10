package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Ad;
import com.tobe.fishking.v2.entity.common.Address;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import com.tobe.fishking.v2.exception.EmailDupException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.admin.MemberDetailDtoForManager;
import com.tobe.fishking.v2.model.admin.MemberManageDtoForPage;
import com.tobe.fishking.v2.model.admin.MemberSearchConditionDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.service.AES;
import javassist.compiler.MemberResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class MemberManageService {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    Environment env;
    @Autowired
    PasswordEncoder encoder;

    /*회원 목록 검색 메소드
    * - MemberSearchConditionDto에 있는 필드들을 리포지토리의 메소드인자로 넘기고 Page를 반환받는다. */
    @Transactional
    public Page<MemberManageDtoForPage> getMemberList(MemberSearchConditionDto dto,int page)
            throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        /*db에 암호화되어 들어가는 필드들에 대해 native query검색을 위해 똑같이 암호화를 해줌.*/
        String key = env.getProperty("encrypKey.key");
        if(dto.getMemberName()!=null){dto.setMemberName(AES.aesEncode(dto.getMemberName(),key));}
        if(dto.getCity()!=null){dto.setCity(AES.aesEncode(dto.getCity(),key));}
        if(dto.getGu()!=null){dto.setGu(AES.aesEncode(dto.getGu(),key));}
        if(dto.getDong()!=null){dto.setDong(AES.aesEncode(dto.getDong(),key));}
        if(dto.getAreaCode()!=null){dto.setAreaCode(AES.aesEncode(dto.getAreaCode(),key));}
        if(dto.getLocalNumber()!=null){dto.setLocalNumber(AES.aesEncode(dto.getLocalNumber(),key));}

        Pageable pageable = PageRequest.of(page, 10);
        return memberRepository.findMemberListByConditions(
                dto.getId(),
                dto.getRole(),
                dto.getMemberName(),
                dto.getUid(),
                dto.getNickName(),
                dto.getEmail(),
                dto.getGender(),
                dto.getIsActive(),
                dto.getSnsType(),
                dto.getSnsId(),
                dto.getCity(),
                dto.getGu(),
                dto.getDong(),
                dto.getAreaCode(),
                dto.getLocalNumber(),
                dto.getJoinDtStart(),
                dto.getJoinDtEnd(),
                pageable
        );
    }

    /*회원 생성*/
    /*@Transactional
    public MemberDetailDtoForManager makeMember(MemberDetailDtoForManager dto){
        *//*이메일 중복체크. *//*
        if(memberRepository.existsByEmail(dto.getEmail())==true){
            *//*!!!!!예외처리 이렇게하는게 맞는지? 그냥 던지기만하면 프론트에서 알아서 처리하는지? *//*
            throw new EmailDupException("이메일이 중복됩니다");
        }

        *//*회원 저장. *//*
        Member member = Member.builder()
                .uid(dto.getUid())
                .memberName(dto.getMemberName())
                .nickName(dto.getNickName())
                .password(encoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .gender(Gender.values()[dto.getGender()])
                .roles(Role.values()[dto.getRoles()])
                .profileImage(dto.getProfileImage())
                .isActive(dto.getIsActive())
                .certifiedNo(dto.getCertifiedNo())
                .isCertified(dto.getIsCertified())
                .joinDt(dto.getJoinDt())
                .snsType((dto.getSnsType()==null)?null:(SNSType.values()[dto.getSnsType()]))
                .snsId(dto.getSnsId())
                .statusMessage(dto.getStatusMessage())
                .address(new Address(dto.getCity(),dto.getGu(),dto.getDong()))
                .phoneNumber(new PhoneNumber(dto.getAreaCode(),dto.getLocalNumber()))
                .build();

        member = memberRepository.save(member);

        dto.setId(member.getId());
        return dto;
    }*/

    /*회원 삭제(비활성화)
    * - */
    @Transactional
    public Long inactivateMemberForManager(Long memberId) throws ResourceNotFoundException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        /*member 비활성화. */
        member.deActivateMember();
        return member.getId();
    }

    /*회원 상세보기*/
    @Transactional
    public MemberDetailDtoForManager showMemberDetail(Long memberId) throws ResourceNotFoundException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+memberId));

        MemberDetailDtoForManager dto = MemberDetailDtoForManager.builder()
                .id(member.getId())
                .uid(member.getUid())
                .memberName(member.getMemberName())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .gender(member.getGender().ordinal())
                .roles(member.getRoles().ordinal())
                .profileImage(member.getProfileImage())
                .isActive(member.getIsActive())
                .certifiedNo(member.getCertifiedNo())
                .isCertified(member.getIsCertified())
                .joinDt(member.getJoinDt())
                .snsType((member.getSnsType()==null)?null:(member.getSnsType().ordinal()))
                .snsId(member.getSnsId())
                .statusMessage(member.getStatusMessage())
                .city(member.getAddress().getCity())
                .gu(member.getAddress().getGu())
                .dong(member.getAddress().getDong())
                .areaCode(member.getPhoneNumber().getAreaCode())
                .localNumber(member.getPhoneNumber().getLocalNumber())
                .build();

        return dto;
    }

}
