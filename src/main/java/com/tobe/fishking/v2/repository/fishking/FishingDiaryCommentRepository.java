package com.tobe.fishking.v2.repository.fishking;


import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Comment;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.FishingDiaryComment;
import com.tobe.fishking.v2.model.NoNameDTO;
import com.tobe.fishking.v2.model.fishing.FishingDiaryCommentDtoForPage;
import com.tobe.fishking.v2.model.fishing.MyFishingDiaryCommentDtoForPage;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FishingDiaryCommentRepository extends BaseRepository<FishingDiaryComment, Long> {

    //
 //   Page<FishingDiary> findFishingDiariesByFishSpeciesIsContaining(String fishSpeciesName, Pageable pageable, Integer totalElements);
   // Page<FishingDiary> findFishingDiariesByFishSpeciesIsContaining(Pageable pageable, Integer totalElements);
    //조행기, 조황일지
 //   Page<FishingDiaryComment> findFishingDiariesByBoard_BoardTypeEquals(Board board,Pageable pageable, Integer totalElements);
    //조행기, 조황일지  + 어종
 //   Page<FishingDiaryComment> findFishingDiariesByBoardEqualsAndFishingSpeciesName(Board board,String fishSpeciesName, Pageable pageable, Integer totalElements);


    Page<FishingDiaryComment> findByFishingDiaryId(Long fishingDiaryId, Pageable pageable , Integer totalElements);
    Optional<FishingDiaryComment> findByIdAndFishingDiaryId(Long id, Long fishingDiaryId);

    /*마이메뉴 - 내글관리 - 내댓글에서 내 댓글목록을 뿌려주기위한 메소드. */
    @Query(value = "select " +
            "c.id as id, " +
            "c.dependent_type as dependentType, " +
            "s.fishing_type as fishingType, " +
            "d.title as title, " +
            "c.contents as contents, " +
            "c.created_date as time," +
            "d.id as fishingDiaryId  " +
            "from fishing_diary_comment as c, fishing_diary as d join ship s on d.fishing_diary_ship_id = s.id " +
            "where c.created_by = :member " +
            "   and c.fishing_diary_id = d.id " +
            "   and c.dependent_type is not null ",
            countQuery = "select c.id " +
                    "from fishing_diary_comment as c, fishing_diary as d join ship s on d.fishing_diary_ship_id = s.id " +
                    "where c.created_by = :member " +
                    "   and c.fishing_diary_id = d.id " +
                    "   and c.dependent_type is not null ",
            nativeQuery = true
    )
    Page<MyFishingDiaryCommentDtoForPage> findByMember(@Param("member") Member member, Pageable pageable);

    /*회원탈퇴시 회원이 작성한 comment를 비활성화 처리해주는 메소드.*/
    /*@Modifying
    @Query("update FishingDiaryComment f set f.isActive = false where f.createdBy = :member")
    int updateIsActiveByMember(@Param("member") Member member);*/

    boolean existsById(Long id);

    /*게시글의 댓글 목록 검색 메소드*/
    @Query(
            value = "select " +
                    "   new com.tobe.fishking.v2.model.fishing.FishingDiaryCommentDtoForPage(" +
                    "   m.id, " +
                    "   c.id, " +
                    "   case when c.isDeleted=true " +
                    "       then (select concat(:path,extraValue1) from CommonCode where code='noImg' and codeGroup.id=92) " +
                    "       else  concat(:path,m.profileImage) end, " +
                    "   case when c.isDeleted=true then '****' else m.nickName end, " +
                    "   c.createdDate, " +
                    "   case when c.isDeleted=true then '삭제된 댓글입니다' when c.isActive=false then '숨김처리된 글입니다' else c.contents end, " +
                    "   case when c.isDeleted=true " +
                    "       then null " +
                    "       else (select concat(:path,'/',f.fileUrl,'/',f.storedFile) from FileEntity f " +
                    "               where f.pid = c.id and f.filePublish=7 and f.isDelete=false) end, " +
                    "   c.likeCount, " +
                    "   (exists (select l.id from LoveTo l where l.createdBy=:member and l.takeType=4 and l.linkId=c.id)), " +
                    "   case when c.parentId = 0 then false else true end, " +
                    "   c.parentId," +
                    "   case when c.createdBy=:member then true else false end " +
                    "   ) " +
                    "from FishingDiaryComment c join Member m on c.createdBy.id = m.id " +
                    "where " +
                    "   c.fishingDiary.id = :fishingDiaryId " +
                    "   and c.parentId = :parentId " +
                    "order by c.createdDate "
    )
    List<FishingDiaryCommentDtoForPage> getCommentList(
            @Param("fishingDiaryId") Long fishingDiaryId,
            @Param("parentId") Long parentId,
            @Param("member") Member member,
            @Param("path") String path
    );

    @Query("select new com.tobe.fishking.v2.model.NoNameDTO(" +
            "   c.id, " +
            "   c.contents, " +
            "   c.contents, " +
            "   case when c.parentId is null then true else false end " +
            ") " +
            "from FishingDiaryComment c "
//            "where case when 1 = 1 then true else false end "
    )
    List<NoNameDTO> noName();
}

