package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.enums.IEnumModel;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.*;
import com.tobe.fishking.v2.enums.common.*;
import com.tobe.fishking.v2.enums.fishing.*;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.*;
import com.tobe.fishking.v2.model.common.DeleteFileDto;
import com.tobe.fishking.v2.model.common.FilePreUploadResponseDto;
import com.tobe.fishking.v2.model.response.ListResult;
import com.tobe.fishking.v2.service.HttpRequestService;
import com.tobe.fishking.v2.service.ResponseService;
import com.tobe.fishking.v2.service.common.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jcodec.api.JCodecException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Api(tags = "Common")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/v2/api")
public class CommonController {

    private final ResponseService responseService;
    private final CommonService commonService;

    private final HttpRequestService httpRequestService;

    /*CodeGroup 추가 api*/
    @ApiOperation(value = "CodeGroup 추가", notes = "" +
            "- 필드 )\n" +
            "   id : 생략\n" +
            "   code : CodeGroup의 코드\n" +
            "   name : CodeGroup의 코드명\n" +
            "   description : CodeGroup에 대한 설명\n" +
            "   remark : 비고")
    @PostMapping("/codeGroup")
    public Long makeCodeGroup(@RequestBody CodeGroupWriteDTO codeGroupWriteDTO, HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return commonService.writeCodeGroup(codeGroupWriteDTO, sessionToken);
    }
    /*CodeGroup 수정 api*/
    @ApiOperation(value = "CodeGroup 수정", notes = "" +
            "- commond CodeGroup 하나를 수정 \n" +
            "- 필드 )\n" +
            "   id : 수정하려는 common CodeGroup id\n" +
            "   code : CodeGroup의 코드\n" +
            "   name : CodeGroup의 코드명\n" +
            "   description : CodeGroup에 대한 설명\n" +
            "   remark : 비고")
    @PutMapping("/codeGroup")
    public Long updateCodeGroup(@RequestBody CodeGroupWriteDTO codeGroupWriteDTO,HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return commonService.updateCodeGroup(codeGroupWriteDTO,sessionToken);
    }

    /*CommonCode 추가해주는 api.*/
    @ApiOperation(value = "Common code 추가", notes = "retValue1, level, active, orderBy 필드 필수. \n" +
            "- common code 추가 api \n" +
            "- 필드 ) \n" +
            "   id : 생략 \n" +
            "   retValue1 : common code의 대체값(double형) \n" +
            "   active : 사용여부 \n" +
            "   level : common code 레벨(common code가 계층형구조를 가질때 쓰임)\n" +
            "   orderBy : 순서 \n" +
            "   code : 코드 \n" +
            "   codeName : 코드명 \n" +
            "   aliasName : 별칭 \n" +
            "   extraValue1 : common code의 대체값(string형)\n" +
            "   remark : 주석 \n" +
            "   codeGroup : code group의 id ")
    @PostMapping("/commonCode")
    public String makeCommonCode(@RequestBody CommonCodeWriteDTO commonCodeWriteDTO,HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return commonService.writeCommonCode(commonCodeWriteDTO,sessionToken);
    }
    /*CommonCode 수정 api*/
    @ApiOperation(value = "CommonCode 수정", notes = "id, retValue1, level, active, orderBy 필드 필수." +
            "- common code 수정 api \n" +
            "- 필드 ) \n" +
            "   id : 수정할 common code의 id \n" +
            "   retValue1 : common code의 대체값(double형) \n" +
            "   active : 사용여부 \n" +
            "   level : common code 레벨(common code가 계층형구조를 가질때 쓰임)\n" +
            "   orderBy : 순서 \n" +
            "   code : 코드 \n" +
            "   codeName : 코드명 \n" +
            "   aliasName : 별칭 \n" +
            "   extraValue1 : common code의 대체값(string형)\n" +
            "   remark : 주석 \n" +
            "   codeGroup : code group의 id ")
    @PutMapping("/commonCode")
    public String updateCommonCode(@RequestBody CommonCodeWriteDTO commonCodeWriteDTO,HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return commonService.updateCommonCode(commonCodeWriteDTO,sessionToken);
    }

