package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDTO;
import com.tobe.fishking.v2.model.fishing.FishingDiaryDtoForPage;
import com.tobe.fishking.v2.model.fishing.MyMenuPageDTO;
import com.tobe.fishking.v2.service.fishking.FishingDiaryService;
import com.tobe.fishking.v2.service.fishking.MyMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*마이메뉴 안에 있더라도 찜, 쿠폰 등과 같은 다룰 api가 좀있는 것들은 컨트롤러를 따로 빼내었고
* 여기엔 그 이외의 것들이 있다. */
@RestController
@Api(tags={"마이메뉴"})
@RequestMapping("/v1/api")
public class MyMenuController {

    @Autowired
    FishingDiaryService fishingDiaryService;
    @Autowired
    MyMenuService myMenuService;

    /*마이메뉴 처음 페이지 조회.
    * - member의 프사, nickname, 예약건수, 쿠폰 수를 dto에 담아서 반환. */
    @ApiOperation(value = "마이메뉴 페이지")
    @GetMapping("/myMenuPage")
    public MyMenuPageDTO getMyMenuPage(@RequestParam("memberId") Long member) throws ResourceNotFoundException {
        return myMenuService.getMyMenuPage(member);

    }


    /*내글관리 - 게시글
    * - member의 role이 선주일때는 조항일지, 일반사용자일때는 조행기가 출력되도록.(선주가 조행기를 작성할수있는지는 고려안해도될듯함)
    * - 게시글 항목으로는 member프사, 이름, 주소, 시간, 제목, 내용(축약), 사진들, 좋아요,스크랩,댓글 개수.   */
    /*@ApiOperation(value = "내글관리 - 게시글")
    @GetMapping("/myFishingPostList/{page}")
    public Page<FishingDiaryDtoForPage> getMyFishingDiary(
            @RequestParam("memberId") Long memberId
    ){
        //page라서dto로 리포지에서 바로받아오려고했는데, 다른필드는그렇다치고, 좋아요,스크랩,댓글 개수까지 한번에 쿼리할수있는지 아직모르겟다.
        //ReviewService에서 하다만것과 같은상황. ReviewService에서는 Map형식으로 반환하려햇음.

    }*/

    /*내글관리 - 댓글*/
    /*@ApiOperation(value = "내글관리 - 댓글")
    @GetMapping("/myFishingCommentList")
    public Page<Fishing>*/

    /*내글관리 - 리뷰*/
    /*@ApiOperation(value = "내글관리 = 리뷰")
    @GetMapping("/myReviewList")
    public Page<ReviewDTO> getReviewList(
            @RequestParam("memberId") Long memberId
    ){
        return fishingDiaryService.getReviewList(memberId);
    }
*/

}
