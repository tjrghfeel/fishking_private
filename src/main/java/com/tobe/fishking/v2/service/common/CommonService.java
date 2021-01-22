package com.tobe.fishking.v2.service.common;

import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.model.common.FilePreUploadResponseDto;
import com.tobe.fishking.v2.model.common.FilesDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.PopularRepository;
import lombok.RequiredArgsConstructor;
import org.jcodec.api.JCodecException;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.CodeGroupWriteDTO;
import com.tobe.fishking.v2.model.CommonCodeDTO;
import com.tobe.fishking.v2.model.CommonCodeWriteDTO;
import com.tobe.fishking.v2.repository.common.CodeGroupRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final CommonCodeRepository commonCodeRepo;
    private final CodeGroupRepository codeGroupRepo;
    private final MemberRepository memberRepo;
    private final FileRepository fileRepo;
    private final PopularRepository popularRepo;
    private final UploadService uploadService;
    private final Environment env;

    //검색 --
    public Page<FilesDTO> getFilesList(Pageable pageable,
                                       @RequestParam(required = false) Map<String, Object> searchRequest
            , Integer totalElement) {


        //popularRepo.save(new Popular(SearchPublish.FISHINGDIARY,  (String)searchRequest.get(key), memberRepo.getOne((long)5)));

        Page<FileEntity> files = null;
        //member 가져오기 jkkim
        for (String key : searchRequest.keySet()) {

            files = searchRequest.isEmpty()
                    ? fileRepo.findAll(pageable, totalElement)
                    : fileRepo.findAllFilesWithPaginationNative((String)searchRequest.get(key), pageable);


            popularRepo.save(new Popular(SearchPublish.TOTAL, (String)searchRequest.get(key), memberRepo.getOne((long)5)));
        }


        return files.map(FilesDTO::of);


    }

    /*CodeGroup 하나 추가 메소드.*/
    @Transactional
    public Long writeCodeGroup(CodeGroupWriteDTO codeGroupWriteDTO, String sessionToken) throws ResourceNotFoundException {
        Member member = memberRepo.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("Member not found for this sessionToken :: "+sessionToken));


        CodeGroup codeGroup = CodeGroup.builder()
                .code(codeGroupWriteDTO.getCode())
                .name(codeGroupWriteDTO.getName())
                .description(codeGroupWriteDTO.getDescription())
                .remark(codeGroupWriteDTO.getRemark())
                .createdBy(member)
                .modifiedBy(member)
                .build();

        return codeGroupRepo.save(codeGroup).getId();
    }
    /*CodeGroup 수정 메소드*/
    @Transactional
    public Long updateCodeGroup(CodeGroupWriteDTO codeGroupWriteDTO,String sessionToken) throws ResourceNotFoundException {
        CodeGroup codeGroup = codeGroupRepo.findById(codeGroupWriteDTO.getId())
                .orElseThrow(()->new ResourceNotFoundException("CodeGroup not found for this id :: " + codeGroupWriteDTO.getId()));
        Member member = memberRepo.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("Member not found for this sessionToken :: "+sessionToken));


        codeGroup.updateCodeGroup(codeGroupWriteDTO, member);

        return codeGroup.getId();
    }


    /*CommonCode를 하나 추가해주는 메소드.
    * 새로 만든 CommonCode Entity의 id를 반환. */
    @Transactional
    public String writeCommonCode(CommonCodeWriteDTO commonCodeWriteDTO,String sessionToken) throws ResourceNotFoundException {
        Member member = memberRepo.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("Member not found for this sessionToken :: "+sessionToken));
        CodeGroup codeGroup = codeGroupRepo.findById(commonCodeWriteDTO.getCodeGroup())
                .orElseThrow(()->new ResourceNotFoundException("CodeGroup not found for this id :: "+commonCodeWriteDTO.getId()));


        //CommdonCode 엔터티 생성.
        CommonCode commonCode = CommonCode.builder()
                //not null필드.
                .retValue1(commonCodeWriteDTO.getRetValue1())
                .createdBy(member)
                .modifiedBy(member)
                //nullable 필드.
                .code(commonCodeWriteDTO.getCode())
                .codeGroup(codeGroup)
                .codeName(commonCodeWriteDTO.getCodeName())
                .aliasName(commonCodeWriteDTO.getAliasName())
                .extraValue1(commonCodeWriteDTO.getExtraValue1())
                .remark(commonCodeWriteDTO.getRemark())
                //not null & default값 존재 필드.
                .iLevel(commonCodeWriteDTO.getLevel())
                .isActive(commonCodeWriteDTO.getActive())
                .orderBy(commonCodeWriteDTO.getOrderBy())
                .build();

        commonCode = commonCodeRepo.save(commonCode);

        return commonCode.getCode();
    }
    /*Common Code 수정 메소드*/
    @Transactional
    public String updateCommonCode(CommonCodeWriteDTO commonCodeWriteDTO,String sessionToken) throws ResourceNotFoundException {
        CommonCode commonCode = commonCodeRepo.findById(commonCodeWriteDTO.getId())
                .orElseThrow(()->new ResourceNotFoundException("CommonCode not found for this id ::"+commonCodeWriteDTO.getId()));
        Member member = memberRepo.findBySessionToken(sessionToken)
                .orElseThrow(()->new ResourceNotFoundException("Member not found for this sessionToken :: "+sessionToken));
        CodeGroup codeGroup = codeGroupRepo.findById(commonCodeWriteDTO.getCodeGroup())
                .orElseThrow(()->new ResourceNotFoundException("CodeGroup not found for this id ::"+commonCodeWriteDTO.getId()));


        commonCode.updateCommonCode(commonCodeWriteDTO,member,codeGroup);

        return commonCode.getCode();
    }


    @Transactional
    public List<CommonCodeDTO> getCommonCodeDTOList(Long codeGroupId) throws ResourceNotFoundException {
        List<CommonCodeDTO> commonCodeDTOList = new ArrayList<CommonCodeDTO>();

        CodeGroup codeGroup = codeGroupRepo.findById(codeGroupId)
                .orElseThrow(()->new ResourceNotFoundException("codeGroup not found for this id :: "+codeGroupId));

        List<CommonCode> commonCodeList = commonCodeRepo.findAllByCodeGroup(codeGroup);
        for(int i=0; i<commonCodeList.size(); i++){
            CommonCode commonCode = commonCodeList.get(i);
            CommonCodeDTO commonCodeDTO = new CommonCodeDTO();
            commonCodeDTO.setId(commonCode.getId());
            commonCodeDTO.setCode(commonCode.getCode());
            commonCodeDTO.setCodeGroup(commonCode.getCodeGroup());
            commonCodeDTO.setCodeName(commonCode.getCodeName());
            commonCodeDTO.setExtraValue1(commonCode.getExtraValue1());
            commonCodeDTO.setRemark(commonCode.getRemark());

            commonCodeDTOList.add(commonCodeDTO);
        }

        return commonCodeDTOList;
    }

    @Transactional
    public FilePreUploadResponseDto preUploadFile(MultipartFile file, String filePublish, String sessionToken)
            throws IOException, ResourceNotFoundException, JCodecException {
        FileEntity fileEntity = uploadService.filePreUpload(file,FilePublish.valueOf(filePublish), sessionToken);

        FilePreUploadResponseDto dto = FilePreUploadResponseDto.builder()
                .fileId(fileEntity.getId())
                .downloadUrl(env.getProperty("file.downloadUrl") + "/"+fileEntity.getFileUrl()+"/"+fileEntity.getStoredFile())
                .build();

        return dto;
    }
}