    /*Code Group에 맞는 CommonCode목록을 반환해주는 api. */
    /*!!!!!common code들에 대한 설명 써두기, api검색을 db의 id가 아닌 스트링으로 하도록 변경 고려. */
    @ApiOperation(value = "Common Code 목록 출력", notes = "Common Group에 해당하는 CommonCode목록 출력. 다음은 code group id 목록이다. \n" +
            "80     : 대표어종 \n" +
            "161    : 두족류 \n" +
            "162    : 기타어종 \n" +
            "83     : 지역 \n" +
            "84     : 장르 \n" +
            "85     : 서비스 \n" +
            "86     : 가격 \n" +
            "87     : 편의시설 \n" +
            "89     : 미끼 종류 \n" +
            "153    : 보유장비 \n" +
            "- 지역 codegroup안에 지역들은 level 1, 2로 분류되며, level1 지역은 level2지역을 포함한다. level1은 '수도권','충청남도'와 같은" +
            "   넓은 범위의 지역, level2는 그 안의 '시/군/구'단위의 지역을 말한다. \n" +
            "- level2 지역들은 extraValue1필드 안에 상위 level1 지역의 code를 가지고 있다. " +
            "   ex) level1 지역 '수도권'의 code가 'capital'이라 하면, 수도권 안에 속하는 level2지역 '인천 중구'는 extraValue1안에 'capital'을 가지고있다. ")
    @GetMapping("/commonCode/{groupId}")
    public ListResult<CommonCodeDTO> getCommonCodeList(@PathVariable("groupId") Long groudId) throws ResourceNotFoundException {

        //서비스로부터 groupId에 해당하는 commonCodeDTO 리스트 받아오기.
        List<CommonCodeDTO> commonCodeDTOList = commonService.getCommonCodeDTOList(groudId);

        //ListResult로 만들어주기.
        return responseService.getListResult(commonCodeDTOList);
    }


    @ApiOperation(value = "enum타입 목록",notes = "common code와 유사하게 정해진 값 저장에 사용되는 enum 리스트를 모두 반환. \n" +
            "성별(gender), 회원 (role), 게시판 유형(boardType), 파일유형(fileType),문의 유형(questionType), 답변 방법(returnType)," +
            " 광고 유형(adType), 알람유형(alertType), 배너유형(bannerType), 지역분류(byRegion),공지사항유형(channelType)," +
            "쿠폰유형(couponType), operatorType, 게시글유형(postTitle), 검색항목(searchPublish), 정렬기준(sortType), " +
            "찜(좋아요) 유형(takeType), useType, accumuateType, 승인상태(approvalStatus), dependentType, 낚시기법(fishingTechnic), " +
            "낚시 유형(fishingType), 오전/오후(meridiem), 예약상태(orderStatus), 결재방법(paymentGroup), 결재옵션(paymentOption), " +
            "payMethod, salesAmountType, 해면 분류(seaDirection), sns유형(sNSType), 물때(tideTime) ")
    @GetMapping("/value")
    public Map<String, List<EnumValueDTO>> getEnumValue() {
        Map<String, List<EnumValueDTO>> enumValues = new LinkedHashMap<>();

        enumValues.put("gender", toEnumValues(Gender.class));
        enumValues.put("role", toEnumValues(Role.class));
        enumValues.put("boardType", toEnumValues(BoardType.class));
        enumValues.put("filePublish", toEnumValues(FilePublish.class));
        enumValues.put("fileType", toEnumValues(FileType.class));
        enumValues.put("questionType", toEnumValues(QuestionType.class));
        enumValues.put("returnType", toEnumValues(ReturnType.class));
        enumValues.put("adType", toEnumValues(AlertType.class));
        enumValues.put("alertType", toEnumValues(AlertType.class));
        enumValues.put("bannerType", toEnumValues(BannerType.class));
        enumValues.put("byRegion", toEnumValues(ByRegion.class));
        enumValues.put("channelType", toEnumValues(ChannelType.class));
        enumValues.put("couponType", toEnumValues(CouponType.class));
        enumValues.put("operatorType", toEnumValues(OperatorType.class));
        enumValues.put("postTitle", toEnumValues(PostTitle.class));
        enumValues.put("searchPublish", toEnumValues(SearchPublish.class));
        enumValues.put("sortType", toEnumValues(SortType.class));
        enumValues.put("takeType", toEnumValues(TakeType.class));
        enumValues.put("UseType", toEnumValues(UseType.class));
        enumValues.put("accumuateType", toEnumValues(AccumuateType.class));
        enumValues.put("approvalStatus", toEnumValues(ApprovalStatus.class));
        enumValues.put("dependentType", toEnumValues(DependentType.class));
        enumValues.put("fishingTechnic", toEnumValues(FishingTechnic.class));
        enumValues.put("fishingType", toEnumValues(FishingType.class));
        enumValues.put("meridiem", toEnumValues(Meridiem.class));

        enumValues.put("orderStatus", toEnumValues(OrderStatus.class));
        enumValues.put("paymentGroup", toEnumValues(PaymentGroup.class));
        enumValues.put("paymentOption", toEnumValues(PaymentOption.class));
        enumValues.put("payMethod", toEnumValues(PayMethod.class));
        enumValues.put("salesAmountType", toEnumValues(SalesAmountType.class));
        enumValues.put("seaDirection", toEnumValues(SeaDirection.class));
        enumValues.put("sNSType", toEnumValues(SNSType.class));
        enumValues.put("tideTime", toEnumValues(TideTime.class));


        return enumValues;
    }

    private List<EnumValueDTO> toEnumValues(Class<? extends IEnumModel> e){

/*
        // Java8이 아닐경우
        List<EnumValue> enumValues = new ArrayList<>();
        for (IEnumModel enumType : e.getEnumConstants()) {
            enumValues.add(new EnumValue(enumType));
        }
        return enumValues;
*/

        return Arrays
                .stream(e.getEnumConstants())
                .map(EnumValueDTO::new)
                .collect(Collectors.toList());
    }

