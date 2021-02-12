package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.ShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ResponseHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/v2/api")
@Api(tags = {"선상 및 갯바위"})
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;
    private final MemberService memberService;

    @ApiOperation(value = "배 리스트", notes = "배 리스트. 필수 아닌 값은 빈 문자열 또는 빈 리스트로 보내면 됩니다. speciesList, servicesList, facilitiesList, genresList는 무시하시면 됩니다.")
    @GetMapping("/ships/{page}")
    @ResponseBody
    public Page<ShipListResponse> getShips(ShipSearchDTO shipSearchDTO,
                                           @ApiParam(value = "페이지. 0부터 시작", required = true, defaultValue = "0", example = "0") @PathVariable int page,
                                           @RequestParam(value = "species[]", required = false) String[] species,
                                           @RequestParam(value = "services[]", required = false) String[] services,
                                           @RequestParam(value = "facilities[]", required = false) String[] facilities,
                                           @RequestParam(value = "genres[]", required = false) String[] genres) {
        if (species != null) {
            if (species.length != 0 ) {
                shipSearchDTO.setSpeciesList(Arrays.asList(species.clone()));
            }
        }
        if (services != null) {
            if (services.length != 0) {
                shipSearchDTO.setServicesList(Arrays.asList(services.clone()));
            }
        }
        if (facilities != null) {
            if (facilities.length != 0) {
                shipSearchDTO.setFacilitiesList(Arrays.asList(facilities.clone()));
            }
        }
        if (genres != null) {
            if (genres.length != 0) {
                shipSearchDTO.setGenresList(Arrays.asList(genres.clone()));
            }
        }
        return shipService.getShips(shipSearchDTO, page);
    }

    @ApiOperation(value = "배 정보", notes = "배 정보." +
            "\n id: ship_id" +
            "\n fishingType: fishingType" +
            "\n name: 선박이름" +
            "\n address: 선박 주소" +
            "\n sido: 선박 시도" +
            "\n longitude: " +
            "\n sigungu: 선박 시군구" +
            "\n tel: 전화번호" +
            "\n weight: 급수 3,5,9 톤" +
            "\n boardingPerson: 배의 승선인원 수 8 18 22 " +
            "\n latitude: 선박 위도" +
            "\n longitude: 선박 경도" +
            "\n avgReview: 전체 평점 평균" +
            "\n tasteByReview: 손맛 점수" +
            "\n serviceByReview: 서비스 점수" +
            "\n cleanByReview: 청결도 점수" +
            "\n reviewCount: 리뷰 수" +
            "\n profileImage: 이미지 주소" +
            "\n liveVideo: 라이브 비디오 주소" +
            "\n goods: 상품리스트" +
            "\n services: 서비스" +
            "\n facilities: 편의시설" +
            "\n devices: 보유장비" +
            "\n fishingDiary: 조황일지 리스트" +
            "\n     { id: 조행기아이디, nickName: 작성자 닉네임, profileImage: 작성자 프로필사진, title: 제목, imageUrlList: 이미지 리스트 (최대 3개까지) , imageCount: 이미지 수 (총 이미지수 - imageUrlList의 크기), content: 내용, createdAt: 작성일 }" +
            "\n fishingDiaryCount: 조황일지 수" +
            "\n fishingBlog: 유저조행기 리스트" +
            "\n     { id: 조행기아이디, nickName: 작성자 닉네임, profileImage: 작성자 프로필사진, title: 제목, imageUrlList: 이미지 리스트 (최대 3개까지), imageCount: 이미지 수 (총 이미지수 - imageUrlList의 크기), content: 내용, createdAt: 작성일 }" +
            "\n fishingBlogCount: 유저조행기 수" +
            "\n ownerWordingTitle: 사장님 한마디 제목" +
            "\n ownerWording: 사장님 한마디 내용" +
            "\n noticeTitle: 공지사항 제목" +
            "\n notice: 공지사항 내용" +
            "\n events: 이벤트 리스트" +
            "\n " +
            "\n 상단 이미지관련" +
            "\n     liveVideo가 빈 문자열이 아닌 경우: 해당 주소를 video 태그로 보여준다. " +
            "\n         (기본적으로HTML5  video  tag를사용하여재생할수있고m3u8 재생을위해추가적인javascript 라이브러리가필요하다.(hls.js : https://github.com/video-dev/hls.js/)) " +
            "\n     liveVideo가 빈 문자열이고 profileImage가 빈 문자열 아닌 경우: profileImage를 보여준다. " +
            "\n     둘 다 빈 문자열인 경우: 기본 이미지를 보여준다. (선박 등록시 처리 할 예정이라 추후에 빈 이미지는 없을 예정입니다) " +
            "\n 기획 전달 사항" +
            "\n     상품 오전, 오후 없음" +
            "\n     선박 명 아래의 점수는 전체 평점 평균, 오른쪽의 숫자는 리뷰 수" +
            "\n     유료서비스, 무료서비스를 서비스 하나로 통합" +
            "\n     주변시설 삭제" +
            "\n ")
    @GetMapping("/ship/{ship_id}")
    @ResponseBody
    public ShipResponse shipDetail(
            @RequestHeader(name = "Authorization") String sessionToken,
            @ApiParam(value = "배 id", required = true, example = "0") @PathVariable Long ship_id) {
        return shipService.getShipDetail(ship_id, sessionToken);
    }

    @ApiOperation(value = "배의 상품 리스트", notes = "배의 상품 리스트 ")
    @GetMapping("/ship/{ship_id}/goods")
    @ResponseBody
    public List<GoodsResponse> shipGoods(
            @RequestHeader(name = "Authorization") String sessionToken,
            @ApiParam(value = "배 id", required = true, example = "0") @PathVariable Long ship_id) {
        return shipService.getShipGoods(ship_id);
    }

    @ApiOperation(value = "해당 상품 정보 ", notes = "해당 상품 정보 " +
            "\n id: goods_id" +
            "\n name: 상품명" +
            "\n price: 가격" +
            "\n startTime: 시작 시간" +
            "\n endTime: 종료 시간" +
            "\n fishSpecies: 어종 리스트" +
            "\n fishingDates: 조업일 리스트" +
            "\n startFishingDates: 조업일 시작" +
            "\n endFishingDates: 조업일 마지막" +
            "\n minPersonnel: 최소 인원" +
            "\n maxPersonnel: 최대 인원" +
            "\n reservationPersonal: 예약한 인원" +
            "\n observerCode: 관측소코드" +
            "\n positions: 상품의 위치 리스트 (배 또는 갯바위 위의 위치)" +
            "\n usedPositions: 사용된 위치 리스트 (배 또는 갯바위 위의 위치)" +
            "\n shipMaxPersonnel: 배의 최대 승선수" +
            "\n " +
            "\n reservation.html 기획변경" +
            "\n     날씨, 풍향, 풍속 보여주지 않음. " +
            "\n     물때, 조위만 보여줌.")
    @GetMapping("/goods/{goods_id}")
    @ResponseBody
    public GoodsResponse getGoodsDetail(@PathVariable Long goods_id) {
        return shipService.getGoodsDetail(goods_id);
    }

    @ApiOperation(value = "해당 상품 날짜 승선위치 ", notes = "해당 상품의 날짜 승선위치. date: yyyy-MM-dd")
    @GetMapping("/goods/{goods_id}/position")
    @ResponseBody
    public Map<String, Object> getGoodsDatePositions(@PathVariable Long goods_id, @RequestParam String date) {
        return shipService.getGoodsDatePositions(goods_id, date);
    }

