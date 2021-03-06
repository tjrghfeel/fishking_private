package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.auth.RegistrationToken;
import com.tobe.fishking.v2.entity.common.*;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.common.AlertType;
import com.tobe.fishking.v2.enums.fishing.EntityType;
import com.tobe.fishking.v2.enums.fishing.SNSType;
import com.tobe.fishking.v2.exception.EmailDupException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.PushAllDto;
import com.tobe.fishking.v2.model.admin.member.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.auth.RegistrationTokenRepository;
import com.tobe.fishking.v2.repository.common.AlertsRepository;
import com.tobe.fishking.v2.repository.common.CodeGroupRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.FishkingScheduler;
import com.tobe.fishking.v2.service.auth.MemberService;
import lombok.RequiredArgsConstructor;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberManageService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final Environment env;
    private final PasswordEncoder encoder;
    private final CodeGroupRepository codeGroupRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final RegistrationTokenRepository tokenRepository;
    private final AlertsRepository alertsRepository;
    private final FishkingScheduler scheduler;

    /*?????? ?????? ?????? ?????????
    * - MemberSearchConditionDto??? ?????? ???????????? ?????????????????? ?????????????????? ????????? Page??? ???????????????. */
    @Transactional
    public Page<MemberManageDtoForPage> getMemberList(MemberSearchConditionDto dto,int page)
            throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        /*db??? ??????????????? ???????????? ???????????? ?????? native query????????? ?????? ????????? ???????????? ??????.*/
        String key = env.getProperty("encrypKey.key");
//        if(dto.getCity()!=null){dto.setCity(AES.aesEncode(dto.getCity(),key));}
//        if(dto.getGu()!=null){dto.setGu(AES.aesEncode(dto.getGu(),key));}
//        if(dto.getDong()!=null){dto.setDong(AES.aesEncode(dto.getDong(),key));}
        Integer roles = null;
        if(dto.getRoles() != null){
            roles = Role.valueOf(dto.getRoles()).ordinal();
        }

        Pageable pageable;
        if(dto.getSort()==null){
            pageable = PageRequest.of(page, dto.getPageCount());
        }
        else{pageable = PageRequest.of(page, dto.getPageCount(),JpaSort.unsafe(Sort.Direction.DESC,"("+dto.getSort()+")"));}

        return memberRepository.findMemberListByConditions(
//                dto.getId(),
                roles,
                dto.getMemberName(),
                dto.getUid(),
                dto.getNickName(),
//                dto.getEmail(),
//                dto.getGender(),
                dto.getIsActive(),
//                dto.getCertifiedNo(),//!!!!!???????????? ???????????? ?????? ????????????????????? ????????? ????????????(city,gu,areacode ???)??? ???????????????????????? ??????.
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
                dto.getIsSuspended(),
                pageable
        );
    }

    /*?????? ??????*/
    @Transactional
    public MemberDetailDtoForManager makeMember(MemberDetailDtoForManager dto){
        /*????????? ????????????.*/
        if(memberRepository.existsByEmail(dto.getEmail())==true){
            /*!!!!!???????????? ?????????????????? ?????????? ?????? ?????????????????? ??????????????? ????????? ????????????????*/
            throw new EmailDupException("???????????? ???????????????");
        }

        /*?????? ??????.*/
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

    /*?????? ??????(????????????)
    * - */
    @Transactional
    public Long inactivateMemberForManager(Long memberId) throws ResourceNotFoundException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        /*member ????????????. */
        member.deActivateMember();
        return member.getId();
    }

    /*?????? ????????????*/
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

    /*?????? ??????*/
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

    //??????????????????
    @Transactional
    public String getSessionToken(String token, Long memberId){
        Member manager = memberService.getMemberBySessionToken(token);

        if(manager.getRoles() != Role.admin){throw new RuntimeException("???????????? ????????????.");}

        Member member = memberService.getMemberById(memberId);
        String sessionToken = member.getSessionToken();
        if(sessionToken == null){
            /*???????????? ?????? ??? ??????. */
            String rawToken = member.getUid() + LocalDateTime.now();
            sessionToken = encoder.encode(rawToken);

            System.out.println("token : "+sessionToken);
            member.setSessionToken(sessionToken);
            memberRepository.save(member);
        }

        return sessionToken;
    }

    //?????? ?????? ??????
    @Transactional
    public Long makeTempMember(MakeTempMemberDto dto, String token) throws ServiceLogicException {
        Member member = null;

        /*uid ?????? ??????*/
        int checkUid = memberService.checkUidDup(dto.getUid());
        if(checkUid==1){            throw new ServiceLogicException("???????????? ???????????????");        }
        else if(checkUid==2){throw new ServiceLogicException("????????? ????????? ?????? ????????????.");}
        /*????????? ?????? ??????*/
        int checkNickName = memberService.checkNickNameDup(dto.getNickName());
        if(checkNickName==1){ throw new ServiceLogicException("???????????? ???????????????"); }
        else if(checkNickName==2){throw new ServiceLogicException("???????????? 4??? ?????? 10??? ??????????????? ?????????.");}

        /*?????? ?????? ??????*/
        /*???????????? ?????? ?????????*/
        String encodedPw = encoder.encode(dto.getPw());

        //?????? ????????????
        CodeGroup alertSetCodeGroup = codeGroupRepository.findByCode("alertSet");
        List<CommonCode> alertSetList = commonCodeRepository.findAllByCodeGroup(alertSetCodeGroup);
        Set<CommonCode> alertSet = new HashSet<>();
        for(int i=0; i<alertSetList.size(); i++){
            alertSet.add(alertSetList.get(i));
        }
        //????????? ????????????
        CodeGroup videoSettingCodeGroup = codeGroupRepository.findByCode("videoSetting");
        List<CommonCode> videoSettingList = commonCodeRepository.findAllByCodeGroup(videoSettingCodeGroup);
        Set<CommonCode> videoSetting = new HashSet<>();
        for(int i=0; i<videoSettingList.size(); i++){
            videoSetting.add(videoSettingList.get(i));
        }

        CodeGroup codeGroup = codeGroupRepository.findByCode("profileImg");
        CommonCode noProfileImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup, "noImg");
        CommonCode noBackgroundImage = commonCodeRepository.findByCodeGroupAndCode(codeGroup, "noBackImg");

        Role role = Role.valueOf(dto.getRole());

        member = Member.builder()
                .uid(dto.getUid())
                .memberName(null)//?????????. ????????????.
                .nickName(dto.getNickName())
                .password(encodedPw)
                .email(dto.getUid())
