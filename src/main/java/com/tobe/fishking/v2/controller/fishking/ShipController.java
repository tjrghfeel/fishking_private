package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.enums.ErrorCodes;
import com.tobe.fishking.v2.exception.ApiException;
import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.AddShipDTO;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.service.common.CommonService;
import com.tobe.fishking.v2.service.fishking.PlacesService;
import com.tobe.fishking.v2.service.fishking.ShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"선상 및 갯바위"})
@RequiredArgsConstructor
public class ShipController {

    private final ShipService shipService;
    private final CommonService commonService;
    private final PlacesService placesService;

    @ApiOperation(value = "배 리스트", notes = "배 리스트. 필수 아닌 값은 빈 문자열 또는 빈 리스트로 보내면 됩니다. speciesList, servicesList, facilitiesList, genresList는 무시하시면 됩니다.")
    @GetMapping("/ships/{page}")
    public Page<ShipListResponse> getShips(ShipSearchDTO shipSearchDTO,
                                           @ApiParam(value = "페이지. 0부터 시작", required = true, defaultValue = "0", example = "0") @PathVariable int page,
                                           @RequestParam(value = "species[]", required = false) String[] species,
                                           @RequestParam(value = "services[]", required = false) String[] services,
                                           @RequestParam(value = "facilities[]", required = false) String[] facilities,
                                           @RequestParam(value = "genres[]", required = false) String[] genres) throws EmptyListException {
        if (species != null) {
            if (species.length != 0) {
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
        Page<ShipListResponse> ship = shipService.getShips(shipSearchDTO, page);
        return ship;
//        if (ship.getTotalElements() == 0) {
//            throw new EmptyListException("결과리스트가 비어있습니다.");
//        } else {
//            return ship;
//        }
    }

    @ApiOperation(value = "배 리스트 ad 통합",
            notes = "{ " +
                    "\n ad: 기존의 /v2/api/ship/ad 로 요청할 때와 같은 응답 값. page 가 0 이 아닌 경우에는 null" +
                    "\n list: 기존의 /v2/api/ships/{page} 로 요청할 때와 같은 응답 값")
    @GetMapping("/ships/list/{page}")
    public Map<String, Object> getShipsList(ShipSearchDTO shipSearchDTO,
                                           @ApiParam(value = "페이지. 0부터 시작", required = true, defaultValue = "0", example = "0") @PathVariable int page,
                                           @RequestParam(value = "species[]", required = false) String[] species,
                                           @RequestParam(value = "services[]", required = false) String[] services,
                                           @RequestParam(value = "facilities[]", required = false) String[] facilities,
                                           @RequestParam(value = "genres[]", required = false) String[] genres) throws EmptyListException {
        if (species != null) {
            if (species.length != 0) {
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
        Page<ShipListResponse> ship = shipService.getShips(shipSearchDTO, page);
        if (ship.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("list", ship);
            if (page == 0) {
                result.put("ad", commonService.shipAdList(shipSearchDTO.getFishingType(), shipSearchDTO.getLatitude(), shipSearchDTO.getLongitude()));
            } else {
                result.put("ad", null);
            }
            return result;
        }
    }

    @ApiOperation(value = "배 리스트", notes = "지도보기를 위한 배 리스트." +
            "\n 해당 필터에 걸리는 모든 선박 정보를 보내줍니다." +
            "\n 필수 아닌 값은 빈 문자열 또는 빈 리스트로 보내면 됩니다. speciesList, servicesList, facilitiesList, genresList는 무시하시면 됩니다." +
            "\n " +
            "\n [{ " +
            "\n id: 상품 id" +
            "\n shipImageFileUrl: 선박 이미지 주소 " +
            "\n shipName: 선박명" +
            "\n sido: 시도" +
            "\n sigungu: 시군구" +
            "\n distance: 거리" +
            "\n location: {" +
            "\n     latitude: 위도" +
            "\n     longitude: 경도" +
            "\n } " +
            "\n address: 주소" +
            "\n fishSpecies: [{" +
            "\n     id: id" +
            "\n     codeGroup: " +
            "\n     codeGroupName: 코드 그룹 명" +
            "\n     code: 코드" +
            "\n     codeName: 코드명" +
            "\n     extraValue1: 대체값" +
            "\n     remark: 주석" +
            "\n }]" +
            "\n fishSpeciesCount: 대상 어종 수" +
            "\n lowPrice: 상품 중 가장 낮은 가격" +
            "\n sold: 결제 수 " +
            "\n }, ... ]" +
            "\n 상단에 선상/포인트/유저조행기 선택은 뺍니다." +
            "\n 선상 탭에서 지도보기 누르는 경우는 선상만, 갯바위에서 누르는 경우는 갯바위만 (fishingType=ship|seaRocks) 보여줍니다.")
    @GetMapping("/ships/map")
    public List<ShipListResponse> getShipsForMap(ShipSearchDTO shipSearchDTO,
                                                 @RequestParam(value = "species[]", required = false) String[] species,
                                                 @RequestParam(value = "services[]", required = false) String[] services,
                                                 @RequestParam(value = "facilities[]", required = false) String[] facilities,
                                                 @RequestParam(value = "genres[]", required = false) String[] genres) throws EmptyListException {
        if (species != null) {
            if (species.length != 0) {
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
        List<ShipListResponse> ship = shipService.getShipsForMap(shipSearchDTO);
        if (ship.size() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return ship;
        }

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
            "\n eventsList: 이벤트 리스트 [{ " +
            "\n         id: 이벤트 id" +
            "\n         title: 제목 " +
            "\n }, ... ]" +
            "\n rockData: 갯바위 데이터. 선상일경우 null [{" +
            "\n     name: 갯바위 명" +
            "\n     averageDepth: 평균 수심" +
            "\n     floorMaterial: 저질" +
            "\n     tideTime: 적정 물때" +
            "\n     introduce: 소개" +
            "\n     address: 주소" +
            "\n     latitude: 위도 " +
            "\n     longitude: 경도" +
            "\n }, ... ]" +
            "\n " +
            "\n 상단 이미지관련" +
            "\n     liveVideo가 빈 문자열이 아닌 경우: 해당 주소를 video 태그로 보여준다. " +
            "\n         (기본적으로HTML5  video  tag를사용하여재생할수있고m3u8 재생을위해추가적인javascript 라이브러리가필요하다.(hls.js : https://github.com/video-dev/hls.js/)) /playVideo.html 참고" +
            "\n     liveVideo가 빈 문자열이고 profileImage가 빈 문자열 아닌 경우: profileImage를 보여준다. " +
            "\n     둘 다 빈 문자열인 경우: 기본 이미지를 보여준다. (선박 등록시 처리 할 예정이라 추후에 빈 이미지는 없을 예정입니다) " +
            "\n 기획 전달 사항" +
            "\n     상품 오전, 오후 없음" +
            "\n     선박 명 아래의 점수는 전체 평점 평균, 오른쪽의 숫자는 리뷰 수" +
            "\n     유료서비스, 무료서비스를 서비스 하나로 통합" +
            "\n     주변시설 삭제" +
            "\n 갯바위데이터 추가" +
            "\n     갯바위의 계절별 주요 대상어종 및 평균수온은 소개로 대체" +
            "\n     기준위치와의 거리 제거" +
            "\n ")
    @GetMapping("/ship/{ship_id}")
    public ShipResponse shipDetail(
            @RequestHeader(name = "Authorization") String sessionToken,
            @ApiParam(value = "배 id", required = true, example = "0") @PathVariable Long ship_id) throws UnsupportedEncodingException {
        return shipService.getShipDetail(ship_id, sessionToken);
    }

    @ApiOperation(value = "배의 상품 리스트", notes = "배의 상품 리스트 ")
    @GetMapping("/ship/{ship_id}/goods")
    public List<GoodsResponse> shipGoods(
            @RequestHeader(name = "Authorization") String sessionToken,
            @ApiParam(value = "선택 날짜", required = true, example = "2021-02-19") @RequestParam String date,
            @ApiParam(value = "배 id", required = true, example = "0") @PathVariable Long ship_id) throws EmptyListException {
        List<GoodsResponse> goods = shipService.getShipGoods(ship_id, date);
        if (goods.size() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return goods;
        }
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
    public GoodsResponse getGoodsDetail(@PathVariable Long goods_id) {
        return shipService.getGoodsDetail(goods_id);
    }

    @ApiOperation(value = "해당 상품 날짜 승선위치 ", notes = "해당 상품의 날짜 승선위치. date: yyyy-MM-dd" +
            "\n total: 배의 승선위치 ([1,2,3,4,5,6] 인 경우 1~6번 위치만 사용한다고 등록) 갯바위 상품의 경우 null" +
            "\n used: 이미 예약된 승선위치 ([1,2] 인 경우 1,2 번 위치는 이미 예약) 갯바위 상품의 경우 포인트의 id " +
            "\n type: 배의 타입 3, 5, 9 세 값중 하나가 전달되며 각각 3톤 (8인승), 5톤 (18인승), 9톤 (22인승). 갯바위 상품의 경우 null" +
            "\n selectPosition: 위치선택 여부" +
            "\n left: 현재 남은 인원" +
            "\n rockData: 갯바위 포인트, 선상 상품의 경우 null [{" +
            "\n     name: 갯바위 명" +
            "\n     address: 갯바위의 주소" +
            "\n     latitude: 갯바위의 위도" +
            "\n     longitude: 갯바위의 경도" +
            "\n     points: 해당 갯바위의 포인트 리스트 [{ " +
            "\n         latitude: 포인트의 위도" +
            "\n         longitude: 포인트의 경도" +
            "\n         id: 포인트 id " +
            "\n     }, ... ]" +
            "\n }, ... ] ")
    @GetMapping("/goods/{goods_id}/position")
    public Map<String, Object> getGoodsDatePositions(@PathVariable Long goods_id, @RequestParam String date) {
        return shipService.getGoodsDatePositions(goods_id, date);
    }

//    @ApiOperation(value = "해당 상품 날짜 갯바위 포인트 ", notes = "해당 상품의 날짜 승선위치. date: yyyy-MM-dd" +
//            "\n rockData: 갯바위 포인트 [{" +
//            "\n     name: 갯바위 명" +
//            "\n     address: 갯바위의 주소" +
//            "\n     latitude: 갯바위의 위도" +
//            "\n     longitude: 갯바위의 경도" +
//            "\n     points: 해당 갯바위의 포인트 리스트 [{ " +
//            "\n         latitude: 포인트의 위도" +
//            "\n         longitude: 포인트의 경도" +
//            "\n         id: 포인트 id " +
//            "\n     }, ... ]" +
//            "\n }, ... ] " +
//            "\n used: 이미 예약된 포인트 id ([1,2] 인 경우 1,2 는 이미 예약)")
//    @GetMapping("/goods/{goods_id}/position/seaRock")
//    public Map<String, Object> getGoodsDatePositionsSeaRock(@PathVariable Long goods_id, @RequestParam String date) {
//        return shipService.getGoodsDatePositionsSeaRock(goods_id, date);
//    }

//    @ApiOperation(value = "배 정보", notes = "배 정보. ")
//    @GetMapping("/ship/{ship_id}")
//    public ShipResponse shipDetail(@ApiParam(value = "배 id", required = true, example = "0") @PathVariable Long ship_id) {
//        return shipService.getShipDetail(ship_id);
//    }

    @ApiOperation(value = "예약", notes = "예약정보 전화번호는 '-' 포함. positionsList 는 무시하시면 됩니다." +
            "\n id: 주문 id" +
            "\n orderNumber: 주문번호, sndOrdernumber 으로 ksnet에 넘깁니다. " +
            "\n goodsName: 상품명, sndGoodname 으로 ksnet에 넘깁니다.  " +
            "\n amount: 결제금액, sndAmount 로 ksnet에 넘깁니다.  " +
            "\n orderName: 주문자 이름, sndOrdername 으로 ksnet에 넘깁니다.  " +
            "\n email: 주문자 이메일, sndEmail 로 ksnet에 넘깁니다.  " +
            "\n phoneNumber: 주문자 전화번호, sndMobile 로 ksnet에 넘깁니다.  " +
            "\n showCard: 보여지는 카드사, sndShowcard 로 ksnet에 넘깁니다.  " +
            "\n installMentType: 선택 가능한 할부 개월 수, sndInstallmenttype 으로 ksnet에 넘깁니다.  " +
            "\n interestType: 가맹점부담 무이자 할부 설정값, sndInterestype 으로 ksnet에 넘깁니다.  " +
            "\n reply: 리턴주소, sndReply 로 ksnet에 넘깁니다.  " +
            "\n shopNumber: 상점번호, sndStoreid 로 ksnet에 넘깁니다. " +
            "\n payMethod: 결제방식, sndPaymethod 로 ksnet에 넘깁니다. " +
            "\n" +
            "\n /pay_request.html 와 /ajax.html 을 참고해주세요")
    @PostMapping(value = "/ship/reserve", produces = "application/json")
    public OrderResponse Reserve(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody ReserveDTO reserveDTO) {
        OrderResponse response = shipService.reserve(reserveDTO, token, reserveDTO.getPersonsName(), reserveDTO.getPersonsPhone(), reserveDTO.getEmergencyPhone(), reserveDTO.getPersonsBirthdate());
//        model.addAttribute("pay", response);
//        return "pay_request";
        return response;
    }

    @ApiOperation(value = "선박 리뷰 리스트", notes = "해당 선박의 리뷰 리스트 page 는 0부터 시작" +
            "\n average: 전체 평균," +
            "\n taste: 전체 손맛," +
            "\n service: 전체 서비스," +
            "\n clean: 전체 청결도," +
            "\n reviews: { " +
            "\n     content: [{" +
            "\n     id: 리뷰 id" +
            "\n     goodsId: 상품 id " +
            "\n     shipId: 선박 id  " +
            "\n     memberId: 작성자 id  " +
            "\n     profileImage: 작성자 프로필 사진  " +
            "\n     nickName: 작성자 닉네임 " +
            "\n     fishingDate: 상품 출조일 " +
            "\n     goodsName: 상품명 " +
            "\n     fishingTideTime: 출조일의 물때 " +
            "\n     avgByReview: 평점 평균 " +
            "\n     tasteByReview: 손맛 평점 " +
            "\n     serviceByReview: 서비스 평점 " +
            "\n     cleanByReview: 청결도 평점 " +
            "\n     content: 내용 " +
            "\n     images: 사진 [{" +
            "\n         files_id: 파일 아이디" +
            "\n         download_url: 파일 주소" +
            "\n     }, ... ]" +
            "\n }, ... ]," +
            "\n ...," +
            "\n totalElements: 총 내용 개수," +
            "\n totalPages: 총 페이지 수 " +
            "\n first: 첫페이지 여부" +
            "\n end: 마지막 페이지 여부" +
            "\n empty: content 가 비어있는가 여부 }" +
            "\n" +
            "\n 퍼블상의 우럭(오후) 는 상품명입니다. 풍향 풍속 데이터 안보여주시면 됩니다.")
    @GetMapping("/ship/{ship_id}/review/{page}")
    public Map<String, Object> getShipReviews(
            @PathVariable Long ship_id,
            @PathVariable Integer page,
            @RequestParam(defaultValue = "10") Integer size) throws EmptyListException {
        return shipService.getReviewByShip(ship_id, page, size);
    }

    @ApiOperation(value = "선상 & 갯바위 광고 리스트", notes = "선상 & 갯바위 광고 리스" +
            "\n premium: 인기 프리미엄 광고 [{ " +
            "\n id: 상품 id" +
            "\n shipImageFileUrl: 선박 이미지 주소 " +
            "\n shipName: 선박명" +
            "\n sido: 시도" +
            "\n sigungu: 시군구" +
            "\n distance: 거리" +
            "\n location: {" +
            "\n     latitude: 위도" +
            "\n     longitude: 경도" +
            "\n } " +
            "\n address: 주소" +
            "\n fishSpecies: [{" +
            "\n     id: id" +
            "\n     codeGroup: " +
            "\n     codeGroupName: 코드 그룹 명" +
            "\n     code: 코드" +
            "\n     codeName: 코드명" +
            "\n     extraValue1: 대체값" +
            "\n     remark: 주석" +
            "\n }, ... ]" +
            "\n normal: 프리미엄 광고 [{ " +
            "\n id: 상품 id" +
            "\n shipImageFileUrl: 선박 이미지 주소 " +
            "\n shipName: 선박명" +
            "\n sido: 시도" +
            "\n sigungu: 시군구" +
            "\n distance: 거리" +
            "\n location: {" +
            "\n     latitude: 위도" +
            "\n     longitude: 경도" +
            "\n } " +
            "\n address: 주소" +
            "\n fishSpecies: [{" +
            "\n     id: id" +
            "\n     codeGroup: " +
            "\n     codeGroupName: 코드 그룹 명" +
            "\n     code: 코드" +
            "\n     codeName: 코드명" +
            "\n     extraValue1: 대체값" +
            "\n     remark: 주석" +
            "\n }, ... ]" +
            "\n" +
            "\n 선상: fishingType=ship" +
            "\n 갯바위: fishingType=seaRocks" +
            "\n 로 요청해주세요" +
            "\n 퍼블상의 이벤트제목, 공지제목은 빼주세요")
    @GetMapping("/ship/ad")
    public Map<String, Object> getShipAd(@RequestParam String fishingType,
                                         @RequestParam(required = false, defaultValue = "37.5642135") Double latitude,
                                         @RequestParam(required = false, defaultValue = "127.0016985") Double longitude) {
        return commonService.shipAdList(fishingType, latitude, longitude);
    }

//    @ApiOperation(value = "거리계산")
//    @GetMapping("/calc")
//    public String calcDisance() {
////        return youtubeService.getYoutube("CAEQAA");
////        return youtubeService.getYoutube(null);
//        return "true";
//    }
}