//    @ApiOperation(value = "배 정보", notes = "배 정보. ")
//    @GetMapping("/ship/{ship_id}")
//    public ShipResponse shipDetail(@ApiParam(value = "배 id", required = true, example = "0") @PathVariable Long ship_id) {
//        return shipService.getShipDetail(ship_id);
//    }

    @ApiOperation(value = "예약", notes = "예약정보 전화번호는 '-' 포함. positionsList 는 무시하시면 됩니다.")
    @PostMapping("/ship/reserve")
    public String Reserve(
//            @RequestHeader(name = "Authorization") String sessionToken,
            ReserveDTO reserveDTO,
            @RequestParam(value = "positions[]") Integer[] positions,
            @RequestParam(value = "personsName[]") String[] personsName,
            @RequestParam(value = "personsPhone[]") String[] personsPhone,
            @RequestParam(value = "personsBirthdate[]") String[] personsBirthdate,
            @RequestParam(value = "token", required = false) String token,
            Model model) {
//        Member member = memberService.getMemberBySessionToken(sessionToken);
        if (positions != null) {
            if (positions.length != 0) {
                reserveDTO.setPositionsList(Arrays.asList(positions.clone()));
            }
        }
        OrderResponse response = shipService.reserve(reserveDTO, token, personsName, personsPhone, personsBirthdate);
        model.addAttribute("pay", response);
        return "pay_request";
    }

//    @ApiOperation(value = "거리계산")
//    @GetMapping("/calc")
//    public String calcDistance() {
//        shipService.calcDistance();
//        return "true";
//    }
}
