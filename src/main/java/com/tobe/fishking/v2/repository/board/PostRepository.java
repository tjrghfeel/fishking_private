package com.tobe.fishking.v2.repository.board;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.ChannelType;
import com.tobe.fishking.v2.model.admin.post.PostManageDtoForPage;
import com.tobe.fishking.v2.model.admin.post.PostManageDtoForPage2;
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
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
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
            "   p.modified_by modifiedBy, " +
            "   p.created_date date  " +
            "from post p " +
            "where p.board_id = (select b.id from board b where b.board_type = 3) " +
            "   and p.target_role = :role " +
            "   and if(:title is null, true, p.title like %:title%) " +
            "   and if(:questionType is null, true, p.question_type = :questionType) " +
            "   and p.is_deleted = false "+
            "order by p.question_type ",
            countQuery = "select p.id " +
                    "from post p " +
                    "where p.board_id = (select b.id from board b where b.board_type = 3) " +
                    "   and p.target_role = :role " +
                    "   and if(:title is null, true, p.title like %:title%) " +
                    "   and if(:questionType is null, true, p.question_type = :questionType) " +
                    "   and p.is_deleted = false "+
                    "order by p.question_type ",
            nativeQuery = true
    )
    Page<FAQDto> findAllFAQList(
            @Param("role") Boolean role,
            @Param("title") String title,
            @Param("questionType") Integer questionType,
            Pageable pageable
    );
    @Query(
            value = "select " +
                    "   p.id id, " +
                    "   p.question_type questionType, " +
                    "   p.title title, " +
                    "   p.contents contents, " +
                    "   p.author_id authorId, " +
                    "   p.created_date date, " +
                    "   p.target_role targetRole " +
                    "from post p " +
                    "where p.id = :postId ",
            nativeQuery = true
    )
    FAQDto findFAQDetail(@Param("postId") Long postId);

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
            "   and p.is_deleted = false "+
            "order by  p.created_date desc",
            countQuery = "select p.id " +
                    "from post p " +
                    "where p.board_id = (select b.id from board b where b.board_type = 2) " +
                    "   and p.author_id = :member " +
                    "   and p.is_deleted = false "+
                    "order by p.created_date desc",
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
            "   (select GROUP_CONCAT(concat(f.file_url,'/',f.stored_file) separator ',') " +
            "       from files f where f.pid = p.id and f.file_publish = 2 and f.is_delete = false " +
            "       group by f.pid) fileUrlList, " +
            "   (select GROUP_CONCAT(f.id separator ',') " +
            "       from files f where f.pid = p.id and f.file_publish = 2 and f.is_delete = false " +
            "       group by f.pid) fileIdList, " +
            "   rp.id replyId, " +
            "   rp.contents replyContents, " +
            "   rp.created_date replyDate, " +
            "   rp.author_id replyAuthorId, " +
            "   rp.author_name replyAuthorName, " +
            "   rp.created_by replyCreatedBy, " +
            "   rp.modified_by replyModifiedBy, " +
            "   (select GROUP_CONCAT(concat(f.file_url,'/',f.stored_file) separator ',') " +
            "       from files f where f.pid = rp.id and f.file_publish = 2 and f.is_delete = false " +
            "       group by f.pid) replyFileUrlList, " +
            "   (select GROUP_CONCAT(f.id separator ',') " +
            "       from files f where f.pid = rp.id and f.file_publish = 2 and f.is_delete = false " +
            "       group by f.pid) replyFileIdList " +
            "from post p left outer join post rp on rp.parent_id = p.id " +
            "where p.id = :postId " +
            "   and p.is_deleted = false ",
            countQuery = "select p.id " +
                    "from post p join post rp on rp.parent_id = p.id " +
                    "where p.id = :postId " +
                    "   and p.is_deleted = false ",
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
            "   and if(:channelType is null, true, p.channel_type = :channelType) " +
            "   and if(:title is null, true, p.title like %:title%) " +
            "   and p.is_deleted = false " +
            "order by p.created_date desc, p.channel_type ",
            countQuery = "select p.id " +
                    "from post p " +
                    "where " +
                    "p.board_id = 74 " +
                    "and p.target_role = :role " +
                    "   and if(:channelType is null, true, p.channel_type = :channelType) " +
                    "   and if(:title is null, true, p.title like %:title%) " +
                    "   and p.is_deleted = false " +
                    "order by p.created_date desc, p.channel_type ",
            nativeQuery = true
    )
    Page<NoticeDtoForPage> findNoticeList(
            @Param("role") Boolean role,
            @Param("channelType") Integer channelType,
            @Param("title") String title,
            Pageable pageable
    );

    /* 공지사항 detail 조회*/
    @Query(value = "" +
            "select " +
            "   p.id id, " +
            "   p.channel_type channelType, " +
            "   p.created_date date, " +
            "   p.title title, " +
            "   p.contents contents, " +
            "   (select GROUP_CONCAT(concat(f.file_url,'/',f.stored_file) separator ',') " +
            "       from files f where f.pid = p.id and f.file_publish = 4 and f.is_delete = false " +
            "       group by f.pid) fileUrlList, " +
//            "   (select group_concat(f.stored_file separator ',') from files f " +
//            "       where f.file_publish = 4 and f.pid = p.id and f.is_delete = false group by f.pid) fileNameList, " +
//            "   (select group_concat(f.file_url separator ',') from files f " +
//            "       where f.file_publish = 4 and f.pid = p.id and f.is_delete = false group by f.pid) filePathList, " +
            "   p.author_id authorId, " +
            "   p.created_by createdBy, " +
            "   p.modified_by modifiedBy " +
            "from post p left outer join post rp on rp.parent_id = p.id " +
            "where p.id = :postId " +
            "   and p.is_deleted = false ",
            countQuery = "select p.id " +
                    "from post p join post rp on rp.parent_id = p.id " +
                    "where p.id = :postId " +
                    "   and p.is_deleted=false ",
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
                    "   LEFT(p.contents,30) content, " +
                    "   p.author_name authorName, " +
                    "   p.author_id authorId, " +
                    "   p.return_type returnType, " +
                    "   p.return_no_address returnNoAddress, " +
                    "   p.created_at createdAt, " +
                    "   p.is_secret isSecret, " +
                    "   p.created_date createdDate, " +
                    "   p.modified_date modifiedDate, " +
                    "   p.created_by createdBy, " +
                    "   p.modified_by modifiedBy, " +
                    "   p.target_role targetRole, " +
                    "   p.is_replied isReplied   " +
                    "from post p join board b on (p.board_id = b.id) " +
                    "where " +
                    "   (:postId is null or (:postId is not null and p.id = :postId))" +
//                    "   if(:postId is null,true,(p.id = :postId)) " +
                    "   and if(:boardId is null,true,(p.board_id = :boardId)) " +
                    "   and if(:parentId is null, (p.parent_id is null), (p.parent_id = :parentId)) " +
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
                    "   and if(:modifiedDateEnd is null,true,(p.modified_date < :modifiedDateEnd)) "+
                    "   and if(:targetRole is null, true, (p.target_role = :targetRole)) " +
                    "   and if(:createdBy is null, true, (p.created_by = :createdBy)) " +
                    "   and if(:modifiedBy is null, true, (p.modified_by = :modifiedBy)) "+
                    "   and if(:isReplied is null, true, (p.is_replied = :isReplied)) ",
            countQuery = "select p.id " +
                    "from post p join board b on (p.board_id = b.id) " +
                    "where " +
                    "   if(:postId is null,true,(p.id = :postId)) " +
                    "   and if(:boardId is null,true,(p.board_id = :boardId)) " +
                    "   and if(:parentId is null, (p.parent_id is null), (p.parent_id = :parentId)) " +
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
                    "   and if(:modifiedDateEnd is null,true,(p.modified_date < :modifiedDateEnd)) " +
                    "   and if(:targetRole is null, true, (p.target_role = :targetRole)) " +
                    "   and if(:createdBy is null, true, (p.created_by = :createdBy)) " +
                    "   and if(:modifiedBy is null, true, (p.modified_by = :modifiedBy)) " +
                    "   and if(:isReplied is null, true, (p.is_replied = :isReplied)) ",
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
            @Param("targetRole") Boolean targetRole,
            @Param("createdBy") Long createdBy,
            @Param("modifiedBy") Long modifiedBy,
            @Param("isReplied") Boolean isReplied,
            Pageable pageable
    );

    @Query(
            value = "select " +
                    "   new com.tobe.fishking.v2.model.admin.post.PostManageDtoForPage2(" +
                    "       p, b"+
                    "   ) " +
                    "from Post p join Board b on (p.board = b) " +
                    "where " +
                    "   (:postId is null or (:postId is not null and p.id = :postId)) " +
                    "   and (:boardId is null or (:boardId is not null and p.board.id = :boardId))" +
                    "   and (:parentId is null or (:parentId is not null and p.parent_id = :parentId)) " +
                    "   and (:channelType is null or (:channelType is not null and p.channelType = :channelType)) " +
                    "   and (:questionType is null or (:questionType is not null and p.questionType = :questionType)) " +
                    "   and (:title is null or (:title is not null and p.title like %:title%)) " +
                    "   and (:content is null or (:content is not null and p.contents like %:content%)) " +
                    "   and (:authorId is null or (:authorId is not null and p.author.id = :authorId)) " +
                    "   and (:nickName is null or (:nickName is not null and p.author.nickName like %:nickName%)) " +
                    "   and (:returnType is null or (:returnType is not null and p.returnType = :returnType)) " +
                    "   and (:returnNoAddress is null or (:returnNoAddress is not null and p.returnNoAddress like %:returnNoAddress%)) " +
                    "   and (:createdAt is null or (:createdAt is not null and p.createdAt like %:createdAt%)) " +
                    "   and (:isSecret is null or (:isSecret is not null and p.isSecret = :isSecret)) " +
                    "   and (:createdDateStart is null or (:createdDateStart is not null and p.createdDate > :createdDateStart)) " +
                    "   and (:createdDateEnd is null or (:createdDateEnd is not null and p.createdDate < :createdDateEnd)) " +
                    "   and (:modifiedDateStart is null or (:modifiedDateStart is not null and p.modifiedDate > :modifiedDateStart)) " +
                    "   and (:modifiedDateEnd is null or (:modifiedDateEnd is not null and p.modifiedDate < :modifiedDateEnd)) "
    )
    Page<PostManageDtoForPage2> findAllByConditions2(
            @Param("postId") Long postId,
            @Param("boardId") Long boardId,
            @Param("parentId") Long parentId,
            @Param("channelType") ChannelType channelType,
            @Param("questionType") QuestionType questionType,
            @Param("title") String title,
            @Param("content") String content,
            @Param("authorId") Long authorId,
            @Param("nickName") String nickName,
            @Param("returnType") ReturnType returnType,
            @Param("returnNoAddress") String returnNoAddress,
            @Param("createdAt") String createdAt,
            @Param("isSecret") Boolean isSecret,
            @Param("createdDateStart") LocalDate createdDateStart,
            @Param("createdDateEnd") LocalDate createdDateEnd,
            @Param("modifiedDateStart") LocalDate modifiedDateStart,
            @Param("modifiedDateEnd") LocalDate modifiedDateEnd,
            Pageable pageable
    );

    @Query("select p from Post p where p.title like concat('%', :title, '%')")
    List<Post> getPostContainTitle(String title);

    @Query("select coalesce(1, (select 'hello' from Post p2)) from Post p ")
    String noName(@Param("aaa") String aaa);
}