package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.TakeResponse;
import com.tobe.fishking.v2.model.common.AddTakeDto;
import com.tobe.fishking.v2.model.common.DeletingTakeDto;
import com.tobe.fishking.v2.service.common.TakeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@Api(tags = {"찜"})
@RequestMapping(value = "/v2/api")
public class TakeController {

    @Autowired
    TakeService takeService;

    /*찜 추가
     * 반환 : 추가된 Take 엔터티의 id반환. */
    @ApiOperation(value = "찜 추가",notes = "업체에 대한 찜 추가. \n" +
            "- 요청 필드 )\n" +
            "   linkId : Integer / 찜 대상의 id \n" +
            "   헤더에 세션토큰 필요\n" +
            "- 반환값 ) 생성된 찜의 id ")
    @PostMapping(value = "/take")
    public Long addTake(@RequestHeader("Authorization") String token, @RequestBody AddTakeDto dto) throws ResourceNotFoundException {
        return takeService.addTake(dto.getLinkId(), token);
    }

    /*찜 삭제*/
    @ApiOperation(value = "찜 삭제",notes = "현재 로그인한 회원의 찜목록에서 해당 찜을 삭제. \n" +
            "요청 필드 )\n" +
            "- linkId : Long / linkId, takeId 둘중 하나 필수 / 찜 대상의 id\n" +
            "- takeId : Long / linkId, takeId 둘중 하나 필수 / 찜의 id\n" +
            "- 헤더에 세션토클 필요\n")
    @DeleteMapping(value = "/take")
    public Long deleteTake(@RequestBody DeletingTakeDto dto,@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        return takeService.deleteTake(dto.getTakeId(),dto.getLinkId(), token);
    }

    /*선상 낚시 찜 목록 조회*/
    @ApiOperation(value = "선상/갯바위 상품 찜 목록 조회",notes = "" +
            "- 요청 필드 )\n" +
            "   fishingType : String / 필수 / 선상 또는 갯바위 / ship(\"선상\"), seaRocks(\"갯바위\")\n" +
            "- 응답 필드 ) \n" +
            "   takeId : 찜id \n" +
            "   name : 찜한 상품의 이름\n" +
            "   fishingType : 선상/갯바위인지\n" +
            "   address : 선상 주소\n" +
            "   distance : 거리\n" +
            "   isLive : 실시간영상 진행 여부\n" +
            "   downloadThumbnailUrl : 선박 이미지 url\n" +
            "   ")
    @GetMapping(value = "/take/{fishingType}/{page}")
    public Page<TakeResponse> getFishingTypeFishTakeList(
            @PathVariable("fishingType") String fishingType,
            @PathVariable("page") int page,
            HttpServletRequest request
    ) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        if(!(fishingType.equals("ship") || fishingType.equals("seaRocks")) ){
            throw new RuntimeException("fishingType 값에는 'ship' 또는 'seaRocks'만 가능합니다.");
        }
        return takeService.getFishingTypeFishTakeList(fishingType, sessionToken,page);
    }

    /*선상, 갯바위 찜 개수 넘겨주는 컨트롤러.
     * 반환 : int형 배열로 0번에 선상 찜개수, 1번에 갯바위 찜 개수를 저장하여 반환.*/
    @ApiOperation(value = "선상/갯바위 상품 찜 개수 조회")
    @GetMapping(value = "/take/count")
    public int[] getTakeCount(HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return takeService.getTakeCount(sessionToken);
    }




}
