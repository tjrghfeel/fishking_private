package com.tobe.fishking.v2.service.fishking;


import com.tobe.fishking.v2.addon.UploadService;
import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.common.LoveTo;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.FishingDiaryComment;
import com.tobe.fishking.v2.entity.fishing.RealTimeVideo;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.enums.common.OperatorType;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.enums.fishing.FishingLure;
import com.tobe.fishking.v2.enums.fishing.FishingTechnic;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.enums.fishing.TideTime;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.common.MapInfoDTO;
import com.tobe.fishking.v2.model.common.ShareStatus;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.common.*;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryRepository;
import com.tobe.fishking.v2.repository.fishking.PlacesRepository;
import com.tobe.fishking.v2.repository.fishking.RealTimeVideoRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.repository.fishking.specs.FishingDiarySpecs;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.utils.NativeResultProcessUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.persistence.Tuple;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FishingDiaryService {

    private final FishingDiaryRepository fishingDiaryRepo;
    private final MemberRepository memberRepo;
    private final ShipRepository shipRepo;
    private final PlacesRepository placesRepo;
    private final CommonCodeRepository commonCodeRepo;
    private final CodeGroupRepository codeGroupRepo;
    private final PopularRepository popularRepo;
    private final FileRepository fileRepository;
    private final BoardRepository boardRepo;
    private final UploadService uploadService;
    private final LoveToRepository loveToRepository;
    private final RealTimeVideoRepository realTimeVideoRepo;
    private final Environment env;
    private final MemberService memberService;

    private static int searchSize = 0;
    //검색 -- 통합검색
    public Page<FishingDiaryDTO.FishingDiaryDTOResp> getFishingDiaryListLike(Board board,
                                                         @RequestParam(required = false) String searchRequest,
                                                         Pageable pageable,
                                                         Integer totalElement) {

        //조행일지중에 어종명으로  검색
        Page<FishingDiary> fishingDiary = searchRequest.isEmpty()
                ? fishingDiaryRepo.findFishingDiariesByBoard_FilePublish(board, pageable, totalElement)
                : fishingDiaryRepo.findFishingDiariesByBoardEqualsAndFishingSpeciesName(board, searchRequest, pageable, totalElement);

        popularRepo.save(new Popular(SearchPublish.TOTAL, searchRequest, memberRepo.getOne((long) 5)));

        return fishingDiary.map(FishingDiaryDTO.FishingDiaryDTOResp::of);
    }


    public Page<FishingDiaryDTO.FishingDiaryDTOResp> getFishingDiaryList(Pageable pageable, Integer totalElements) {
        Page<FishingDiary> fishingDiary = fishingDiaryRepo.findAll(pageable, totalElements);
        return fishingDiary.map(FishingDiaryDTO.FishingDiaryDTOResp::of);
    }

    //검색 -- 조황일지, 조행기
    public Page<FishingDiaryDTO.FishingDiaryDTOResp> getFishingDiaryList(Pageable pageable,
                                                     @RequestParam(required = false) Map<String,  ///total를 제외한 모든 것 조회
                                                             Object> searchRequest, Integer totalElement) {

        searchSize = 0;

        Map<FishingDiarySpecs.SearchKey, Object> searchKeys = new HashMap<>();
        for (String key : searchRequest.keySet()) {
            searchKeys.put(FishingDiarySpecs.SearchKey.valueOf(key.toUpperCase()), searchRequest.get(key));
            searchSize += 1;
        }

        Page<FishingDiary> fishingDiary = null;

        if (searchSize > 1) {
            fishingDiary = searchKeys.isEmpty()
                    ? fishingDiaryRepo.findAll(pageable, totalElement)
                    : fishingDiaryRepo.findAll(FishingDiarySpecs.searchWith(searchKeys), pageable, totalElement);
        } else {
            //어종?
            fishingDiary = searchKeys.isEmpty()
                    ? fishingDiaryRepo.findAll(pageable, totalElement)
                    : fishingDiaryRepo.findAll(FishingDiarySpecs.searchWith(searchKeys), pageable, totalElement);
        }

        //member 가져오기 jkkim
        for (String key : searchRequest.keySet()) {
            popularRepo.save(new Popular(SearchPublish.TOTAL, (String) searchRequest.get(key), memberRepo.getOne((long) 5)));
        }


        return fishingDiary.map(FishingDiaryDTO.FishingDiaryDTOResp::of);
    }


  /*
    public List<MapInfoDTO> getLatitudeAndLongitudeList() {
        return fishingDiaryRepo.findLatitudeAndLongitudeList();
    }
  */

    /*fishingDiary글쓰기
    * - */
    @Transactional
    public Long writeFishingDiary(WriteFishingDiaryDto dto, String token) throws ResourceNotFoundException {
        Member author = memberRepo.findBySessionToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("member not found for this token :: "+token));
        if(dto.getCategory()==FilePublish.fishingDiary.getValue() || dto.getCategory()==FilePublish.fishingBlog.getValue()){
            throw new RuntimeException("카테고리는 fishingBlog 또는 fishingDiary만 가능합니다.");
        }
        Board board = boardRepo.findBoardByFilePublish(FilePublish.valueOf(dto.getCategory()));
        Ship ship = null;
        String fishingLocation = null;
        Double latitude=null;
        Double longitude = null;
        if(dto.getShipId()!=null){
            ship = shipRepo.findById(dto.getShipId())
                    .orElseThrow(()->new ResourceNotFoundException("ship not found for this id :: "+dto.getShipId()));
        }
        else{
            fishingLocation = dto.getAddress();
            latitude = dto.getLatitude();
            longitude = dto.getLongitude();
        }

        /*낚시 일자*/
        LocalDate fishingDate = dto.getFishingDate();
        if(fishingDate.isAfter(LocalDate.now())){
            throw new RuntimeException("잘못된 날짜입니다. 오늘 포함 이전 날짜만 입력할 수 있습니다");
        }
        /*fishingType변환.*/
        FishingType fishingType=null;
        if(dto.getFishingType()!=null){fishingType = FishingType.valueOf(dto.getFishingType());}
        /*fishingSpecies 문자열로 만들기*/
        String fishSpecies =null;
        if(dto.getFishingSpecies()!=null) {
            List<CommonCode> fishSpeciesCommonCodeList = commonCodeRepo.findFishSpeciesCodeByCodeList(dto.getFishingSpecies());
//            CodeGroup fishSpeciesCodeGroup = codeGroupRepo.findByCode("mainfishspecies");
//            String[] fishSpeciesCommonCodeList = commonCodeRepo.findCodeNameByCodeList(dto.getFishingSpecies(), fishSpeciesCodeGroup);
            fishSpecies = fishSpeciesCommonCodeList.get(0).getCodeName();
            for (int i = 1; i < fishSpeciesCommonCodeList.size(); i++) {
                fishSpecies += ", " + fishSpeciesCommonCodeList.get(i).getCodeName();
            }
        }
        /*물때*/
        String tideTime=null;
        if(dto.getTide()!=null) {
            tideTime = TideTime.valueOf(dto.getTide()).getValue();
        }
        /*낚시 기법*/
        String fishTechnic = null;
        if(dto.getFishingTechnicList()!=null) {
            fishTechnic = FishingTechnic.valueOf(dto.getFishingTechnicList()[0]).getValue();
            for (int i = 1; i < dto.getFishingTechnicList().length; i++) {
                fishTechnic += ", " + FishingTechnic.valueOf(dto.getFishingTechnicList()[i]).getValue();
            }
        }
        /*미끼*/
        String fishLure = null;
        if(dto.getFishingLureList()!=null) {
            CodeGroup fishLureCodeGroup = codeGroupRepo.findByCode("bait");
            String[] fishLureCommonCodeList = commonCodeRepo.findCodeNameByCodeList(dto.getFishingLureList(), fishLureCodeGroup);
            fishLure = fishLureCommonCodeList[0];
            for (int i = 1; i < fishLureCommonCodeList.length; i++) {
                fishLure += ", " + fishLureCommonCodeList[i];
            }
        }

        FishingDiary fishingDiary = FishingDiary.builder()
                .board(board)
                .filePublish(FilePublish.valueOf(dto.getCategory()))
                .ship(ship)
                .member(author)
//                .goods()
                .title(dto.getTitle())
                .contents(dto.getContent())
//                .location()
                .fishingSpeciesName(fishSpecies)
                .fishingDate(dto.getFishingDate().toString())
                .fishingTideTime(tideTime)
                .fishingTechnic(fishTechnic)
                .fishingLure(fishLure)
                .fishingType(fishingType)
//                .fishLength()
//                .fishWeight()
//                .fishingDiaryFishingTechnics()
//                .fishingDiaryFishingLures()
//                .fishingLocation()
//                .writeLocation()
//                .writeLatitude()
//                .writeLongitude()
//                .scrapMembers
//                .status()
                .status(new ShareStatus(0,0,0,0))
                .createdBy(author)
                .modifiedBy(author)
                .fishingLocation(fishingLocation)
                .writeLatitude(latitude)
                .writeLongitude(longitude)
                .isDeleted(false)
                .isActive(true)
                .isHidden(false)
                .build();
        fishingDiary = fishingDiaryRepo.save(fishingDiary);

        /*이미지 파일 엔터티 할당*/
        for(int i=0; i<dto.getFileList().length; i++){
            Long fileEntityId = dto.getFileList()[i];
            FileEntity fileEntity = fileRepository.findById(fileEntityId)
                    .orElseThrow(()-> new ResourceNotFoundException("file not found for this id :: "+fileEntityId));
            fileEntity.saveTemporaryFile(fishingDiary.getId());
        }
        /*동영상 파일 엔터티 할당*/
        if(dto.getVideoId()!=null){
            FileEntity fileEntity = fileRepository.findById(dto.getVideoId())
                    .orElseThrow(()->new ResourceNotFoundException("file not found for this id :: "+dto.getVideoId()));
            fileEntity.saveTemporaryFile(fishingDiary.getId());
        }

        return fishingDiary.getId();
    }


    public List<FishingDiaryDTO.FishingDiaryDTORespData> getFishingDiaryListsForMap(FilePublish filePublish) {

        if (filePublish != FilePublish.fishingBlog) return null;

        //       List<FishingDiaryDTO.FishingDiaryDTOResp> fishingDiaryEntityList = fishingDiaryRepo.findAllFishingDiaryAndLocation(filePublish);

        List<Tuple> tupleRespList = fishingDiaryRepo.findAllFishingDiaryAndLocation(filePublish);

        List<FishingDiaryDTO.FishingDiaryDTORespData> fishingDiaryDTOList1 = tupleRespList.stream().map(tuple -> {
            // Use the tool class to turn tuple into an object
            FishingDiaryDTO.FishingDiaryDTORespData fishingDiaryDTOResp = NativeResultProcessUtils.processResult(tuple, FishingDiaryDTO.FishingDiaryDTORespData.class);
            return fishingDiaryDTOResp;
        }).collect(Collectors.toList());


        return fishingDiaryDTOList1;
    }

    //forEach(System.out::println);


  /*      for (int i = 0; i < fishingDiaryDTORespList.size(); i++) {

            FishingDiaryDTO.FishingDiaryDTOResp entity = (FishingDiaryDTO.FishingDiaryDTOResp) fishingDiaryDTORespList.get(i);

       //     FileEntity fileEntity = fileRepo.findTop1ByPidAndFilePublishAndIsRepresent(entity.getFishingDiaryId(), FilePublish.fishingBlog, true);

            //조행기(일지) 대표이미지 썸네일
       //     if (fileEntity != null) entity.setFishingDiaryRepresentUrl(env.getProperty("file.downloadUrl") + "/"+fileEntity.getFileUrl()+"/"+fileEntity.getThumbnailFile());
         //   else entity.setFishingDiaryRepresentUrl("https://");

            fishingDiaryDTORespList.set(i, entity);
*/



