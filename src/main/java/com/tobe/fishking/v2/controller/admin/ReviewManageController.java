package com.tobe.fishking.v2.controller.admin;

import com.tobe.fishking.v2.model.common.WriteReviewDto;
import com.tobe.fishking.v2.service.common.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Review")
@RestController
@RequestMapping(value = "/v2/api")
public class ReviewManageController {
    @Autowired
    ReviewService reviewService;

    /*리뷰 생성*/
    /*@ApiOperation(value = "리뷰 생성",notes = "" +
            "")
    @PostMapping("/review")
    public Long writeReview(@RequestBody WriteReviewDtoForManage dto, @RequestHeader("Authorization") String token){

    }*/


}
