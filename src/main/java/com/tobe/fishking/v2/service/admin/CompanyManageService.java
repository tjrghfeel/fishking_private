package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.Company;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.admin.company.CompanyCreateDtoForManage;
import com.tobe.fishking.v2.model.admin.company.CompanyManageDtoForPage;
import com.tobe.fishking.v2.model.admin.company.CompanyModifyDtoForManage;
import com.tobe.fishking.v2.model.admin.company.CompanySearchConditionDto;
import com.tobe.fishking.v2.model.fishing.CompanyDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.fishking.CompanyRepository;
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

    /*업체 목록 검색 메소드*/
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
                dto.getAdtId(),
                dto.getNhnId(),
                dto.getCreatedBy(),
                dto.getCreatedDateStart(),
                dto.getCreatedDateEnd(),
                dto.getShipName(),
//                dto.getSort(),
                pageable
        );
    }

    /*Company의 모든 필드 반환 메소드*/
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

    /*company 생성*/
    @Transactional
    public Long createCompany(CompanyCreateDtoForManage dto, String token) throws Exception {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){throw new RuntimeException("권한이 없습니다.");}
        Member member = memberRepository.findByUid(dto.getMemberUid())
                .orElseThrow(()->new ResourceNotFoundException("member not found for this uid :: "+dto.getMemberUid()));

        /*이미 등록 또는 등록대기중인 company가 있는지 확인*/
        if(companyRepository.existsByMember(member)){throw new RuntimeException("해당 회원은 이미 업체등록을 하였습니다");}

        FileEntity bizNoFileEntity = fileRepository.findById(dto.getBizNoFile() )
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getBizNoFile()));
        FileEntity representFileEntity = fileRepository.findById(dto.getRepresentFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getRepresentFile()));
        FileEntity accountFileEntity = fileRepository.findById(dto.getAccountFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getAccountFile()));

        /*Company 엔터티 등록. */
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
                .adtId(dto.getAdtId())
                .adtPw(dto.getAdtPw())
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

    /*업체 수정*/
    @Transactional
    public Boolean modifyCompany(CompanyModifyDtoForManage dto, String token) throws Exception {
        Member manager = memberService.getMemberBySessionToken(token);
        if(manager.getRoles() != Role.admin){ throw new RuntimeException("권한이 없습니다."); }

        Company company = companyRepository.findById(dto.getId())
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id :: "+dto.getId()));
        Member member = company.getMember();

        /*일단 기존의 파일들을 모두 is_delete로 삭제처리해준뒤, 입력받은 id들에 해당하는 fileEntity들을 saveTemporary로 저장 */
        /*이미지 파일 수정.*/
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

        /*기존 파일엔터티 id들 저장*/
//        Long preBizNoFileId = company.getBizNoFileId().getId();
//        Long preRepresentFileId = company.getRepresentFileId().getId();
//        Long preAccountFileId = company.getAccountFileId().getId();
//
//        /*입력받은 파일 세개 저장. null이 들어올수도있음. */
//        MultipartFile[] fileList = new MultipartFile[3];
//        MultipartFile inputBizNoFile = dto.getBizNoFile();
//        MultipartFile inputRepresentFile = dto.getRepresentFile();
//        MultipartFile inputAccountFile = dto.getAccountFile();
//
//        /*최종적으로 저장할 파일 세개의 변수 */
//        FileEntity bizNoFileEntity = null;
//        FileEntity representFileEntity = null;
//        FileEntity accountFileEntity = null;
//
//        if(inputBizNoFile!=null) {//새로운 파일이 들어왔으면,
//            Long bizNoFileId = companyService.saveFile(16L, inputBizNoFile);//저장후
//            bizNoFileEntity = fileRepository.findById(bizNoFileId)//최종적으로 저장할 파일 변수에 저장.
//                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+bizNoFileId));
//        }
//        else{bizNoFileEntity = fileRepository.findById(preBizNoFileId)//새로운파일이 안들어왔으면, 최종적으로 저장할 파일변수에 기존의 파일 저장.
//                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+preBizNoFileId));}
//        if(inputRepresentFile!=null) {
//            Long representFileId = companyService.saveFile(16L, inputRepresentFile);
//            representFileEntity = fileRepository.findById(representFileId)
//                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+representFileId));
//        }
//        else{representFileEntity = fileRepository.findById(preRepresentFileId)
//                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+preRepresentFileId));}
//        if(inputAccountFile!=null) {
//            Long accountFileId = companyService.saveFile(16L, inputAccountFile);
//            accountFileEntity = fileRepository.findById(accountFileId)
//                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+accountFileId));
//        }
//        else{accountFileEntity = fileRepository.findById(preAccountFileId)
//                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+preAccountFileId));}

        if(dto.getSkbPassword()!=null){dto.setSkbPassword(encoder.encode(dto.getSkbPassword()));}

        FileEntity[] fileEntityList = new FileEntity[3];
        fileEntityList[0] = bizNoFile;
        fileEntityList[1] = representFile;
        fileEntityList[2] = accountFile;
        company.updateCompanyForManage(dto,manager,member,fileEntityList);
        companyRepository.save(company);

//        if(inputBizNoFile!=null)uploadService.removeFileEntity(preBizNoFileId);
//        if(inputRepresentFile!=null)uploadService.removeFileEntity(preRepresentFileId);
//        if(inputAccountFile!=null)uploadService.removeFileEntity(preAccountFileId);

        return true;
    }

    //업체 등록 요청 승인
    @Transactional
    public Boolean acceptRequest(String token, Long companyId) throws ResourceNotFoundException {
        Member manager = memberService.getMemberBySessionToken(token);

        if(manager.getRoles() != Role.admin){throw new RuntimeException("권한이 없습니다.");}

        Company company = companyRepository.findById(companyId)
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id :: "+companyId));
        company.acceptRequest();
        companyRepository.save(company);

        //회원 등급 수정
        Member member = company.getMember();
        if(member.getRoles() == Role.member){//일반회원인 경우에만, 업주회원으로 변경시켜줌.
            member.setRoles(Role.shipowner);
            memberRepository.save(member);
        }

        return true;
    }

    //영업 상태 수정
    @Transactional
    public Boolean setIsOpen(String token, Long companyId, String inputIsOpen) throws ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);

        if(member.getRoles() != Role.admin){throw new RuntimeException("권한이 없습니다.");}

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
