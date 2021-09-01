package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.exception.EmptyListException;
import com.tobe.fishking.v2.model.fishing.ShipSearchDTO;
import com.tobe.fishking.v2.model.fishing.TvListResponse;
import com.tobe.fishking.v2.service.YoutubeService;
import com.tobe.fishking.v2.service.fishking.ShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"어복TV"})
@RequiredArgsConstructor
public class TvController {

    private final YoutubeService youtubeService;
    private final ShipService shipService;

    @GetMapping("/tv/list")
    @ApiOperation(value = "어복TV 유튜브 리스트", notes = "어복TV 유튜브 리스트 {" +
            "\n items: 영상 리스트 [ {" +
            "\n id: 영상 id" +
            "\n snippet: {" +
            "\n     publishedAt: 게시시간" +
            "\n     title: 제목" +
            "\n     description: 설명" +
            "\n     thumbnails: {" +
            "\n         high: {" +
            "\n              url: 썸네일이미지 주소" +
            "\n         }" +
            "\n     }" +
            "\n }" +
            "\n statistics: {" +
            "\n     viewCount: 조회수" +
            "\n }" +
            "\n }, ... ]" +
            "\n playUrl: 영상주소. https://www.youtube.com/watch?v= 뒤에 각 영상의 id를 붙이면 해당 영상의 주소가 됩니다." +
            "\n nextPageToken: 다음 페이지 토큰, 다음페이지 없는 경우 null. " +
            "\n")
    public Map<String, Object> getYoutubeList(
            @RequestParam(required = false, defaultValue = "") String nextPageToken,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        return youtubeService.getYoutube(nextPageToken, pageSize);
    }

    @ApiOperation(value = "어복TV 라이브 리스트", notes = "어복TV 라이브 리스트. {" +
            "\n content: [{" +
            "\n     id: 선박 id" +
            "\n     shipName: 선박명" +
            "\n     thumbnailUrl: 섬네일주소" +
            "\n }, ... ]" +
            "\n ...," +
            "\n totalElements: 총 내용 개수," +
            "\n totalPages: 총 페이지 수 " +
            "\n first: 첫페이지 여부" +
            "\n end: 마지막 페이지 여부" +
            "\n empty: content 가 비어있는가 여부 }" +
            "\n 요청값은 선상 리스트와 같습니다. " +
            "\n fishingType 의 경우 아무 값이나 보내셔도 ship, seaRock 모두 포함됩니다." +
            "\n hasRealTimeVideo 의 경우 이 API 에서는 항상 true 입니다. " +
            "\n")
    @GetMapping("/tv/lives/{page}")
    public Page<TvListResponse> getTvList(ShipSearchDTO shipSearchDTO,
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
        Page<TvListResponse> tv = shipService.getTvList(shipSearchDTO, page);
        if (tv.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return tv;
        }
    }

    @ApiOperation(value = "어복TV 라이브 상세", notes = "어복TV 라이브 상세. " +
            "\n {" +
            "\n     cameraData: 현재 카메라 정보 {" +
            "\n         id: 카메라 id" +
            "\n         name: 카메라 이름" +
            "\n         thumbnail: 썸네일 주소" +
            "\n         liveVideo: 영상주소" +
            "\n         species: 선박의 어종" +
            "\n         address: 선박의 주소" +
            "\n     }" +
            "\n     cameraList: 선박의 카메라 리스트 [ {" +
            "\n         id: 카메라 id" +
            "\n         name: 카메라 이름" +
            "\n         thumbnail: 썸네일 주소" +
            "\n         liveVideo: 선택된 카메라의 경우 영상주소, 외에는 빈 문자열" +
            "\n         species: 선택된 카메라의 경우 선박의 어종, 외에는 빈 문자열" +
            "\n         address: 선택된 카메라의 경우 선박의 주소, 외에는 빈 문자열" +
            "\n     }, ... ]" +
            "\n }")
    @GetMapping("/tv/live")
    public Map<String, Object> getLiveDetail(@RequestParam Long shipId,
                                             @RequestParam(required = false, defaultValue = "0") Long cameraId) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return shipService.getLiveDetail(shipId, cameraId);
    }

    @ApiOperation(value = "어복tude 목록 삭제", notes = "" +
            "req args ) \n" +
            "\t videoId : String / youtube video의 id\n" +
            "return ) boolean")
    @DeleteMapping("/tv/{videoId}")
    public Boolean deleteFishingTube(@PathVariable("videoId") String videoId, @RequestHeader("Authorization") String token){
        return youtubeService.deleteFishingTube(videoId, token);
    }

    @ApiOperation(value = "어복tude 목록 추가", notes = "" +
            "req args ) \n" +
            "\t videoId : String / youtube video의 id\n" +
            "return ) boolean")
    @PostMapping("/tv/{videoId}")
    public Boolean addFishingTube(@PathVariable("videoId") String videoId, @RequestHeader("Authorization") String token)
            throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return youtubeService.addFishingTube(videoId, token);
    }
}
