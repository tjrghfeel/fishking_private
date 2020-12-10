package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.entity.fishing.FishingDiaryComment;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.common.ReviewDto;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.service.fishking.FishingDiaryService;
import com.tobe.fishking.v2.service.fishking.MyMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/*마이메뉴 안에 있더라도 찜, 쿠폰 등과 같은 다룰 api가 좀있는 것들은 컨트롤러를 따로 빼내었고
* 여기엔 그 이외의 것들이 있다. */
@RestController
@Api(tags={"마이메뉴"})
@RequestMapping("/v2/api")
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
    * - 게시글 항목으로는 member프사, 이름, 주소, 시간, 제목, 내용(축약), 사진들, 좋아요,스크랩,댓글 개수,이미지url들.
    * - '이미지url들'이란, 해당 게시물에 속한 이미지의 downloadUrl들이 하나의 String변수 안에 들어가있으며, 콤마(,)로 구분되어있다.
    *       ex) 'aaa.jpg,bbb.png,ccc.jpg'  */
    @ApiOperation(value = "내글관리 - 게시글")
    @GetMapping("/myFishingPostList/{page}")
    public Page<FishingDiaryDtoForPage> getMyFishingDiary(
            @RequestParam("memberId") Long memberId,
            @PathVariable("page") int page
    ) throws ResourceNotFoundException {
        return myMenuService.getMyFishingDiary(memberId, page);
    }

    /*내글관리 - 댓글
    * - 댓글을 단 fishingDiary가 선상인지 갯바위인지, fishingDiary 제목, 댓글 내용, 시간이 담긴 DTO를 Page로 반환. */
    @ApiOperation(value = "내글관리 - 댓글")
    @GetMapping("/myFishingCommentList/{page}")
    public Page<FishingDiaryCommentDtoForPage> getMyFishingDiaryComment(
            @RequestParam("memberId") Long memberId,
            @PathVariable("page") int page
    ) throws ResourceNotFoundException {
        return myMenuService.getMyFishingDiaryComment(memberId,page);
    }
    
    /*내글관리 - 스크랩
    * - 현재 사용자가 스크립한 fishing diary 리스트를 가져옴. 세부사항은 '내글관리 - 게시글'과 동일. */
    @ApiOperation(value = "내글관리 - 스크랩")
    @GetMapping("/myFishingDiaryScrap/{page}")
    public Page<FishingDiaryDtoForPage> getMyFishingDiaryScrap(
            @RequestParam("memberId") Long memberId,
            @PathVariable("page") int page
    ) throws ResourceNotFoundException {
        return myMenuService.getMyFishingDiaryScrap(memberId, page);
    }

    /*내글관리 - 리뷰
    * - 사용자가 상품에 대해 작성한 모든 리뷰를 페이징형태로 반환.
    * - DTO에 들은 데이터는 리뷰,상품,배의 id, 작성자 프사, 작성자 닉네임, 낚시 날짜, 낚시 어종, 오전/오후, 거리, 물때,
    *   총평점, 맛,서비스,청결도 평점, 리뷰내용, 이미지url들.
    * - '이미지url들'이란, 해당 게시물에 속한 이미지의 downloadUrl들이 하나의 String변수 안에 들어가있으며, 콤마(,)로 구분되어있다.
    *       ex) 'aaa.jpg,bbb.png,ccc.jpg'  */
    @ApiOperation(value = "내글관리 - 리뷰")
    @GetMapping("/myReviewList/{page}")
    public Page<ReviewDto> getReviewList(
            @RequestParam("memberId") Long memberId,
            @PathVariable("page") int page
    ) throws ResourceNotFoundException {
        return myMenuService.getMyReview(memberId, page);
    }

    /*예약 내역 리스트 보기
    * - DTO에 필요한 정보를 담아서 반환. 데이터는 ship 대표사진, ship name, 선상구분, 주소, 거리, orders상태, (d-day), 이용일, 예약번호
    * - 넘어온 orders상태값에 해당하는 예약목록을 반환. 디폴트는 전체보기.
    * - 전체보기시 순서는 orders상태에 따라 예약대기 - 대기자예약 - 예약확정 - 예약취소 - 출조완료 - 예약완료 순으로.  */
    @ApiOperation(value = "예약내역 리스트")
    @GetMapping("/myOrdersList/{page}")
    public Page<OrdersDtoForPage> getMyOrdersList(
            @PathVariable("page") int page,
            @RequestParam("memberId") Long memberId,
            @RequestParam(value = "sort", required = false, defaultValue = "none") String sort
    ) throws ResourceNotFoundException {
        return myMenuService.getMyOrdersList(memberId, page, sort);
    }

    /*예약 상세보기
    * - orders에 대한 세부정보가 들은 dto반환. 데이터는 ship이름,ordersStatus,선상구분,주소,거리,출발시간(예약일),낚시어종,오전/오후,
    *   출항시간, 상품가격,상품수량,예약번호,예약자명,예약자연락처,결제일,결재수단,총주문금액,쿠폰할인금액,총결제액. */
    @ApiOperation(value = "예약 상세")
    @GetMapping("/OrdersDetail")
    public OrdersDetailDto getOrdersDetail(
            @RequestParam("ordersId") Long ordersId
    ) throws ResourceNotFoundException {
        return myMenuService.getOrdersDetail(ordersId);
    }



}
