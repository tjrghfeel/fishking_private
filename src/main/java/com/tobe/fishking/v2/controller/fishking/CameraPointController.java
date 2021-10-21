package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.exception.ServiceLogicException;
import com.tobe.fishking.v2.model.fishing.AddCameraPointDto;
import com.tobe.fishking.v2.model.fishing.CameraPointDetailDto;
import com.tobe.fishking.v2.model.fishing.CameraPointDetailDtoForPage;
import com.tobe.fishking.v2.service.fishking.CameraPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags={"카메라 포인트"})
@RequestMapping("/v2/api")
public class CameraPointController {
    @Autowired
    CameraPointService cameraPointService;

    //항구 생성
    @ApiOperation(value = "카메라 포인트 생성",
            notes = "request ) \n" +
                    "- name : String / 1~30자 / 필수 / 카메라 포인트명\n" +
                    "- sido : String / 필수 / 주소(시,도)\n" +
                    "- gungu : String / 필수 / 주소(군,구)\n" +
                    "- address : String / 필수 / 주소(도로명)\n" +
                    "- lat : Double / 필수 / 위도\n" +
                    "- lon : Double / 필수 / 경도\n" +
                    "- adtId : String / 선택 / 카메라 id\n" +
                    "- adtPw : String / 선택 / 카메라 pw\n" +
                    "- imgFileId : Long / 필수 / 등록한 대표 이미지 file id\n"
    )
    @PostMapping("/cameraPoint")
    public Boolean createHarbor(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid AddCameraPointDto dto
    ) throws ServiceLogicException, ResourceNotFoundException {
        cameraPointService.addCameraPoint(dto, token);

        return true;
    }
    //카메라 포인트 수정
    @ApiOperation(value = "카메라 포인트 수정",
            notes = "request ) \n" +
                    "- name : String / 1~30자 / 필수 / 카메라 포인트명\n" +
                    "- sido : String / 필수 / 주소(시,도)\n" +
                    "- gungu : String / 필수 / 주소(군,구)\n" +
                    "- address : String / 필수 / 주소(도로명)\n" +
                    "- lat : Double / 필수 / 위도\n" +
                    "- lon : Double / 필수 / 경도\n" +
                    "- adtId : String / 선택 / 카메라 id\n" +
                    "- adtPw : String / 선택 / 카메라 pw\n" +
                    "- imgFileId : Long / 선택 / 수정한 대표 이미지 file id\n"
    )
    @PutMapping("/cameraPoint/info/{cameraPointId}")
    public Boolean modifyCameraPoint(
            @PathVariable("cameraPointId") Long cameraPointId,
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid AddCameraPointDto dto
    ) throws ServiceLogicException, ResourceNotFoundException {
        cameraPointService.modifyCameraPoint(cameraPointId, dto, token);

        return true;
    }

    //카메라 포인트 정보 조회
    @ApiOperation(value = "카메라 포인트 조회",
            notes = "response )\n" +
                    "- id : Long / 카메라 포인트 id\n" +
                    "- name : String / 카메라 포인트명\n" +
                    "- sido : String / 주소(시,도)\n" +
                    "- gungu : String / 주소(군,구)\n" +
                    "- address : String / 주소(도로명)\n" +
                    "- lat : Double / 위도\n" +
                    "- lon : Double / 경도\n" +
                    "- adtId : String / 카메라 id\n" +
                    "- adtPw : String / 카메라 pw\n" +
                    "- imgUrl : String / 대표 이미지 file url\n"
    )
    @GetMapping("/cameraPoint/{cameraPointId}")
    public CameraPointDetailDto getCameraPointDetail(
            @PathVariable("cameraPointId") Long cameraPointId,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return cameraPointService.getCameraPointDetail(cameraPointId, token);
    }

    //카메라 포인트 리스트(관리자용)
    @ApiOperation(value="카메라 포인트 리스트 조회",
            notes = ""
    )
    @GetMapping("/cameraPoint/list/{page}")
    public Page<CameraPointDetailDtoForPage> getCameraPointList(
            @RequestHeader("Authorization") String token,
            @PathVariable("page") int page,
            @RequestParam(name = "pageCount", required = false, defaultValue = "100") int pageCount
    ) throws ServiceLogicException {
        return cameraPointService.getCameraPointList(token, page, pageCount);
    }
    //카메라 포인트 리스트(고객앱 메인홈용)
    @ApiOperation(value = "카메라 포인트 리스트 조회(고객앱 메인홈)",
            notes = ""
    )
    @GetMapping("/cameraPoint/list")
    public List<CameraPointDetailDtoForPage> getCameraPointList(){
        return cameraPointService.getCameraPointList();
    }

    //카메라 포인트 삭제
    @ApiOperation(value="카메라 포인트 삭제")
    @DeleteMapping("/cameraPoint/{cameraPointId}")
    public Boolean deleteCameraPoint(
            @RequestHeader("Authorization") String token,
            @PathVariable("cameraPointId") Long cameraPointId
//            @PathVariable("isDeleted") Boolean isDeleted
    ) throws ServiceLogicException {
        return cameraPointService.deleteCameraPoint(cameraPointId, token);
    }

    //카메라 포인트 활성화, 비활성화.
    @ApiOperation(value = "카메라 포인트 활성화/비활성화")
    @PutMapping("/cameraPoint/isActive/{cameraPointId}/{isActive}")
    public Boolean setIsActive(
            @RequestHeader("Authorization") String token,
            @PathVariable("cameraPointId") Long cameraPointId,
            @PathVariable("isActive") Boolean isActive
    ) throws ServiceLogicException {
        return cameraPointService.setIsActive(cameraPointId, isActive, token);
    }


}
