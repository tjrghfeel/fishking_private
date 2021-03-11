package com.tobe.fishking.v2.repository.board;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.entity.board.Comment;
import com.tobe.fishking.v2.entity.fishing.FishingDiaryComment;
import com.tobe.fishking.v2.enums.common.TakeType;
import com.tobe.fishking.v2.enums.fishing.DependentType;
import com.tobe.fishking.v2.model.board.CommentDtoForPage;
import com.tobe.fishking.v2.model.fishing.FishingDiaryCommentDtoForPage;
import com.tobe.fishking.v2.model.fishing.MyFishingDiaryCommentDtoForPage;
import com.tobe.fishking.v2.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends BaseRepository<Comment, Long> {

    /*게시글의 댓글 목록 검색 메소드*/
    @Query(
            value = "select " +
                    "   new com.tobe.fishking.v2.model.board.CommentDtoForPage(" +
                    "   m.id, " +
                    "   c.id, " +
                    "   case when c.isDeleted=true " +
                    "       then (select concat(:path,extraValue1) from CommonCode where code='noImg' and codeGroup.id=92) " +
                    "       else  concat(:path,m.profileImage) end, " +
                    "   case when c.isDeleted=true then '****' else m.nickName end, " +
                    "   c.createdDate, " +
                    "   case when c.isDeleted=true then '삭제된 댓글입니다' else c.contents end, " +
                    "   case when c.isDeleted=true " +
                    "       then null " +
                    "       else (select concat(:path,'/',f.fileUrl,'/',f.storedFile) from FileEntity f " +
                    "                   where f.pid = c.id and f.filePublish=14 and f.isDelete = false) end, " +
                    "   case when c.isDeleted=true " +
                    "       then null " +
                    "       else (select f.id from FileEntity f where f.pid = c.id and f.filePublish=14 and f.isDelete=false) end, " +
                    "   c.likeCount, " +
                    "   (exists (select l.id from LoveTo l where l.createdBy.id=:memberId and l.takeType=7 and l.linkId=c.id)), " +
                    "   case when c.parentId = 0 then false else true end, " +
                    "   c.parentId," +
                    "   case when c.createdBy.id=:memberId then true else false end " +
                    "   ) " +
                    "from Comment c join Member m on c.createdBy.id = m.id " +
                    "where " +
                    "   c.linkId = :linkId " +
                    "   and c.dependentType = :dependentType " +
                    "   and c.parentId = :parentId " +
                    "order by c.createdDate "
    )
    List<CommentDtoForPage> getCommentList(
            @Param("linkId") Long linkId,
            @Param("dependentType") DependentType dependentType,
            @Param("parentId") Long parentId,
            @Param("memberId") Long memberId,
            @Param("path") String path
    );
}
