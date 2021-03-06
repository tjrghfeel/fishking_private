package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Company;
import com.tobe.fishking.v2.entity.fishing.TblSubmitQueue;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.company.CompanyCreateDtoForManage;
import com.tobe.fishking.v2.model.admin.company.CompanyManageDtoForPage;
import com.tobe.fishking.v2.model.admin.company.CompanyModifyDtoForManage;
import com.tobe.fishking.v2.model.admin.company.CompanySearchConditionDto;
import com.tobe.fishking.v2.model.fishing.CompanyDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.fishking.CompanyRepository;
import com.tobe.fishking.v2.repository.fishking.TblSubmitQueueRepository;
import com.tobe.fishking.v2.service.AES;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class CompanyManageService {

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    Environment env;
    @Autowired
    UploadService uploadService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    CompanyService companyService;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    MemberService memberService;
    @Autowired
    TblSubmitQueueRepository tblSubmitQueueRepository;

    /*?????? ?????? ?????? ?????????*/
    @Transactional
    public Page<CompanyManageDtoForPage> getCompanyList(CompanySearchConditionDto dto,int page)
            throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {



        Pageable pageable = PageRequest.of(page, dto.getPageCount(), JpaSort.unsafe(Sort.Direction.DESC,"("+dto.getSort()+")"));
        return companyRepository.findCompanyListByConditions(
                dto.getCompanyId(),
                dto.getMemberId(),
                dto.getMemberName(),
                dto.getMemberNickName(),
                dto.getAreaCode(),
                dto.getLocalNumber(),
                dto.getCompanyName(),
                dto.getShipOwner(),
                dto.getSido(),
                dto.getGungu(),
                dto.getTel(),
                dto.getPhoneNumber(),
                dto.getBizNo(),
                dto.getHarbor(),
                dto.getAccountNum(),
                dto.getBank(),
                dto.getIsOpen(),
                dto.getSkbAccount(),
                dto.getCompanyAddress(),
                dto.getIsRegistered(),
                dto.getNhnId(),
                dto.getCreatedBy(),
                dto.getCreatedDateStart(),
                dto.getCreatedDateEnd(),
                dto.getShipName(),
//                dto.getSort(),
                pageable
        );
    }

    /*Company??? ?????? ?????? ?????? ?????????*/
    @Transactional
    public CompanyDTO getCompanyDetail(Long companyId) throws ResourceNotFoundException {
        return companyService.getCompany(companyId);

        /*Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("company not found for this id :: "+companyId));
        Member member = memberRepository.findById(company.getMember().getId())
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+company.getMember().getId()));

        CompanyDTO companyDTO = new CompanyDTO(company, member, env.getProperty("file.downloadUrl"));
        return companyDTO;*/
    }

    /*company ??????*/
    @Transactional
    public Long createCompany(CompanyCreateDtoForManage dto, String token) throws Exception {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){throw new RuntimeException("????????? ????????????.");}
        Member member = memberRepository.findByUid(dto.getMemberUid())
                .orElseThrow(()->new ResourceNotFoundException("member not found for this uid :: "+dto.getMemberUid()));

        /*?????? ?????? ?????? ?????????????????? company??? ????????? ??????*/
        if(companyRepository.existsByMember(member)){throw new RuntimeException("?????? ????????? ?????? ??????????????? ???????????????");}

        FileEntity bizNoFileEntity = fileRepository.findById(dto.getBizNoFile() )
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getBizNoFile()));
        FileEntity representFileEntity = fileRepository.findById(dto.getRepresentFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getRepresentFile()));
        FileEntity accountFileEntity = fileRepository.findById(dto.getAccountFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getAccountFile()));

        /*Company ????????? ??????. */
        Company company = Company.builder()
                .member(member)
                .companyName(dto.getCompanyName())
                .shipOwner(member.getMemberName())
                .sido(dto.getSido())
                .gungu(dto.getGungu())
                .tel(dto.getTel())
                .phoneNumber(dto.getPhoneNumber())
                .bizNo(dto.getBizNo())
                .harbor(dto.getHarbor())
                .bank(dto.getBank())
                .accountNo(dto.getAccountNo())
                .ownerWording(dto.getOwnerWording())
                .isOpen(false)
                .skbAccount(dto.getSkbAccount())
                .skbPassword(dto.getSkbPassword())
                .companyAddress(dto.getCompanyAddress())
                .isRegistered(false)
                .nhnId(dto.getNhnId())
                .nhnPw(dto.getNhnPw())
                .bizNoFileId(bizNoFileEntity)
                .representFileId(representFileEntity)
                .accountFileId(accountFileEntity)
                .bizNoFileDownloadUrl("/"+bizNoFileEntity.getFileUrl()+"/"+bizNoFileEntity.getStoredFile())
                .representFileDownloadUrl("/"+representFileEntity.getFileUrl()+"/"+representFileEntity.getStoredFile())
                .accountFileDownloadUrl("/"+accountFileEntity.getFileUrl()+"/"+accountFileEntity.getStoredFile())
                .createdBy(manager)
                .modifiedBy(manager)
                .build();
        return companyRepository.save(company).getId();
    }

    /*?????? ??????*/
    @Transactional
    public Boolean modifyCompany(CompanyModifyDtoForManage dto, String token) throws Exception {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){ throw new RuntimeException("????????? ????????????."); }

        Company company = companyRepository.findById(dto.getId())
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id :: "+dto.getId()));
        Member member = company.getMember();

        /*?????? ????????? ???????????? ?????? is_delete??? ?????????????????????, ???????????? id?????? ???????????? fileEntity?????? saveTemporary??? ?????? */
        /*????????? ?????? ??????.*/
        List<FileEntity> preFileList = fileRepository.findByPidAndFilePublishAndFileTypeAndIsDelete(
                company.getId(),FilePublish.companyRequest, FileType.image, false);
        for(int i=0; i<preFileList.size(); i++){
            preFileList.get(i).setIsDelete(true);
        }
        FileEntity bizNoFile = fileRepository.findById(dto.getBizNoFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getBizNoFile()));
        bizNoFile.saveTemporaryFile(company.getId());
        FileEntity representFile = fileRepository.findById(dto.getRepresentFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getRepresentFile()));
        representFile.saveTemporaryFile(company.getId());
        FileEntity accountFile = fileRepository.findById(dto.getAccountFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getAccountFile()));
        accountFile.saveTemporaryFile(company.getId());

        if(dto.getSkbPassword()!=null){dto.setSkbPassword(encoder.encode(dto.getSkbPassword()));}

        FileEntity[] fileEntityList = new FileEntity[3];
        fileEntityList[0] = bizNoFile;
        fileEntityList[1] = representFile;
        fileEntityList[2] = accountFile;
        company.updateCompanyForManage(dto,manager,member,fileEntityList);
        companyRepository.save(company);

        return true;
    }

    //?????? ?????? ?????? ??????
    @Transactional
    public Boolean acceptRequest(String token, Long companyId) throws ResourceNotFoundException {
        Member manager = memberService.getMemberBySessionToken(token);

        if(manager.getRoles() != Role.admin){throw new RuntimeException("????????? ????????????.");}

        Company company = companyRepository.findById(companyId)
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id :: "+companyId));
        company.acceptRequest();
        company.setIsOpen(true);
        companyRepository.save(company);

        //?????? ?????? ??????
        Member member = company.getMember();
        if(member.getRoles() == Role.member){//??????????????? ????????????, ?????????????????? ???????????????.
            member.setRoles(Role.shipowner);
            memberRepository.save(member);
        }

        return true;
    }
    //?????? ?????? ?????? ??????
    @Transactional
    public Boolean rejectRequest(String token, Long companyId) throws ResourceNotFoundException, ServiceLogicException {
        Member member = memberService.getMemberBySessionToken(token);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id :: "+companyId));

        if(member.getRoles() != Role.admin){throw new ServiceLogicException("????????? ????????????.");}
        if(company.getIsRegistered() == true) { return false; }//?????? ????????? ??? ???????????? ??????????????? ????????????.

        /* ?????? ??????. */
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String requestDate = company.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String content = "[????????????????????????]\n "+requestDate+"????????? "+company.getCompanyName()+"?????? ???????????? ??????????????? ?????????????????????. \n" +
                "????????? ????????? 1566-2996?????? ??????????????????";
        TblSubmitQueue tblSubmitQueue = TblSubmitQueue.builder()
                .usrId("6328")
                .smsGb("1")
                .usedCd("10")
                .reservedFg("I")
                .reservedDttm(time)
                .savedFg("0")
                .rcvPhnId(company.getPhoneNumber())
                .sndPhnId("0612778002")
                .sndMsg(content)
                .contentCnt(1)
                .smsStatus("0")
                .contentMimeType("text/plain")
                .build();
        tblSubmitQueueRepository.save(tblSubmitQueue);

        //company????????? ??????
        companyService.deleteCompanyRegisterRequest(companyId, token);
        return true;
    }

    //?????? ?????? ??????
    @Transactional
    public Boolean setIsOpen(String token, Long companyId, String inputIsOpen) throws ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);

        if(member.getRoles() != Role.admin){throw new RuntimeException("????????? ????????????.");}

        Company company = companyRepository.findById(companyId)
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id :: "+companyId));
        Boolean isOpen = null;
        if(inputIsOpen.equals("true")){isOpen=true;}
        else{isOpen = false;}

        company.setIsOpen(isOpen);
        companyRepository.save(company);

        return true;
    }
}
