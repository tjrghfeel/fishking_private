package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.enums.fishing.FishingType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.TakeResponse;
import com.tobe.fishking.v2.service.common.TakeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(tags = {"찜"})
@RequestMapping(value = "/v1/api")
public class TakeController {

    @Autowired
    TakeService takeService;

    /*찜 추가
    * 반환 : 추가된 Take 엔터티의 id반환. */
    @PostMapping(value = "/take")
    public Long addTake(Long linkId, int takeType, Long member) throws ResourceNotFoundException {
        return takeService.addTake(linkId, takeType, member);
    }

    /*찜 삭제*/
    @DeleteMapping(value = "/take")
    public Long deleteTake(Long takeId) throws ResourceNotFoundException {
        return takeService.deleteTake(takeId);
    }

    /*선상 낚시 찜 목록 조회*/
    @GetMapping(value = "/take/{fishingType}")
    public List<TakeResponse> getFishingTypeFishTakeList(@PathVariable("fishingType") int fishingType, Long memberId) throws ResourceNotFoundException {
        return takeService.getFishingTypeFishTakeList(fishingType, memberId);
    }

    /*갯바위 낚시 찜 목록 조회. */

    /*선상, 갯바위 찜 개수 넘겨주는 컨트롤러.
    * 반환 : int형 배열로 0번에 선상 찜개수, 1번에 갯바위 찜 개수를 저장하여 반환.*/
    @GetMapping(value = "/take/count")
    public int[] getTakeCount(Long memberId) throws ResourceNotFoundException {
        return takeService.getTakeCount(memberId);
    }




}
