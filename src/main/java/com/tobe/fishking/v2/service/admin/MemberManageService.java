package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.Address;
import com.tobe.fishking.v2.entity.common.PhoneNumber;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import com.tobe.fishking.v2.exception.EmailDupException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.admin.member.MakeTempMemberDto;
import com.tobe.fishking.v2.model.admin.member.MemberDetailDtoForManager;
import com.tobe.fishking.v2.model.admin.member.MemberManageDtoForPage;
import com.tobe.fishking.v2.model.admin.member.MemberSearchConditionDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.auth.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
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
import java.time.LocalDateTime;

@Service
public class MemberManageService {
    @Autowired
    MemberService memberService;
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
//        if(dto.getCity()!=null){dto.setCity(AES.aesEncode(dto.getCity(),key));}
//        if(dto.getGu()!=null){dto.setGu(AES.aesEncode(dto.getGu(),key));}
//        if(dto.getDong()!=null){dto.setDong(AES.aesEncode(dto.getDong(),key));}

        Pageable pageable;
        if(dto.getSort()==null){
            pageable = PageRequest.of(page, dto.getPageCount());
        }
        else{pageable = PageRequest.of(page, dto.getPageCount(),JpaSort.unsafe(Sort.Direction.DESC,"("+dto.getSort()+")"));}

        return memberRepository.findMemberListByConditions(
//                dto.getId(),
                dto.getRoles(),
                dto.getMemberName(),
                dto.getUid(),
                dto.getNickName(),
//                dto.getEmail(),
//                dto.getGender(),
                dto.getIsActive(),
//                dto.getCertifiedNo(),//!!!!!인증번호 필드인데 혹시 암호화필요하면 위에서 다른필드(city,gu,areacode 등)들 암호화하는것처럼 하기.
//                dto.getIsCertified(),
//                dto.getSnsType(),
//                dto.getSnsId(),
//                dto.getCity(),
//                dto.getGu(),
//                dto.getDong(),
                dto.getAreaCode(),
                dto.getLocalNumber(),
//                dto.getJoinDtStart(),
//                dto.getJoinDtEnd(),
                pageable
        );
    }

    /*회원 생성*/
    @Transactional
    public MemberDetailDtoForManager makeMember(MemberDetailDtoForManager dto){
        /*이메일 중복체크.*/
        if(memberRepository.existsByEmail(dto.getEmail())==true){
            /*!!!!!예외처리 이렇게하는게 맞는지? 그냥 던지기만하면 프론트에서 알아서 처리하는지?*/
            throw new EmailDupException("이메일이 중복됩니다");
        }

        /*회원 저장.*/
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
    }

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
                .city((member.getAddress()==null)?null:(member.getAddress().getCity()))
                .gu((member.getAddress()==null)?null:(member.getAddress().getGu()))
                .dong((member.getAddress()==null)?null:(member.getAddress().getDong()))
                .areaCode(member.getPhoneNumber().getAreaCode())
                .localNumber(member.getPhoneNumber().getLocalNumber())
                .build();

        return dto;
    }

    /*회원 수정*/
    @Transactional
    public boolean modifyMember(MemberDetailDtoForManager dto) throws ResourceNotFoundException {
        Member member = memberRepository.findById(dto.getId())
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+dto.getId()));

        member.setUid(dto.getUid());
        member.setMemberName(dto.getMemberName());
        member.setNickName(dto.getNickName());
        if(dto.getPassword()!=null){member.setPassword(encoder.encode(dto.getPassword()));}
        member.setEmail(dto.getEmail());
        member.setGender(Gender.values()[dto.getGender()]);
        member.setRoles(Role.values()[dto.getRoles()]);
        member.setProfileImage(dto.getProfileImage());
        member.setIsActive(dto.getIsActive());
        member.setCertifiedNo(dto.getCertifiedNo());
        member.setIsCertified(dto.getIsCertified());
        member.setJoinDt(dto.getJoinDt());
        member.setSnsType((dto.getSnsType()==null)?null:(SNSType.values()[dto.getSnsType()]));
        member.setSnsId(dto.getSnsId());
        member.setStatusMessage(dto.getStatusMessage());
        member.setAddress(new Address(dto.getCity(),dto.getGu(),dto.getDong()));
        member.setPhoneNumber(new PhoneNumber(dto.getAreaCode(),dto.getLocalNumber()));

        member = memberRepository.save(member);
        return true;
    }

    //세션토큰반환
    @Transactional
    public String getSessionToken(String token, Long memberId){
        Member manager = memberService.getMemberBySessionToken(token);

        if(manager.getRoles() != Role.admin){throw new RuntimeException("관리자가 아닙니다.");}

        Member member = memberService.getMemberById(memberId);
        String sessionToken = member.getSessionToken();
        if(sessionToken == null){
            /*세션토큰 생성 및 저장. */
            String rawToken = member.getUid() + LocalDateTime.now();
            sessionToken = encoder.encode(rawToken);

            System.out.println("token : "+sessionToken);
            member.setSessionToken(sessionToken);
            memberRepository.save(member);
        }

        return sessionToken;
    }

    //임시 회원 생성
    @Transactional
    public Long makeTempMember(MakeTempMemberDto dto, String token){
        return 1L;
    }
}
