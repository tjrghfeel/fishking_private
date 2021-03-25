package com.tobe.fishking.v2.controller.smartsail;

import com.tobe.fishking.v2.enums.ErrorCodes;
import com.tobe.fishking.v2.exception.ApiException;
import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.smartfishing.FishingShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"스마트승선 카메라관리"})
@RequiredArgsConstructor
public class CameraController {

    private final MemberService memberService;
    private final FishingShipService fishingShipService;

    @ApiOperation(value = "카메라 사용여부 설정", notes = "카메라 사용여부 설정." +
            "\n cameraId: 설정할 카메라 id" +
            "\n isUse: 사용인경우 true, 미사용인경우 false")
    @PostMapping("/sail/camera/update")
    public Map<String, Object> changeCameraStatus(@RequestHeader(name = "Authorization") String token,
                                                  @RequestParam Long cameraId,
                                                  @RequestParam Boolean isUse) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Map<String, Object> result = new HashMap<>();
        try {
            fishingShipService.changeCameraStatus(cameraId, isUse);
            result.put("result", "success");
            result.put("id", cameraId);
            return result;
        } catch (Exception e) {
            throw new ApiException(ErrorCodes.DB_INSERT_ERROR, "선박 등록에 실패했습니다.");
        }
    }

    @ApiOperation(value = "선박의 카메라 리스트", notes = "선박의 카메라 리스트" +
            "\n 요청값" +
            "\n shipId: 선박 id" +
            "\n {" +
            "\n     shipId: 선박 id" +
            "\n     shipName: 선박명" +
            "\n     count: 카메라 수" +
            "\n     content: 카메라 [ {" +
            "\n         cameraId: 카메라 id" +
            "\n         name: 카메라명" +
            "\n         image: 이미지" +
            "\n         isUse: 현재 사용여부" +
            "\n     }, ... ]" +
            "\n }")
    @GetMapping("/sail/camera")
    public Map<String, Object> getShipCamera(@RequestHeader(name = "Authorization") String token,
                                             @RequestParam Long shipId) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        return fishingShipService.getShipCamera(shipId);
    }

    @ApiOperation(value = "카메라탭 선박 리스트", notes = "카메라탭 선박 리스트" +
            "\n 검색 타입은 '선박명' 한종류입니다." +
            "\n 요청값" +
            "\n keyword: 키워드" +
            "\n hasVideo: 녹화영상 유뮤 (전체: null, 유: true, 무: false)" +
            "\n 응답 값" +
            "\n [{" +
            "\n     shipId: 선박 id" +
            "\n     shipName: 선박명" +
            "\n     profileImage: 프로필사진" +
            "\n     takes: 하트모양 옆의 숫자 (선박 찜 수)" +
            "\n     hasVideo: 녹화영상 유무" +
            "\n }, ... ]" +
            "\n 플레이 수 는 빼주세요")
    @GetMapping("/sail/ships")
    public List<Map<String, Object>> getShipsCameraTab(@RequestHeader(name = "Authorization") String token,
                                                       @RequestParam(defaultValue = "") String keyword,
                                                       @RequestParam(required = false) Boolean hasVideo) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Long memberId = memberService.getMemberSeqBySessionToken(token);
        return fishingShipService.getShipsCameraTab(memberId, keyword, hasVideo);
    }
}
