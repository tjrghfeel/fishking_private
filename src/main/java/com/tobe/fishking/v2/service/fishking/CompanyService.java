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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    /*업체 등록 요청 처리 메소드. */
    @Transactional
    public Long handleCompanyRegisterReq(CompanyWriteDTO companyWriteDTO, MultipartFile[] files) throws IOException, ResourceNotFoundException {
        FileEntity[] fileEntityList = new FileEntity[3];

        Member member = memberRepository.findById(companyWriteDTO.getMember())
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+companyWriteDTO.getMember()));

        Long[] fileEntityIdList = saveFile(member.getId(), files);
        for(int i=0;i<3;i++){
            Long fileEntityId = fileEntityIdList[i];
            fileEntityList[i] = fileRepository.findById(fileEntityId)
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id ::"+fileEntityId));
        }

        /*Company 엔터티 등록. */
        Company company = Company.builder()
                .companyName(companyWriteDTO.getCompanyName())
                .shipOwner(companyWriteDTO.getShipOwner())
                .sido(companyWriteDTO.getSido())
                .gungu(companyWriteDTO.getGungu())
                .tel(companyWriteDTO.getTel())
                .bizNo(companyWriteDTO.getBizNo())
                .harbor(companyWriteDTO.getHarbor())
                .bank(companyWriteDTO.getBank())
                .accountNo(companyWriteDTO.getAccountNo())
                .ownerWording(companyWriteDTO.getOwnerWording())
                .companyAddress(companyWriteDTO.getCompanyAddress())
                .bizNoFileId(fileEntityList[0])
                .representFileId(fileEntityList[1])
                .accountFileId(fileEntityList[2])
                .bizNoFileDownloadUrl(fileEntityList[0].getDownloadUrl())
                .representFileDownloadUrl(fileEntityList[1].getDownloadUrl())
                .accountFileDownloadUrl(fileEntityList[2].getDownloadUrl())
                .createdBy(member)
                .modifiedBy(member)
                .member(member)
                .build();
        return companyRepository.save(company).getId();
    }

    /*MultipartServletRequest를 받아 안에들어잇는 파일저장하는 메소드. */
    @Transactional
    Long[] saveFile(Long memberId, MultipartFile[] files) throws ResourceNotFoundException, IOException {
        Long[] fileEntityIdList = new Long[3];//각 증빙서류 FileEntity 저장 후, 그 FileEntity들 저장할 배열.
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+memberId));

        //List<MultipartFile> fileList = files.getFiles("file");
        /*파일 저장. */
        for(int i=0; i<3; i++){//넘어온 파일들에 대해 반복.
            MultipartFile file = files[i];
            //파일 저장.
            Map<String, Object> fileInfo = uploadService.initialFile(file, FileType.image, FilePublish.companyRequest, "");

            //FileEntity 저장.
            FileEntity fileEntity = FileEntity.builder()
                    .originalFile(file.getOriginalFilename())
                    .fileName(file.getOriginalFilename())
                    .fileNo(i)
                    .filePublish(FilePublish.companyRequest)
                    .fileType(FileType.image)
                    .fileUrl((String)fileInfo.get("fileUrl"))
                    .downloadUrl((String)fileInfo.get("fileDownloadUrl"))
                    .thumbnailFile((String)fileInfo.get("thumbUploadPath"))
                    .downloadThumbnailUrl((String)fileInfo.get("thumbDownloadUrl"))
                    .size(file.getSize())
                    .storedFile((String)fileInfo.get("fileName"))
                    .createdBy(member)
                    .modifiedBy(member)
                    .locations("sampleLocation")
                    .build();
            fileEntity = fileRepository.save(fileEntity);
            fileEntityIdList[i] = fileEntity.getId();//Company 엔터이 저장시 FileEntity에 대한 fk로 주기위해 저장.
        }

        return fileEntityIdList;
    }

    /*업체등록 요청 수정 메소드*/
    @Transactional
    public Long updateCompanyRegisterReq(CompanyWriteDTO companyWriteDTO, MultipartFile[] files) throws ResourceNotFoundException, IOException {
        /*다시올린사람이 누구인지 받아옴.*/
        Member member = memberRepository.findById(companyWriteDTO.getMember())
                .orElseThrow(()->new ResourceNotFoundException("member not found for this id ::"+companyWriteDTO.getMember()));

        Company company = companyRepository.findById(companyWriteDTO.getId())
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id ::"+companyWriteDTO.getId()));

        //기존의 파일들을 저장해둠.
        Long[] preFileIdList = new Long[3];
        preFileIdList[0] = company.getBizNoFileId().getId();
        preFileIdList[1] = company.getAccountFileId().getId();
        preFileIdList[2] = company.getRepresentFileId().getId();

        //넘어온 파일들 다시 등록.
        Long[] fileEntityIdList = saveFile(companyWriteDTO.getMember(), files);

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

        return company.getId();
    }

    /*Company 하나 조회 메소드. */
    @Transactional
    public CompanyDTO getCompany(Long companyId) throws ResourceNotFoundException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()->new ResourceNotFoundException("company not found for this id ::"+companyId));
        CompanyDTO companyDTO = new CompanyDTO(company);

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
