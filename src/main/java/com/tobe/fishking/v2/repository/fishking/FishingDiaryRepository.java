package com.tobe.fishking.v2.repository.fishking;


import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface FishingDiaryRepository extends BaseRepository<FishingDiary, Long> {

    //
 //   Page<FishingDiary> findFishingDiariesByFishSpeciesIsContaining(String fishSpeciesName, Pageable pageable, Integer totalElements);
   // Page<FishingDiary> findFishingDiariesByFishSpeciesIsContaining(Pageable pageable, Integer totalElements);
    //조행기, 조행일지
//    Page<FishingDiary> findFishingDiariesByBoard_BoardTypeEquals(Board board,Pageable pageable, Integer totalElements);
    Page<FishingDiary> findFishingDiariesByBoard_FilePublish(Board board,Pageable pageable, Integer totalElements);

    //조행기, 조행일지  + 어종
    Page<FishingDiary> findFishingDiariesByBoardEqualsAndFishingSpeciesName(Board board,String fishSpeciesName, Pageable pageable, Integer totalElements);
    
}

