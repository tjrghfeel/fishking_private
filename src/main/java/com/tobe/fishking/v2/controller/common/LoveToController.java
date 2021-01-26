package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.MakeLoveToDto;
import com.tobe.fishking.v2.service.common.LoveToService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"좋아요"})
@RequiredArgsConstructor
@RequestMapping(value = "/v2/api")
@RestController
public class LoveToController {
    @Autowired
    LoveToService loveToService;

    /*좋아요 추가*/
    @ApiOperation(value = "좋아요 추가",notes = "" +
            "요청필드 )\n" +
            "- linkId : Long / 필수 / 좋아요 대상의 id\n" +
            "- takeType : String / 필수 / 좋아요 대상의 유형 / goods(\"상품\"), ship(\"업체\"), fishingDiary(\"조황일지\")," +
            "    fishingBlog(\"조행기\"), comment(\"댓글\"), fishkingTv(\"어복TV\")\n" +
            "응답 필드 ) 좋아요 추가 성공시 true, 넘겨준 필드값에 해당하는 좋아요가 이미 존재할시 false. ")
    @PostMapping("/loveto")
    public Boolean addLoveTo(
            @RequestBody @Valid MakeLoveToDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return loveToService.addLoveTo(dto,token);
    }

    /*좋아요 취소*/
    @ApiOperation(value = "좋아요 취소",notes = "" +
            "요청필드 )\n" +
            "- linkId : Long / 필수 / 좋아요 대상의 id\n" +
            "- takeType : String / 필수 / 좋아요 대상의 유형 / goods(\"상품\"), ship(\"업체\"), fishingDiary(\"조황일지\")," +
            "    fishingBlog(\"조행기\"), comment(\"댓글\"), fishkingTv(\"어복TV\")\n" +
            "- 헤더에 세션토큰 필요\n" +
            "응답 필드 ) 삭제성공시 true, 넘겨준 필드값에 해당하는 좋아요가 없으면 false. ")
    @DeleteMapping("/loveto")
    public Boolean deleteLoveTo(
            @RequestBody @Valid MakeLoveToDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return loveToService.deleteLoveTo(dto,token);
    }


}
