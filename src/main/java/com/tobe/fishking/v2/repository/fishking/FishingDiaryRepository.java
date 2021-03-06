package com.tobe.fishking.v2.repository.fishking;



import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.enums.fishing.FishingType;
//import com.tobe.fishking.v2.model.NoNameDTO;
//import com.tobe.fishking.v2.model.NoNameDTOInterface;
import com.tobe.fishking.v2.model.common.MapInfoDTO;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDTO;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDtoForPage;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface FishingDiaryRepository extends BaseRepository<FishingDiary, Long>, FishingDiaryCustom {

    //
 //   Page<FishingDiary> findFishingDiariesByFishSpeciesIsContaining(String fishSpeciesName, Pageable pageable, Integer totalElements);
   // Page<FishingDiary> findFishingDiariesByFishSpeciesIsContaining(Pageable pageable, Integer totalElements);
    //조행기, 조황일지
//    Page<FishingDiary> findFishingDiariesByBoard_BoardTypeEquals(Board board,Pageable pageable, Integer totalElements);
    Page<FishingDiary> findFishingDiariesByBoard_FilePublish(Board board,Pageable pageable, Integer totalElements);

    //조행기, 조황일지  + 어종
    Page<FishingDiary> findFishingDiariesByBoardEqualsAndFishingSpeciesName(Board board,String fishSpeciesName, Pageable pageable, Integer totalElements);

    /*member가 작성한 유저조행기 또는 조황일지의 개수 카운트*/
    int countByMember(Member member);

    /*마이메뉴 - 내글관리 - 게시글 관리에서 뿌려줄 게시글리스트의 주요 정보들을 dto에 담아주는 메소드. */
    @Query(value = "select " +
            "   d.id id, " +
            "   if(m.is_active = true, m.profile_image, (select c.extra_value1 from common_code c where code_group_id=92 and code='noImg')) profileImage, " +
            "   (select case " +
            "       when d.file_publish = 5 then if(m.is_active=true, m.nick_name, '탈퇴한 회원입니다') " +
            "       when d.file_publish = 6 then s.ship_name " +
            "       end " +
            "   ) nickName, " +
            "   s.id shipId, " +
            "   m.id memberId, " +
            "   s.sigungu address, " +
            "   d.created_date createdDate, " +
            "   if(m.is_active=true, d.title, '탈퇴한 회원의 글입니다.') title, " +
            "   s.fishing_type fishingType, " +
            "   if(m.is_active=true, LEFT(d.contents,50), '탈퇴한 회원의 글입니다.') contents, " +
            "   d.file_publish fishingDiaryType, " +
            "   (select case when exists (select l.id from loveto as l " +
            "       where l.created_by=:member and (l.take_type=2 or l.take_type=3) and link_id=d.id) then 'true' else 'false' end) isLikeTo, " +
            "   (select case when exists (select dm.fishing_diary_id from fishing_diary_scrap_members dm " +
            "       where dm.scrap_members_id = :member and dm.fishing_diary_id = d.id) then 'true' else 'false' end) isScraped, " +
            "   d.like_count likeCount, " +
            "   d.comment_count commentCount, " +
            "   d.share_count scrapCount, " +
//            "   (select count(l.id) from loveto l where (l.take_type = 2 or l.take_type = 3) and l.link_id = d.id) likeCount, " +
//            "   (select count(c.id) from fishing_diary_comment c where c.fishing_diary_id = d.id) commentCount, " +
//            "   (select count(dc.fishing_diary_id) from fishing_diary_scrap_members dc where dc.fishing_diary_id = d.id) scrapCount, " +
            "   if(m.is_active=true, (select GROUP_CONCAT(f2.stored_file separator ',') " +
            "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f.is_delete = false " +
            "       group by f2.pid order by f2.file_no), null) fileNameList, " +
            "   if(m.is_active=true, (select GROUP_CONCAT(f2.file_url separator ',') " +
            "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f.is_delete = false " +
            "       group by f2.pid order by f2.file_no), null) filePathList, " +
            "   if(m.is_active=true, (select f2.id " +
            "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=3 and f.is_delete = false), null) videoId " +
            "from fishing_diary as d, ship as s, Member as m  " +
            "where d.fishing_diary_member_id = :user " +
            "   and d.fishing_diary_ship_id = s.id " +
            "   and d.fishing_diary_member_id = m.id " +
            "   and d.is_deleted = false " +
            "   and m.is_active = true " +
            "order by d.created_date desc ",
            countQuery = "select d.id " +
                    "from fishing_diary as d, ship as s, Member as m  " +
                    "where d.fishing_diary_member_id = :user " +
                    "   and d.fishing_diary_ship_id = s.id " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    "order by d.created_date desc ",
            nativeQuery = true
    )
    Page<FishingDiaryDtoForPage> findByMember(@Param("user") Member user, @Param("member") Member member, Pageable pageable);
    
    /*마이메뉴 - 내글관리 - 스크랩 에서 스크랩한 게시글 목록을 가져와주는 메소드*/
    @Query(value = "select " +
            "   d.id id, " +
            "   if(m.is_active = true, m.profile_image, (select c.extra_value1 from common_code c where code_group_id=92 and code='noImg')) profileImage, " +
            "   (select case " +
            "       when d.file_publish = 5 then if(m.is_active=true, m.nick_name, '탈퇴한 회원입니다') " +
            "       when d.file_publish = 6 then s.ship_name " +
            "       end " +
            "   ) nickName, " +
            "   s.id shipId, " +
            "   m.id memberId, " +
            "   s.sigungu address, " +
            "   d.created_date createdDate, " +
            "   if(m.is_active=true, d.title, '탈퇴한 회원의 글입니다.') title, " +
            "   s.fishing_type fishingType, " +
            "   if(m.is_active=true, LEFT(d.contents,50), '탈퇴한 회원의 글입니다.') contents, " +
            "   (select case when exists (select l.id from loveto as l " +
            "       where l.created_by=:member and (l.take_type=2 or l.take_type=3) and link_id=d.id) then 'true' else 'false' end) isLikeTo, " +
            "   (select case when exists (select dm.fishing_diary_id from fishing_diary_scrap_members dm " +
            "       where dm.scrap_members_id = :member and dm.fishing_diary_id = d.id) then 'true' else 'false' end) isScraped, " +
            "   d.like_count likeCount, " +
            "   d.comment_count commentCount, " +
            "   d.share_count scrapCount, " +
            "   d.file_publish fishingDiaryType, " +
//            "   (select count(l.id) from loveto l where (l.take_type = 2 or l.take_type = 3) and l.link_id = d.id) likeCount, " +
//            "   (select count(c.id) from fishing_diary_comment c where c.fishing_diary_id = d.id) commentCount, " +
//            "   (select count(dc.fishing_diary_id) from fishing_diary_scrap_members dc where dc.fishing_diary_id = d.id) scrapCount, " +
            "   if(m.is_active=true, (select GROUP_CONCAT(f2.stored_file separator ',') " +
            "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f.is_delete = false " +
            "       group by f2.pid order by f2.file_no), null) fileNameList, " +
            "   if(m.is_active=true, (select GROUP_CONCAT(f2.file_url separator ',') " +
            "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f.is_delete = false " +
            "       group by f2.pid order by f2.file_no), null) filePathList, " +
            "   if(m.is_active=true, (select f2.id " +
            "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=3 and f.is_delete = false ), null) videoId " +
            "from fishing_diary as d, ship as s, Member as m, fishing_diary_scrap_members as sm  " +
            "where sm.scrap_members_id = :member " +
            "   and sm.fishing_diary_id = d.id " +
            "   and d.fishing_diary_ship_id = s.id " +
            "   and d.fishing_diary_member_id = m.id " +
            "   and d.is_deleted = false " +
            "   and m.is_active = true " +
            "order by d.created_date desc ",
            countQuery = "select d.id " +
                    "from fishing_diary as d, ship as s, Member as m, fishing_diary_scrap_members as sm  " +
                    "where sm.scrap_members_id = :member " +
                    "   and sm.fishing_diary_id = d.id " +
                    "   and d.fishing_diary_ship_id = s.id " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    "order by d.created_date desc ",
            nativeQuery = true
    )
    Page<FishingDiaryDtoForPage> findByScrapMembers(@Param("member") Member member, Pageable pageable);

    /*내글 관리 - 스크랩*/
    @Query(
            value = "select " +
                    "   d.id id, " +
                    "   s.id shipId, " +
                    "   m.id memberId, " +
                    "   d.created_date as createdDate, " +
                    "   if(m.is_active = false or d.is_deleted = true, (select c.extra_value1 from common_code c where code_group_id=92 and code='noImg'), m.profile_image ) profileImage, " +
                    "   (select case " +
                    "       when d.is_deleted = true then '****' " +
                    "       when d.file_publish = 5 then if(m.is_active=true, m.nick_name, '탈퇴한 회원입니다') " +
                    "       when d.file_publish = 6 then s.ship_name " +
                    "       end " +
                    "   ) nickName, " +
                    "   if(d.fishing_diary_ship_id is null, d.fishing_location, s.address) address, " +
                    "   if(m.is_active=true, if(d.is_deleted=true,'삭제된 게시글입니다',d.title), '탈퇴한 회원의 글입니다.') title, " +
                    "   d.fishing_type fishingType, " +
                    "   if(m.is_active=true, if(d.is_deleted=true,'삭제된 게시글입니다',LEFT(d.contents,50)), '탈퇴한 회원의 글입니다.') contents, " +
                    "   (select case when exists (select l.id from loveto as l " +
                    "       where l.created_by=:memberId and (l.take_type=2 or l.take_type=3) and link_id=d.id) then 'true' else 'false' end) isLikeTo, " +
                    "   (select case when exists (select dm.fishing_diary_id from fishing_diary_scrap_members dm " +
                    "       where dm.scrap_members_id = :memberId and dm.fishing_diary_id = d.id) then 'true' else 'false' end) isScraped, " +
                    "   d.like_count likeCount, " +
                    "   d.comment_count commentCount, " +
                    "   d.share_count scrapCount, " +
                    "   d.file_publish fishingDiaryType, " +
                    "   if(m.is_active=false or d.is_deleted=true, null, (select GROUP_CONCAT(f2.stored_file separator ',') " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f2.is_delete = false " +
                    "       group by f2.pid order by f2.file_no)) fileNameList, " +
                    "   if(m.is_active=false or d.is_deleted=true, null, (select GROUP_CONCAT(f2.file_url separator ',') " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f2.is_delete = false " +
                    "       group by f2.pid order by f2.file_no)) filePathList, " +
                    "   if(m.is_active=false or d.is_deleted=true, null, (select f2.id " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=3 and f2.is_delete = false )) videoId " +
                    "from fishing_diary d left join ship s on d.fishing_diary_ship_id=s.id, member m, fishing_diary_scrap_members as sm  " +
                    "where sm.scrap_members_id = :memberId " +
                    "   and sm.fishing_diary_id = d.id " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    " group by d.id " +
                    "order by createdDate desc ",
            countQuery = "select d.id " +
                    "from fishing_diary d left join ship s on d.fishing_diary_ship_id=s.id, member m, fishing_diary_scrap_members as sm  " +
                    "where sm.scrap_members_id = :memberId " +
                    "   and sm.fishing_diary_id = d.id " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    " group by d.id ",
            nativeQuery = true
    )
    Page<FishingDiaryDtoForPage> getMyScrapList(
            @Param("memberId") Long memberId,
            Pageable pageable
    );

    /*어복스토리 - 조항일지 목록 조회용 메소드 - 최신순정렬*/
    @Query(
            value = "select " +
                    "   d.id id, " +
                    "   s.id shipId, " +
                    "   m.id memberId, " +
                    "   d.created_date as createdDate, " +
                    "   m.profile_image profileImage, " +
                    "   (select case " +
                    "       when d.file_publish = 5 then m.nick_name " +
                    "       when d.file_publish = 6 then if(d.fishing_diary_ship_id is null, m.nick_name, s.ship_name) " +
                    "       else m.nick_name " +
                    "       end " +
                    "   ) nickName, " +
                    "   if(d.fishing_diary_ship_id is null, d.fishing_location, s.address) address, " +
                    "   if(:isManager = true or d.is_hidden = false, d.title, '숨김처리된 글입니다.') title, " +
                    "   d.fishing_type fishingType, " +
                    "   if(:isManager = true or d.is_hidden = false, LEFT(d.contents,50), '숨김처리된 글입니다.') contents, " +
                    "   d.file_publish fishingDiaryType, " +
                    "   (select case when exists (select v.id from realtime_video as v " +
                    "       where v.rtvideos_ship_id=s.id) then 'true' else 'false' end) hasLiveCam, " +
                    "   (select case when exists (select l.id from loveto as l " +
                    "       where l.created_by=:memberId and (l.take_type=2 or l.take_type=3) and link_id=d.id) then 'true' else 'false' end) isLikeTo, " +
                    "   (select case when exists (select dm.fishing_diary_id from fishing_diary_scrap_members dm " +
                    "       where dm.scrap_members_id = :memberId and dm.fishing_diary_id = d.id) then 'true' else 'false' end) isScraped, " +
                    "   d.like_count likeCount, " +
                    "   d.comment_count commentCount, " +
                    "   d.share_count scrapCount, " +
                    "   if(:isManager = true or d.is_hidden = false, (select GROUP_CONCAT(f2.stored_file separator ',') " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f2.is_delete = false " +
                    "       group by f2.pid order by f2.file_no), null) fileNameList, " +
                    "   if(:isManager = true or d.is_hidden = false, (select GROUP_CONCAT(f2.file_url separator ',') " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f2.is_delete = false " +
                    "       group by f2.pid order by f2.file_no), null) filePathList, " +
                    "   if(:isManager = true or d.is_hidden = false, (select f2.id " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=3 and f2.is_delete = false ), null) videoId, " +
                    //관리자페이지에서 추가로 사용할 항목들
                    "   d.is_active isActive, " +
                    "   s.ship_name shipName, " +
                    "   m.nick_name memberNickName, " +
                    "   d.is_deleted isDelete, " +
                    "   d.is_hidden isHidden,  " +
                    "   (select count(a.id) from accuse a where a.link_id=d.id and a.target_type=11) accuseCount " +
                    "from fishing_diary d left join ship s on d.fishing_diary_ship_id=s.id, member m " +
                    "where  " +
                    "   if(:category is null, true, d.file_publish = :category) " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and if(:myPost, m.id = :memberId, true) " +
                    "   and if(:userId is null, true, m.id = :userId) "+
                    "   and ( if(:district1 is null, true, s.address regexp :district1) " +
                    "           or if(:district1 is null, true, d.fishing_location regexp :district1)) "+
                    "   and ( if(:district2Regex is null, true, s.address regexp :district2Regex) " +
                    "           or if(:district2Regex is null, true, d.fishing_location regexp :district2Regex)) " +
                    "   and if(:fishSpeciesRegex is null, true, d.fishing_species_name regexp :fishSpeciesRegex) " +
                    "   and if(:searchTarget = 'address',(s.address like %:searchKey%) or (d.fishing_location like %:searchKey%),true) " +
                    "   and if(:searchTarget = 'title',d.title like %:searchKey%,true) " +
                    "   and if(:searchTarget = 'content',d.contents like %:searchKey%, true) " +
                    "   and if(:shipId is null, true, s.id = :shipId) " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    "   and if(:isManager = true, true, d.is_active = true) " +
                    //관리자 조회시 사용되는 조건들.
                    "   and if(:shipName is null, true, s.ship_name like %:shipName%) " +
                    "   and if(:nickName is null, true, m.nick_name like %:nickName%) " +
                    "   and if(:title is null, true, d.title like %:title%) " +
                    "   and if(:content is null, true, d.contents like %:content%) " +
                    "   and if(:createdDateStart is null, true, d.created_date >= :createdDateStart) " +
                    "   and if(:createdDateEnd is null, true, d.created_date <= :createdDateEnd) " +
                    "   and if(:hasShipData is null, true, " +
                    "       if(:hasShipData = true, d.fishing_diary_ship_id is not null, d.fishing_diary_ship_id is null)) " +
                    " group by d.id " +
                    "order by d.created_date desc ",
            countQuery = "select d.id " +
                    "from fishing_diary d left join ship s on d.fishing_diary_ship_id=s.id, member m " +
                    "where  " +
                    "   if(:category is null, true, d.file_publish = :category) " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and if(:myPost, m.id = :memberId, true) " +
                    "   and if(:userId is null, true, m.id = :userId) "+
                    "   and ( if(:district1 is null, true, s.address regexp :district1) " +
                    "           or if(:district1 is null, true, d.fishing_location regexp :district1)) "+
                    "   and ( if(:district2Regex is null, true, s.address regexp :district2Regex) " +
                    "           or if(:district2Regex is null, true, d.fishing_location regexp :district2Regex)) " +
                    "   and if(:fishSpeciesRegex is null, true, d.fishing_species_name regexp :fishSpeciesRegex) " +
                    "   and if(:searchTarget = 'address',(s.address like %:searchKey%) or (d.fishing_location like %:searchKey%),true) " +
                    "   and if(:searchTarget = 'title',d.title like %:searchKey%,true) " +
                    "   and if(:searchTarget = 'content',d.contents like %:searchKey%, true) " +
                    "   and if(:shipId is null, true, s.id = :shipId) " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    "   and if(:isManager = true, true, d.is_active = true) " +
                    //관리자 조회시 사용되는 조건들.
                    "   and if(:shipName is null, true, s.ship_name like %:shipName%) " +
                    "   and if(:nickName is null, true, m.nick_name like %:nickName%) " +
                    "   and if(:title is null, true, d.title like %:title%) " +
                    "   and if(:content is null, true, d.contents like %:content%) " +
                    "   and if(:createdDateStart is null, true, d.created_date >= :createdDateStart) " +
                    "   and if(:createdDateEnd is null, true, d.created_date <= :createdDateEnd) " +
                    "   and if(:hasShipData is null, true, " +
                    "       if(:hasShipData = true, d.fishing_diary_ship_id is not null, d.fishing_diary_ship_id is null)) " +
                    " group by d.id "+
                    "order by d.created_date desc ",
            nativeQuery = true
    )
    Page<FishingDiaryDtoForPage> getFishingDiaryListOrderByCreatedDate(
            @Param("category") Integer category,
            @Param("district1") String district1,
            @Param("district2Regex") String district2Regex,
            @Param("fishSpeciesRegex") String fishSpeciesRegex,
            @Param("searchKey") String searchKey,
            @Param("userId") Long userId,
            @Param("memberId") Long memberId,
            @Param("myPost") Boolean myPost,
            @Param("searchTarget") String searchTarget,
            @Param("shipId") Long shipId,
            //아래는 관리자에서 조회시 사용되는 인자들.
            @Param("shipName") String shipName,
            @Param("nickName") String nickName,
            @Param("title") String title,
            @Param("content") String content,
            @Param("createdDateStart") LocalDate createdDateStart,
            @Param("createdDateEnd") LocalDate createdDateEnd,
            @Param("hasShipData") Boolean hasShipData,
            @Param("isManager") Boolean isManager,
            Pageable pageable
    );
    /*어복스토리 - 조항일지 목록 조회용 메소드 - 신고순*/
    @Query(
            value = "select " +
                    "   d.id id, " +
                    "   s.id shipId, " +
                    "   m.id memberId, " +
                    "   d.created_date as createdDate, " +
                    "   m.profile_image profileImage, " +
                    "   (select case " +
                    "       when d.file_publish = 5 then m.nick_name " +
                    "       when d.file_publish = 6 then if(d.fishing_diary_ship_id is null, m.nick_name, s.ship_name)  " +
                    "       else m.nick_name " +
                    "       end " +
                    "   ) nickName, " +
                    "   if(d.fishing_diary_ship_id is null, d.fishing_location, s.address) address, " +
                    "   if(:isManager = true or d.is_hidden = false, d.title, '숨김처리된 글입니다.') title, " +
                    "   d.fishing_type fishingType, " +
                    "   if(:isManager = true or d.is_hidden = false, LEFT(d.contents,50), '숨김처리된 글입니다.') contents, " +
                    "   d.file_publish fishingDiaryType, " +
                    "   (select case when exists (select v.id from realtime_video as v " +
                    "       where v.rtvideos_ship_id=s.id) then 'true' else 'false' end) hasLiveCam, " +
                    "   (select case when exists (select l.id from loveto as l " +
                    "       where l.created_by=:memberId and (l.take_type=2 or l.take_type=3) and link_id=d.id) then 'true' else 'false' end) isLikeTo, " +
                    "   (select case when exists (select dm.fishing_diary_id from fishing_diary_scrap_members dm " +
                    "       where dm.scrap_members_id = :memberId and dm.fishing_diary_id = d.id) then 'true' else 'false' end) isScraped, " +
                    "   d.like_count likeCount, " +
                    "   d.comment_count commentCount, " +
                    "   d.share_count scrapCount, " +
                    "   if(:isManager = true or d.is_hidden = false, (select GROUP_CONCAT(f2.stored_file separator ',') " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f2.is_delete = false " +
                    "       group by f2.pid order by f2.file_no), null) fileNameList, " +
                    "   if(:isManager = true or d.is_hidden = false, (select GROUP_CONCAT(f2.file_url separator ',') " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f2.is_delete = false " +
                    "       group by f2.pid order by f2.file_no), null) filePathList, " +
                    "   if(:isManager = true or d.is_hidden = false, (select f2.id " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=3 and f2.is_delete = false ), null) videoId, " +
                    //관리자페이지에서 추가로 사용할 항목들
                    "   d.is_active isActive, " +
                    "   s.ship_name shipName, " +
                    "   m.nick_name memberNickName, " +
                    "   d.is_deleted isDelete, " +
                    "   d.is_hidden isHidden,  " +
                    "   (select count(a.id) from accuse a where a.link_id=d.id and a.target_type=11) accuseCount " +
                    "from fishing_diary d left join ship s on d.fishing_diary_ship_id=s.id, member m " +
                    "where  " +
                    "   if(:category is null, true, d.file_publish = :category) " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and if(:myPost, m.id = :memberId, true) " +
                    "   and if(:userId is null, true, m.id = :userId) "+
                    "   and ( if(:district1 is null, true, s.address regexp :district1) " +
                    "           or if(:district1 is null, true, d.fishing_location regexp :district1)) "+
                    "   and ( if(:district2Regex is null, true, s.address regexp :district2Regex) " +
                    "           or if(:district2Regex is null, true, d.fishing_location regexp :district2Regex)) " +
                    "   and if(:fishSpeciesRegex is null, true, d.fishing_species_name regexp :fishSpeciesRegex) " +
                    "   and if(:searchTarget = 'address',(s.address like %:searchKey%) or (d.fishing_location like %:searchKey%),true) " +
                    "   and if(:searchTarget = 'title',d.title like %:searchKey%,true) " +
                    "   and if(:searchTarget = 'content',d.contents like %:searchKey%, true) " +
                    "   and if(:shipId is null, true, s.id = :shipId) " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    "   and if(:isManager = true, true, d.is_active = true) " +
                    //관리자 조회시 사용되는 조건들.
                    "   and if(:shipName is null, true, s.ship_name like %:shipName%) " +
                    "   and if(:nickName is null, true, m.nick_name like %:nickName%) " +
                    "   and if(:title is null, true, d.title like %:title%) " +
                    "   and if(:content is null, true, d.contents like %:content%) " +
                    "   and if(:createdDateStart is null, true, d.created_date >= :createdDateStart) " +
                    "   and if(:createdDateEnd is null, true, d.created_date <= :createdDateEnd) " +
                    "   and if(:hasShipData is null, true, " +
                    "       if(:hasShipData = true, d.fishing_diary_ship_id is not null, d.fishing_diary_ship_id is null)) " +
                    " group by d.id " +
                    "order by d.created_date desc ",
            countQuery = "select d.id " +
                    "from fishing_diary d left join ship s on d.fishing_diary_ship_id=s.id, member m " +
                    "where  " +
                    "   if(:category is null, true, d.file_publish = :category) " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and if(:myPost, m.id = :memberId, true) " +
                    "   and if(:userId is null, true, m.id = :userId) "+
                    "   and ( if(:district1 is null, true, s.address regexp :district1) " +
                    "           or if(:district1 is null, true, d.fishing_location regexp :district1)) "+
                    "   and ( if(:district2Regex is null, true, s.address regexp :district2Regex) " +
                    "           or if(:district2Regex is null, true, d.fishing_location regexp :district2Regex)) " +
                    "   and if(:fishSpeciesRegex is null, true, d.fishing_species_name regexp :fishSpeciesRegex) " +
                    "   and if(:searchTarget = 'address',(s.address like %:searchKey%) or (d.fishing_location like %:searchKey%),true) " +
                    "   and if(:searchTarget = 'title',d.title like %:searchKey%,true) " +
                    "   and if(:searchTarget = 'content',d.contents like %:searchKey%, true) " +
                    "   and if(:shipId is null, true, s.id = :shipId) " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    "   and if(:isManager = true, true, d.is_active = true) " +
                    //관리자 조회시 사용되는 조건들.
                    "   and if(:shipName is null, true, s.ship_name like %:shipName%) " +
                    "   and if(:nickName is null, true, m.nick_name like %:nickName%) " +
                    "   and if(:title is null, true, d.title like %:title%) " +
                    "   and if(:content is null, true, d.contents like %:content%) " +
                    "   and if(:createdDateStart is null, true, d.created_date >= :createdDateStart) " +
                    "   and if(:createdDateEnd is null, true, d.created_date <= :createdDateEnd) " +
                    "   and if(:hasShipData is null, true, " +
                    "       if(:hasShipData = true, d.fishing_diary_ship_id is not null, d.fishing_diary_ship_id is null)) " +
                    " group by d.id " +
                    "order by d.created_date desc ",
            nativeQuery = true
    )
    Page<FishingDiaryDtoForPage> getFishingDiaryListOrderByAccuseCount(
            @Param("category") Integer category,
            @Param("district1") String district1,
            @Param("district2Regex") String district2Regex,
            @Param("fishSpeciesRegex") String fishSpeciesRegex,
            @Param("searchKey") String searchKey,
            @Param("userId") Long userId,
            @Param("memberId") Long memberId,
            @Param("myPost") Boolean myPost,
            @Param("searchTarget") String searchTarget,
            @Param("shipId") Long shipId,
            //아래는 관리자에서 조회시 사용되는 인자들.
            @Param("shipName") String shipName,
            @Param("nickName") String nickName,
            @Param("title") String title,
            @Param("content") String content,
            @Param("createdDateStart") LocalDate createdDateStart,
            @Param("createdDateEnd") LocalDate createdDateEnd,
            @Param("hasShipData") Boolean hasShipData,
            @Param("isManager") Boolean isManager,
            Pageable pageable
    );
    /*어복스토리 - 조항일지 목록 조회용 메소드 - 좋아요순정렬*/
    @Query(
            value = "select " +
                    "   d.id id, " +
                    "   s.id shipId, " +
                    "   m.id memberId, " +
                    "   d.created_date as createdDate, " +
                    "   m.profile_image profileImage, " +
                    "   (select case " +
                    "       when d.file_publish = 5 then m.nick_name " +
                    "       when d.file_publish = 6 then if(d.fishing_diary_ship_id is null, m.nick_name, s.ship_name) " +
                    "       else m.nick_name " +
                    "       end " +
                    "   ) nickName, " +
                    "   if(d.fishing_diary_ship_id is null, d.fishing_location, s.address) address, " +
                    "   if(d.is_hidden = false, d.title, '숨김처리된 글입니다.') title, " +
                    "   d.fishing_type fishingType, " +
                    "   if(d.is_hidden = false, LEFT(d.contents,50), '숨김처리된 글입니다.') contents, " +
                    "   d.file_publish fishingDiaryType, " +
                    "   (select case when exists (select v.id from realtime_video as v " +
                    "       where v.rtvideos_ship_id=s.id) then 'true' else 'false' end) hasLiveCam, " +
                    "   (select case when exists (select l.id from loveto as l " +
                    "       where l.created_by=:memberId and (l.take_type=2 or l.take_type=3) and link_id=d.id) then 'true' else 'false' end) isLikeTo, " +
                    "   (select case when exists (select dm.fishing_diary_id from fishing_diary_scrap_members dm " +
                    "       where dm.scrap_members_id = :memberId and dm.fishing_diary_id = d.id) then 'true' else 'false' end) isScraped, " +
                    "   d.like_count likeCount, " +
                    "   d.comment_count commentCount, " +
                    "   d.share_count scrapCount, " +
                    "   if(d.is_hidden = false, (select GROUP_CONCAT(f2.stored_file separator ',') " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f2.is_delete = false " +
                    "       group by f2.pid order by f2.file_no), null) fileNameList, " +
                    "   if(d.is_hidden = false, (select GROUP_CONCAT(f2.file_url separator ',') " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f2.is_delete = false " +
                    "       group by f2.pid order by f2.file_no), null) filePathList, " +
                    "   if(d.is_hidden = false, (select f2.id " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=3 and f2.is_delete = false ), null) videoId, " +
                    "   d.is_hidden isHidden " +
                    "from fishing_diary d left join ship s on d.fishing_diary_ship_id=s.id, member m " +
                    "where  " +
                    "   if(:category is null, true, d.file_publish = :category) " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and if(:myPost, m.id = :memberId, true) " +
                    "   and if(:userId is null, true, m.id = :userId) "+
                    "   and ( if(:district1 is null, true, s.address regexp :district1) " +
                    "           or if(:district1 is null, true, d.fishing_location regexp :district1)) "+
                    "   and ( if(:district2Regex is null, true, s.address regexp :district2Regex) " +
                    "           or if(:district2Regex is null, true, d.fishing_location regexp :district2Regex)) " +
                    "   and if(:fishSpeciesRegex is null, true, d.fishing_species_name regexp :fishSpeciesRegex) " +
                    "   and if(:searchTarget = 'address',(s.address like %:searchKey%) or (d.fishing_location like %:searchKey%),true) " +
                    "   and if(:searchTarget = 'title',d.title like %:searchKey%,true) " +
                    "   and if(:searchTarget = 'content',d.contents like %:searchKey%, true) " +
                    "   and if(:shipId is null, true, s.id = :shipId) " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    "   and d.is_active = true " +
                    " group by d.id " +
                    "order by d.like_count desc ",
            countQuery = "select d.id " +
                    "from fishing_diary d left join ship s on d.fishing_diary_ship_id=s.id, member m " +
                    "where  " +
                    "   if(:category is null, true, d.file_publish = :category) " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and if(:myPost, m.id = :memberId, true) " +
                    "   and if(:userId is null, true, m.id = :userId) "+
                    "   and ( if(:district1 is null, true, s.address regexp :district1) " +
                    "           or if(:district1 is null, true, d.fishing_location regexp :district1)) "+
                    "   and ( if(:district2Regex is null, true, s.address regexp :district2Regex) " +
                    "           or if(:district2Regex is null, true, d.fishing_location regexp :district2Regex)) " +
                    "   and if(:fishSpeciesRegex is null, true, d.fishing_species_name regexp :fishSpeciesRegex) " +
                    "   and if(:searchTarget = 'address',(s.address like %:searchKey%) or (d.fishing_location like %:searchKey%),true) " +
                    "   and if(:searchTarget = 'title',d.title like %:searchKey%,true) " +
                    "   and if(:searchTarget = 'content',d.contents like %:searchKey%, true) " +
                    "   and if(:shipId is null, true, s.id = :shipId) " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    "   and d.is_active = true " +
                    " group by d.id "+
                    "order by d.like_count desc ",
            nativeQuery = true
    )
    Page<FishingDiaryDtoForPage> getFishingDiaryListOrderByLikeCount(
            @Param("category") Integer category,
            @Param("district1") String district1,
            @Param("district2Regex") String district2Regex,
            @Param("fishSpeciesRegex") String fishSpeciesRegex,
            @Param("searchKey") String searchKey,
            @Param("userId") Long userId,
            @Param("memberId") Long memberId,
            @Param("myPost") Boolean myPost,
            @Param("searchTarget") String searchTarget,
            @Param("shipId") Long shipId,
            Pageable pageable
    );
    /*어복스토리 - 조항일지 목록 조회용 메소드 - 댓글순정렬*/
    @Query(
            value = "select " +
                    "   d.id id, " +
                    "   s.id shipId, " +
                    "   m.id memberId, " +
                    "   d.created_date as createdDate, " +
                    "   m.profile_image profileImage, " +
                    "   (select case " +
                    "       when d.file_publish = 5 then if(m.is_active=true, m.nick_name, '탈퇴한 회원입니다') " +
                    "       when d.file_publish = 6 then if(d.fishing_diary_ship_id is null, m.nick_name, s.ship_name) " +
                    "       else m.nick_name " +
                    "       end " +
                    "   ) nickName, " +
                    "   if(d.fishing_diary_ship_id is null, d.fishing_location, s.address) address, " +
                    "   if(d.is_hidden = false, d.title, null) title, " +
                    "   d.fishing_type fishingType, " +
                    "   if(d.is_hidden = false, LEFT(d.contents,50), null) contents, " +
                    "   d.file_publish fishingDiaryType, " +
                    "   (select case when exists (select v.id from realtime_video as v " +
                    "       where v.rtvideos_ship_id=s.id) then 'true' else 'false' end) hasLiveCam, " +
                    "   (select case when exists (select l.id from loveto as l " +
                    "       where l.created_by=:memberId and (l.take_type=2 or l.take_type=3) and link_id=d.id) then 'true' else 'false' end) isLikeTo, " +
                    "   (select case when exists (select dm.fishing_diary_id from fishing_diary_scrap_members dm " +
                    "       where dm.scrap_members_id = :memberId and dm.fishing_diary_id = d.id) then 'true' else 'false' end) isScraped, " +
                    "   d.like_count likeCount, " +
                    "   d.comment_count commentCount, " +
                    "   d.share_count scrapCount, " +
                    "   if(d.is_hidden = false, (select GROUP_CONCAT(f2.stored_file separator ',') " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f2.is_delete = false " +
                    "       group by f2.pid order by f2.file_no), null) fileNameList, " +
                    "   if(d.is_hidden = false, (select GROUP_CONCAT(f2.file_url separator ',') " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=0 and f2.is_delete = false " +
                    "       group by f2.pid order by f2.file_no), null) filePathList, " +
                    "   if(d.is_hidden = false, (select f2.id " +
                    "       from files f2 where f2.pid = d.id and f2.file_publish = d.file_publish and file_type=3 and f2.is_delete = false ), null) videoId, " +
                    "   d.is_hidden isHidden " +
                    "from fishing_diary d left join ship s on d.fishing_diary_ship_id=s.id, member m " +
                    "where  " +
                    "   if(:category is null, true, d.file_publish = :category) " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and if(:myPost, m.id = :memberId, true) " +
                    "   and if(:userId is null, true, m.id = :userId) "+
                    "   and ( if(:district1 is null, true, s.address regexp :district1) " +
                    "           or if(:district1 is null, true, d.fishing_location regexp :district1)) "+
                    "   and ( if(:district2Regex is null, true, s.address regexp :district2Regex) " +
                    "           or if(:district2Regex is null, true, d.fishing_location regexp :district2Regex)) " +
                    "   and if(:fishSpeciesRegex is null, true, d.fishing_species_name regexp :fishSpeciesRegex) " +
                    "   and if(:searchTarget = 'address',(s.address like %:searchKey%) or (d.fishing_location like %:searchKey%),true) " +
                    "   and if(:searchTarget = 'title',d.title like %:searchKey%,true) " +
                    "   and if(:searchTarget = 'content',d.contents like %:searchKey%, true) " +
                    "   and if(:shipId is null, true, s.id = :shipId) " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    "   and d.is_active = true " +
                    " group by d.id " +
                    "order by d.comment_count desc ",
            countQuery = "select d.id " +
                    "from fishing_diary d left join ship s on d.fishing_diary_ship_id=s.id, member m " +
                    "where  " +
                    "   if(:category is null, true, d.file_publish = :category) " +
                    "   and d.fishing_diary_member_id = m.id " +
                    "   and if(:myPost, m.id = :memberId, true) " +
                    "   and if(:userId is null, true, m.id = :userId) "+
                    "   and ( if(:district1 is null, true, s.address regexp :district1) " +
                    "           or if(:district1 is null, true, d.fishing_location regexp :district1)) "+
                    "   and ( if(:district2Regex is null, true, s.address regexp :district2Regex) " +
                    "           or if(:district2Regex is null, true, d.fishing_location regexp :district2Regex)) " +
                    "   and if(:fishSpeciesRegex is null, true, d.fishing_species_name regexp :fishSpeciesRegex) " +
                    "   and if(:searchTarget = 'address',(s.address like %:searchKey%) or (d.fishing_location like %:searchKey%),true) " +
                    "   and if(:searchTarget = 'title',d.title like %:searchKey%,true) " +
                    "   and if(:searchTarget = 'content',d.contents like %:searchKey%, true) " +
                    "   and if(:shipId is null, true, s.id = :shipId) " +
                    "   and d.is_deleted = false " +
                    "   and m.is_active = true " +
                    "   and d.is_active = true " +
                    "" +
                    " group by d.id " +
                    "order by d.comment_count desc ",
            nativeQuery = true
    )
    Page<FishingDiaryDtoForPage> getFishingDiaryListOrderByCommentCount(
            @Param("category") Integer category,
            @Param("district1") String district1,
            @Param("district2Regex") String district2Regex,
            @Param("fishSpeciesRegex") String fishSpeciesRegex,
            @Param("searchKey") String searchKey,
            @Param("userId") Long userId,
            @Param("memberId") Long memberId,
            @Param("myPost") Boolean myPost,
            @Param("searchTarget") String searchTarget,
            @Param("shipId") Long shipId,
            Pageable pageable
    );


    /*@Query(
            "select " +
                    "   d.id as fishingDiaryId," +
                    "   s.id as shipId, " +
                    "   m.id as authorId, " +
                    "   d.createdDate as createdDate " +
                    "from FishingDiary d join Ship s on d.ship=s, Member m " +
                    "where  " +
                    "   d.member = m " +
                    "   and d.fishingSpecies in :commonCodeIdList " +
                    "   and case when (:districtList is null) then true else (s.sido in :districtList) end " +
//                    "   and if(:districtList is null, true, s.sido in :districtList) " +
                    "   and s.address like %:searchKey% " +
                    "   " +
                    "group by d.id "
    )
    Page<FishingDiaryDtoForPage> getFishingDiaryList2(
        @Param("districtList") String[] districtList,
        @Param("commonCodeIdList") List<CommonCode> commonCodeIdList,
        @Param("searchKey") String searchKey,
//      @Param("memberId") Long memberId,
        Pageable pageable
    );*/

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


