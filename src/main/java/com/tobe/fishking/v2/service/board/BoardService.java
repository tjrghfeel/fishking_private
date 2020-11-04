package com.tobe.fishking.v2.service.board;

import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.entity.fishing.Goods;
import com.tobe.fishking.v2.enums.board.FilePublish;
import com.tobe.fishking.v2.model.fishing.GoodsDTO;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository repository;

    public Board getBoardByFilePublish(FilePublish filePublish){
        Board board = repository.getBoardByFilePublish(filePublish);
        return board;
    }

}