/*        List<FishingDiaryDTO.FishingDiaryDTOResp> fishingDiaryDTORespList = fishingDiaryEntityList.stream().map(FishingDiaryDTO.FishingDiaryDTOResp::of).collect(Collectors.toList());  //O

        //대표이미지
        for (int i = 0; i < fishingDiaryDTORespList.size(); i++) {

            FishingDiaryDTO.FishingDiaryDTOResp entity = (FishingDiaryDTO.FishingDiaryDTOResp) fishingDiaryDTORespList.get(i);

            FileEntity fileEntity = fileRepo.findTop1ByPidAndFilePublishAndIsRepresent(entity.getFishingDiaryId(), FilePublish.fishingBlog, true);

            // File thumbnailFile = new File(env.getProperty("file.location")+File.separator+fileUrl+File.separator+thumbnailName);
            // File getStoredFile = new File(env.getProperty("file.location")+File.separator+fileUrl+File.separator+getStoredFile);

      //      if (fileEntity != null) entity.setFishingDiaryImageFileUrl(env.getProperty("file.downloadUrl") + "/"+fileEntity.getFileUrl()+"/"+fileEntity.getThumbnailFile());
      //      else entity.setFishingDiaryImageFileUrl("https://");

            fishingDiaryDTORespList.set(i, entity);
        }
*/
    //  return fishingDiaryDTORespList;

    //  }
