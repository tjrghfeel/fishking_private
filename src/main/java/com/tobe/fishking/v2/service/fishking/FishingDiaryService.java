package com.tobe.fishking.v2.service.fishking;

import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.common.Popular;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.common.OperatorType;
import com.tobe.fishking.v2.enums.common.SearchPublish;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDTO;
import com.tobe.fishking.v2.repository.auth.MemberRepository;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import com.tobe.fishking.v2.repository.common.CodeGroupRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
import com.tobe.fishking.v2.repository.common.PopularRepository;
import com.tobe.fishking.v2.repository.fishking.FishingDiaryRepository;
import com.tobe.fishking.v2.repository.fishking.PlacesRepository;
import com.tobe.fishking.v2.repository.fishking.ShipRepository;
import com.tobe.fishking.v2.repository.fishking.specs.FishingDiarySpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

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

    private final BoardRepository boardRepo;


    //검색 -- 통합검색
    public Page<FishingDiaryDTO> getFishingDiaryListLike(Board board,
                                                         @RequestParam(required = false) String searchRequest,
                                                         Pageable pageable,
                                                         Integer totalElement) {

        //조행일지중에 어종명으로  검색
        Page<FishingDiary> fishingDiary = searchRequest.isEmpty()
                ? fishingDiaryRepo.findFishingDiariesByBoard_BoardTypeEquals(board, pageable, totalElement)
                : fishingDiaryRepo.findFishingDiariesByBoardEqualsAndFishingSpeciesName(board, searchRequest, pageable, totalElement);

        popularRepo.save(new Popular(SearchPublish.TOTAL, searchRequest, memberRepo.getOne((long) 5)));

        return fishingDiary.map(FishingDiaryDTO::of);
    }


    public Page<FishingDiaryDTO> getFishingDiaryList(Pageable pageable, Integer totalElements) {
        Page<FishingDiary> fishingDiary = fishingDiaryRepo.findAll(pageable, totalElements);
        return fishingDiary.map(FishingDiaryDTO::of);
    }

    //검색 --
    public Page<FishingDiaryDTO> getFishingDiaryList(Pageable pageable, OperatorType orType,
                                                     @RequestParam(required = false) Map<String,  ///total를 제외한 모든 것 조회
                                                             Object> searchRequest, Integer totalElement) {

        Map<FishingDiarySpecs.SearchKey, Object> searchKeys = new HashMap<>();
        for (String key : searchRequest.keySet()) {
            searchKeys.put(FishingDiarySpecs.SearchKey.valueOf(key.toUpperCase()), searchRequest.get(key));
        }

        Page<FishingDiary> fishingDiary = null;

        if (OperatorType.and == orType) {
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


        return fishingDiary.map(FishingDiaryDTO::of);
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

}