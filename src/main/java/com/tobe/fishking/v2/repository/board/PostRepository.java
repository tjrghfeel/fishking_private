package com.tobe.fishking.v2.repository.board;



import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.board.Post;
import com.tobe.fishking.v2.model.board.PostDTO;
import com.tobe.fishking.v2.model.board.PostListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByBoard(Board board);
  //  List<  Goods> findByBoardOrderBy  GoodsIdDesc(Board board);

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
                    "p.is_secret isSecret, " +
                    "p.parent_id parentId " +
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
}