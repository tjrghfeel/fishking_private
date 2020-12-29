package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.entity.fishing.Company;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.CompanyDTO;
import com.tobe.fishking.v2.model.fishing.CompanyListDTO;
import com.tobe.fishking.v2.model.fishing.CompanyUpdateDTO;
import com.tobe.fishking.v2.model.fishing.CompanyWriteDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.fishking.CompanyRepository;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UploadService uploadService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    Environment env;
    @Autowired
    PasswordEncoder encoder;


    /*업체 등록 요청 처리 메소드. */
    @Transactional
    public Long handleCompanyRegisterReq(CompanyWriteDTO dto, String token) throws Exception {
        MultipartFile bizNoFile = dto.getBizNoFile();
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
        return companyRepository.save(company).getId();
    }

    /*MultipartServletRequest를 받아 안에들어잇는 파일저장하는 메소드. */
    @Transactional
    public Long saveFile(Long memberId, MultipartFile file) throws Exception {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        //List<MultipartFile> fileList = files.getFiles("file");
        /*파일 저장. */
        if(uploadService.checkFileType(file)!=FileType.image){
            throw new Exception();//!!!!!어떤 예외 던져야 할지.
        }
        Map<String, Object> fileInfo = uploadService.initialFile(file, FilePublish.companyRequest, "");

        //FileEntity 저장.
        FileEntity fileEntity = FileEntity.builder()
                .originalFile(file.getOriginalFilename())
                .fileName(file.getOriginalFilename())
                .fileNo(0)
                .filePublish(FilePublish.companyRequest)
                .fileType(FileType.image)
                .fileUrl((String)fileInfo.get("path"))
//                    .downloadUrl((String)fileInfo.get("fileDownloadUrl"))
                .thumbnailFile((String)fileInfo.get("thumbnailName"))
//                    .downloadThumbnailUrl((String)fileInfo.get("thumbDownloadUrl"))
                .size(file.getSize())
                .storedFile((String)fileInfo.get("fileName"))
                .createdBy(member)
                .modifiedBy(member)
                .locations("sampleLocation")
                .build();
        fileEntity = fileRepository.save(fileEntity);
        return fileEntity.getId();
    }

    /*업체등록 요청 수정 메소드*/
    @Transactional
    public Long updateCompanyRegisterReq(CompanyUpdateDTO dto, MultipartFile[] files,String token) throws Exception {
        /*다시올린사람이 누구인지 받아옴.*/
        Member member = memberRepository.findBySessionToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("member not found for this sessionToken ::" + token));

        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("company not found for this id ::" + dto.getCompanyId()));

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

        if (inputBizNoFile != null) {//새로운 파일이 들어왔으면,
            Long bizNoFileId = saveFile(16L, inputBizNoFile);//저장후
            bizNoFileEntity = fileRepository.findById(bizNoFileId)//최종적으로 저장할 파일 변수에 저장.
                    .orElseThrow(() -> new ResourceNotFoundException("file not found for this id :: " + bizNoFileId));
        } else {
            bizNoFileEntity = fileRepository.findById(preBizNoFileId)//새로운파일이 안들어왔으면, 최종적으로 저장할 파일변수에 기존의 파일 저장.
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

        /*//기존의 파일들을 저장해둠.
        Long[] preFileIdList = new Long[3];
        preFileIdList[0] = company.getBizNoFileId().getId();
        preFileIdList[1] = company.getAccountFileId().getId();
        preFileIdList[2] = company.getRepresentFileId().getId();

        //넘어온 파일들 다시 등록.
        Long[] fileEntityIdList = saveFile(member.getId(), files);

        //Company엔터티 업데이트.
        FileEntity[] fileEntityList = new FileEntity[3];
        for(int i=0;i<3;i++){
            Long fileEntityId = fileEntityIdList[i];
            fileEntityList[i] = fileRepository.findById(fileEntityId)
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id ::"+fileEntityId));
        }
        company.updateCompanyRegisterRequest(companyWriteDTO, member, fileEntityList);

        //Company의 파일들과 FileEntity삭제.
        uploadService.removeFileEntity(preFileIdList[0]);
        uploadService.removeFileEntity(preFileIdList[1]);
        uploadService.removeFileEntity(preFileIdList[2]);

        return company.getId();*/
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

    /*업체등록 요청 취소 메소드.*/
    @Transactional
    public Long deleteCompanyRegisterRequest(Long companyId) throws ResourceNotFoundException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id ::"+companyId));

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

}
