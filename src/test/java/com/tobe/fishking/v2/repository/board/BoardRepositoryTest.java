package com.tobe.fishking.v2.repository.board;

import com.tobe.fishking.v2.IntegrationTest;
import com.tobe.fishking.v2.entity.board.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BoardRepositoryTest extends IntegrationTest {

    @Autowired
    BoardRepository boardRepository;

    @Test
    void findByName() {
        Board board = boardRepository.findByName("업체요청 게시판");

        assertNotNull(board);
    }
}