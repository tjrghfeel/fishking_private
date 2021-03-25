package com.tobe.fishking.v2.controller.board;

import com.tobe.fishking.v2.enums.fishing.DependentType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.*;
import com.tobe.fishking.v2.model.fishing.DeleteFishingDiaryCommentDto;
import com.tobe.fishking.v2.model.fishing.FishingDiaryCommentDtoForPage;
import com.tobe.fishking.v2.model.fishing.MakeFishingDiaryCommentDto;
import com.tobe.fishking.v2.model.fishing.ModifyFishingDiaryCommentDto;
import com.tobe.fishking.v2.service.board.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"댓글"})
@RequiredArgsConstructor
@RequestMapping(value = "/v2/api")
@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    /*댓글 작성*/
    @ApiOperation(value = "댓글 작성",notes = "" +
            "요청 필드 ) \n" +
            "- linkId : Long / 필수 / 원글의 id\n" +
            "- dependentType : String / 필수 / 원글이 어떤 유형의 글인지. / 'event' 중 하나.\n" +
            "- parentId : Long / 필수 (대댓글 아닐경우 0) / 댓글에 대한 댓글일 경우(대댓글), 상위 댓글의 id. \n" +
            "- content : String / 필수 / 댓글 내용 / 1~50자.\n" +
            "- fileId : Long / 선택 (없을 경우 null) / 이미지 추가시, /v2/api/filePreUpload를 통해 업로드된 파일의 id. \n" +
            "응답 필드 ) 생성된 댓글의 id.\n ")
    @PostMapping("/comment")
    public Long makeComment(
            @RequestBody @Valid MakeCommentDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        if(!(dto.getDependentType().equals("event") )){
            throw new RuntimeException("dependentType의 값에는 'event' 중 하나만 가능합니다.");
        }

        return commentService.makeComment(
                DependentType.valueOf(dto.getDependentType()),dto.getLinkId(),dto.getParentId(),
                dto.getContent(), dto.getFileId(),token);
    }

    /*댓글 수정*/
    @ApiOperation(value = "댓글 수정",notes = "" +
            "요청 필드 ) \n" +
            "- commontId : Long / 필수 / 삭제하려는 댓글 id\n" +
            "- content : String / 필수 / 수정된 댓글 내용\n" +
            "- fileId : Long / 선택 (없을시 null) / Common > /v2/api/filePreUpload 를 통해 업로드한 파일의 id\n" +
            "응답 필드 ) 수정 성공시 true\n")
    @PutMapping("/comment")
    public Boolean modifyComment(
            @RequestBody @Valid ModifyCommentDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return commentService.modifyComment(dto.getCommentId(),dto.getContent(),dto.getFileId(),token);
    }

    /*댓글 삭제*/
    @ApiOperation(value = "댓글 삭제",notes = "" +
            "요청 필드 ) \n" +
            "- commentId : 삭제할 댓글의 id\n" +
            "응답 필드 ) 삭제 성공시 true. ")
    @DeleteMapping("/comment")
    public Boolean deleteComment(
            @RequestBody @Valid DeleteCommentDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return commentService.deleteComment(dto,token);
    }

    /*댓글 목록 출력*/
    @ApiOperation(value = "댓글 목록 가져오기",notes = "" +
            "조항일지 또는 유저조행기에 대한 댓글 목록을 가져온다. 댓글에 대한 댓글(대댓글)이 가능하며, 대댓글의 깊이는 2(댓글에 대한 댓글까지만)이다. " +
            "요청 필드 ) \n" +
            "- linkId : Long / 필수 / 원글id\n" +
            "- dependentType : String / 필수 / 어떤 종류의 대상에 대한 댓글인지 / 'event' 중 하나.\n" +
            "- 헤더에 세션토큰 필요\n" +
            "응답 필드 ) \n" +
            "- title : String / 원글의 제목\n" +
            "- commentCount : Integer / 댓글 수 \n" +
            "- commentList : 댓글 객체 배열. 아래는 객체의 항목들\n" +
            "\t\t\t- authorId : Long / 댓글 작성자 id\n" +
            "\t\t\t- commentId : Long / 댓글 id\n" +
            "\t\t\t- profileImage : String / 댓글 작성자 프사 url\n" +
            "\t\t\t- nickName : String / 댓글 작성자 닉네임\n" +
            "\t\t\t- writeTime : LocalDateTime / 댓글 작성 시각\n" +
            "\t\t\t- content : String / 댓글 내용\n" +
            "\t\t\t- fileId : Long / 댓글에 첨부된file의 id\n" +
            "\t\t\t- fileUrl : String / 댓글 내용 - 이미지 파일 url\n" +
            "\t\t\t- likeCount : Integer / 댓글에 대한 좋아요수\n" +
            "\t\t\t- isLikeTo : Boolean / 현재 회원의 댓글에 대한 좋야요 여부\n" +
            "\t\t\t- isChildComment : Boolean / 현재 댓글이 대댓글인지 여부\n" +
            "\t\t\t- parentId : Long / 현재 댓글이 대댓글이라면 상위 댓글의 id / 대댓글이 아닌 댓글이라면 값은 0. \n" +
            "\t\t\t- isMine : Boolean / 현재 댓글이 자신의 댓글인지. \n" +
            "\t\t\t- childList : 현재 댓글에 대한 대댓글 목록. / 똑같은 필드를 가지는 객체를 리스트로 가지고 있으며, " +
            "   대댓글이 없을시 리스트가 비어있다.  ")
    @GetMapping("/comment")
    public CommentPageDto getCommentList(
            @RequestParam("linkId") Long linkId,
            @RequestParam("dependentType") String dependentType,
            @RequestHeader(value = "Authorization",required = false) String token
    ) throws ResourceNotFoundException {
        if(!(dependentType.equals("event") )){
            throw new RuntimeException("dependentType의 값에는 'event' 중 하나만 가능합니다.");
        }

        if(token==null){}
        else if(token.equals("")){token = null;}

        return commentService.getCommentList(linkId,DependentType.valueOf(dependentType),token);
    }

    //숨김처리
    @ApiOperation(value = "댓글 숨김처리",notes = "")
    @PutMapping("/comment/hide/{id}/{active}")
    public Boolean hideComment(
            @PathVariable("id") Long id,
            @PathVariable("active") String active,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        if(!active.equals("true") && !active.equals("false") ){ throw new RuntimeException("active값에는 'true'또는 'false'만 가능합니다.");}
        return commentService.hideComment( id, active, token);
    }
}
