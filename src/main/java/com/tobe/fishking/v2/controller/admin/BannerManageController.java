package com.tobe.fishking.v2.controller.admin;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.tobe.fishking.v2.enums.common.AdType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.admin.MainBannerDto;
import com.tobe.fishking.v2.model.admin.ModifyMainBannerDto;
import com.tobe.fishking.v2.service.admin.BannerManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Api(tags={"배너관리"})
@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping(value = "/v2/api")
public class BannerManageController {
    private final BannerManageService bannerManageService;

    //전체 배너 상태 조회
    @ApiOperation(value = "전체 배너 상태 조회")
    @GetMapping("/manage/banner")
    public Map<String, Object> getAllBanner(
            @RequestHeader("Authorization") String token
    ) throws ServiceLogicException {
        return bannerManageService.getAllBanner(token);
    }

    //배너 설정
    @ApiOperation(value = "배너 설정")
    @PutMapping("/manage/banner/{bannerCode}/{shipId}/{addRemove}")
    public Boolean setBanner(
            @PathVariable("bannerCode") String bannerCode,
            @PathVariable("shipId") Long shipId,
            @PathVariable("addRemove") String addRemove,
            @RequestHeader("Authorization") String token
    ) throws ServiceLogicException, ResourceNotFoundException {
        //path value검증
        try {
            AdType.valueOf(bannerCode);
        }catch (Exception e){throw new ServiceLogicException("bannerCode값이 알맞은 코드값이 아닙니다");}
        if(!addRemove.equals("add") && !addRemove.equals("remove") ){throw new ServiceLogicException("addRemove값으로는 'add', 'remove'만 가능합니다");}

        Boolean result = bannerManageService.setBanner(bannerCode, shipId, addRemove, token);
        return result;
    }

    //메인 배너 조회
    @ApiOperation(value = "메인 배너 조회")
    @GetMapping("/manage/banner/main")
    public List<MainBannerDto> getMainBannerList(
            @RequestHeader("Authorization") String token
    ) throws ServiceLogicException {
        return bannerManageService.getMainBanner();
    }


    //메인 배너 설정
    @ApiOperation(value="메인 배너 설정",notes = "" +
            "배열 요소들이 하나의 배너가되며, 기존 배너리스트를 대체한다. 배너 순서는 배열의 index순서이다. \n" +
            "요청필드 ) \n" +
            "- bannerList : [\n" +
            "\t{ \n" +
            "\t\timageUrl : String / 이미지 url\n" +
            "\t\tfileId : Long / 이미지 file의 id\n" +
            "\t\tlinkUrl : String / 클릭시 이동될 url\n" +
            "\t},\n" +
            "...]")
    @PutMapping("/manage/banner/mainBanner")
    public Boolean setMainBanner(
            @RequestBody @Valid ModifyMainBannerDto[] dto,
            @RequestHeader("Authorization") String token
    ) throws ServiceLogicException, ResourceNotFoundException {
        return bannerManageService.setMainBanner(dto, token);
    }



}
