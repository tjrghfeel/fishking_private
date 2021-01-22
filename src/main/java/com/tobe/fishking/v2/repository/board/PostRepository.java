package com.tobe.fishking.v2.repository.board;



import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.model.admin.post.PostManageDtoForPage;
import com.tobe.fishking.v2.model.board.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByBoard(Board board);
  //  List<  Goods> findByBoardOrderBy  GoodsIdDesc(Board board);

    /*FAQ 리스트 조회*/
    @Query(value = "" +
            "select " +
            "   p.id id, " +
            "   p.question_type questionType, " +
            "   p.title title, " +
            "   p.contents contents, " +
            "   p.author_id authorId, " +
            "   p.created_by createdBy, " +
            "   p.modified_by modifiedBy " +
            "from post p " +
            "where p.board_id = (select b.id from board b where b.board_type = 3) " +
            "   and p.target_role = :role " +
            "order by p.question_type ",
            countQuery = "select p.id " +
                    "from post p " +
                    "where p.board_id = (select b.id from board b where b.board_type = 3) " +
                    "   and p.target_role = :role " +
                    "order by p.question_type ",
            nativeQuery = true
    )
    Page<FAQDto> findAllFAQList(@Param("role") Boolean role, Pageable pageable);

    /*QnA 리스트 조회*/
    @Query(value = "" +
            "select " +
            "   p.id id, " +
            "   p.question_type questionType, " +
            "   p.created_date createdDate, " +
            "   (select case when exists (select p2.id from post as p2 where p.id = p2.parent_id) then 'true' else 'false' end) replied " +
            "from post p " +
            "where p.board_id = (select b.id from board b where b.board_type = 2) " +
            "   and p.author_id = :member " +
            "order by  p.created_date ",
            countQuery = "select p.id " +
                    "from post p " +
                    "where p.board_id = (select b.id from board b where b.board_type = 2) " +
                    "   and p.author_id = :member " +
                    "order by p.created_date ",
            nativeQuery = true
    )
    Page<QnADtoForPage> findAllQnAList(@Param("member") Member member, Pageable pageable);

    /*QnA detail 조회*/
    @Query(value = "" +
            "select " +
            "   p.id id, " +
            "   p.question_type questionType, " +
            "   (select case when exists (select p2.id from post as p2 where p.id = p2.parent_id) then 'true' else 'false' end) replied, " +
            "   p.created_date date, " +
            "   p.contents contents, " +
            "   p.author_id authorId, " +
            "   p.author_name authorName, " +
            "   p.return_type returnType, " +
            "   p.return_no_address returnNoAddress, " +
            "   p.created_by createdBy, " +
            "   p.modified_by modifiedBy, " +
            "   (select group_concat(f.stored_file separator ',') from files f " +
            "       where f.file_publish = 2 and f.pid = p.id group by f.pid) fileNameList, " +
            "   (select group_concat(f.file_url separator ',') from files f " +
            "       where f.file_publish = 2 and f.pid = p.id group by f.pid) filePathList, " +
            "   rp.contents replyContents, " +
            "   rp.created_date replyDate, " +
            "   rp.author_id replyAuthorId, " +
            "   rp.created_by replyCreatedBy, " +
            "   rp.modified_by replyModifiedBy, " +
            "   (select group_concat(f2.thumbnail_file separator ',') from files f2 " +
            "       where f2.file_publish = 2 and f2.pid = rp.id group by f2.pid) replyFileNameList, " +
            "   (select group_concat(f2.file_url separator ',') from files f2 " +
            "       where f2.file_publish = 2 and f2.pid = rp.id group by f2.pid) replyFilePathList " +
            "from post p left outer join post rp on rp.parent_id = p.id " +
            "where p.id = :postId ",
            countQuery = "select p.id " +
                    "from post p join post rp on rp.parent_id = p.id " +
                    "where p.id = :postId ",
            nativeQuery = true
    )
    QnADetailDto findQnADetailByPostId(@Param("postId") Long postId);

    /*공지사항 리스트 페이지 반환*/
    @Query(value = "" +
            "select " +
            "   p.id id, " +
            "   p.channel_type channelType, " +
            "   p.title title, " +
            "   p.created_date date " +
            "from post p " +
            "where p.board_id = 74 " +
            "   and p.target_role = :role " +
            "order by p.created_date, p.channel_type ",
            countQuery = "select p.id from post p where p.board_id = 74 and p.target_role = :role order by p.created_date, p.channel_type ",
            nativeQuery = true
    )
    Page<NoticeDtoForPage> findNoticeList(@Param("role") Boolean role, Pageable pageable);

    /* 공지사항 detail 조회*/
    @Query(value = "" +
            "select " +
            "   p.id id, " +
            "   p.channel_type channelType, " +
            "   p.created_date date, " +
            "   p.title title, " +
            "   p.contents contents, " +
            "   (select group_concat(f.stored_file separator ',') from files f " +
            "       where f.file_publish = 4 and f.pid = p.id group by f.pid) fileNameList, " +
            "   (select group_concat(f.file_url separator ',') from files f " +
            "       where f.file_publish = 4 and f.pid = p.id group by f.pid) filePathList, " +
            "   p.author_id authorId, " +
            "   p.created_by createdBy, " +
            "   p.modified_by modifiedBy " +
            "from post p left outer join post rp on rp.parent_id = p.id " +
            "where p.id = :postId ",
            countQuery = "select p.id " +
                    "from post p join post rp on rp.parent_id = p.id " +
                    "where p.id = :postId ",
            nativeQuery = true
    )
    NoticeDetailDto findNoticeDetailByPostId(@Param("postId") Long postId);

    /*PostResponse를 Page형태로 반환해주는 메소드.
     * 반환하는 PostResponse에는 contents필드가 포함되어있지 않다. */
    @Query(
            value = "select " +
                    "p.id id, " +
                    "p.board_id boardId, " +
                    //"p.channel_type channelType, " +
                    "p.title title, " +
//                    "p.contents contents, " +
                    "p.author_id authorId, " +
                    "p.author_name authorName, " +
                    //"p.return_type returnType, " +
                    //"p.return_no_address returnNoAddress, " +
                    //"p.created_at createdAt, " +
                    //"p.created_by createdById, " +
                    //"p.modified_by modifiedById, " +
                    "p.created_date createdDate, " +
                    "p.modified_date modifiedDate, " +
                    "p.is_secret isSecret " +
//                    "p.parent_id parentId " +
                    "from post p " +
                    "where p.board_id = :boardId " +
                    "order by p.created_date desc ",
            countQuery = "select p.id from post p where p.board_id = :boardId",
            nativeQuery = true
    )
    Page<PostListDTO> findAllByBoard(@Param("boardId") Long boardId, Pageable pageable);

    /*Board의 Post 리스트를 반환하는 메소드.
     * 반환하는 PostResponse에는 Post의 모든 필드가 들어있다. */
    /*FAQ용으로 만든 메소드. PostController에 getPostList()에 적힌 이유로 일단 보류.
    @Query(
            value = "select " +
                    "p.id id, " +
                    "p.board_id boardId, " +
                    "p.channel_type channelType, " +
                    "p.title title, " +
                    "p.contents contents, " +
                    "p.author_id authorId, " +
                    "p.author_name authorName, " +
                    "p.return_type returnType, " +
                    "p.return_no_address returnNoAddress, " +
                    "p.created_at createdAt, " +
                    "p.created_by createdById, " +
                    "p.modified_by modifiedById, " +
                    "p.created_date createdDate, " +
                    "p.modified_date modifiedDate, " +
                    "p.is_secret isSecret, " +
                    "p.parent_id parentId " +
                    "from post p " +
                    "where p.board_id = :boardId " +
                    "order by p.created_date desc ",
            countQuery = "select p.id from post p where p.board_id = :boardId",
            nativeQuery = true
    )
    List<PostResponse> findAllByBoard(@Param("boardId") Long boardId);*/

    /*작성자가 쓴 post의 개수 카운트. */
    @Query("select count(p) from Post p where p.author = :author")
    int countPostByAuthor(@Param("author") Member author);

    /*회원탈퇴시 회원이 작성한 post를 비활성화 처리해주는 메소드.*/
    /*@Modifying
    @Query("update Post p set p.isActive = false where p.author = :member")
    int updateIsActiveByMember(@Param("member") Member member);*/

    /*관리자페이지 > 공지사항,FAQ,1:1문의 조건 검색 메소드*/
    @Query(
            value = "select " +
                    "   p.id id, " +
                    "   p.board_id boardId, " +
                    "   b.name boardname, " +
                    "   p.parent_id parentId, " +
                    "   p.channel_type channelType, " +
                    "   p.question_type questionType, " +
                    "   p.title title, " +
                    "   p.author_name authorName, " +
                    "   p.author_id authorId, " +
                    "   p.is_secret isSecret, " +
                    "   p.created_date createdDate, " +
                    "   p.modified_date modifiedDate  " +
                    "from post p join board b on (p.board_id = b.id) " +
                    "where " +
                    "   if(:postId is null,true,(p.id = :postId)) " +
                    "   and if(:boardId is null,true,(p.board_id = :boardId)) " +
                    "   and if(:parentId is null,true,(p.parent_id = :parentId)) " +
                    "   and if(:channelType is null,true,(p.channel_type = :channelType)) " +
                    "   and if(:questionType is null,true,(p.question_type = :questionType)) " +
                    "   and if(:title is null,true,(p.title like %:title%)) " +
                    "   and if(:content is null,true,(p.contents like %:content%)) " +
                    "   and if(:authorId is null,true,(p.author_id = :authorId)) " +
                    "   and if(:authorName is null,true,(p.author_name = :authorName)) " +
                    "   and if(:returnType is null,true,(p.return_type = :returnType)) " +
                    "   and if(:returnNoAddress is null,true,(p.return_no_address like %:returnNoAddress%)) " +
                    "   and if(:createdAt is null,true,(p.created_at like %:createdAt%)) " +
                    "   and if(:isSecret is null,true,(p.is_secret = :isSecret)) " +
                    "   and if(:createdDateStart is null,true,(p.created_date > :createdDateStart)) " +
                    "   and if(:createdDateEnd is null,true,(p.created_date < :createdDateEnd)) " +
                    "   and if(:modifiedDateStart is null,true,(p.modified_date > :modifiedDateStart)) " +
                    "   and if(:modifiedDateEnd is null,true,(p.modified_date < :modifiedDateEnd)) ",
            countQuery = "select p.id " +
                    "from post p join board b on (p.board_id = b.id) " +
                    "where " +
                    "   if(:postId is null,true,(p.id = :postId)) " +
                    "   and if(:boardId is null,true,(p.board_id = :boardId)) " +
                    "   and if(:parentId is null,true,(p.parent_id = :parentId)) " +
                    "   and if(:channelType is null,true,(p.channel_type = :channelType)) " +
                    "   and if(:questionType is null,true,(p.question_type = :questionType)) " +
                    "   and if(:title is null,true,(p.title like %:title%)) " +
                    "   and if(:content is null,true,(p.contents like %:content%)) " +
                    "   and if(:authorId is null,true,(p.author_id = :authorId)) " +
                    "   and if(:authorName is null,true,(p.author_name = :authorName)) " +
                    "   and if(:returnType is null,true,(p.return_type = :returnType)) " +
                    "   and if(:returnNoAddress is null,true,(p.return_no_address like %:returnNoAddress%)) " +
                    "   and if(:createdAt is null,true,(p.created_at like %:createdAt%)) " +
                    "   and if(:isSecret is null,true,(p.is_secret = :isSecret)) " +
                    "   and if(:createdDateStart is null,true,(p.created_date > :createdDateStart)) " +
                    "   and if(:createdDateEnd is null,true,(p.created_date < :createdDateEnd)) " +
                    "   and if(:modifiedDateStart is null,true,(p.modified_date > :modifiedDateStart)) " +
                    "   and if(:modifiedDateEnd is null,true,(p.modified_date < :modifiedDateEnd)) ",
            nativeQuery = true
    )
    Page<PostManageDtoForPage> findAllByConditions(
            @Param("postId") Long postId,
            @Param("boardId") Long boardId,
            @Param("parentId") Long parentId,
            @Param("channelType") Integer channelType,
            @Param("questionType") Integer questionType,
            @Param("title") String title,
            @Param("content") String content,
            @Param("authorId") Long authorId,
            @Param("authorName") String authorName,
            @Param("returnType") Integer returnType,
            @Param("returnNoAddress") String returnNoAddress,
            @Param("createdAt") String createdAt,
            @Param("isSecret") Boolean isSecret,
            @Param("createdDateStart") LocalDate createdDateStart,
            @Param("createdDateEnd") LocalDate createdDateEnd,
            @Param("modifiedDateStart") LocalDate modifiedDateStart,
            @Param("modifiedDateEnd") LocalDate modifiedDateEnd,
            Pageable pageable
    );
}