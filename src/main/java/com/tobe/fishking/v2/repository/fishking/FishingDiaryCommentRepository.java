package com.tobe.fishking.v2.repository.fishking;


import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Comment;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.FishingDiaryComment;
import com.tobe.fishking.v2.model.fishing.FishingDiaryCommentDtoForPage;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    /*마이메뉴 - 내글관리 - 내댓글에서 내 댓글목록을 뿌려주기위한 메소드. */
    @Query("select " +
            "c.id as id, " +
            "c.dependentType as dependentType, " +
            "d.title as title, " +
            "c.contents as contents, " +
            "c.createdDate as time " +
            "from FishingDiaryComment as c, FishingDiary as d " +
            "where c.createdBy = :member " +
            "   and c.fishingDiary = d " +
            "   and c.dependentType is not null ")
    Page<FishingDiaryCommentDtoForPage> findByMember(@Param("member") Member member, Pageable pageable);

    /*회원탈퇴시 회원이 작성한 comment를 비활성화 처리해주는 메소드.*/
    /*@Modifying
    @Query("update FishingDiaryComment f set f.isActive = false where f.createdBy = :member")
    int updateIsActiveByMember(@Param("member") Member member);*/
}

