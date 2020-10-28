package com.tobe.fishking.v2.controller.board;

import com.tobe.fishking.v2.entity.board.Board;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.repository.board.BoardRepository;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(tags = {"게시판"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/api")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;


    @ApiOperation(value = "게시판 목록 조회", notes = "게시판 목록을 조회합니다.")
    @GetMapping("/boards")
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }


/*    @ApiOperation(value = "게시판 목록 검색 조회", notes = "게시판 목록을 검색조건에 의해   조회합니다.")
    @GetMapping("/boards")
    public List<Board> getSearchBoards() {
        return boardRepository.findAll();
    }
*/



    @ApiOperation(value = "게시판 상세 조회", notes = "게시판를 상세 조회합니다.")
    @GetMapping("/boards/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable(value = "id") Long boardId)
            throws ResourceNotFoundException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found for this id :: " + boardId));
        return ResponseEntity.ok().body(board);
    }


    @ApiOperation(value = "게시판 등록", notes = "게시판을 등록합니다.")
    @PostMapping("/boards")
    public Board createBoard(@Valid @RequestBody Board board) {
        return boardRepository.save(board);
    }
    @ApiOperation(value = "게시판 수정", notes = "게시판을 수정합니다.")
    @PutMapping("/boards/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable(value = "id") Long boardId,
                                             @Valid @RequestBody Board boardDetails) throws ResourceNotFoundException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found for this id :: " + boardId));


        final Board updatedBoard = boardRepository.save(board);
        return ResponseEntity.ok(updatedBoard);
    }

    @ApiOperation(value = "게시판 삭제", notes = "게시판을 삭제합니다.")
    @DeleteMapping("/boards/{id}")
    public Map<String, Boolean> deleteBoard(@PathVariable(value = "id") Long boardId)
            throws ResourceNotFoundException {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found for this id :: " + boardId));

        boardRepository.delete(board);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}