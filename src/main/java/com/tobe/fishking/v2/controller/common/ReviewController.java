package com.tobe.fishking.v2.controller.common;


import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.DeleteReviewDto;
import com.tobe.fishking.v2.model.common.WriteReviewDto;
import com.tobe.fishking.v2.service.common.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Review")
@RestController
@RequestMapping(value = "/v2/api")
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    /*리뷰 작성*/
    @ApiOperation(value = "리뷰 작성",notes = "" +
            "요청 필드 ) \n" +
            "- goodsId : Long / 필수 / 상품 id\n" +
            "- cleanScore : Double / 필수 / 청결도 점수 / 0~5점\n" +
            "- serviceScore : Double / 필수 / 서비스 점수 / 0~5점\n" +
            "- tasteScore : Double / 필수 / 손맛 점수 / 0~5점\n" +
            "- content : String / 필수 / 내용 / 5자~1000자 \n" +
            "- fileList : Long[] / 선택 / Common > /v2/api/filePreUpload를 통해 미리업로드한 파일 id / 0~20장\n" +
            "응답 필드 ) 생성된 리뷰의 id\n")
    @PostMapping("/review")
    public Long writeReview(@RequestBody @Valid WriteReviewDto dto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return reviewService.writeReview(dto, token);
    }

    /*리뷰 삭제*/
    @ApiOperation(value = "리뷰 삭제",notes = "" +
            "내가 작성한 리뷰를 삭제. 해당 리뷰의 작성자이거나 관리자회원일 경우에만 삭제가능. \n" +
            "요청 필드 )\n" +
            "- reviewId : 삭제할 리뷰의 id\n" +
            "응답 필드 ) 삭제 성공시 true. \n")
    @DeleteMapping("/review")
    public Boolean deleteReview(@RequestBody DeleteReviewDto dto, @RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return reviewService.deleteReview(dto,token);
    }


}
