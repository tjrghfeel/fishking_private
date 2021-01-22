package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.ShipListForWriteFishingDiary;
import com.tobe.fishking.v2.model.fishing.WriteFishingDiaryDto;
import com.tobe.fishking.v2.service.fishking.FishingDiaryService;
import com.tobe.fishking.v2.service.fishking.ShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.regex.Pattern;

@Api(tags = {"조항일지, 유저조행기"})
@RequiredArgsConstructor
@RequestMapping(value = "/v2/api")
@RestController
public class FishingDiaryController {
    @Autowired FishingDiaryService fishingDiaryService;
    @Autowired ShipService shipService;

    /*조항일지, 유저조행기 글쓰기*/
    @ApiOperation(value = "조항일지, 유저조행기 글쓰기",notes = "" +
            "요청 필드 ) \n" +
            "- category : String / 필수 / 글의 카테고리 / fishingBlog(유저조행기), fishingDiary(조항일지)\n" +
            "- title : String / 필수 / 제목 / 5자~30자 사이.\n" +
            "- fishingSpecies : String[] / 필수 / 어종 리스트 / Common > /v2/api/commonCode/{groupId}에서 반환하는 codeGroup값 80을 " +
            "가지는 commonCode 어종리스트의 'code'값. \n" +
            "- writeDate : LocalDate(yyyy-mm-dd) / 필수 / 낚시 날짜\n" +
            "- tide : String (값 없을시 null) / 선택 / 물때 / Common > /v2/api/value에서 반환하는 'tideTime'의 키값.\n" +
            "- fishingTechnicList : String[] (값 없을시 null) / 선택 / 낚시 기법 / Common > /v2/api/value에서 반환하는 'fishingTechnic'의 키값.\n" +
            "- fishingLureList : String[] (값 없을시 null) / 선택 / 미끼 / Common > /v2/api/commonCode/{groupId}에서 반환하는 codeGroup값 89을 " +
            "가지는 commonCode 미끼목록의 'code'값. \n" +
            "- fishingType : String (값 없을시 null) / 선택 / 선상인지 갯바위인지 / Common > /v2/api/value에서 반환하는 'fishingType'의 키값.\n" +
            "- shipId : Long / 필수 / 글의 대상이 되는 선상id\n" +
            "- content : String / 필수 / 내용 / 5~1000자\n" +
            "- fileList : Long[] / 필수 / Common > /v2/api/filePreUpload를 통해 미리업로드한 파일의 id. / 최소1장부터 20장까지 가능.\n" +
            "- videoId : Long (값 없을시 null) / 선택 / Common > /v2/api/filePreUpload를 통해 미리업로드한 동영상 파일의 id.\n"+
            "응답 필드 ) 생성된 글의 id \n")
    @PostMapping("/fishingDiary")
    public Long writeFishingDiary(
            @RequestBody @Valid WriteFishingDiaryDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return fishingDiaryService.writeFishingDiary(dto,token);
    }

    /*글쓰기 - ship검색*/
    @ApiOperation(value = "글쓰기 - ship검색",notes = "" +
            "요청 필드 )\n" +
            "- keyword : String / 검색 키워드(선상명, 선상주소, 선상 전화번호로 검색가능) / 한글,영어,숫자,공백 포함 2자 이상. 키워드 검색 안할경우 null. \n" +
//            "- sortBy : String / 정렬 기준 / distance(거리순), name(명칭순) 입력. / name이 디폴트값. \n" +
            "응답 필드 )\n" +
            "- shipId : 배 id\n" +
            "- name : 선상명\n" +
            "- fishingType : 선상/갯바위\n" +
            "- address : 주소\n" +
            "- distance : 거리\n" +
            "- thumbnailUrl : 선상 대표 섬네일 url\n" +
            "- isVideo : 선상 대표 섬네일이 동영상 섬네일인지 여부\n")
    @GetMapping("/fishingDiary/searchShip/{page}")
    public Page<ShipListForWriteFishingDiary> searchShipForWriteFishingDiary(
            @RequestParam(value = "keyword", required = false) String keyword,
//            @RequestParam(value = "sortBy", required = false, defaultValue = "name") String sortBy,
            @PathVariable("page") int page
    ){
        /*검색 키워드 검증*/
        if(keyword!=null) {
            if (!(Pattern.matches("^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣\\s]{2,}$", keyword))) {
                throw new RuntimeException("검색어는 한글, 영어, 숫자만 2자 이상이어야합니다.");
            }
        }
        else{keyword="";}
        /*정렬 기준값 검증.*//*
        if(!(sortBy.equals("distance") || sortBy.equals("name"))){
            throw new RuntimeException("정렬조건은 'distance'또는 'name'만 가능합니다.");
        }*/

        return shipService.searchShipForWriteFishingDiary(keyword/*,sortBy*/,page);
    }

}
