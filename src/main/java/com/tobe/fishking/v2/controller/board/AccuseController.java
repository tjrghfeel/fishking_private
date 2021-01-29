package com.tobe.fishking.v2.controller.board;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.AddAccuseDto;
import com.tobe.fishking.v2.service.board.AccuseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags={"신고"})
@RestController
@RequestMapping("/v2/api")
public class AccuseController {
    @Autowired
    AccuseService accuseService;

    /*신고하기*/
    @ApiOperation(value = "신고하기",notes = "" +
            "요청 필드 ) \n" +
            "- linkId : Long / 신고 대상의 id\n" +
            "- targetType : String / 신고 대상의 유형 / fishingDiary(조항일지, 유저조행기), comment(댓글) 중 하나 \n" +
            "응답 필드 ) 생성된 신고의 id\n")
    @PostMapping("/addAccuse")
    public Long addAccuse(
            @RequestBody AddAccuseDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        String targetType = dto.getTargetType();
        if(!(targetType.equals("fishingDiary") || targetType.equals("comment"))){
            throw new RuntimeException("targetType 값에는 'fishingDiary', 'comment' 중 하나이어야 합니다.");
        }
        return accuseService.addAccuse(dto,token);

    }


}
