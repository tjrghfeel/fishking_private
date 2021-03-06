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

    /*?????? ?????? ?????? ?????? ?????????. */
    @Transactional
    public Long handleCompanyRegisterReq(CompanyWriteDTO dto, String token) throws Exception {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this sessionToken ::"+token));

        /*?????? ?????? ?????? ?????????????????? company??? ????????? ??????*/
        if(companyRepository.existsByMember(member)){throw new ServiceLogicException("?????? ????????? ?????? ??????????????? ???????????????");}

        FileEntity bizNoFileEntity = fileRepository.findById(dto.getBizNoFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getBizNoFile()));
        FileEntity representFileEntity = fileRepository.findById(dto.getRepresentFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getRepresentFile()));
        FileEntity accountFileEntity = fileRepository.findById(dto.getAccountFile())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getAccountNo()));

        /*Company ????????? ??????. */
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
                .skbAccount(dto.getAdtId())
                .skbPassword((dto.getAdtPw() != null)?HashUtil.sha256(dto.getAdtPw()) : null)
                .companyAddress(dto.getCompanyAddress())
                .isRegistered(false)
                .bizNoFileId(bizNoFileEntity)
                .representFileId(representFileEntity)
                .accountFileId(accountFileEntity)
                .bizNoFileDownloadUrl("/"+bizNoFileEntity.getFileUrl()+"/"+bizNoFileEntity.getStoredFile())
                .representFileDownloadUrl("/"+representFileEntity.getFileUrl()+"/"+representFileEntity.getStoredFile())
                .accountFileDownloadUrl("/"+accountFileEntity.getFileUrl()+"/"+accountFileEntity.getStoredFile())
                .createdBy(member)
                .modifiedBy(member)
                .member(member)
                .adtId(null)
                .adtPw(null)
                .nhnId(dto.getNhnId())
                .nhnPw(dto.getNhnPw())
                .build();
        company = companyRepository.save(company);

        bizNoFileEntity.saveTemporaryFile(company.getId());
        representFileEntity.saveTemporaryFile(company.getId());
        accountFileEntity.saveTemporaryFile(company.getId());

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

        *//*Company ????????? ??????. *//*
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

    /*???????????? ?????? ?????? ?????????*/
    @Transactional
    public Long updateCompanyRegisterReq(CompanyUpdateDTO dto, String token) throws Exception {
        /*????????????????????? ???????????? ?????????.*/
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("member not found for this sessionToken ::" + token));
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("company not found for this id ::" + dto.getCompanyId()));

        ArrayList<Long> deleteFileList = new ArrayList<>();

        FileEntity bizNoFileEntity = null;
        FileEntity representFileEntity = null;
        FileEntity accountFileEntity = null;

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

        FileEntity[] fileEntityList = new FileEntity[3];
        fileEntityList[0] = bizNoFileEntity;
        fileEntityList[1] = representFileEntity;
        fileEntityList[2] = accountFileEntity;
        company.updateCompanyRegisterRequest(dto, member, fileEntityList);
        company = companyRepository.save(company);

        /*??????????????? ??????*/
        for(int i=0; i<deleteFileList.size(); i++){
            uploadService.removeFileEntity(deleteFileList.get(i));
        }

        return company.getId();

        /*?????? ??????????????? id??? ??????*/
        /*Long preBizNoFileId = company.getBizNoFileId().getId();
        Long preRepresentFileId = company.getRepresentFileId().getId();
        Long preAccountFileId = company.getAccountFileId().getId();

        *//*???????????? ?????? ?????? ??????. null??? ?????????????????????. *//*
        MultipartFile[] fileList = new MultipartFile[3];
        MultipartFile inputBizNoFile = dto.getBizNoFile();
        MultipartFile inputRepresentFile = dto.getRepresentFile();
        MultipartFile inputAccountFile = dto.getAccountFile();

        *//*??????????????? ????????? ?????? ????????? ?????? *//*
        FileEntity bizNoFileEntity = null;
        FileEntity representFileEntity = null;
        FileEntity accountFileEntity = null;

        if (inputBizNoFile != null) {//????????? ????????? ???????????????,
            Long bizNoFileId = saveFile(16L, inputBizNoFile);//?????????
            bizNoFileEntity = fileRepository.findById(bizNoFileId)//??????????????? ????????? ?????? ????????? ??????.
                    .orElseThrow(() -> new ResourceNotFoundException("file not found for this id :: " + bizNoFileId));
        } else {
            bizNoFileEntity = fileRepository.findById(preBizNoFileId)//?????????????????? ??????????????????, ??????????????? ????????? ??????????????? ????????? ?????? ??????.
                    .orElseThrow(() -> new ResourceNotFoundException("file not found for this id :: " + preBizNoFileId));
        }
        if (inputRepresentFile != null) {
            Long representFileId = saveFile(16L, inputRepresentFile);
            representFileEntity = fileRepository.findById(representFileId)
                    .orElseThrow(() -> new ResourceNotFoundException("file not found for this id :: " + representFileId));
        } else {
            representFileEntity = fileRepository.findById(preRepresentFileId)
                    .orElseThrow(() -> new ResourceNotFoundException("file not found for this id :: " + preRepresentFileId));
        }
        if (inputAccountFile != null) {
            Long accountFileId = saveFile(16L, inputAccountFile);
            accountFileEntity = fileRepository.findById(accountFileId)
                    .orElseThrow(() -> new ResourceNotFoundException("file not found for this id :: " + accountFileId));
        } else {
            accountFileEntity = fileRepository.findById(preAccountFileId)
                    .orElseThrow(() -> new ResourceNotFoundException("file not found for this id :: " + preAccountFileId));
        }

        if (dto.getSkbPassword() != null) {
            dto.setSkbPassword(encoder.encode(dto.getSkbPassword()));
        }

        FileEntity[] fileEntityList = new FileEntity[3];
        fileEntityList[0] = bizNoFileEntity;
        fileEntityList[1] = representFileEntity;
        fileEntityList[2] = accountFileEntity;
        company.updateCompanyRegisterRequest(dto, member, fileEntityList);
        companyRepository.save(company);

        if (inputBizNoFile != null) uploadService.removeFileEntity(preBizNoFileId);
        if (inputRepresentFile != null) uploadService.removeFileEntity(preRepresentFileId);
        if (inputAccountFile != null) uploadService.removeFileEntity(preAccountFileId);

        return company.getId();
*/
        /*//????????? ???????????? ????????????.
        Long[] preFileIdList = new Long[3];
        preFileIdList[0] = company.getBizNoFileId().getId();
        preFileIdList[1] = company.getAccountFileId().getId();
        preFileIdList[2] = company.getRepresentFileId().getId();

        //????????? ????????? ?????? ??????.
        Long[] fileEntityIdList = saveFile(member.getId(), files);

        //Company????????? ????????????.
        FileEntity[] fileEntityList = new FileEntity[3];
        for(int i=0;i<3;i++){
            Long fileEntityId = fileEntityIdList[i];
            fileEntityList[i] = fileRepository.findById(fileEntityId)
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id ::"+fileEntityId));
        }
        company.updateCompanyRegisterRequest(companyWriteDTO, member, fileEntityList);

        //Company??? ???????????? FileEntity??????.
        uploadService.removeFileEntity(preFileIdList[0]);
        uploadService.removeFileEntity(preFileIdList[1]);
        uploadService.removeFileEntity(preFileIdList[2]);

        return company.getId();*/
    }

    /*Company ?????? ?????? ?????????. */
    @Transactional
    public CompanyDTO getCompany(Long companyId) throws ResourceNotFoundException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id ::"+companyId));
        CompanyDTO companyDTO = new CompanyDTO(company, company.getMember(), env.getProperty("file.downloadUrl"));

        return companyDTO;
    }

    /*???????????? ?????? ????????? ?????? ?????????.
    * Company ????????? ???????????? ???????????????????????? ????????? CompanyRepsonse??? ???????????? ??????????????? ???????????????. */
    @Transactional
    public Page<CompanyListDTO> getCompanyRegisterRequestList(int page) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(page, 10);
        Page<CompanyListDTO> companyRegisterRequestPage = companyRepository.findCompanyRegisterRequestList(false, pageable);

        return companyRegisterRequestPage;
    }

    /*???????????? ?????? ?????????.*/
    @Transactional
    public Long deleteCompanyRegisterRequest(Long companyId, String token) throws ResourceNotFoundException, ServiceLogicException {
        Member member = memberService.getMemberBySessionToken(token);
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id ::"+companyId));

        if(member.getRoles() != Role.admin && member.getId() != company.getMember().getId()){
            throw new ServiceLogicException("????????? ????????????.");
        }
        Long[] fileIdList = new Long[3];
        fileIdList[0] = company.getBizNoFileId().getId();
        fileIdList[1] = company.getAccountFileId().getId();
        fileIdList[2] = company.getRepresentFileId().getId();

        companyRepository.delete(company);

        uploadService.removeFileEntity(fileIdList[0]);
        uploadService.removeFileEntity(fileIdList[1]);
        uploadService.removeFileEntity(fileIdList[2]);

        return companyId;
    }

    /*?????? ?????? ?????? */
    @Transactional
    public Boolean cancelCompanyRegister(String token) throws ResourceNotFoundException, ServiceLogicException {
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("member not found for this token ::"+token));
        Company company = companyRepository.findByMember(member);

        deleteCompanyRegisterRequest(company.getId(), token);
        return true;
    }

    //???????????? ?????? ?????? ??????
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
