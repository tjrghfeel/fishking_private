package com.tobe.fishking.v2.repository.fishking;


import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.enums.fishing.FishingType;
//import com.tobe.fishking.v2.model.NoNameDTO;
//import com.tobe.fishking.v2.model.NoNameDTOInterface;
import com.tobe.fishking.v2.model.common.MapInfoDTO;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDtoForPage;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Query(value = "select " +
            "   d.id id, " +
            "   s.id shipId, " +
            "   m.id memberId, " +
            "   m.profile_image profileImage, " +
            "   m.nick_name nickName, " +
            "   s.sigungu address, " +
            "   d.created_date createdDate, " +
            "   d.title title, " +
            "   LEFT(d.contents,50) contents, " +
            "   (select count(l.id) from loveto l where (l.take_type = 2 or l.take_type = 3) and l.link_id = d.id) likeCount, " +
            "   (select count(c.id) from fishing_diary_comment c where c.fishing_diary_id = d.id) commentCount, " +
            "   (select count(dc.fishing_diary_id) from fishing_diary_scrap_members dc where dc.fishing_diary_id = d.id) scrapCount, " +
            "   (select GROUP_CONCAT(f2.stored_file separator ',') " +
            "       from files f2 where f2.pid = d.id and (f2.file_publish = 5 or f2.file_publish = 6)" +
            "       group by f2.pid order by f2.file_no) fileNameList, " +
            "   (select GROUP_CONCAT(f2.file_url separator ',') " +
            "       from files f2 where f2.pid = d.id and (f2.file_publish = 5 or f2.file_publish = 6)" +
            "       group by f2.pid order by f2.file_no) filePathList " +
            "from fishing_diary as d, ship as s, Member as m  " +
            "where d.fishing_diary_member_id = :member " +
            "   and d.fishing_diary_ship_id = s.id " +
            "   and d.fishing_diary_member_id = m.id " +
            "order by d.created_date desc ",
            countQuery = "select d.id " +
                    "from fishing_diary as d, ship as s, Member as m  " +
                    "where d.fishing_diary_member_id = :member " +
                    "   and d.fishing_diary_ship_id = s.id " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "order by d.created_date desc ",
            nativeQuery = true
    )
    Page<FishingDiaryDtoForPage> findByMember(@Param("member") Member member, Pageable pageable);
    
    /*마이메뉴 - 내글관리 - 스크랩 에서 스크랩한 게시글 목록을 가져와주는 메소드*/
    @Query(value = "select " +
            "   d.id id, " +
            "   s.id shipId, " +
            "   m.id memberId, " +
            "   m.profile_image profileImage, " +
            "   m.nick_name nickName, " +
            "   s.sigungu address, " +
            "   d.created_date createdDate, " +
            "   d.title title, " +
            "   d.file_publish fishingType, " +
            "   LEFT(d.contents,50) contents, " +
            "   (select case when exists (select l.id from loveto as l " +
            "       where l.created_by=:member and (l.take_type=2 or l.take_type=3) and link_id=d.id) then 'true' else 'false' end) isLikeTo, " +
            "   (select count(l.id) from loveto l where (l.take_type = 2 or l.take_type = 3) and l.link_id = d.id) likeCount, " +
            "   (select count(c.id) from fishing_diary_comment c where c.fishing_diary_id = d.id) commentCount, " +
            "   (select count(dc.fishing_diary_id) from fishing_diary_scrap_members dc where dc.fishing_diary_id = d.id) scrapCount, " +
            "   (select GROUP_CONCAT(f2.stored_file separator ',') " +
            "       from files f2 where f2.pid = d.id and (f2.file_publish = 5 or f2.file_publish = 6)" +
            "       group by f2.pid order by f2.file_no) fileNameList, " +
            "   (select GROUP_CONCAT(f2.file_url separator ',') " +
            "       from files f2 where f2.pid = d.id and (f2.file_publish = 5 or f2.file_publish = 6)" +
            "       group by f2.pid order by f2.file_no) filePathList " +
            "from fishing_diary as d, ship as s, Member as m, fishing_diary_scrap_members as sm " +
            "where sm.scrap_members_id = :member " +
            "   and sm.fishing_diary_id = d.id " +
            "   and d.fishing_diary_ship_id = s.id " +
            "   and d.fishing_diary_member_id = m.id " +
            "order by d.created_date desc ",
            countQuery = "select d.id " +
                    "from fishing_diary as d, ship as s, Member as m, fishing_diary_scrap_members as sm " +
                    "where sm.scrap_members_id = :member " +
                    "   and sm.fishing_diary_id = d.id " +
                    "   and d.fishing_diary_ship_id = s.id " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "order by d.created_date desc ",
            nativeQuery = true
    )
    Page<FishingDiaryDtoForPage> findByScrapMembers(@Param("member") Member member, Pageable pageable);

    /*회원탈퇴시, 회원이한 스크랩목록을 삭제하기위해 manyToMany 연결테이블의 row를 삭제해주는 메소드*/
    @Modifying
    @Query(value = "delete from fishing_diary_scrap_members " +
            "where scrap_members_id = :member " +
            "   or fishing_diary_id in (select d.id from fishing_diary d where d.fishing_diary_member_id = :member)",
            nativeQuery = true
    )
    int deleteAllScrapByMember(@Param("member") Member member);

    /*회원탈퇴시 회원이 작성한 fishingDiary를 비활성화 처리해주는 메소드.*/
    /*@Modifying
    @Query("update FishingDiary f set f.isActive = false where f.member = :member")
    int updateIsActiveByMember(@Param("member") Member member);*/


    /* 조항일지, 조행기, 낚시포인트 위*경도 */
    @Query(nativeQuery = true,value = "SELECT re.id, re.latitude, re.longitude,re.type FROM ( " +
            "SELECT  board.id AS id, a.writeLatitude AS latitude, a.writeLongitude AS latitude, 1 AS type " +
            "FROM FishingDiary a where  a.writeLatitude is not null  and  a.writeLatitude is not null " +
            "UNION ALL " +
            "SELECT  999 AS id,  b.Latitude AS latitude, b.Longitude AS latitude ,2 AS type " +
            "FROM FishingPoints b where  b.Latitude is not null  and  b.Latitude is not null   ) re")
    List<MapInfoDTO> findLatitudeAndLongitudeList();

}

