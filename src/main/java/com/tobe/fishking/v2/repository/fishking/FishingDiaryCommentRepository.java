package com.tobe.fishking.v2.repository.fishking;


import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Comment;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.FishingDiaryComment;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FishingDiaryCommentRepository extends BaseRepository<FishingDiaryComment, Long> {

    //
 //   Page<FishingDiary> findFishingDiariesByFishSpeciesIsContaining(String fishSpeciesName, Pageable pageable, Integer totalElements);
   // Page<FishingDiary> findFishingDiariesByFishSpeciesIsContaining(Pageable pageable, Integer totalElements);
    //조행기, 조행일지
 //   Page<FishingDiaryComment> findFishingDiariesByBoard_BoardTypeEquals(Board board,Pageable pageable, Integer totalElements);
    //조행기, 조행일지  + 어종
 //   Page<FishingDiaryComment> findFishingDiariesByBoardEqualsAndFishingSpeciesName(Board board,String fishSpeciesName, Pageable pageable, Integer totalElements);


    Page<FishingDiaryComment> findByFishingDiaryId(Long fishingDiaryId, Pageable pageable , Integer totalElements);
    Optional<FishingDiaryComment> findByIdAndFishingDiaryId(Long id, Long fishingDiaryId);
}

