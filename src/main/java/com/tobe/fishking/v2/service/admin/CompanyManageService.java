package com.tobe.fishking.v2.service.admin;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.fishing.Company;
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

    /*업체 목록 검색 메소드*/
    @Transactional
    public Page<CompanyManageDtoForPage> getCompanyList(CompanySearchConditionDto dto,int page)
            throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        if(dto.getMemberName()!=null) {dto.setMemberName(AES.aesEncode(dto.getMemberName(),env.getProperty("encrypKey.key")));}
        Pageable pageable = PageRequest.of(page,10, JpaSort.unsafe(Sort.Direction.DESC,"("+dto.getSort()+")"));
        return companyRepository.findCompanyListByConditions(
                dto.getCompanyId(),
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
                dto.getCreatedDateStart(),
                dto.getCreatedDateEnd(),
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
    public Long createCompany(CompanyCreateDtoForManage dto) throws Exception {
        Member manager = memberRepository.findById(16L)//!!!!!관리자 로그인 어떻게할지 생각해보고 수정.
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+16L));
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+dto.getMemberId()));

        MultipartFile bizNoFile = dto.getBizNoFile();
        MultipartFile representFile = dto.getRepresentFile();
        MultipartFile accountFile =dto.getAccountFile();

        Long bizNoFileEntityId = companyService.saveFile(member.getId(), bizNoFile);
        Long representFileEntityId = companyService.saveFile(member.getId(), representFile);
        Long accountFileEntityId = companyService.saveFile(member.getId(), accountFile);

        FileEntity bizNoFileEntity = fileRepository.findById(bizNoFileEntityId)
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+bizNoFileEntityId));
        FileEntity representFileEntity = fileRepository.findById(representFileEntityId)
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+representFileEntityId));
        FileEntity accountFileEntity = fileRepository.findById(accountFileEntityId)
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+accountFileEntityId));

        /*Company 엔터티 등록. */
        Company company = Company.builder()
                .companyName(dto.getCompanyName())
                .shipOwner(member.getMemberName())
                .sido(dto.getSido())
                .gungu(dto.getGungu())
                .tel(dto.getTel())
                .bizNo(dto.getBizNo())
                .harbor(dto.getHarbor())
                .bank(dto.getBank())
                .accountNo(dto.getAccountNo())
                .ownerWording(dto.getOwnerWording())
                .isOpen(dto.getIsOpen())
                .skbAccount(dto.getSkbAccount())
                .skbPassword(dto.getSkbPassword())
                .companyAddress(dto.getCompanyAddress())
                .isRegistered(dto.getIsRegistered())
                .bizNoFileId(bizNoFileEntity)
                .representFileId(representFileEntity)
                .accountFileId(accountFileEntity)
                .bizNoFileDownloadUrl("/"+bizNoFileEntity.getFileUrl()+"/"+bizNoFileEntity.getStoredFile())
                .representFileDownloadUrl("/"+representFileEntity.getFileUrl()+"/"+representFileEntity.getStoredFile())
                .accountFileDownloadUrl("/"+accountFileEntity.getFileUrl()+"/"+accountFileEntity.getStoredFile())
                .createdBy(manager)
                .modifiedBy(manager)
                .member(member)
                .build();
        return companyRepository.save(company).getId();
    }

    /*업체 수정*/
    @Transactional
    public Boolean modifyCompany(CompanyModifyDtoForManage dto) throws Exception {
        Member manager = memberRepository.findById(16L)//!!!!!관리자 로그인 어떻게할지 생각해보고 수정.
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+16L));
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id :: "+dto.getMemberId()));
        Company company = companyRepository.findById(dto.getId())
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id :: "+dto.getId()));

        /*기존 파일엔터티 id들 저장*/
        Long preBizNoFileId = company.getBizNoFileId().getId();
        Long preRepresentFileId = company.getRepresentFileId().getId();
        Long preAccountFileId = company.getAccountFileId().getId();

        /*입력받은 파일 세개 저장. null이 들어올수도있음. */
        MultipartFile[] fileList = new MultipartFile[3];
        MultipartFile inputBizNoFile = dto.getBizNoFile();
        MultipartFile inputRepresentFile = dto.getRepresentFile();
        MultipartFile inputAccountFile = dto.getAccountFile();

        /*최종적으로 저장할 파일 세개의 변수 */
        FileEntity bizNoFileEntity = null;
        FileEntity representFileEntity = null;
        FileEntity accountFileEntity = null;

        if(inputBizNoFile!=null) {//새로운 파일이 들어왔으면,
            Long bizNoFileId = companyService.saveFile(16L, inputBizNoFile);//저장후
            bizNoFileEntity = fileRepository.findById(bizNoFileId)//최종적으로 저장할 파일 변수에 저장.
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+bizNoFileId));
        }
        else{bizNoFileEntity = fileRepository.findById(preBizNoFileId)//새로운파일이 안들어왔으면, 최종적으로 저장할 파일변수에 기존의 파일 저장.
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+preBizNoFileId));}
        if(inputRepresentFile!=null) {
            Long representFileId = companyService.saveFile(16L, inputRepresentFile);
            representFileEntity = fileRepository.findById(representFileId)
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+representFileId));
        }
        else{representFileEntity = fileRepository.findById(preRepresentFileId)
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+preRepresentFileId));}
        if(inputAccountFile!=null) {
            Long accountFileId = companyService.saveFile(16L, inputAccountFile);
            accountFileEntity = fileRepository.findById(accountFileId)
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+accountFileId));
        }
        else{accountFileEntity = fileRepository.findById(preAccountFileId)
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+preAccountFileId));}

        if(dto.getSkbPassword()!=null){dto.setSkbPassword(encoder.encode(dto.getSkbPassword()));}

        FileEntity[] fileEntityList = new FileEntity[3];
        fileEntityList[0] = bizNoFileEntity;
        fileEntityList[1] = representFileEntity;
        fileEntityList[2] = accountFileEntity;
        company.updateCompanyForManage(dto,manager,member,fileEntityList);
        companyRepository.save(company);

        if(inputBizNoFile!=null)uploadService.removeFileEntity(preBizNoFileId);
        if(inputRepresentFile!=null)uploadService.removeFileEntity(preRepresentFileId);
        if(inputAccountFile!=null)uploadService.removeFileEntity(preAccountFileId);

        return true;
    }
}