//}


    /*조항일지, 유저조행기 수정*/
    @Transactional
    public Boolean modifyFishingDiary(ModifyFishingDiaryDto dto, String token) throws ResourceNotFoundException {
        FishingDiary fishingDiary = fishingDiaryRepo.findById(dto.getFishingDiaryId())
                .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+dto.getFishingDiaryId()));
        Member author = memberRepo.findBySessionToken(token)
                .orElseThrow(()-> new ResourceNotFoundException("member not found for this token :: "+token));
//        if(dto.getCategory()==FilePublish.fishingDiary.getValue() || dto.getCategory()==FilePublish.fishingBlog.getValue()){
//            throw new RuntimeException("카테고리는 fishingBlog 또는 fishingDiary만 가능합니다.");
//        }
//        Board board = boardRepo.findBoardByFilePublish(FilePublish.valueOf(dto.getCategory()));

        if(fishingDiary.getCreatedBy()!=author && author.getRoles()!=Role.admin){
            throw new RuntimeException("해당 게시글에 대한 수정 권한이 없습니다.");
        }

        Ship ship = null;
        String fishingLocation = null;
        Double latitude=null;
        Double longitude = null;
        if(dto.getShipId()!=null){
            ship = shipRepo.findById(dto.getShipId())
                    .orElseThrow(()->new ResourceNotFoundException("ship not found for this id :: "+dto.getShipId()));
        }
        else{
            fishingLocation = dto.getAddress();
            latitude = dto.getLatitude();
            longitude = dto.getLongitude();
        }

        /*fishingType변환.*/
        FishingType fishingType=null;
        if(dto.getFishingType()!=null){fishingType = FishingType.valueOf(dto.getFishingType());}
        /*fishingSpecies 문자열로 만들기*/
        String fishSpecies =null;
        if(dto.getFishingSpecies()!=null) {
            List<CommonCode> fishSpeciesCommonCodeList = commonCodeRepo.findFishSpeciesCodeByCodeList(dto.getFishingSpecies());
//            CodeGroup fishSpeciesCodeGroup = codeGroupRepo.findByCode("mainfishspecies");
//            String[] fishSpeciesCommonCodeList = commonCodeRepo.findCodeNameByCodeList(dto.getFishingSpecies(), fishSpeciesCodeGroup);
            fishSpecies = fishSpeciesCommonCodeList.get(0).getCodeName();
            for (int i = 1; i < fishSpeciesCommonCodeList.size(); i++) {
                fishSpecies += ", " + fishSpeciesCommonCodeList.get(i).getCodeName();
            }

        }
        /*물때*/
        String tideTime=null;
        if(dto.getTide()!=null) {
            tideTime = TideTime.valueOf(dto.getTide()).getValue();
        }
        /*낚시 기법*/
        String fishTechnic = null;
        if(dto.getFishingTechnicList()!=null) {
            fishTechnic = FishingTechnic.valueOf(dto.getFishingTechnicList()[0]).getValue();
            for (int i = 1; i < dto.getFishingTechnicList().length; i++) {
                fishTechnic += ", " + FishingTechnic.valueOf(dto.getFishingTechnicList()[i]).getValue();
            }
        }
        /*미끼*/
        String fishLure = null;
        if(dto.getFishingLureList()!=null) {
            CodeGroup fishLureCodeGroup = codeGroupRepo.findByCode("bait");
            String[] fishLureCommonCodeList = commonCodeRepo.findCodeNameByCodeList(dto.getFishingLureList(), fishLureCodeGroup);
            fishLure = fishLureCommonCodeList[0];
            for (int i = 1; i < fishLureCommonCodeList.length; i++) {
                fishLure += ", " + fishLureCommonCodeList[i];
            }
        }

        fishingDiary.modify(
            ship, dto.getTitle(), dto.getContent(), fishSpecies, dto.getFishingDate().toString(), tideTime, fishTechnic,
                fishLure, fishingType, fishingLocation, latitude, longitude, author
        );
        fishingDiary = fishingDiaryRepo.save(fishingDiary);

        /*일단 기존의 파일들을 모두 is_delete로 삭제처리해준뒤, 입력받은 id들에 해당하는 fileEntity들을 saveTemporary로 저장 */
        /*이미지 파일 수정.*/
        List<FileEntity> preFileList = fileRepository.findByPidAndFilePublishAndFileTypeAndIsDelete(
                fishingDiary.getId(),fishingDiary.getFilePublish(), FileType.image, false);
        for(int i=0; i<preFileList.size(); i++){
            preFileList.get(i).setIsDelete(true);
        }
        List<FileEntity> fileList = fileRepository.findAllById(dto.getFileList());//!!!!!정상작동할지 미지수.
        for(int i=0; i<dto.getFileList().length; i++){
            fileList.get(i).saveTemporaryFile(fishingDiary.getId());
        }
        /*동영상 파일 수정.*/
        List<FileEntity> preVideo = fileRepository.findByPidAndFilePublishAndFileTypeAndIsDelete(
                fishingDiary.getId(), fishingDiary.getFilePublish(), FileType.video, false);
        for(int i=0; i<preVideo.size(); i++){
            preVideo.get(i).setIsDelete(true);
        }
        if(dto.getVideoId()!=null) {
            FileEntity video = fileRepository.findById(dto.getVideoId())
                    .orElseThrow(() -> new ResourceNotFoundException("fileEntity not found for this id :: " + dto.getVideoId()));
            video.saveTemporaryFile(fishingDiary.getId());
        }

        return true;
    }

    /*어복스토리 리스트 출력
    * 인자 )
    * - category : fishingDiary / fishingBlog. 필수.
    * - districtList : 지역검색. common code의 행정구역 1단계 코드값 배열. 해당없으면 null
    * - searchKey : 검색 키워드. 해당없으면 빈문자열 ""
    * - searchTarget : searchKey가 적용될 항목. content / title / address 중 1. 해당없으면 null
    * - shipId : 특정 선박에 대한 글만 검색하고자할때. 해당없으면 null.
    * - fishSpecies : 어종검색. 글 작성시 선택한 어종을 기준으로 검색. 해당없으면 null.
    * - sort : 정렬기준. createdDate / likeCount / commentCount 중 1. 필수.
    * - token : 세션토큰. 필수
    * - myPost : 자신의 글만 볼지 아닐지. 필수. */
    @Transactional
    public Page<FishingDiaryDtoForPage> getFishingDiaryList(
            int page, String category, String district1, String[] district2List, String searchKey, String searchTarget, Long shipId,
            String[] fishSpecies, String shipName, String nickName, String title, String content, LocalDate createdDateStart,
            LocalDate createdDateEnd, Boolean hasShipData, Integer pageCount, String sort, String token, Boolean myPost)
            throws ResourceNotFoundException {
        Member member =null;
        Long memberId = null;
        if(token !=null){
            member = memberRepo.findBySessionToken(token)
                    .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
            memberId = member.getId();
        }
        /*카테고리*/
        Integer filePublish = null;
        if(category != null){
            filePublish = FilePublish.valueOf(category).ordinal();
        }
        /*지역검색 - 행정구역1단계*/
        String district1Regex = null;
        String district2Regex=null;
        if(district1 !=null){
            CodeGroup district1CodeGroup = codeGroupRepo.findByCode("districtL1");
            CommonCode district1Code = commonCodeRepo.findByCodeGroupAndCode(district1CodeGroup, district1);
            district1Regex = district1Code.getCodeName() +"|"+district1Code.getExtraValue1();

            /*지역검색 - 행정구역2단계*/
            if(!district1Code.getCode().equals("제주특별자치도") && !district1Code.getCode().equals("세종특별자치시")) {
                if (district2List != null && district2List.length != 0) {
                    CodeGroup codeGroup = codeGroupRepo.findByCode("districtL2");
                    List<CommonCode> districtListCommonCodeList = commonCodeRepo.findCommonCodesByCodeGroupAndCodes(codeGroup, Arrays.asList(district2List.clone()));
                    district2Regex = districtListCommonCodeList.get(0).getCodeName();
                    for (int i = 1; i < districtListCommonCodeList.size(); i++) {
                        district2Regex += "|" + districtListCommonCodeList.get(i).getCodeName();
                    }
                }
            }
        }

        /*어종검색*/
        String fishSpeciesRegex=null;
        if(fishSpecies!=null&&fishSpecies.length!=0) {
            CodeGroup codeGroup = codeGroupRepo.findByCode("mainfishspecies");
            List<CommonCode> districtListCommonCodeList = commonCodeRepo.findCommonCodesByCodeGroupAndCodes(codeGroup, Arrays.asList(fishSpecies.clone()));
            fishSpeciesRegex = districtListCommonCodeList.get(0).getCodeName();
            for (int i = 1; i < districtListCommonCodeList.size(); i++) {
                fishSpeciesRegex += "|"+districtListCommonCodeList.get(i).getCodeName() ;
            }
        }

        if(createdDateEnd!=null){createdDateEnd = createdDateEnd.plusDays(1);}

        Pageable pageable = PageRequest.of(page, pageCount);
        if(sort.equals("createdDate")){
            if(member !=null && member.getRoles() == Role.admin){
                return fishingDiaryRepo.getFishingDiaryListOrderByCreatedDate(
                        filePublish,district1Regex, district2Regex, fishSpeciesRegex, searchKey, null,
                        memberId,myPost,searchTarget,shipId,shipName,nickName,title,content, createdDateStart,createdDateEnd,
                        hasShipData, true, pageable
                );
            }
            else {
                return fishingDiaryRepo.getFishingDiaryListOrderByCreatedDate(
                        filePublish, district1Regex, district2Regex, fishSpeciesRegex, searchKey, null,
                        memberId, myPost, searchTarget, shipId, null, null, null, null, null,null,
                        hasShipData, false, pageable
                );
            }
        }
        else if(sort.equals("accuseCount")){
            if(member !=null && member.getRoles() == Role.admin){
                return fishingDiaryRepo.getFishingDiaryListOrderByAccuseCount(
                        filePublish,district1Regex, district2Regex, fishSpeciesRegex, searchKey, null,
                        memberId,myPost,searchTarget,shipId,shipName,nickName,title,content, createdDateStart,createdDateEnd,
                        hasShipData, true, pageable
                );
            }
            else {
                return fishingDiaryRepo.getFishingDiaryListOrderByCreatedDate(
                        filePublish, district1Regex, district2Regex, fishSpeciesRegex, searchKey, null,
                        memberId, myPost, searchTarget, shipId, null, null, null, null, null,null,
                        hasShipData, false, pageable
                );
            }
        }
        else if(sort.equals("likeCount")){
            return fishingDiaryRepo.getFishingDiaryListOrderByLikeCount(
                    filePublish, district1Regex, district2Regex, fishSpeciesRegex, searchKey, null, memberId,myPost,searchTarget,shipId,pageable);}
        else if(sort.equals("commentCount")){
            return fishingDiaryRepo.getFishingDiaryListOrderByCommentCount(
                    filePublish, district1Regex, district2Regex, fishSpeciesRegex, searchKey, null, memberId,myPost,searchTarget,shipId,pageable);}
        else{
            return fishingDiaryRepo.getFishingDiaryListOrderByCreatedDate(
                    filePublish, district1Regex, district2Regex, fishSpeciesRegex, searchKey, null,
                    memberId, myPost, searchTarget, shipId, null, null, null, null, null,null,
                    hasShipData, false, pageable
            );
        }
    }

    /*어복스토리 상세보기*/
    @Transactional
    public FishingDiaryDetailDto getFishingDiaryDetail(Long fishingDiaryId, String token) throws ResourceNotFoundException {
        FishingDiaryDetailDto result = null;

        Member member = null;
        Boolean isManager = false;
        if(token !=null){
            member = memberRepo.findBySessionToken(token)
                    .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
            if(member.getRoles() == Role.admin){isManager= true;}
        }

        FishingDiary fishingDiary  = fishingDiaryRepo.findById(fishingDiaryId)
                .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+fishingDiaryId));
        Long shipId = null;
        String shipName = null;
        String shipAddress = null;
        String shipImageUrl = null;
        if(fishingDiary.getShip()!=null){
            shipId = fishingDiary.getShip().getId();
            shipName = fishingDiary.getShip().getShipName();
            shipAddress = fishingDiary.getShip().getAddress();
            shipImageUrl = env.getProperty("file.downloadUrl")+fishingDiary.getShip().getProfileImage();
        }
        Boolean isHidden = false;
        if(fishingDiary.getMember().getIsActive() == false){throw new RuntimeException("탈퇴한 회원의 글입니다.");}
        else if(fishingDiary.getIsDeleted() == true){ throw new RuntimeException("삭제된 게시물입니다.");}
        else if(isManager == false && fishingDiary.getIsActive() == false){throw new RuntimeException("숨김처리된 글입니다.");}
        else if(fishingDiary.getIsHidden()==true){isHidden =true;}

        /*닉네임, 글 종류 설정.*/
        String nickName = null;
        String fishingDiaryType = null;
        if(fishingDiary.getFilePublish()==FilePublish.fishingDiary){
            nickName = fishingDiary.getShip().getShipName();
            fishingDiaryType = FilePublish.fishingDiary.getValue();
        }
        else if(fishingDiary.getFilePublish()==FilePublish.fishingBlog){
            nickName = fishingDiary.getMember().getNickName();
            fishingDiaryType = FilePublish.fishingBlog.getValue();
        }
        /*fishingType 설정*/
        String fishingType = null;
        if(fishingDiary.getFishingType()!=null){
            fishingType = fishingDiary.getFishingType().getValue();
        }
        /*isLive 설정*/
        Boolean isLive = false;
        if(shipId != null) {
            List<RealTimeVideo> realTimeVideoList = realTimeVideoRepo.getRealTimeVideoByShipsId(shipId);
            if(realTimeVideoList.size()>0){ isLive = true; }
        }
        /*isLikeTo 설정*/
        Boolean isLikeTo = null;
        TakeType takeType = TakeType.valueOf(fishingDiary.getFilePublish().getKey());
        LoveTo loveTo = loveToRepository.findByLinkIdAndTakeTypeAndCreatedBy(fishingDiaryId,takeType, member);
        if(loveTo!=null){ isLikeTo = true;}
        else { isLikeTo = false; }
        /*isScraped 설정*/
        Boolean isScraped = null;
        List<Member> scrapMemberList = fishingDiary.getScrapMembers();
        if(scrapMemberList.contains(member)){ isScraped = true; }
        else { isScraped = false; }

        /*imageUrlList 설정*/
        ArrayList<String> imageUrlList = new ArrayList<>();
        ArrayList<Long> imageIdList =new ArrayList<>();
        String path = env.getProperty("file.downloadUrl");
        if(isHidden == false || isManager == true) {
            List<FileEntity> fileEntityList = fileRepository.findByPidAndFilePublishAndFileTypeAndIsDelete(
                    fishingDiaryId, fishingDiary.getFilePublish(), FileType.image, false);
            for (int i = 0; i < fileEntityList.size(); i++) {
                FileEntity fileEntity = fileEntityList.get(i);
                imageUrlList.add(path + "/" + fileEntity.getFileUrl() + "/" + fileEntity.getStoredFile());
                imageIdList.add(fileEntity.getId());
            }
        }
        /*비디오 url 설정*/
        String videoUrl = null;
        Long videoId = null;
        if(isHidden == false || isManager == true) {
            List<FileEntity> video = fileRepository.findByPidAndFilePublishAndFileTypeAndIsDelete(
                    fishingDiaryId, fishingDiary.getFilePublish(), FileType.video, false);
            if (video.size() != 0) {
                videoUrl = path + "/" + video.get(0).getFileUrl() + "/" + video.get(0).getStoredFile();
                videoId = video.get(0).getId();
            }
        }
        /*자신글여부 설정*/
        Boolean isMine = null;
        if(member==fishingDiary.getMember()){isMine=true;}
        else{isMine=false;}
        /*fish species의 common code 추가*/
        String codeNameString = fishingDiary.getFishingSpeciesName();
        String[] codeNameArray = codeNameString.split(", ");
        CodeGroup speciesCodeGroup = codeGroupRepo.findById(80L)
                .orElseThrow(()->new ResourceNotFoundException("codeGroup not found for this id :: "+80L));
        ArrayList<String> codeList = commonCodeRepo.findCodeByCodeNameAndCodeGroup(codeNameArray, speciesCodeGroup);
        //tide
        String tideCode = null;
        if(fishingDiary.getFishingTideTime()!=null){
            String tideTime = fishingDiary.getFishingTideTime();
            TideTime[] tideTimeEnumList = TideTime.values();
            for(int i=0; i<tideTimeEnumList.length; i++){
                if(tideTimeEnumList[i].getValue().equals(tideTime)){tideCode = tideTimeEnumList[i].getKey();}
            }
        }
        /*fishingTechnicList*/
        ArrayList<String> techCodeList = new ArrayList<>();
        if(fishingDiary.getFishingTechnic()!=null){
            String techNameString = fishingDiary.getFishingTechnic();
            String[] techNameArray = techNameString.split(", ");
            FishingTechnic[] technicEnumList = FishingTechnic.values();
            for(int j=0; j<techNameArray.length; j++){
                for(int i=0; i<technicEnumList.length; i++){
                    if(technicEnumList[i].getValue().equals(techNameArray[j])){techCodeList.add(technicEnumList[i].getKey());}
                }
            }
        }
        //fishingLureList
        ArrayList<String> lureCodeList = new ArrayList<>();
        if(fishingDiary.getFishingLure()!=null){
            String lureNameString = fishingDiary.getFishingLure();
            String[] lureNameArray = lureNameString.split(", ");
            CodeGroup lureCodeGroup = codeGroupRepo.findById(89L)
                    .orElseThrow(()->new ResourceNotFoundException("codeGroup not found for this id :: "+89L));
            lureCodeList = commonCodeRepo.findCodeByCodeNameAndCodeGroup(lureNameArray, lureCodeGroup);
        }
        //fishingType. enum값으로 저장.
        String fishingTypeCode = null;
        if(fishingDiary.getFishingType() != null){
            fishingTypeCode = fishingDiary.getFishingType().getKey();
        }

        /*글의 조회수 증가*/
        fishingDiary.getStatus().plusViewCount();

        result = FishingDiaryDetailDto.builder()
                .authorId(fishingDiary.getMember().getId())
                .fishingDiaryId(fishingDiary.getId())
                .fishingDiaryType(fishingDiaryType)
                .shipId(shipId)
                .shipName(shipName)
                .shipAddress(shipAddress)
                .shipImageUrl(shipImageUrl)
                .address(fishingDiary.getFishingLocation())
                .latitude(fishingDiary.getWriteLatitude())
                .longitude(fishingDiary.getWriteLongitude())
                .nickName(nickName)
                .profileImage(path + fishingDiary.getMember().getProfileImage())
                .isLive(isLive)
                .fishingType(fishingType)
                .fishingTypeCode(fishingTypeCode)
                .title((isHidden && isManager==false)?"숨김처리된 글입니다.":fishingDiary.getTitle())
                .createdDate(fishingDiary.getCreatedDate())
                .fishingSpecies(fishingDiary.getFishingSpeciesName())
                .fishingSpeciesCodeList(codeList)
                .fishingDate(fishingDiary.getFishingDate())
                .tide(fishingDiary.getFishingTideTime())
                .tideCode(tideCode)
                .fishingLure(fishingDiary.getFishingLure())
                .fishingLureCodeList(lureCodeList)
                .fishingTechnic(fishingDiary.getFishingTechnic())
                .fishingTechnicCodeList(techCodeList)
                .content((isHidden && isManager==false)?"숨김처리된 글입니다.":fishingDiary.getContents())
                .imageUrlList(imageUrlList)
                .imageIdList(imageIdList)
                .videoUrl(videoUrl)
                .videoId(videoId)
                .isLikeTo(isLikeTo)
                .isScraped(isScraped)
                .likeCount(fishingDiary.getStatus().getLikeCount())
                .commentCount(fishingDiary.getStatus().getCommentCount())
                .scrapCount(fishingDiary.getStatus().getShareCount())
                .viewCount(fishingDiary.getStatus().getViewCount())
                .isMine(isMine)
                .build();

        return result;
    }

    /*조항일지, 유저조행기 삭제*/
    @Transactional
    public Boolean deleteFishingDiary(DeleteFishingDiary dto, String token) throws ResourceNotFoundException {
        Member member = memberRepo.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        FishingDiary fishingDiary = fishingDiaryRepo.findById(dto.getFishingDiaryId())
                .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+dto.getFishingDiaryId()));

        if(member!=fishingDiary.getMember() && member.getRoles()!=Role.admin){
            throw new RuntimeException("해당 게시글에 대한 삭제 권한이 없습니다.");
        }

        fishingDiary.delete();
        List<FileEntity> fileList = fileRepository.findByPidAndFilePublishAndIsDelete(fishingDiary.getId(), fishingDiary.getFilePublish(), false);
        for(int i=0; i<fileList.size(); i++){
            fileList.get(i).setIsDelete(true);
        }
        return true;
    }
    //숨김처리
    @Transactional
    public Boolean hideFishingDiary(Long id, String active, String token) throws ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);
        Boolean isActive = null;
        if(active.equals("true")){isActive=true;}
        else if(active.equals("false")){isActive = false; }

        if(member.getRoles()==Role.admin){
            FishingDiary fishingDiary = fishingDiaryRepo.findById(id)
                    .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+id));
            fishingDiary.setActive(isActive);
            return true;
        }
        else return false;
    }
    //내용 숨김처리
    @Transactional
    public Boolean hideContent(Long id, String active, String token) throws ResourceNotFoundException {
        Member member = memberService.getMemberBySessionToken(token);
        Boolean isHidden = null;
        if(active.equals("true")){isHidden=true;}
        else if(active.equals("false")){isHidden = false; }

        if(member.getRoles()==Role.admin){
            FishingDiary fishingDiary = fishingDiaryRepo.findById(id)
                    .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+id));
            fishingDiary.setHide(isHidden);
            return true;
        }
        else return false;
    }

    /*스크랩 추가*/
    @Transactional
    public Boolean addScrap(AddScrapDto dto, String token) throws ResourceNotFoundException {
        Member member = memberRepo.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        FishingDiary fishingDiary = fishingDiaryRepo.findById(dto.getFishingDiaryId())
                .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+dto.getFishingDiaryId()));

        /*이미 스크랩되어있는지 확인*/
        List<Member> scrapMemberList = fishingDiary.getScrapMembers();
        if(scrapMemberList.contains(member)){ return false; }
        else {
            scrapMemberList.add(member);
            /*fishingDiary에 스크랩수 증가*/
            fishingDiary.getStatus().plusShareCount();
            return  true;
        }
    }
    /*스크랩 삭제*/
    @Transactional
    public Boolean deleteScrap(DeleteScrapDto dto, String token) throws ResourceNotFoundException {
        Member member = memberRepo.findBySessionToken(token)
                .orElseThrow(()->new ResourceNotFoundException("member not found for this token :: "+token));
        FishingDiary fishingDiary = fishingDiaryRepo.findById(dto.getFishingDiaryId())
                .orElseThrow(()->new ResourceNotFoundException("fishingDiary not found for this id :: "+dto.getFishingDiaryId()));

        /*스크랩되어있는지 확인후, 삭제*/
        List<Member> scrapMemberList = fishingDiary.getScrapMembers();
        if(scrapMemberList.contains(member)){
            scrapMemberList.remove(member);
            /*fishingDiary의 스크랩수 감소*/
            fishingDiary.getStatus().subShareCount();
            return true;
        }
        else{ return false;}
    }

    //비디오
    @Transactional
    public StreamingResponseBody getVideoStream(Long fileId) throws ResourceNotFoundException, FileNotFoundException, ServiceLogicException {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(()->new ServiceLogicException("file not found for this id :: "+fileId));

        File file = new File(env.getProperty("file.location") + "/"+fileEntity.getFileUrl()+"/"+fileEntity.getStoredFile());
        final InputStream is = new FileInputStream(file);
        return os -> {
            readAndWrite(is, os);
        };
    }

    private void readAndWrite(final InputStream is, OutputStream os) throws IOException {
        byte[] data = new byte[2048];
        int read = 0;
        while ((read = is.read(data)) > 0) {
            os.write(data, 0, read);
        }
        os.flush();
    }


