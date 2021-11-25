package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.fishing.Company;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.fishing.CompanyDTO;
import com.tobe.fishking.v2.model.fishing.CompanyListDTO;
import com.tobe.fishking.v2.model.fishing.CompanyUpdateDTO;
import com.tobe.fishking.v2.model.fishing.CompanyWriteDTO;
import com.tobe.fishking.v2.model.smartfishing.AccountDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.fishking.CompanyRepository;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.utils.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.rmi.server.ServerCloneException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UploadService uploadService;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;
    private final Environment env;
    private final PasswordEncoder encoder;
    private final CommonCodeRepository codeRepository;
    private final MemberService memberService;

    /*업체 등록 요청 처리 메소드. */
    @Transactional
    public Long  handleCompanyRegisterReq(CompanyWriteDTO dto, String token) throws Exception {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+token));

        /*이미 등록 또는 등록대기중인 company가 있는지 확인*/
        if(companyRepository.existsByMember(member)){throw new ServiceLogicException("해당 회원은 이미 업체등록을 하였습니다");}

        FileEntity bizNoFileEntity = fileRepository.findById(dto.getBizNoFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getBizNoFile()));
        FileEntity representFileEntity = fileRepository.findById(dto.getRepresentFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getRepresentFile()));
        FileEntity accountFileEntity = fileRepository.findById(dto.getAccountFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getAccountFile()));
        FileEntity fishingBoatBizFileEntity = null;
        String fishingBoatBizFileUrl = "";
        if(dto.getFishingBoatBizReportFile()!=null){
            fishingBoatBizFileEntity = fileRepository.findById(dto.getFishingBoatBizReportFile())
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getFishingBoatBizReportFile()));
            fishingBoatBizFileUrl = "/"+fishingBoatBizFileEntity.getFileUrl()+"/"+fishingBoatBizFileEntity.getStoredFile();
        }
        FileEntity marineLicenseFileEntity = null;
        String marineLicenseFileUrl = "";
        if(dto.getMarineLicenseFile()!=null){
            marineLicenseFileEntity = fileRepository.findById(dto.getMarineLicenseFile())
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getMarineLicenseFile()));
            marineLicenseFileUrl = "/"+marineLicenseFileEntity.getFileUrl()+"/"+marineLicenseFileEntity.getStoredFile();
        }


        /*Company 엔터티 등록. */
        Company company = Company.builder()
                .companyName(dto.getCompanyName())
                .shipOwner(member.getMemberName())
                .sido(null)
                .gungu(null)
                .tel(dto.getTel())
                .phoneNumber(dto.getPhoneNumber())
                .bizNo(dto.getBizNo())
                .harbor(dto.getHarbor())
                .bank(dto.getBank())
                .accountNo(dto.getAccountNo())
                .ownerWording("")
                .isOpen(false)
//                .skbAccount(dto.getAdtId())
//                .skbPassword((dto.getAdtPw() != null)?HashUtil.sha256(dto.getAdtPw()) : null)
                .companyAddress(dto.getCompanyAddress())
                .isRegistered(false)
                .bizNoFileId(bizNoFileEntity)
                .representFileId(representFileEntity)
                .accountFileId(accountFileEntity)
                .fishingBoatBizReportFileId(fishingBoatBizFileEntity)
                .marineLicenseFileId(marineLicenseFileEntity)
                .bizNoFileDownloadUrl("/"+bizNoFileEntity.getFileUrl()+"/"+bizNoFileEntity.getStoredFile())
                .representFileDownloadUrl("/"+representFileEntity.getFileUrl()+"/"+representFileEntity.getStoredFile())
                .accountFileDownloadUrl("/"+accountFileEntity.getFileUrl()+"/"+accountFileEntity.getStoredFile())
                .fishingBoatBizReportFileDownloadUrl(fishingBoatBizFileUrl)
                .marineLicenseFileDownloadUrl(marineLicenseFileUrl)
                .createdBy(member)
                .modifiedBy(member)
                .member(member)
                .adtId(null)
                .adtPw(null)
