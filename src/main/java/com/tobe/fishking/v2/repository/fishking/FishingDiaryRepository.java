package com.tobe.fishking.v2.repository.fishking;


import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDtoForPage;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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

    /*member가 작성한 유저조행기 또는 조황일지의 개수 카운트*/
    int countByMember(Member member);

    /*마이메뉴 - 내글관리 - 게시글 관리에서 뿌려줄 게시글리스트의 주요 정보들을 dto에 담아주는 메소드. */
    /*@Query(value = "select " +
            "    " +
            "from fishing_diary fd, ship s, files f " +
            "where fd.fishing_diary_ship_id = s.id " +
            "   and s.id = f.pid " +
            "   and f.file_publish = 0 " +
            "order by created_date desc ",
            countQuery = "",
            nativeQuery = true
    )
    Page<???>*/

    @Query(value = "" +
            "select " +
            "   m.profileImage profileImage, " +
            "   (select f.download_url from f where f.pid = d.id and f.file_publish = 6 ) fileList " +
            "from member m, ship s, fishing_diary d, files f " +
            "where d.fishing_diary_member_id = m.id " +
            "   and d.fishing_diary_ship_id = s.id ",
            countQuery = "select * " +
                    "from member m, ship s, fishing_diary d, files f " +
                    "where d.fishing_diary_member_id = m.id " +
                    "   and d.fishing_diary_ship_id = s.id ",
            nativeQuery = true
    )
    Page<FishingDiaryDtoForPage> findFishingDiaryDtoListForPage(Pageable pageable);
    
    /*마이메뉴 - 내글관리 - 게시글 관리에서 뿌려줄 게시글리스트의 각 게시글의 좋아요수 계산*/

    /*마이메뉴 - 내글관리 - 게시글 관리에서 뿌려줄 게시글리스트의 각 게시글의 댓글수 계산*/

    /*마이메뉴 - 내글관리 - 스크랩 에서 스크랩한 게시글 목록을 가져와주는 메소드*/


}

