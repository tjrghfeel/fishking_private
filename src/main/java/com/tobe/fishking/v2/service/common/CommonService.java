package com.tobe.fishking.v2.service.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.common.*;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.enums.common.AdType;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.CodeGroupWriteDTO;
import com.tobe.fishking.v2.model.CommonCodeDTO;
import com.tobe.fishking.v2.model.CommonCodeWriteDTO;
import com.tobe.fishking.v2.model.board.FishingDiaryMainResponse;
import com.tobe.fishking.v2.model.board.FishingDiarySearchResponse;
import com.tobe.fishking.v2.model.common.*;
import com.tobe.fishking.v2.model.fishing.ShipListResponse;
import com.tobe.fishking.v2.model.fishing.SmallShipResponse;
import com.tobe.fishking.v2.model.response.TidalLevelResponse;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.utils.DateUtils;
import com.tobe.fishking.v2.utils.HolidayUtil;
import lombok.RequiredArgsConstructor;
import org.jcodec.api.JCodecException;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final CommonCodeRepository commonCodeRepo;
    private final CodeGroupRepository codeGroupRepo;
    private final MemberRepository memberRepo;
    private final FileRepository fileRepo;
    private final PopularRepository popularRepo;
    private final TidalLevelRepository tidalLevelRepository;
    private final ObserverCodeRepository observerCodeRepository;
    private final UploadService uploadService;
    private final Environment env;
    private final AdRepository adRepository;
    private final FishingDiaryRepository fishingDiaryRepository;
    private final ShipRepository shipRepository;
    private final SearchKeywordRepository searchKeywordRepository;

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

    @Transactional
    public Boolean deleteFile(DeleteFileDto dto, String token) throws ResourceNotFoundException {
        Member member = memberRepo.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        FileEntity file = fileRepo.findById(dto.getFileId())
                .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getFileId()));

        if(member != file.getCreatedBy() && member.getRoles() != Role.admin){
            throw new RuntimeException("해당 파일에 대한 삭제권한이 없습니다.");
        }

        uploadService.removeFileEntity(dto.getFileId());
        return true;
    }

    @Transactional
    public List<TidalLevelResponse> findAllByDateAndCode(String date, String code) {
        return tidalLevelRepository.findAllByDateAndCode(DateUtils.getDateFromString(date), code);
    }

    public Map<String, Object> findTideTime(String date) {
        int lunarDay = Integer.parseInt(HolidayUtil.convertSolarToLunar(date.replaceAll("-", "")).substring(8, 10));
        int tideTime = (lunarDay + 7) % 15;
        String tideTimeString;
        if (tideTime == 0) {
            tideTimeString = "조금";
        } else {
            tideTimeString = String.valueOf(tideTime) + "물";
        }
        Map<String, Object> result = new HashMap<>();
        result.put("tideTime", tideTimeString);
        return result;
    }

    @Transactional
    public List<ObserverCodeResponse> getAllObserverCode() {
        List<ObserverCode> list = observerCodeRepository.findAll();
        return list.stream().map(ObserverCodeResponse::new).collect(Collectors.toList());
    }

    /* 광고리스트 */
    @Transactional
    public List<SmallShipResponse> getAdList(AdType type) {
        return adRepository.getAdByType(type);
    }

    @Transactional
    public Map<String, Object> getMainScreenData() {
        String path = env.getProperty("file.downloadUrl");

        Map<String, Object> result = new HashMap<>();
        List<SmallShipResponse> live = adRepository.getAdByType(AdType.MAIN_LIVE);
        List<SmallShipResponse> ship = adRepository.getAdByType(AdType.MAIN_SHIP);
        List<SmallShipResponse> ad = adRepository.getAdByType(AdType.MAIN_AD);

        for (SmallShipResponse r : live) {
            List<CommonCode> s = commonCodeRepo.getShipSpeciesName(r.getId());
            r.setSpecies(s);
        }

        for (SmallShipResponse r : ship) {
            List<CommonCode> s = commonCodeRepo.getShipSpeciesName(r.getId());
            r.setSpecies(s);
        }

        for (SmallShipResponse r : ad) {
            List<CommonCode> s = commonCodeRepo.getShipSpeciesName(r.getId());
            r.setSpecies(s);
        }

        result.put("live", live);
        result.put("ship", ship);
        result.put("ad", ad);
        result.put("species", commonCodeRepo.getMainSpeciesCount());
        List<MainSpeciesResponse> directions = commonCodeRepo.getMainDistrictCount();
        List<String> d = directions.stream().map(MainSpeciesResponse::getCode).collect(Collectors.toList());
        List<CommonCode> codes = commonCodeRepo.getByGroupId(157L);
        for (CommonCode code : codes) {
            if (!d.contains(code.getExtraValue1())) {
                directions.add(new MainSpeciesResponse(code.getExtraValue1(), code.getCodeName(), null, 0L));
            }
        }
        result.put("direction", directions);
        List<FishingDiaryMainResponse> diaries = fishingDiaryRepository.getMainDiaries();
        for (FishingDiaryMainResponse diary : diaries) {
            List<FileEntity> fileEntityList = fileRepo.findByPidAndFilePublishAndFileTypeAndIsDelete(
                    diary.getId(), FilePublish.fishingDiary, FileType.image, false);
            if (fileEntityList.size() > 0) {
                diary.setImageUrl(path + "/" + fileEntityList.get(0).getFileUrl() + "/" + fileEntityList.get(0).getThumbnailFile());
            }
        }
        result.put("fishingDiaries", diaries);

        return result;
    }

    @Transactional
    public Map<String, Object> searchTotal(String keyword, Double lat, Double lng) {
        Map<String, Object> result = new HashMap<>();
        Pageable pageable = PageRequest.of(0, 4, Sort.by("createdDate"));
        result.put("keyword", keyword);
        result.put("diary", fishingDiaryRepository.searchDiaryOrBlog(keyword, "diary", pageable));
        result.put("blog", fishingDiaryRepository.searchDiaryOrBlog(keyword, "blog", pageable));
        Page<ShipListResponse> shipListResponsePage = shipRepository.searchMain(keyword, "ship", lat, lng, pageable);
        Page<ShipListResponse> liveListResponsePage = shipRepository.searchMain(keyword, "live", lat, lng, pageable);
        List<ShipListResponse> newShipList = new ArrayList<>();
        for(ShipListResponse r : shipListResponsePage.getContent()) {
            List<CommonCode> fish = commonCodeRepo.getShipSpeciesName(r.getId());
            r.setSpecies(fish);
            newShipList.add(r);
        }
        List<ShipListResponse> newLiveList = new ArrayList<>();
        for(ShipListResponse r : liveListResponsePage.getContent()) {
            List<CommonCode> fish = commonCodeRepo.getShipSpeciesName(r.getId());
            r.setSpecies(fish);
            newLiveList.add(r);
        }
        result.put("ship", new PageImpl<>(newShipList, pageable, shipListResponsePage.getTotalElements()));
        result.put("live", new PageImpl<>(newLiveList, pageable, shipListResponsePage.getTotalElements()));
        return result;
    }

    @Transactional
    public Map<String, Object> searchShip(String keyword, Integer page, String order, Double lat, Double lng) throws EmptyListException {
        Map<String, Object> result = new HashMap<>();
        Pageable pageable = PageRequest.of(page, 10,
                order.equals("") ? Sort.by("createdDate") : Sort.by("createdDate").and(Sort.by(order)));
        Page<ShipListResponse> ship = shipRepository.searchMain(keyword, "ship", lat, lng, pageable);
        if (ship.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            List<ShipListResponse> newList = new ArrayList<>();
            for(ShipListResponse r : ship.getContent()) {
                List<CommonCode> fish = commonCodeRepo.getShipSpeciesName(r.getId());
                r.setSpecies(fish);
                newList.add(r);
            }
            result.put("keyword", keyword);
            result.put("ship", new PageImpl<>(newList, pageable, ship.getTotalElements()));
            return result;
        }
    }

    @Transactional
    public Map<String, Object> searchShipWithType(String keyword, Integer page, String order, String type, Double lat, Double lng) throws EmptyListException {
        Map<String, Object> result = new HashMap<>();
        Pageable pageable = PageRequest.of(page, 10,
                order.equals("") ? Sort.by("createdDate") : Sort.by("createdDate").and(Sort.by(order)));
        Page<ShipListResponse> ship = shipRepository.searchMainWithType(keyword, type, lat, lng, pageable);
        if (ship.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            List<ShipListResponse> newShipList = new ArrayList<>();
            for(ShipListResponse r : ship.getContent()) {
                List<CommonCode> fish = commonCodeRepo.getShipSpeciesName(r.getId());
                r.setSpecies(fish);
                newShipList.add(r);
            }
            result.put("keyword", keyword);
            result.put("type", "ship");
            result.put("ship", new PageImpl<>(newShipList, pageable, ship.getTotalElements()));
            return result;
        }
//        result.put("keyword", keyword);
//        result.put("type", "ship");
//        result.put("ship", shipRepository.searchMainWithType(keyword, type, lat, lng, pageable));
//        return result;
    }

    @Transactional
    public Map<String, Object> searchLive(String keyword, Integer page, String order, Double lat, Double lng) throws EmptyListException {
        Map<String, Object> result = new HashMap<>();
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdDate"));
        Page<ShipListResponse> live = shipRepository.searchMain(keyword, "live", lat, lng, pageable);
        if (live.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            List<ShipListResponse> newLiveList = new ArrayList<>();
            for(ShipListResponse r : live.getContent()) {
                List<CommonCode> fish = commonCodeRepo.getShipSpeciesName(r.getId());
                r.setSpecies(fish);
                newLiveList.add(r);
            }
            result.put("keyword", keyword);
            result.put("live", new PageImpl<>(newLiveList, pageable, live.getTotalElements()));
            return result;
        }
    }

    @Transactional
    public Map<String, Object> searchDiary(String keyword, Integer page, String order) throws EmptyListException {
        Map<String, Object> result = new HashMap<>();
        Pageable pageable = PageRequest.of(page, 10,
                order.equals("") ? Sort.by("createdDate") : Sort.by("createdDate").and(Sort.by(order)));
        Page<FishingDiarySearchResponse> diaries = fishingDiaryRepository.searchDiaryOrBlog(keyword, "diary", pageable);
        if (diaries.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            result.put("keyword", keyword);
            result.put("diary", diaries);
            return result;
        }
    }

    @Transactional
    public Map<String, Object> searchBlog(String keyword, Integer page, String order) throws EmptyListException {
        Map<String, Object> result = new HashMap<>();
        Pageable pageable = PageRequest.of(page, 10,
                order.equals("") ? Sort.by("createdDate") : Sort.by("createdDate").and(Sort.by(order)));
        Page<FishingDiarySearchResponse> diaries = fishingDiaryRepository.searchDiaryOrBlog(keyword, "blog", pageable);
        if (diaries.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            result.put("keyword", keyword);
            result.put("blog", diaries);
            return result;
        }
//        result.put("keyword", keyword);
//        result.put("ship", fishingDiaryRepository.searchDiaryOrBlog(keyword, "blog", pageable));
//        return result;
    }

    @Transactional
    public void addSearchKeys(String token, String keyword, SearchPublish publish) {
        Optional<Member> optMem = memberRepo.findBySessionToken(token);
        optMem.ifPresent(member -> popularRepo.save(
                new Popular(publish, keyword, member)
        ));
        Optional<SearchKeyword> searchKeyword = searchKeywordRepository.getSearchKeywordBySearchKeyword(keyword);
        if (searchKeyword.isPresent()) {
            SearchKeyword k = searchKeyword.get();
            k.updateCount();
            searchKeywordRepository.save(k);
        } else {
            searchKeywordRepository.save(
                    new SearchKeyword(keyword)
            );
        }
    }

    @Transactional
    public Map<String, Object> shipAdList(String fishingType, Double lat, Double lng) {
        Map<String, Object> result = new HashMap<>();
        List<SmallShipResponse> premium;
        List<SmallShipResponse> normal;
        if (fishingType.equals("ship")) {
            premium = adRepository.getAdByType(AdType.SHIP_PREMIUM_AD);
            normal = adRepository.getAdByType(AdType.SHIP_AD);
        } else {
            premium = adRepository.getAdByType(AdType.ROCK_PREMIUM_AD);
            normal = adRepository.getAdByType(AdType.ROCK_AD);
        }
        for (SmallShipResponse p : premium) {
            if (lat != 0) {
                p.setDistance(p.getLocation().getDistance(lat, lng));
            }
            List<CommonCode> s = commonCodeRepo.getShipSpeciesName(p.getId());
            p.setSpecies(s);
        }
        for (SmallShipResponse p : normal) {
            if (lat != 0) {
                p.setDistance(p.getLocation().getDistance(lat, lng));
            }
            List<CommonCode> s = commonCodeRepo.getShipSpeciesName(p.getId());
            p.setSpecies(s);
        }
        result.put("premium", premium);
        result.put("normal", normal);
        return result;
    }

    @Transactional
    public List<CommonCodeDTO> getAddrCodes(Long groupId, String parCode) {
        List<CommonCodeDTO> commonCodeDTOList = new ArrayList<>();
        List<CommonCode> commonCodeList = commonCodeRepo.findAllByCodeGroupIdAndParCode(groupId, parCode);
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
}
