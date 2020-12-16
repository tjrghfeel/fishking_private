package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.enums.IEnumModel;
import com.tobe.fishking.v2.enums.auth.Gender;
import com.tobe.fishking.v2.enums.auth.Role;
import com.tobe.fishking.v2.enums.board.BoardType;
import com.tobe.fishking.v2.enums.board.FileType;
import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.enums.board.ReturnType;
import com.tobe.fishking.v2.enums.common.*;
import com.tobe.fishking.v2.enums.fishing.*;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.*;
import com.tobe.fishking.v2.model.response.ListResult;
import com.tobe.fishking.v2.service.ResponseService;
import com.tobe.fishking.v2.service.common.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Api(tags = "Common")
@RestController
@RequestMapping(value = "/v2/api")
public class CommonController {
    @Autowired
    ResponseService responseService;
    @Autowired
    CommonService commonService;

    /*CodeGroup 추가 api*/
    @ApiOperation(value = "CodeGroup 추가", notes = "id 필드 생략. ")
    @PostMapping("/codeGroup")
    public Long makeCodeGroup(@RequestBody CodeGroupWriteDTO codeGroupWriteDTO, HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return commonService.writeCodeGroup(codeGroupWriteDTO, sessionToken);
    }
    /*CodeGroup 수정 api*/
    @ApiOperation(value = "CodeGroup 수정", notes = "")
    @PutMapping("/codeGroup")
    public Long updateCodeGroup(@RequestBody CodeGroupWriteDTO codeGroupWriteDTO,HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return commonService.updateCodeGroup(codeGroupWriteDTO,sessionToken);
    }

    /*CommonCode 추가해주는 api.*/
    @ApiOperation(value = "Common code 추가", notes = "retValue1, level, active, orderBy 필드 필수. ")
    @PostMapping("/commonCode")
    public String makeCommonCode(@RequestBody CommonCodeWriteDTO commonCodeWriteDTO,HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return commonService.writeCommonCode(commonCodeWriteDTO,sessionToken);
    }
    /*CommonCode 수정 api*/
    @ApiOperation(value = "CommonCode 수정", notes = "id, retValue1, level, active, orderBy 필드 필수. ")
    @PutMapping("/commonCode")
    public String updateCommonCode(@RequestBody CommonCodeWriteDTO commonCodeWriteDTO,HttpServletRequest request) throws ResourceNotFoundException {
        String sessionToken = request.getHeader("Authorization");
        return commonService.updateCommonCode(commonCodeWriteDTO,sessionToken);
    }

    /*Code Group에 맞는 CommonCode목록을 반환해주는 api. */
    /*!!!!!common code들에 대한 설명 써두기, api검색을 db의 id가 아닌 스트링으로 하도록 변경 고려. */
    @ApiOperation(value = "Common Code 목록 출력", notes = "Common Group에 해당하는 CommonCode목록 출력. 다음은 code group id 목록이다. \n" +
            "80 : 어종 \n" +
            "83 : 지역 \n" +
//            "84 : 낚시기법 \n" +
            "85 : 서비스 \n" +
            "86 : 가격 \n" +
            "87 : 편의시설 \n" +
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

    @ApiOperation(value = "enum타입 목록"/*,notes = "성별, 회원 role, 게시판 유형, 파일유형,문의 유형, 답변 방법, 광고 유형, 알람유형, " +
            "배너 유형, 해면 종류,공지사항유형,쿠폰유형,operatorType,게시글유형, "*/)
    @GetMapping("/value")
    public Map<String, List<EnumValueDTO>> getEnumValue() {
        Map<String, List<EnumValueDTO>> enumValues = new LinkedHashMap<>();

        enumValues.put("gender", toEnumValues(Gender.class));
        enumValues.put("role", toEnumValues(Role.class));
        enumValues.put("boardType", toEnumValues(BoardType.class));
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



}
