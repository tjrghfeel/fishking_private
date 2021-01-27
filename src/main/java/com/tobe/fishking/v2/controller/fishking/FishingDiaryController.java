package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.fishing.*;
import com.tobe.fishking.v2.repository.common.CodeGroupRepository;
import com.tobe.fishking.v2.repository.common.CommonCodeRepository;
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
    @Autowired
    CodeGroupRepository codeGroupRepository;
    @Autowired
    CommonCodeRepository commonCodeRepository;

    /*조항일지, 유저조행기 글쓰기*/
    @ApiOperation(value = "조항일지, 유저조행기 글쓰기",notes = "" +
            "요청 필드 ) \n" +
            "- category : String / 필수 / 글의 카테고리 / fishingBlog(유저조행기), fishingDiary(조항일지)\n" +
            "- title : String / 필수 / 제목 / 5자~30자 사이.\n" +
            "- fishingSpecies : String[] / 필수 / 어종 리스트 / Common > /v2/api/commonCode/{groupId}에서 반환하는 codeGroup값 80을 " +
            "가지는 commonCode 어종리스트의 'code'값. \n" +
            "- fishingDate : LocalDate(yyyy-mm-dd) / 필수 / 낚시 날짜\n" +
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

    /*글쓰기 수정*/
    /*@ApiOperation(value = "조항일지, 유저조행기 수정",notes = "" +
            "")
    @PutMapping("/fishingDiary")
    public Boolean modifyFishingDiary(
            @RequestBody @Valid ModifyFishingDiaryDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return fishingDiaryService.modifyFishingDiary(dto,token);
    }*/

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

    /*어복스토리 리스트 출력*/
    @ApiOperation(value = "어복스토리 - 조항일지/유저조행기 목록 출력",notes = "" +
            "선택된 지역에 해당하면서 직접입력한 검색어에 해당하는 글들을 검색.\n" +
            "아무 지역 선택하지 않으면, 모든 지역을 선택한것으로 간주.\n" +
            "요청 필드 ) \n" +
            "- category : String / 필수 / 검색하려는 글이 조항일지인지 유저조행기인지. / fishingDiary(조항일지), fishingBlog(유저조행기) 중 택 1.\n" +
            "- districtList : String[] / 선택 / 팝업창에서 선택한 지역에 해당하는 common code의 code값들의 배열. \n" +
            "   ex) 팝업창에서 '전라남도'선택시, common code에서 '전라남도'의 code값인 '전남'  \n" +
            "- districtSearchKey : String / 선택 / 팝업창에서 '지역명 검색'부분에 직접입력한 값. \n" +
            "- fishSpecies : String[] / 선택 / 팝업창에서 선택한 어종에 해당하는 common code의 code값들의 배열.\n" +
            "- sort : String / 선택 / 글들의 정렬기준. / 'createdDate', 'likeCount', 'commentCount' 중 하나 입력. "+
            "- header에 세션토큰 필요.\n" +
            "응답 필드 )\n" +
            "- id : Long / 게시글의 id \n" +
            "- profileImage : String / 작성자 프로필 사진 download url \n" +
            "- address : String / 게시글의 대상 선상의 주소 \n" +
            "- shipId : Long / 게시글 대상 상품의 선상id\n" +
            "       ㄴ ship : 선상\n" +
            "       ㄴ sealocks : 갯바위\n " +
            "- memberId : Long / 작성자 id \n" +
            "- nickName : String / 작성자 닉네임\n" +
            "- fishingType : String / 선상인지 갯바위인지 \n" +
            "- isLikeTo : Boolean / 게시글에 대한 좋아요 여부\n" +
            "- isScraped : Boolean / 게시글에 대한 스크랩 여부\n" +
            "- createdDate : LocalDateTime / 작성일자 \n" +
            "- likeCount : Integer / 좋아요 수 \n" +
            "- commentCount : Integer / 댓글 수 \n" +
            "- scrapCount : Integer / 스크랩 수 \n" +
            "- title : String / 글 제목\n" +
            "- contents : String / 게시글 내용(일부만 출력)\n" +
            "- fileList : String[] / 이미지 파일 download url 리스트")
    @GetMapping("/fishingDiary/list/{page}")
    public Page<FishingDiaryDtoForPage> getFishingDiaryList(
            @PathVariable("page") int page,
            @RequestParam("category") String category,
            @RequestParam(value = "districtList", required = false) String[] districtList,
            @RequestParam(value = "searchKey", required = false, defaultValue = "") String districtSearchKey,
            @RequestParam(value = "fishSpeciesList", required = false) String[] fishSpecies,
            @RequestParam(value = "sort", required = false, defaultValue = "createdDate") String sort,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        if(!(sort.equals("createdDate") || sort.equals("likeCount") || sort.equals("commentCount"))){
            throw new RuntimeException("sort값에는 'createdDate', 'likeCount', 'commentCount' 중 하나만 가능합니다.");
        }
        if(!(category.equals("fishingDiary") || category.equals("fishingBlog"))){
            throw new RuntimeException("category값에는 'fishingDiary', 'fishingBlog' 중 하나만 가능합니다.");
        }

        return fishingDiaryService.getFishingDiaryList(
                page, category, districtList, districtSearchKey, "address",null, fishSpecies, sort, token, false);
    }


    /*스마트 출조 - 조황관리 - 조황일지 목록 검색*/
    @ApiOperation(value = "스마트 출조 - 조황관리 - 조황일지 목록 검색",notes = "" +
            "요청 필드 ) \n" +
            "- page : 페이지\n" +
            "- searchKey : String / 선택 / 검색 키워드\n" +
            "- searchTarget : String / 선택 / 어떤 항목에 대해 검색할건지. / content(내용), title(제목) 중 선택.\n" +
            "- shipId : Long / 선택 / 선박id. 선박에 대한 글만 검색하려할때 사용.\n" +
            "- 헤더에 세션토큰 필요. \n" +
            "응답 필드 )\n" +
            "- id : Long / 게시글의 id \n" +
            "- profileImage : String / 작성자 프로필 사진 download url \n" +
            "- address : String / 게시글의 대상 선상의 주소 \n" +
            "- shipId : Long / 게시글 대상 상품의 선상id\n" +
            "       ㄴ ship : 선상\n" +
            "       ㄴ sealocks : 갯바위\n " +
            "- memberId : Long / 작성자 id \n" +
            "- nickName : String / 작성자 닉네임\n" +
            "- fishingType : String / 선상인지 갯바위인지 \n" +
            "- isLikeTo : Boolean / 게시글에 대한 좋아요 여부\n" +
            "- isScraped : Boolean / 게시글에 대한 스크랩 여부\n" +
            "- createdDate : LocalDateTime / 작성일자 \n" +
            "- likeCount : Integer / 좋아요 수 \n" +
            "- commentCount : Integer / 댓글 수 \n" +
            "- scrapCount : Integer / 스크랩 수 \n" +
            "- title : String / 글 제목\n" +
            "- contents : String / 게시글 내용(일부만 출력)\n" +
            "- fileList : String[] / 이미지 파일 download url 리스트")
    @GetMapping("/smartFishing/fishingDiary/list/{page}")
    public Page<FishingDiaryDtoForPage> getFishingDiaryListForSmartFishing(
            @PathVariable("page") int page,
//            @RequestParam(value = "districtList", required = false) String[] districtList,
            @RequestParam(value = "searchKey", required = false, defaultValue = "") String districtSearchKey,
            @RequestParam(value = "searchTarget", required = false) String searchTarget,
            @RequestParam(value = "shipId",required = false) Long shipId,
//            @RequestParam(value = "fishSpeciesList", required = false) String[] fishSpecies,
//            @RequestParam(value = "sort", required = false, defaultValue = "createdDate") String sort,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        if(searchTarget!=null) {
            if (!(searchTarget.equals("content") || searchTarget.equals("title"))) {
                throw new RuntimeException("searchTarget의 값은 'content', 'title'중 하나이어야 합니다.");
            }
        }

        return fishingDiaryService.getFishingDiaryList(
                page, "fishingDiary", null, districtSearchKey, searchTarget, shipId,
                null, "createdDate", token, true);
    }

    /*조항일지, 유저조행기 상세보기*/
    @ApiOperation(value = "조항일지, 유저조행기 상세보기",notes = "" +
            "요청 필드 ) \n" +
            "- fishingDiaryId : 상세보기하려는 글의 id\n" +
            "- 헤더에 세션토큰 필요\n" +
            "응답 필드 ) \n" +
            "- authorId : Long / 작성자 id\n" +
            "- fishingDiaryId : Long / 글 id\n" +
            "- shipId : Long / 글에 해당하는 선박 id\n" +
            "- nickName : String / 작성자명. 유저조행기일경우 회원의 닉네임, 조항일지일 경우 선박명.\n" +
            "- profileImage : String / 프사 url\n" +
            "- isLive : Boolean / 현장실시간 여부\n" +
            "- fishingType : String / 선상,갯바위\n" +
            "- title : String / 제목\n" +
            "- createdDate : LocalDateTime / 작성일\n" +
            "- fishingSpecies : String / 어종 목록\n" +
            "- fishingDate : String / 낚시일\n" +
            "- tide : String / 물때\n" +
            "- fishingLure : String / 미끼 목록 \n" +
            "- fishingTechnic : String / 낚시 기법\n" +
            "- content : String / 내용 \n" +
            "- imageUrlList : ArrayList<String> / 이미지 url 리스트\n" +
            "- videoUrl : String / 비디오 url\n" +
            "- isLikeTo : Boolean / 현재 글에 대한 좋아요 여부\n" +
            "- isScraped : Boolean / 현재 글에 대한 스크랩 여부\n" +
            "- likeCount : Integer / 좋아요수 \n" +
            "- commentCount : Integer / 댓글수 \n" +
            "- scrapCount : Integer / 스크랩 수\n" +
            "- viewCount : Integer / 조회수\n")
    @GetMapping("/fishingDiary/detail")
    public FishingDiaryDetailDto getFishingDiaryDetail(
            @RequestParam("fishingDiaryId") Long fishingDiaryId,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return fishingDiaryService.getFishingDiaryDetail(fishingDiaryId,token);
    }

}