/*
    */
/* 조항일지, 조행기, 낚시포인트 위*경도 *//*

    @Query(nativeQuery = true,value = "SELECT re.id, re.latitude, re.longitude,re.type FROM ( " +
            "SELECT  board.id AS id, a.writeLatitude AS latitude, a.writeLongitude AS latitude, 1 AS type " +
            "FROM FishingDiary a where  a.writeLatitude is not null  and  a.writeLatitude is not null " +
            "UNION ALL " +
            "SELECT  999 AS id,  b.Latitude AS latitude, b.Longitude AS latitude ,2 AS type " +
            "FROM FishingPoints b where  b.Latitude is not null  and  b.Latitude is not null   ) re")
    List<MapInfoDTO> findLatitudeAndLongitudeList();



*/
   //조행기맍 조회함(조황일지는 나중에 혹시)
    //조행기대표사진, 제목, 내용, 프사, 사용자명, 일자, 위/경도
    //mysql ifnull => coalese alias를 반드시 주어야 한다.
    @Query(value = "SELECT " +
                         " a.id   as fishingDiaryId " +
                         " , a.title   as title    " +
                         " , a.contents as contents   " +
                         " , b.downloadThumbnailUrl   as fishingDiaryRepresentUrl  " +
                         " , coalesce(a.fishingSpeciesName , '')  as fishingSpeciesName " +
                         " , a.fishingLocation  as fishingLocation" +
                         " , a.status  as status"  +
                         " , a.createdDate as createdDate" +
                        " , a.createdBy.id as createdById " +
                         " , a.createdBy.nickName as createdByNickName " +
                         ",  a.createdBy.profileImage as createdByProfileImage " +
                         " , a.writeLongitude  as writeLongitude  " +
                         " , a.writeLatitude  as  writeLatitude " +
                     " FROM FishingDiary a" +
                       "   , FileEntity  b " +
                       "    where  a.writeLongitude is not null  and  a.writeLatitude is not null  and a.filePublish = :filePublish and a.id = b.pid and b.isRepresent = true and b.id is not null ")
    List<Tuple> findAllFishingDiaryAndLocation(@Param("filePublish") FilePublish filePublish);


}

