package com.tobe.fishking.v2.controller.fishking;

import com.tobe.fishking.v2.entity.common.CodeGroup;
import com.tobe.fishking.v2.entity.common.CommonCode;
import com.tobe.fishking.v2.entity.fishing.FishingDiary;
import com.tobe.fishking.v2.exception.EmptyListException;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
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
            "- shipId : Long (값 없을시 null) / 선택 / 글의 대상이 되는 선상id\n" +
            "- content : String / 필수 / 내용 / 5~1000자\n" +
            "- fileList : Long[] / 필수 / Common > /v2/api/filePreUpload를 통해 미리업로드한 파일의 id. / 최소1장부터 20장까지 가능.\n" +
            "- videoId : Long (값 없을시 null) / 선택 / Common > /v2/api/filePreUpload를 통해 미리업로드한 동영상 파일의 id.\n" +
            "- address : String (선박선택시 null) / 선택 / '위치선택'을 통해 선택한 위치의 주소값.\n" +
            "- latitude : Double (선박선택시 null) / 선택 / '위치선택'을 통해 선택한 위치의 위도값\n" +
            "- longitude : Double (선박선택시 null) / 선택 / '위치선택'을 통해 선택한 위치의 경도값\n"+
            "응답 필드 ) 생성된 글의 id \n")
    @PostMapping("/fishingDiary")
    public Long writeFishingDiary(
            @RequestBody @Valid WriteFishingDiaryDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        /*선박 또는 '위치선택' 둘중 하나는 반드시 입력했는지 검증. */
        if(dto.getShipId()==null && dto.getAddress()==null && dto.getLatitude()==null && dto.getLongitude()==null){
            throw new RuntimeException("shipId와 address 중 하나는 반드시 존재해야 합니다.");
        }
        else if(dto.getShipId()!=null && dto.getAddress()!=null){
            throw new RuntimeException("shipId와 address 둘중 하나만 입력하여야 합니다.");
        }

        return fishingDiaryService.writeFishingDiary(dto,token);
    }

    /*글쓰기 수정*/
    @ApiOperation(value = "조항일지, 유저조행기 수정",notes = "" +
            "요청 필드 ) \n" +
            "- fishingDiaryId : Long / 필수 / 수정하려는 글의 id\n" +
            "- title : String / 필수 / 제목 / 5자~30자 사이.\n" +
            "- fishingSpecies : String[] / 필수 / 어종 리스트 / Common > /v2/api/commonCode/{groupId}에서 반환하는 codeGroup값 80을 " +
            "가지는 commonCode 어종리스트의 'code'값. \n" +
            "- fishingDate : LocalDate(yyyy-mm-dd) / 필수 / 낚시 날짜\n" +
            "- tide : String (값 없을시 null) / 선택 / 물때 / Common > /v2/api/value에서 반환하는 'tideTime'의 키값.\n" +
            "- fishingTechnicList : String[] (값 없을시 null) / 선택 / 낚시 기법 / Common > /v2/api/value에서 반환하는 'fishingTechnic'의 키값.\n" +
            "- fishingLureList : String[] (값 없을시 null) / 선택 / 미끼 / Common > /v2/api/commonCode/{groupId}에서 반환하는 codeGroup값 89을 " +
            "가지는 commonCode 미끼목록의 'code'값. \n" +
            "- fishingType : String (값 없을시 null) / 선택 / 선상인지 갯바위인지 / Common > /v2/api/value에서 반환하는 'fishingType'의 키값.\n" +
            "- shipId : Long (값 없을시 null) / 선택 / 글의 대상이 되는 선상id\n" +
            "- content : String / 필수 / 내용 / 5~1000자\n" +
            "- fileList : Long[] / 필수 / Common > /v2/api/filePreUpload를 통해 미리업로드한 파일의 id. / 최소1장부터 20장까지 가능.\n" +
            "- videoId : Long (값 없을시 null) / 선택 / Common > /v2/api/filePreUpload를 통해 미리업로드한 동영상 파일의 id.\n" +
            "- address : String (선박선택시 null) / 선택 / '위치선택'을 통해 선택한 위치의 주소값.\n" +
            "- latitude : Double (선박선택시 null) / 선택 / '위치선택'을 통해 선택한 위치의 위도값\n" +
            "- longitude : Double (선박선택시 null) / 선택 / '위치선택'을 통해 선택한 위치의 경도값\n"+
            "응답 필드 ) 수정 성공시 true. \n")
    @PutMapping("/fishingDiary")
    public Boolean modifyFishingDiary(
            @RequestBody @Valid ModifyFishingDiaryDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        /*선박 또는 '위치선택' 둘중 하나는 반드시 입력했는지 검증. */
        if(dto.getShipId()==null && dto.getAddress()==null && dto.getLatitude()==null && dto.getLongitude()==null){
            throw new RuntimeException("shipId와 address 중 하나는 반드시 존재해야 합니다.");
        }
        else if(dto.getShipId()!=null && dto.getAddress()!=null){
            throw new RuntimeException("shipId와 address 둘중 하나만 입력하여야 합니다.");
        }

        return fishingDiaryService.modifyFishingDiary(dto,token);
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
    ) throws EmptyListException {
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

//        return shipService.searchShipForWriteFishingDiary(keyword/*,sortBy*/,page);
        Page<ShipListForWriteFishingDiary> diaries = shipService.searchShipForWriteFishingDiary(keyword/*,sortBy*/,page);
        if (diaries.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return diaries;
        }
    }

    /*어복스토리 리스트 출력*/
    @ApiOperation(value = "어복스토리 - 조항일지/유저조행기 목록 출력",notes = "" +
            "선택된 지역에 해당하면서 직접입력한 검색어에 해당하는 글들을 검색.\n" +
            "아무 지역 선택하지 않으면, 모든 지역을 선택한것으로 간주.\n" +
            "요청 필드 ) \n" +
            "- category : String / 필수 / 검색하려는 글이 조항일지인지 유저조행기인지. / fishingDiary(조항일지), fishingBlog(유저조행기) 중 택 1.\n" +
            "- district1 : String / 선택(district2List입력시 필수) / 팝업창에서 선택한 지역에 해당하는 common code의 code값들의 배열. 행정구역1단계(code group id 152)에 해당하는 코드값들임.\n" +
            "- district2List : String[] / 선택 / 팝업창에서 선택한 지역에 해당하는 common code의 code값들의 배열. 행정구역2단계(code group id 156)에 해당하는 코드값들임.\n" +
            "   ex) 팝업창에서 '전라남도'선택시, common code에서 '전라남도'의 code값인 '전남'  \n" +
            "- districtSearchKey : String / 선택 / 팝업창에서 '지역명 검색'부분에 직접입력한 값. \n" +
            "- fishSpecies : String[] / 선택 / 팝업창에서 선택한 어종에 해당하는 common code의 code값들의 배열.\n" +
            "- shipId : Long / 선택 / 선박의 id. 특정 선박에 대한 조항일지 또는 유저조행기만 보려할때 입력.\n" +
            "- sort : String / 선택 / 글들의 정렬기준. / 'createdDate', 'likeCount', 'commentCount' 중 하나 입력. \n" +
            "- pageCount : Integer / 선택 / 페이지당 요소 개수. \n"+
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
            "- fishingDiaryType : String / 글이 조항일지인지, 유저조행기인지\n" +
            "- hasLiveCam : Boolean / 실시간캠 여부\n" +
            "- fileList : String[] / 이미지 파일 download url 리스트")
    @GetMapping("/fishingDiary/list/{page}")
    public Page<FishingDiaryDtoForPage> getFishingDiaryList(
            @PathVariable("page") int page,
            @RequestParam("category") String category,
            @RequestParam(value = "district1",required = false) String district1,
            @RequestParam(value = "district2List", required = false) String[] district2List,
            @RequestParam(value = "searchKey", required = false, defaultValue = "") String districtSearchKey,
            @RequestParam(value = "fishSpeciesList", required = false) String[] fishSpecies,
            @RequestParam(value = "shipId",required = false) Long shipId,
            @RequestParam(value = "sort", required = false, defaultValue = "createdDate") String sort,
            @RequestHeader(value = "Authorization", required = false) String token,
            //아래는 관리자 조회시 사용하는 필드들
            @RequestParam(value = "shipName",required = false) String shipName,
            @RequestParam(value = "nickName",required = false) String nickName,
            @RequestParam(value = "title",required = false) String title,
            @RequestParam(value= "content",required = false) String content,
            @RequestParam(value = "createdDateStart",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate createdDateStart,
            @RequestParam(value = "createdDateEnd",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate createdDateEnd,
            @RequestParam(value = "hasShipData",required = false) Boolean hasShipData,
            @RequestParam(value = "pageCount",required = false,defaultValue = "20") Integer pageCount
    ) throws ResourceNotFoundException, EmptyListException {
        if(!(sort.equals("createdDate") || sort.equals("likeCount") || sort.equals("commentCount"))){
            throw new RuntimeException("sort값에는 'createdDate', 'likeCount', 'commentCount' 중 하나만 가능합니다.");
        }
        if(!(category.equals("fishingDiary") || category.equals("fishingBlog"))){
            throw new RuntimeException("category값에는 'fishingDiary', 'fishingBlog' 중 하나만 가능합니다.");
        }

        if(token == null){}
        else if(token.equals("")){token = null;}
//        return fishingDiaryService.getFishingDiaryList(
//                page, category, district1, district2List, districtSearchKey, "address",shipId, fishSpecies, sort, token, false);
        Page<FishingDiaryDtoForPage> diaries = fishingDiaryService.getFishingDiaryList(
                page, category, district1, district2List, districtSearchKey, "address",shipId, fishSpecies,
                shipName, nickName, title, content, createdDateStart, createdDateEnd, hasShipData, pageCount, sort, token, false);
        if (diaries.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return diaries;
        }
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
    ) throws ResourceNotFoundException, EmptyListException {
        if(searchTarget!=null) {
            if (!(searchTarget.equals("content") || searchTarget.equals("title"))) {
                throw new RuntimeException("searchTarget의 값은 'content', 'title'중 하나이어야 합니다.");
            }
        }

//        return fishingDiaryService.getFishingDiaryList(
//                page, "fishingDiary", null, null, districtSearchKey, searchTarget, shipId,
//                null, "createdDate", token, true);
        Page<FishingDiaryDtoForPage> diaries = fishingDiaryService.getFishingDiaryList(
                page, "fishingDiary", null, null, districtSearchKey, searchTarget, shipId,
                null, null, null, null, null, null, null, null, 30, "createdDate", token, true);
        if (diaries.getTotalElements() == 0) {
            throw new EmptyListException("결과리스트가 비어있습니다.");
        } else {
            return diaries;
        }
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
            "- shipAddress : String\n" +
            "- address : String (shipId존재시 null) / '위치선택'을 통해 선택한 위치의 주소값.\n" +
            "- latitude : Double (shipId존재시 null) / '위치선택'을 통해 선택한 위치의 위도값\n" +
            "- longitude : Double (shipId존재시 null) / '위치선택'을 통해 선택한 위치의 경도값\n"+
            "- nickName : String / 작성자명. 유저조행기일경우 회원의 닉네임, 조항일지일 경우 선박명.\n" +
            "- profileImage : String / 프사 url\n" +
            "- isLive : Boolean / 현장실시간 여부\n" +
            "- fishingType : String / 선상,갯바위\n" +
            "- title : String / 제목\n" +
            "- createdDate : LocalDateTime / 작성일\n" +
            "- fishingSpecies : String / 어종 목록\n" +
            "- fishingSpeciesCodeList : ArrayList<String> / 어종 코드 배열\n" +
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
            "- viewCount : Integer / 조회수\n" +
            "- isMine : Boolean / 해당글이 자신의 글인지 여부\n")
    @GetMapping("/fishingDiary/detail")
    public FishingDiaryDetailDto getFishingDiaryDetail(
            @RequestParam("fishingDiaryId") Long fishingDiaryId,
            @RequestHeader(value = "Authorization",required = false) String token
    ) throws ResourceNotFoundException {
        if(token == null){}
        else if(token.equals("")){token = null;}
        return fishingDiaryService.getFishingDiaryDetail(fishingDiaryId,token);
    }

    /*조항일지, 유저조행기 삭제*/
    @ApiOperation(value = "조항일지, 유저조행기 삭제",notes = "" +
            "요청 필드 ) \n" +
            "- fishingDiaryId : 삭제하려는 게시글 id\n" +
            "응답 필드 ) 삭제 성공시 true\n")
    @DeleteMapping("/fishingDiary")
    public Boolean deleteFishingDiary(
            @RequestBody @Valid DeleteFishingDiary dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return fishingDiaryService.deleteFishingDiary(dto,token);
    }
    //숨김처리
    @ApiOperation(value = "글 전체 숨김처리")
    @PutMapping("/fishingDiary/hide/{id}/{active}")
    public Boolean hideFishingDiary(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long fishingDiaryId,
            @PathVariable("active") String active
    ) throws ResourceNotFoundException {
        return fishingDiaryService.hideFishingDiary(fishingDiaryId,active, token);
    }
    //내용 숨김처리
    @ApiOperation(value = "글 내용 숨김처리")
    @PutMapping("/fishingDiary/hideContent/{id}/{hide}")
    public Boolean hideContent(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long fishingDiaryId,
            @PathVariable("hide") String hide
    ) throws ResourceNotFoundException {
        //입력값 검증
        if(!hide.equals("true") && !hide.equals("false")){throw new RuntimeException("hide값으로는 'true'또는 'false'만 가능합니다.");}
        return fishingDiaryService.hideContent(fishingDiaryId, hide, token);
    }

    /*스크랩 추가*/
    @ApiOperation(value = "스크랩 추가",notes = "" +
            "요청필드 ) \n" +
            "- fishingDiaryId : 조항일지,유저조행기의 id\n" +
            "- 헤더에 세션토큰 필요\n" +
            "응답 필드 ) \n" +
            "- 스크랩 성공시 true, 이미 스크랩되어있으면 false. \n")
    @PostMapping("/fishingDiary/scrap")
    public Boolean addScrap(
            @RequestBody AddScrapDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return fishingDiaryService.addScrap(dto,token);
    }
    /*스크랩 삭제*/
    @ApiOperation(value = "스크랩 삭제",notes = "" +
            "요청필드 ) \n" +
            "- fishingDiaryId : 조항일지,유저조행기의 id\n" +
            "- 헤더에 세션토큰 필요\n" +
            "응답 필드 ) \n" +
            "- 스크랩 삭제 성공시 true, 스크랩 되어있지 않았으면 false. \n")
    @DeleteMapping("/fishingDiary/scrap")
    public Boolean deleteScrap(
            @RequestBody DeleteScrapDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return fishingDiaryService.deleteScrap(dto,token);
    }
}
