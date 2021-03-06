package com.tobe.fishking.v2.repository.board;


import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.enums.board.FilePublish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByName(String name);

    Board getBoardByFilePublish(FilePublish filePublish);

    @Query("select b from Board b where b.filePublish = :filePublish")
    Board findBoardByFilePublish(@Param("filePublish") FilePublish filePublish);

}