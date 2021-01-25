package com.tobe.fishking.v2.service.fishking;


import com.tobe.fishking.v2.entity.FileEntity;
import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.Ship;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.OperatorType;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.enums.fishing.FishingLure;
import com.tobe.fishking.v2.enums.fishing.FishingTechnic;
import com.tobe.fishking.v2.enums.fishing.TideTime;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.MapInfoDTO;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDTO;
import com.tobe.fishking.v2.model.fishing.WriteFishingDiaryDto;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.common.CodeGroupRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import com.tobe.fishking.v2.repository.common.FileRepository;
import com.tobe.fishking.v2.repository.common.PopularRepository;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryRepository;
import com.tobe.fishking.v2.repository.fishking.PlacesRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.repository.fishking.specs.FishingDiarySpecs;
import com.tobe.fishking.v2.utils.NativeResultProcessUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Tuple;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Ship ship = shipRepo.findById(dto.getShipId())
                .orElseThrow(()->new ResourceNotFoundException("ship not found for this id :: "+dto.getShipId()));
        /*fishingSpecies 문자열로 만들기*/
        String fishSpecies =null;
        if(dto.getFishingSpecies()!=null) {
            CodeGroup fishSpeciesCodeGroup = codeGroupRepo.findByCode("fishspecies");
            String[] fishSpeciesCommonCodeList = commonCodeRepo.findCodeNameByCodeList(dto.getFishingSpecies(), fishSpeciesCodeGroup);
            fishSpecies = fishSpeciesCommonCodeList[0];
            for (int i = 1; i < fishSpeciesCommonCodeList.length; i++) {
                fishSpecies += ", " + fishSpeciesCommonCodeList[i];
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
                .fishingDate(dto.getWriteDate().toString())
                .fishingTideTime(tideTime)
                .fishingTechnic(fishTechnic)
                .fishingLure(fishLure)
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
                .createdBy(author)
                .modifiedBy(author)
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
        Ship ship = shipRepo.findById(dto.getShipId())
                .orElseThrow(()->new ResourceNotFoundException("ship not found for this id :: "+dto.getShipId()));

        if(author.getRoles()!= Role.admin && author!=fishingDiary.getMember()){
            throw new RuntimeException("해당 글의 수정 권한이 없습니다.");
        }

        /*fishingSpecies 문자열로 만들기*/
        String fishSpecies =null;
        if(dto.getFishingSpecies()!=null) {
            CodeGroup fishSpeciesCodeGroup = codeGroupRepo.findByCode("fishspecies");
            String[] fishSpeciesCommonCodeList = commonCodeRepo.findCodeNameByCodeList(dto.getFishingSpecies(), fishSpeciesCodeGroup);
            fishSpecies = fishSpeciesCommonCodeList[0];
            for (int i = 1; i < fishSpeciesCommonCodeList.length; i++) {
                fishSpecies += ", " + fishSpeciesCommonCodeList[i];
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

        /*fishingDiary 수정.*/
        fishingDiary.modify(ship,dto.getTitle(),dto.getContent(), fishSpecies, tideTime, fishTechnic, fishLure, author);
        fishingDiary = fishingDiaryRepo.save(fishingDiary);

        /*기존 파일들 삭제*/
        List<FileEntity> preFileIdList = fileRepository.findByPidAndFilePublish(fishingDiary.getId(),fishingDiary.getFilePublish());
        for(int i=0; i<preFileIdList.size(); i++){
            uploadService.removeFileEntity(preFileIdList.get(i).getId());
        }

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

        return true;
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