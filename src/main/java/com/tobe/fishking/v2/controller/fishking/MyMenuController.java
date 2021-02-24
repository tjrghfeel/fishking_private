package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.enums.common.AlertType;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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
    @ApiOperation(value = "마이메뉴 페이지",notes = "" +
            "- 필드 )\n" +
            "   profileImage : 프로필 사진 download url\n" +
            "   nickName : 사용자 닉네임\n" +
            "   bookingCount : !!!!!\n" +
            "   couponCount : 현재 다운받은 사용가능한 쿠폰 개수")
    @GetMapping("/myMenuPage")
    public MyMenuPageDTO getMyMenuPage(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return myMenuService.getMyMenuPage(token);

    }


    /*내글관리 - 게시글
    * - member의 role이 선주일때는 조항일지, 일반사용자일때는 조행기가 출력되도록.(선주가 조행기를 작성할수있는지는 고려안해도될듯함)
    * - 게시글 항목으로는 member프사, 이름, 주소, 시간, 제목, 내용(축약), 사진들, 좋아요,스크랩,댓글 개수,이미지url들.
    * - '이미지url들'이란, 해당 게시물에 속한 이미지의 downloadUrl들이 하나의 String변수 안에 들어가있으며, 콤마(,)로 구분되어있다.
    *       ex) 'aaa.jpg,bbb.png,ccc.jpg'  */
    @ApiOperation(value = "내글관리 - 게시글", notes = "" +
            "- 내가 쓴 fishingDiary들의 목록을 반환하는 api \n" +
            "- 응답 필드 )\n" +
            "   id : 게시글의 id \n" +
            "   profileImage : 작성자 프로필 사진 download url \n" +
            "   address : 게시글의 대상 선상의 주소 \n" +
            "   shipId : 게시글 대상 상품의 선상id\n" +
            "   memberId : 작성자 id \n" +
            "   nickName : 작성자 닉네임\n" +
            "   fishingType : 선상인지 갯바위인지 \n" +
            "       ㄴ ship : 선상\n" +
            "       ㄴ sealocks : 갯바위\n " +
            "   isLikeTo : 게시글에 대한 좋아요 여부\n" +
            "   createdDate : 작성일자 \n" +
            "   likeCount : 좋아요 수 \n" +
            "   commentCount : 댓글 수 \n" +
            "   scrapCount : 스크랩 수 \n" +
            "   title : 글 제목\n" +
            "   contents : 게시글 내용(일부만 출력)\n" +
            "   fileList : 이미지 파일 download url 리스트")
    @GetMapping("/myFishingPostList/{page}")
    public Page<FishingDiaryDtoForPage> getMyFishingDiary(
            @RequestHeader("Authorization") String token,
            @PathVariable("page") int page
    ) throws ResourceNotFoundException {
        return myMenuService.getMyFishingDiary(token, page);
    }

    /*내글관리 - 댓글
    * - 댓글을 단 fishingDiary가 선상인지 갯바위인지, fishingDiary 제목, 댓글 내용, 시간이 담긴 DTO를 Page로 반환. */
    @ApiOperation(value = "내글관리 - 댓글", notes = "" +
            "- 내가 쓴 댓글 목록 반환 api. \n" +
            "- 필드 )\n" +
            "   id : 댓글id\n" +
            "   dependentType : 선상/갯바위. 댓글이 달린 본글의 조항 종류가 선상인지 갯바위인지. \n" +
            "       board(\"게시판\"), fishingDiary(\"조황일지\"), fishingBlog(\"조황기\")\n" +
            "   title : 본글의 제목\n" +
            "   contents : 댓글의 내용\n" +
            "   time : 댓글작성 시간\n" +
            "   fishingDiaryId : 본글의 id")
    @GetMapping("/myFishingCommentList/{page}")
    public Page<MyFishingDiaryCommentDtoForPage> getMyFishingDiaryComment(
            @RequestHeader("Authorization") String token,
            @PathVariable("page") int page
    ) throws ResourceNotFoundException {
        return myMenuService.getMyFishingDiaryComment(token,page);
    }
    
    /*내글관리 - 스크랩
    * - 현재 사용자가 스크립한 fishing diary 리스트를 가져옴. 세부사항은 '내글관리 - 게시글'과 동일. */
    @ApiOperation(value = "내글관리 - 스크랩",notes = "" +
            "- 내가 스크랩한 게시글 목록을 반환하는 api \n" +
            "- 필드 )\n" +
            "   id : 게시글의 id \n" +
            "   profileImage : 작성자 프로필 사진 download url \n" +
            "   address : 게시글의 대상 선상의 주소 \n" +
            "   shipId : 게시글 대상 상품의 선상id\n" +
            "   memberId : 작성자 id \n" +
            "   nickName : 작성자 닉네임\n" +
            "   fishingType : 선상인지 갯바위인지 \n" +
            "       ㄴ ship : 선상\n" +
            "       ㄴ sealocks : 갯바위\n " +
            "   isLikeTo : 게시글에 대한 좋아요 여부\n" +
            "   createdDate : 작성일자 \n" +
            "   likeCount : 좋아요 수 \n" +
            "   commentCount : 댓글 수 \n" +
            "   scrapCount : 스크랩 수 \n" +
            "   title : 글 제목\n" +
            "   contents : 게시글 내용(일부만 출력)\n" +
            "   fileList : 이미지 파일 download url 리스트")
    @GetMapping("/myFishingDiaryScrap/{page}")
    public Page<FishingDiaryDtoForPage> getMyFishingDiaryScrap(
            @RequestHeader("Authorization") String token,
            @PathVariable("page") int page
    ) throws ResourceNotFoundException {
        return myMenuService.getMyFishingDiaryScrap(token, page);
    }

    /*내글관리 - 리뷰
    * - 사용자가 상품에 대해 작성한 모든 리뷰를 페이징형태로 반환.
    * - DTO에 들은 데이터는 리뷰,상품,배의 id, 작성자 프사, 작성자 닉네임, 낚시 날짜, 낚시 어종, 오전/오후, 거리, 물때,
    *   총평점, 맛,서비스,청결도 평점, 리뷰내용, 이미지url들.
    * - '이미지url들'이란, 해당 게시물에 속한 이미지의 downloadUrl들이 하나의 String변수 안에 들어가있으며, 콤마(,)로 구분되어있다.
    *       ex) 'aaa.jpg,bbb.png,ccc.jpg'  */
    @ApiOperation(value = "내글관리 - 리뷰",notes = "" +
            "- 내가 쓴 리뷰 목록 반환 api\n" +
            "- 필드 )\n" +
            "   id : 리뷰id\n" +
            "   goodsId : 리뷰 대상 상품의 id\n" +
            "   shipId : 리뷰 대상 상품의 선상id\n" +
            "   profileImage : 작성자 프로필사진 download url\n" +
            "   nickName : 작성자 닉네임\n" +
            "   fishingDate : 낚시일\n" +
            "   goodsFishSpecies : 어종\n" +
//            "   meridiem : 낚시 시간대가 오전/오후 인지\n" +
//            "       ㄴ am : 오전\n" +
//            "       ㄴ pm : 오후\n " +
//            "   distance : 거리\n" +
            "   fishingTideTime : 물때\n" +
            "   totalAvgByReview : 리뷰 총 평점\n" +
            "   tasteByReview : 손맛 평점 \n" +
            "   serviceByReview : 서비스 평점\n" +
            "   cleanByReview : 청결도 평점\n" +
            "   content : 리뷰 내용\n" +
            "   fileList : 이미지 파일 download url 리스트")
    @GetMapping("/myReviewList/{page}")
    public Page<ReviewDto> getReviewList(
            @RequestHeader("Authorization") String token,
            @PathVariable("page") int page
    ) throws ResourceNotFoundException {
        return myMenuService.getMyReview(token, page);
    }

    /*예약 내역 리스트 보기
    * - DTO에 필요한 정보를 담아서 반환. 데이터는 ship 대표사진, ship name, 선상구분, 주소, 거리, orders상태, (d-day), 이용일, 예약번호
    * - 넘어온 orders상태값에 해당하는 예약목록을 반환. 디폴트는 전체보기.
    * - 전체보기시 순서는 orders상태에 따라 예약대기 - 대기자예약 - 예약확정 - 예약취소 - 출조완료 - 예약완료 순으로.  */
    @ApiOperation(value = "예약내역 리스트",notes = "" +
            "- 마이메뉴 > 나의 예약 리스트 클릭시, 내 예약 목록 데이터 반환하는 api \n" +
            "- 요청 필드 ) \n" +
            "   sort : 어떤 예약 상태를 검색할건지. 반환필드에서 orderStatus필드를 보면, 영어로된것이 키값, 소괄호 안의 한글이 value값이다. " +
            "           여기서 key값을 string으로 넣어주면된다.  \n" +
            "- 필드 )\n" +
            "   id : 예약id\n" +
            "   goodsId : 예약한 상품의 id\n" +
            "   shipId : 상품이 속한 선박의 id\n" +
            "   shipImageUrl : 선상 대표이미지 download url\n" +
            "   shipName : 선상명\n" +
            "   fishingType : 선상낚시 / 갯바위 낚시\n" +
            "       ㄴ ship : 선상\n" +
            "       ㄴ sealocks : 갯바위\n " +
            "   sigungu : 주소(시,군,구)\n" +
//            "   distance : 거리\n" +
            "   orderStatus : 예약상태 / book(\"예약 대기\"), waitBook(\"대기자 예약\"), bookFix(\"예약 확정\"), bookCancel(\"예약 취소\")," +
            "    fishingComplete(\"출조 완료\"), bookConfirm(\"예약 완료\")\n" +
            "   fishingDate : 낚시일\n" +
            "   ordersNum : 예약번호")
    @GetMapping("/myOrdersList/{page}")
    public Page<OrdersDtoForPage> getMyOrdersList(
            @PathVariable("page") int page,
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "sort", required = false, defaultValue = "none") String sort
    ) throws ResourceNotFoundException {
        return myMenuService.getMyOrdersList(token, page, sort);
    }

    /*예약 상세보기
    * - orders에 대한 세부정보가 들은 dto반환. 데이터는 ship이름,ordersStatus,선상구분,주소,거리,출발시간(예약일),낚시어종,오전/오후,
    *   출항시간, 상품가격,상품수량,예약번호,예약자명,예약자연락처,결제일,결재수단,총주문금액,쿠폰할인금액,총결제액. */
    @ApiOperation(value = "예약 상세",notes = "" +
            "- \n" +
            "- 필드 ) \n" +
            "   id : 예약id\n" +
            "   shipId : 상품이 속한 선박의 id\n" +
            "   goodsId : 상품의 id\n" +
            "   shipName : 선상명\n" +
            "   orderStatus : 예약상태 / book(\"예약 대기\"), waitBook(\"대기자 예약\"), bookFix(\"예약 확정\"), bookCancel(\"예약 취소\")," +
            "    fishingComplete(\"출조 완료\"), bookConfirm(\"예약 완료\")\n" +
            "   fishingType : 선상낚시/갯바위낚시 / ship(\"선상\"), sealocks(\"갯바위\")\n" +
            "   sigungu : 주소(시,군,구)\n" +
//            "   distance : 거리\n" +
            "   fishingDate : 낚시일\n" +
            "   fishSpecies : 어종\n" +
            "   meridiem : 낚시 시간대 오전/오후\n" +
            "   shipStartTime : 출항시간\n" +
            "   goodPrice : 상품가격\n" +
            "   personnel : 상품 수량\n" +
            "   ordersNum : 예약 번호\n" +
            "   memberName : 회원명\n" +
            "   areaCode : 전화번호?!!!!!\n" +
            "   localNumber : 전화번호?!!!!!\n" +
            "   orderDate : 결재일\n" +
            "   paymentGroup : 결재수단 \n" +
            "   totalAmount : 총 주문금액\n" +
            "   discountAmount : 할인금액\n" +
            "   paymentAmount : 결재금액\n")
    @GetMapping("/OrdersDetail")
    public OrdersDetailDto getOrdersDetail(
            @RequestParam("ordersId") Long ordersId
    ) throws ResourceNotFoundException {
        return myMenuService.getOrdersDetail(ordersId);
    }


    /*물때 - 항구? 목록 반환*/
    @ApiOperation(value = "관측 지점 목록 반환",notes = "" +
            "" +
            "요청 필드 ) \n" +
            "- type : String / 필수 / '오늘의 물때'페이지에서의 관측소 목록이면 'today'. '날짜별 물때'페이지에서의 관측소 목록이면 'daily'.\n" +
            "- 헤더에 세션토큰 (선택)\n" +
            "응답 필드 ) \n" +
            "- observerId : Long / 관측소의 id\n" +
            "- observerName : String / 관측소 명\n" +
            "- observerCode : String / 관측소 코드\n" +
            "- isAlerted : Boolean / 현재 관측소에 대해 알람이 설정되어 있는지여부(로그인 되어있는경우에만 true일 수 있다)\n")
    @GetMapping("/searchPointList")
    public List<ObserverDtoList> getSearchPointList(
            @RequestParam("type") String type,
            @RequestHeader(value = "Authorization",required = false) String token
    ) throws ResourceNotFoundException {
        if(token == null){}
        else if(token.equals("")){token = null;}

        AlertType alertType = null;
        if(!(type.equals("today") || type.equals("daily"))){throw new RuntimeException("type값으로는 'today'또는 'daily'만 가능합니다.");}
        else if(type.equals("today")){ alertType = AlertType.tideLevel; }
        else if(type.equals("daily")){ alertType = AlertType.tide; }
        return myMenuService.getSearchPointList( token, alertType);
    }

    /*오늘의 물때정보 반환*/
    @ApiOperation(value = "오늘의 물때 정보",notes = "" +
            "요청 필드 ) \n" +
            "- observerId : Long / 필수 / 관측소 id\n" +
            "- 헤더에 세션토큰 (선택)\n" +
            "응답 필드 ) \n" +
            "- observerId : Long / 관측소 id\n" +
            "- observerName : String / 관측소명\n" +
            "- isAlerted : Boolean / 현재 관측소에 대해 알림설정이 되어있는지 여부\n" +
            "- date : String / 오늘 날짜\n" +
            "- weather : String / 현재 날씨\n" +
            "- tideList : 조위 최고, 최저 객체 리스트. 객체의 필드들은 아래와 같다. \n" +
            "\t\t\t     ㄴ dateTime: 날짜 \n" +
            "\t\t\t     ㄴ level: 조위 \n" +
            "\t\t\t     ㄴ peak: 고조/저조 \n" +
            "- highWater : Boolean / 만조 알림 여부\n" +
            "- highWaterBefore1 : Boolean / 만조 1시간 전 알림 여부\n" +
            "- highWaterBefore2 : Boolean / 만조 2시간 전 알림 여부\n" +
            "- highWaterAfter1 : Boolean / 만조 1시간 후 알림 여부\n" +
            "- highWaterAfter2 : Boolean / 만조 2시간 후 알림 여부\n" +
            "- lowWater : Boolean / 간조 알림 여부\n" +
            "- lowWaterBefore1 : Boolean / 간조 1시간 전 알림 여부\n" +
            "- lowWaterBefore2 : Boolean / 간조 2시간 전 알림 여부\n" +
            "- lowWaterAfter1 : Boolean / 간조 1시간 후 알림 여부\n" +
            "- lowWaterAfter2 : Boolean / 간조 2시간 후 알림 여부\n" +
            "")
    @GetMapping("/todayTide")
    public TodayTideDto getTodayTide(
            @RequestParam("observerId") Long observerId,
            @RequestHeader(value = "Authorization",required = false) String token
    ) throws IOException, ResourceNotFoundException {
        if(token == null){}
        else if(token.equals("")){token = null;}
        return myMenuService.getTodayTide(observerId,token);
    }

    /*조위 알림 추가*/
    @ApiOperation(value = "오늘의 물때 알림 추가",notes = "" +
            "요청 필드 ) \n" +
            "- highTideAlert : Integer[] / 만조 알람 시간이 들어있는 배열. ex) 만조 두시간 전 알림이면 -2, 만조 한시간 후 알림이면 1, 만조 알림이면 0\n" +
            "- lowTideAlert : Integer[] / 간조 알림 시간이 들어있는 배열. highTideAlert와 동일한 방식.\n" +
            "- observerId : Long / 관측소 id\n" +
            "- 헤더에 세션토큰 필수. \n" +
            "응답필드 ) 성공시 true\n")
    @PostMapping("/addTideLevelAlert")
    public Boolean addTideLevelAlert(
            @RequestHeader("Authorization") String token,
            @RequestBody AddTideLevelAlertDto dto
    ) throws ResourceNotFoundException {
        return myMenuService.addTideLevelAlert(dto.getHighTideAlert(), dto.getLowTideAlert(), dto.getObserverId(), token);
    }

    /*날짜별 물때정보*/
    @ApiOperation(value = "날짜별 물때정보",notes = "" +
            "요청 필드 ) \n" +
            "- observerId : Long / 필수 / 관측소 id\n" +
            "- date : String / 필수 / 날짜. 'yyyy-MM-dd'형태.\n" +
            "- 헤더에 세션토큰 (선택)\n" +
            "응답 필드 ) \n" +
            "- observerId : Long / 관측소 id\n" +
            "- observerName : String / 관측소 명\n" +
            "- isAlerted : Boolean / 현재 관측소에 대해 알림이 설정되어있는지 여부\n" +
            "- date : String / 날짜\n" +
            "- weather : String / 날씨. 현재를 기준으로 3일후부터 10일까지의 데이터만존재. 없을시 null. 3~7일까지는 오전,오후날씨. 이후로는 하루평균날씨.\n " +
            "- tideList : 조위 최고, 최저 객체 리스트. 객체의 필드들은 아래와 같다. \n" +
            "\t\t\t     ㄴ dateTime: 날짜 \n" +
            "\t\t\t     ㄴ level: 조위 \n" +
            "\t\t\t     ㄴ peak: 고조/저조 \n" +
            "- alertTideList : Boolean형 배열 / 물때 알림 여부. index순서대로 1물,2물,...,13물,14물,15물(조금).\n" +
            "- alertDayList : Boolean형 배열 / 몇일전 알림 여부. index순서대로 1일전,2일전,...,7일전\n" +
            "- alertTimeList : Boolean형 배열 / 몇시 알림 여부. index순서대로 0시,3시,6시,9시,12시\n")
    @GetMapping("/tideByDate")
    public TideByDateDto getTideByDate(
            @RequestParam("observerId") Long observerId,
            @RequestParam("date") String date,
            @RequestHeader(value = "Authorization",required = false) String token
    ) throws ResourceNotFoundException, IOException, ParseException {
        if(token == null){}
        else if(token.equals("")){token = null;}
        return myMenuService.getTideByDate(observerId,date,token);
    }


    /*물때 알림 추가*/
    @ApiOperation(value = "물때 알림 추가",notes = "" +
            "요청 필드  ) \n" +
            "- observerId : Long / 필수 / 위치의 id\n" +
            "- tide : Integer[] / 필수 / 알림 물때의 리스트 / 1물 : 배열에 1추가, 조금 : 15추가. \n" +
            "- day : Integer[] / 필수 / 몇일 전에 알림을 받을지 리스트. 1일전이면 1추가, 7일전이면 7추가. \n" +
            "- time : Integer[] / 필수 / 몇시에 알림을 받을지 리스트. 0시,3시,6시,9시,12시만 가능하며, 입력은 3시일경우 3, 12시일경우 12, ...\n" +
            "응답 필드 ) 성공시 true\n")
    @PostMapping("/addTideAlert")
    public Boolean addTideAlert(
            @RequestBody AddTideAlertDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return myMenuService.addTideAlert(dto.getObserverId(),dto.getTide(),dto.getDay(),dto.getTime(),token);
    }
}