//                .nhnId(dto.getNhnId())
//                .nhnPw(dto.getNhnPw())
                .build();
        company = companyRepository.save(company);

        bizNoFileEntity.saveTemporaryFile(company.getId());
        representFileEntity.saveTemporaryFile(company.getId());
        accountFileEntity.saveTemporaryFile(company.getId());
        if(fishingBoatBizFileEntity!=null){fishingBoatBizFileEntity.saveTemporaryFile(company.getId());}
        if(marineLicenseFileEntity!=null){marineLicenseFileEntity.saveTemporaryFile(company.getId());}

        return company.getId();


        /*MultipartFile bizNoFile = dto.getBizNoFile();
        MultipartFile representFile = dto.getRepresentFile();
        MultipartFile accountFile =dto.getAccountFile();

        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+token));

        Long bizNoFileEntityId = saveFile(member.getId(), bizNoFile);
        Long representFileEntityId = saveFile(member.getId(), representFile);
        Long accountFileEntityId = saveFile(member.getId(), accountFile);

        FileEntity bizNoFileEntity = fileRepository.findById(bizNoFileEntityId)
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+bizNoFileEntityId));
        FileEntity representFileEntity = fileRepository.findById(representFileEntityId)
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+representFileEntityId));
        FileEntity accountFileEntity = fileRepository.findById(accountFileEntityId)
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+accountFileEntityId));

        *//*Company 엔터티 등록. *//*
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
//                .isRegistered(dto.getIsRegistered())
                .bizNoFileId(bizNoFileEntity)
                .representFileId(representFileEntity)
                .accountFileId(accountFileEntity)
                .bizNoFileDownloadUrl("/"+bizNoFileEntity.getFileUrl()+"/"+bizNoFileEntity.getStoredFile())
                .representFileDownloadUrl("/"+representFileEntity.getFileUrl()+"/"+representFileEntity.getStoredFile())
                .accountFileDownloadUrl("/"+accountFileEntity.getFileUrl()+"/"+accountFileEntity.getStoredFile())
                .createdBy(member)
                .modifiedBy(member)
                .member(member)
                .build();
        return companyRepository.save(company).getId();*/
    }

    /*업체등록 요청 수정 메소드*/
    @Transactional
    public Long updateCompanyRegisterReq(CompanyUpdateDTO dto, String token) throws Exception {
        /*다시올린사람이 누구인지 받아옴.*/
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("member not found for this sessionToken ::" + token));
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("company not found for this id ::" + dto.getCompanyId()));

        ArrayList<Long> deleteFileList = new ArrayList<>();

        FileEntity bizNoFileEntity = null;
        FileEntity representFileEntity = null;
        FileEntity accountFileEntity = null;
        FileEntity fishingBoatBizReportFileEntity = null;
        FileEntity marineLicenseFileEntity = null;

        if(dto.getBizNoFile() != company.getBizNoFileId().getId()){
            bizNoFileEntity = fileRepository.findById(dto.getBizNoFile())
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getBizNoFile()));
            bizNoFileEntity.saveTemporaryFile(company.getId());
            deleteFileList.add(company.getBizNoFileId().getId());
        }
        else{
            bizNoFileEntity = company.getBizNoFileId();
        }
        if(dto.getAccountFile() != company.getRepresentFileId().getId()){
            accountFileEntity = fileRepository.findById(dto.getAccountFile())
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getAccountFile()));
            accountFileEntity.saveTemporaryFile(company.getId());
            deleteFileList.add(company.getAccountFileId().getId());
        }
        else{
            accountFileEntity = company.getAccountFileId();
        }
        if(dto.getRepresentFile() != company.getAccountFileId().getId()){
            representFileEntity = fileRepository.findById(dto.getRepresentFile())
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getRepresentFile()));
            representFileEntity.saveTemporaryFile(company.getId());
            deleteFileList.add(company.getRepresentFileId().getId());
        }
        else{
            representFileEntity = company.getRepresentFileId();
        }

        if(dto.getFishingBoatBizReportFile() != company.getFishingBoatBizReportFileId().getId()){
            fishingBoatBizReportFileEntity = fileRepository.findById(dto.getFishingBoatBizReportFile())
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getFishingBoatBizReportFile()));
            fishingBoatBizReportFileEntity.saveTemporaryFile(company.getId());
            deleteFileList.add(company.getFishingBoatBizReportFileId().getId());
        }
        else{
            fishingBoatBizReportFileEntity = company.getFishingBoatBizReportFileId();
        }

        if(dto.getMarineLicenseFile() != company.getMarineLicenseFileId().getId()){
            marineLicenseFileEntity = fileRepository.findById(dto.getMarineLicenseFile())
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getMarineLicenseFile()));
            marineLicenseFileEntity.saveTemporaryFile(company.getId());
            deleteFileList.add(company.getMarineLicenseFileId().getId());
        }
        else{
            marineLicenseFileEntity = company.getMarineLicenseFileId();
        }

        FileEntity[] fileEntityList = new FileEntity[5];
        fileEntityList[0] = bizNoFileEntity;
        fileEntityList[1] = representFileEntity;
        fileEntityList[2] = accountFileEntity;
        fileEntityList[3] = fishingBoatBizReportFileEntity;
        fileEntityList[4] = marineLicenseFileEntity;
        company.updateCompanyRegisterRequest(dto, member, fileEntityList);
        company = companyRepository.save(company);

        /*이전파일들 삭제*/
        for(int i=0; i<deleteFileList.size(); i++){
            uploadService.removeFileEntity(deleteFileList.get(i));
        }

        return company.getId();
    }

    /*Company 하나 조회 메소드. */
    @Transactional
    public CompanyDTO getCompany(Long companyId) throws ResourceNotFoundException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id ::"+companyId));
        CompanyDTO companyDTO = new CompanyDTO(company, company.getMember(), env.getProperty("file.downloadUrl"));

        return companyDTO;
    }

    /*업체등록 요청 리스트 조회 메소드.
    * Company 엔터티 리스트를 리포지토리로부터 받아와 CompanyRepsonse에 필드들을 매핑시켜서 반환해준다. */
    @Transactional
    public Page<CompanyListDTO> getCompanyRegisterRequestList(int page) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(page, 10);
        Page<CompanyListDTO> companyRegisterRequestPage = companyRepository.findCompanyRegisterRequestList(false, pageable);

        return companyRegisterRequestPage;
    }

    /*업체등록 삭제 메소드.*/
    @Transactional
    public Long deleteCompanyRegisterRequest(Long companyId, String token) throws ResourceNotFoundException, ServiceLogicException {
        Member member = memberService.getMemberBySessionToken(token);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id ::"+companyId));

        if(member.getRoles() != Role.admin && member.getId() != company.getMember().getId()){
            throw new ServiceLogicException("권한이 없습니다.");
        }
        Long[] fileIdList = new Long[3];
        fileIdList[0] = company.getBizNoFileId().getId();
        fileIdList[1] = company.getAccountFileId().getId();
        fileIdList[2] = company.getRepresentFileId().getId();
        FileEntity fishingBoatBizReportFileEntity = company.getFishingBoatBizReportFileId();
        FileEntity marineLicenseFileEntity = company.getMarineLicenseFileId();

        companyRepository.delete(company);

        uploadService.removeFileEntity(fileIdList[0]);
        uploadService.removeFileEntity(fileIdList[1]);
        uploadService.removeFileEntity(fileIdList[2]);
        if(fishingBoatBizReportFileEntity != null){uploadService.removeFileEntity(fishingBoatBizReportFileEntity.getId());}
        if(marineLicenseFileEntity != null){uploadService.removeFileEntity(marineLicenseFileEntity.getId());}

        return companyId;
    }

    /*업체 등록 취소 */
    @Transactional
    public Boolean cancelCompanyRegister(String token) throws ResourceNotFoundException, ServiceLogicException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("member not found for this token ::"+token));
        Company company = companyRepository.findByMember(member);

        deleteCompanyRegisterRequest(company.getId(), token);
        return true;
    }

    //업체등록 요청 여부 확인
    @Transactional
    public Boolean checkRequestExist(String token){
        Member member = memberService.getMemberBySessionToken(token);
        Company company = companyRepository.findByMember(member);

        if(company != null){return true;}
        else return false;
    }

    public Long findAllByIsRegistered() {
        return companyRepository.findAllByIsRegistered();
    }

    @Transactional
    public Map<String, Object> getAccount(Member member) {
        Company company = companyRepository.findByMember(member);
        String bankName;
        if (company.getBank() == null) {
            bankName = "";
        } else {
            bankName = codeRepository.findCommonCodeByGroupAndCode(164L, company.getBank()).getCodeName();
        }
        companyRepository.save(company);
        Map<String, Object> response = new HashMap<>();
        response.put("bank", bankName);
        response.put("bankCode", company.getBank());
        response.put("accountNum", company.getAccountNo());
        response.put("name", company.getAccountName());
        return response;
    }

    @Transactional
    public void updateAccount(AccountDTO accountDTO, Member member) {
        Company company = companyRepository.findByMember(member);
        company.changeAccount(accountDTO.getBankCode(), accountDTO.getAccountNum(), accountDTO.getName(), member);
        companyRepository.save(company);
    }

    @Transactional
    public Map<String, Object> getAlarmSetting(Member member) {
        Company company = companyRepository.findByMember(member);
        Map<String, Object> response = new HashMap<>();
        response.put("alarm", company.getAlarm());
        return response;
    }

    @Transactional
    public void updateAlarmSetting(Member member, Boolean alarm) {
        Company company = companyRepository.findByMember(member);
        if (alarm) {
            company.useAlarm(member);
        } else {
            company.notUseAlarm(member);
        }
        companyRepository.save(company);
    }

}