/*

    public FishingDiary getFishingDiary(long fishingDiaryId) {
        return fishingDiaryRepo.findById(fishingDiaryId).orElseThrow(CResourceNotExistException::new);
    }
    public Ship getShip(long shipId) {
        return shipRepo.findById(shipId).orElseThrow(CResourceNotExistException::new);
    }
    public Places getPlaces(long placesId) {
        return placesRepo.findById(placesId).orElseThrow(CResourceNotExistException::new);
    }



    public List<Object[]> getCountTotalFishingDiaryByFishSpecies() {
        return fishingDiaryRepo.countTotalFishingDiaryByFishSpecies();
        // return null;
    }

    public List<Object[]> getCountTotalFishingDiaryByRegion() {
        return fishingDiaryRepo.countTotalFishingDiaryByRegion();
    }

    public FishingDiary writeFishingDiary(String uid, long shipId, ParamsFishingDiary paramsFishingDiary) {

        CodeGroup cgSpecies =  codeGroupRepo.findByCode("fishspecies");

        List<CommonCode> fishSpecies = commonCodeRepo.findCommonCodesByCodeGroupAndCodes(cgSpecies, paramsFishingDiary.getFishSpecies() );

        CodeGroup cgLures =  codeGroupRepo.findByCode("fishinglure");

        List<CommonCode> fishingLures = commonCodeRepo.findCommonCodesByCodeGroupAndCodes(cgLures, paramsFishingDiary.getFishSpecies() );


        FishingDiary fishingDiary = new FishingDiary(memberRepo.findByUid(uid).orElseThrow(CMemberNotFoundException::new), shipRepo.findById(shipId).orElseThrow(CResourceNotExistException::new), paramsFishingDiary);

        fishingDiary.setFishSpecies(fishSpecies);
        fishingDiary.setFishSpecies(fishingLures);


        return fishingDiaryRepo.save(fishingDiary);
    }


    // 게시글을 수정합니다. 게시글 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리합니다.
    //@CachePut(value = CacheKey.POST, key = "#postId") 갱신된 정보만 캐시할경우에만 사용!
    
    public FishingDiary updateFishingDiary(long fishingDiaryId, String uid, long shipId, long placesId, ParamsFishingDiary paramsFishingDiary) {
        FishingDiary fishingDiary = getFishingDiary(fishingDiaryId);
        Ship ship = getShip(shipId);
        Places places = getPlaces(placesId);

        Member member = fishingDiary.getCreatedBy();
        if (!uid.equals(member.getUid()))
            throw new CNotOwnerException();

        // 영속성 컨텍스트의 변경감지(dirty checking) 기능에 의해 조회한 FishingDiary내용을 변경만 해도 Update쿼리가 실행됩니다.
        fishingDiary.setUpdate(member, ship, places, paramsFishingDiary);
        return fishingDiary;
    }



    // 게시글을 삭제합니다. 게시글 등록자와 로그인 회원정보가 틀리면 CNotOwnerException 처리합니다.
    public boolean deleteFishingDiary(long fishingDiaryId, String uid) {
        FishingDiary fishingDiary = getFishingDiary(fishingDiaryId);
        Member member = fishingDiary.getCreatedBy();
        if (!uid.equals(member.getUid()))
            throw new CNotOwnerException();


        fishingDiary.getFishingLures().removeAll(fishingDiary.getFishingLures());
        fishingDiary.getFishSpecies().removeAll(fishingDiary.getFishSpecies());

        fishingDiaryRepo.delete(fishingDiary);

        return true;
    }
    

*/
/*
    public Optional<FishingDiary>  getFishingDiary(Long fishingDiaryId) {
        return fishingDiaryRepository.findById(fishingDiaryId)
                .orElseThrow(() -> new ResourceNotFoundException("FishingDiary not found for this id :: " + fishingDiaryId));
    }*/


    /*마이페이지 - 게시글에서 Page형식으로 뿌려줄*/



}