//                    .gender(gender)
                .roles(role)
                .profileImage(noProfileImage.getExtraValue1())
                .profileBackgroundImage(noBackgroundImage.getExtraValue1())
                .isActive(true)
                .isCertified(false)
                .isSignedUp(true)
                .snsType(null)
                .snsId(null)
                .alertSet(alertSet)
                .videoSetting(videoSetting)
                .phoneNumber(new PhoneNumber("***","********"))
//                    .phoneNumber(new PhoneNumber(phoneNumber[0],phoneNumber[1]))
                .build();
        member = memberRepository.save(member);

        return member.getId();
    }

    //?????? ??????
    @Transactional
    public Boolean setIsSuspended(String token, Long memberId, String suspend) throws ServiceLogicException {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){throw new ServiceLogicException("????????? ????????????.");}

        Boolean suspendValue = null;
        if(suspend.equals("false")){suspendValue = false;}
        else suspendValue = true;
        Member member = memberService.getMemberById(memberId);
        member.setIsSuspended(suspendValue);
        memberRepository.save(member);

        return true;
    }

    //?????? ?????????
    @Transactional
    public Boolean initialPw(InitialPwDto dto, String token) throws ServiceLogicException {
        String pw = dto.getPw();

        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){throw new ServiceLogicException("????????? ????????? ????????????");}

        Member member = memberService.getMemberById(dto.getMemberId());

        String encodedPw = encoder.encode(pw);
        member.setPassword(encodedPw);
        memberRepository.save(member);

        return true;
    }

    //?????? ??????
    @Transactional
    public Boolean pushAll(PushAllDto dto, String token) throws ServiceLogicException, IOException {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){throw new ServiceLogicException("????????? ????????????");}



        List<RegistrationToken> tokenList = tokenRepository.findAll();
//        Member member = memberService.getMemberById(3710L);
//        List<RegistrationToken> tokenList = tokenRepository.findAllByMember(member);
        for(RegistrationToken item : tokenList){

            Alerts alerts = Alerts.builder()
                    .alertType(AlertType.pushAll)
                    .entityType(EntityType.none)
                    .content(null)
                    .sentence(dto.getContent())
                    .isRead(false)
                    .isSent(false)
                    .receiver(item.getMember())
                    .alertTime(LocalDateTime.now())
                    .type(item.getType())
                    .createdBy(manager)
                    .build();
            alertsRepository.save(alerts);

            scheduler.sendPushAlert(dto.getTitle(), dto.getContent(), alerts, item.getToken());
        }
        return true;
    }
}
