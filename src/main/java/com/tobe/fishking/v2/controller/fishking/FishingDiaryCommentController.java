package com.tobe.fishking.v2.controller.fishking;


import com.tobe.fishking.v2.entity.fishing.FishingDiaryComment;
import com.tobe.fishking.v2.enums.fishing.DependentType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.DeleteFishingDiaryCommentDto;
import com.tobe.fishking.v2.model.fishing.FishingDiaryCommentDtoForPage;
import com.tobe.fishking.v2.model.fishing.MakeFishingDiaryCommentDto;
import com.tobe.fishking.v2.model.fishing.ModifyFishingDiaryCommentDto;
import com.tobe.fishking.v2.service.fishking.FishingDiaryCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Api(tags = {"조항일지, 유저조행기 댓글"})
@RequiredArgsConstructor
@RequestMapping(value = "/v2/api")
@RestController
public class FishingDiaryCommentController {
    @Autowired
    FishingDiaryCommentService commentService;

    /*댓글 작성*/
    @ApiOperation(value = "댓글 작성",notes = "" +
            "요청 필드 ) \n" +
            "- fishingDiaryId : Long / 필수 / 원글의 id\n" +
            "- dependentType : String / 필수 / 원글이 어떤 유형의 글인지. / 'fishingDiary', 'fishingBlog' 중 하나.\n" +
            "- parentId : Long / 필수 (대댓글 아닐경우 0) / 댓글에 대한 댓글일 경우(대댓글), 상위 댓글의 id. \n" +
            "- content : String / 필수 / 댓글 내용 / 1~50자.\n" +
            "- fileId : Long / 선택 (없을 경우 null) / 이미지 추가시, /v2/api/filePreUpload를 통해 업로드된 파일의 id. \n" +
            "응답 필드 ) 생성된 댓글의 id.\n ")
    @PostMapping("/fishingDiaryComment")
    public Long makeFishingDiaryComment(
            @RequestBody @Valid MakeFishingDiaryCommentDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        if(!(dto.getDependentType().equals("fishingDiary") || dto.getDependentType().equals("fishingBlog"))){
            throw new RuntimeException("dependentType의 값에는 'fishingDiary', 'fishingBlog' 중 하나만 가능합니다.");
        }

        return commentService.makeFishingDiaryComment(
                DependentType.valueOf(dto.getDependentType()),dto.getFishingDiaryId(),dto.getParentId(),
                dto.getContent(), dto.getFileId(),token);
    }

    /*댓글 수정*/
    @ApiOperation(value = "댓글 수정",notes = "" +
            "")
    @PutMapping("/fishingDiaryComment")
    public Boolean modifyFishingDiaryComment(
            @RequestBody @Valid ModifyFishingDiaryCommentDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return commentService.modifyFishingDiaryComment(dto.getCommentId(),dto.getContent(),dto.getFileId(),token);
    }

    /*댓글 삭제*/
    @ApiOperation(value = "댓글 삭제",notes = "" +
            "요청 필드 ) \n" +
            "- commentId : 삭제할 댓글의 id\n" +
            "응답 필드 ) 삭제 성공시 true. ")
    @DeleteMapping("/fishingDiaryComment")
    public Boolean deleteFishingDiaryComment(
            @RequestBody @Valid DeleteFishingDiaryCommentDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return commentService.deleteFishingDiaryComment(dto,token);
    }

    /*댓글 목록 출력*/
    @ApiOperation(value = "댓글 목록 가져오기",notes = "" +
            "조항일지 또는 유저조행기에 대한 댓글 목록을 가져온다. 댓글에 대한 댓글(대댓글)이 가능하며, 대댓글의 깊이는 2(댓글에 대한 댓글까지만)이다. " +
            "요청 필드 ) \n" +
            "- fishingDiaryId : Long / 필수 / 원글id\n" +
            "- 헤더에 세션토큰 필요\n" +
            "응답 필드 ) \n" +
            "- authorId : Long / 댓글 작성자 id\n" +
            "- commentId : Long / 댓글 id\n" +
            "- profileImage : String / 댓글 작성자 프사 url\n" +
            "- nickName : String / 댓글 작성자 닉네임\n" +
            "- writeTime : LocalDateTime / 댓글 작성 시각\n" +
            "- content : String / 댓글 내용\n" +
            "- fileUrl : String / 댓글 내용 - 이미지 파일 url\n" +
            "- likeCount : Integer / 댓글에 대한 좋아요수\n" +
            "- isLikeTo : Boolean / 현재 회원의 댓글에 대한 좋야요 여부\n" +
            "- isChildComment : Boolean / 현재 댓글이 대댓글인지 여부\n" +
            "- parentId : Long / 현재 댓글이 대댓글이라면 상위 댓글의 id / 대댓글이 아닌 댓글이라면 값은 0. \n" +
            "- isMine : Boolean / 현재 댓글이 자신의 댓글인지. \n" +
            "- childList : 현재 댓글에 대한 대댓글 목록. / 똑같은 필드를 가지는 객체를 리스트로 가지고 있으며, " +
            "   대댓글이 없을시 리스트가 비어있다.  ")
    @GetMapping("/fishingDiaryComment")
    public List<FishingDiaryCommentDtoForPage> getCommentList(
        @RequestParam("fishingDiaryId") Long fishingDiaryId,
        @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return commentService.getCommentList(fishingDiaryId,token);
    }
}