    /*파일 업로드 미리보기용 컨트롤러메소드
    * - 파일업로드시 미리보기 지원 및 용량 감소 효과를 위해 업로드한 파일을 미리 저장하기위해 사용되는 컨트롤러메소드.
    * - 넘어온 파일을 저장해주고 FileEntity생성하고 이 id를반환. 이때, FileEntity의 isDelete필드가 true상태로 생성시켜주어 미리업드로용으로 저장한
    *   FileEntity임을 구분해준다. */
    @ApiOperation(value = "파일 업로드용 api", notes = "파일업로드시 파일을 미리 업로드해주는 api. \n" +
            "- 파일을 서버에 저장하고 download url과 저장된파일의 id를 반환. \n" +
            "- 실제 파일을 올릴때 파일이 아닌 이때 반환받은 id를 함께 반환해주면 된다. \n" +
            "- 'file'이란 이름으로 파일을 넘기고 \n " +
            "- 파일과 함께 'filePublish'라는 이름의 파라미터로 다음의 값중 하나를 넘겨주어야 한다 \n" +
            "   ship : 선상 사진 업로드의 경우\n" +
            "   post : 게시판에 파일 업로드하는 경우\n" +
            "   one2one : 일대일문의 게시판에 파일 업로드\n" +
            "   faq : FAQ에 파일업로드\n" +
            "   notice : 공지사항에 파일업로드\n" +
            "   fishingBlog : 조행기에 파일업로드\n" +
            "   fishingDiary : 조행일지에 파일업로드\n" +
            "   comment : 조항일지, 유저조행기 댓글에 파일업로드\n" +
            "   commonComment : 공통댓글(이벤트 등에 대한 댓글)에 파일업로드\n" +
            "   fishkingTv : 어복TV에 파일업로드\n" +
            "   companyRequest : 업체요청에 파일업로드\n" +
            "   profile : 프로필에 파일업로드\n" +
            "   review : 리뷰에 파일업로드 \n")
    @PostMapping("/filePreUpload")
    public FilePreUploadResponseDto preUploadFile(MultipartHttpServletRequest request) throws IOException, ResourceNotFoundException, JCodecException {
        return commonService.preUploadFile(
                request.getFile("file"), request.getParameter("filePublish"), request.getHeader("Authorization"));
    }

    /*pre업로드한 파일 삭제*/
    @ApiOperation(value="파일 삭제",notes = "" +
            "요청 필드 ) \n" +
            "- fileId : /v2/api/filePreUpload에서 반환받은 파일의 id.\n" +
            "응답 필드 ) 삭제 성공시 true. \n")
    @DeleteMapping("/fileDelete")
    public Boolean deleteFile(
            @RequestBody DeleteFileDto dto,
            @RequestHeader("Authorization") String token
    ) throws ResourceNotFoundException {
        return commonService.deleteFile(dto,token);
    }

    @GetMapping("/toast")
    public String toastTest() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnsupportedEncodingException {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4MTYiLCJleHAiOjE2MTQzMTk1NjgsImF1ZCI6ImJhODQ5Y2IwLTY5Y2MtMTFlYi04NDFhLTAwNTA1NmFjN2YwMiIsImlzcyI6ImRldkB0by1iZS5rciJ9.dN2G1QMyaPiEbUpVvmlA1gboHq5tPSTemUfiX8J8kU0";
        String result = "";
        List<Map<String, Object>> cameras = httpRequestService.getCameraList(token);
        for (Map<String, Object> camera : cameras) {
            String serial = camera.get("serialNo").toString();
            if (serial.equals("0023637698D5")) {
                String type = camera.get("recordType").toString();
                String streamStatus = camera.get("streamStatus").toString();
                String controlStatus = camera.get("controlStatus").toString();
                if (type.equals("24h")) {
                    if (streamStatus.equals("on")) {
                        result = httpRequestService.getPlayUrl(token, serial);
                    }
                } else {
                    if (controlStatus.equals("on")) {
                        result = httpRequestService.getPlayUrl(token, serial);
                    }
                }
            }
        }
        return result;
    }

    @ApiOperation(value = "행정구역 코드리스트", notes = "행정구역 코드리스트. \n" +
            "152    : 시/도 \n" +
            "156    : 시/군/구 \n" +
            "시/군/구 의 경우 parCode에 검색할 시/도의 코드값을 넣어주세요 \n" +
            "{id: 코드 id, code: 코드, code_name: 코드명, ... } \n" +
            "위의 세 데이터 외에는 크게 필요 없을것으로 보입니다. ")
    @GetMapping("/commonCode/area")
    public List<CommonCodeDTO> getAddrCodes(@RequestParam Long groupId, @RequestParam(required = false) String parCode) {
        return commonService.getAddrCodes(groupId, parCode);
    }

}
