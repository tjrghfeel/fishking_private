package com.tobe.fishking.v2.controller;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.HelloResponseDto;
import com.tobe.fishking.v2.service.HelloService;
import com.tobe.fishking.v2.service.auth.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jcodec.api.JCodecException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Api(tags = {"Hello Test"})
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/v2/api")
public class HelloController {
    @Autowired
    HelloService helloService;
    @Autowired
    MemberService memberService;

    @ApiOperation(value = "Hello", notes = "Hello Test")

    @PostMapping("/hello")
    public String hello(@RequestParam("username") String username,@RequestParam("pw") String pw){
        System.out.println(">>> username : "+username+", pw : " +pw);
        return "hello";
    }

    @GetMapping("/noName2")
    public String helloDto(
            HttpSession session,
            @RequestParam("name") String name,
            @RequestParam("amount") int amount,
            Model model
    ) {
        model.addAttribute("hello",new HelloResponseDto(name, amount));
        return "kakaoStoryShare";
    }

    @GetMapping("/noName")
    @ResponseBody
    public String noName() throws ResourceNotFoundException, IOException, JCodecException {
//        helloService.noName();

//        String url = "https://kapi.kakao.com/v1/api/story/post/link";
//
//        String method = "POST";
//        Map<String,String> parameter = new HashMap<String, String>();
//        parameter.put("permission","A");
//        parameter.put("link_info","{   \"url\": \"https://www.naver.com/\",   \"requested_url\": \"http://www.naver.com\",   \"host\": \"www.naver.com\",   \"title\": \"네이버\",   \"image\": [     \"https://scrap.kakaocdn.net/dn/bnkNcK/hyJjXLh5Pb/UqZAdKMcRvgAFch8jqxU81/img.png?width=270&height=270&face=0_0_270_270\",     \"https://scrap.kakaocdn.net/dn/mdLqG/hyJjXEwPFZ/80nmPIkINCrgc834c9AML1/img.png?width=270&height=270&face=0_0_270_270\"   ],   \"description\": \"네이버 메인에서 다양한 정보와 유용한 컨텐츠를 만나 보세요\",   \"type\": \"website\",   \"section\": \"\" }");
//        String responseForAccessCode = memberService.sendRequest(url,method,parameter,"Bearer 6vK1LmfsKp40E5tU1Wj_2rhfwU-2i2uYP1xO4wopyV8AAAF3sw3Edw");
        String url = "https://fcm.googleapis.com/fcm/send";
        Map<String,String> parameter = new HashMap<>();
        parameter.put("json",
                "{ \"notification\": " +
                        "{" +
                        "\"title\": \"sampleTitle\", " +
                        "\"body\": \"sample message body\", " +
                        "\"android_channel_id\": \"notification.native_fishking\"" +
                        "}," +
                    "\"to\" : " +
                        "\"fPMt6cgBSEim5ZHm6FNcXs:APA91bFlhoi3YyjpmrnuNBk-iGYhxo9CdugGEsI44GxLs4LLLWW66VXHdfCWZ_TYeeHVo7IAibFwoGuu6dXgWhn5AT845s2rluEWi0SNIiurpYbGbTqPf-2yCYEkcs4vY87ZT0f6Mm6G\"" +
                "}");
        String response = memberService.sendRequest(url, "JSON", parameter,"key=AAAAlI9VsDY:APA91bGtlb8VOtuRGVFU4jmWrgdDnNN3-qfKBm-5sz2LZ0MqsSvsDBzqHrLPapE2IALudZvlyB-f94xRCrp7vbGcQURaZon368Uey9HQ4_CtTOQQSEa089H_AbmWNVfToR42qA8JGje5");
        return response;
//        model.addAttribute("hello","hello2");
//        return "kakaoStoryShare";

    }

    @GetMapping("/naver")
    @ResponseBody
    public String naver(HttpServletResponse response) throws IOException {
//        response.sendRedirect("http://www.naver.com");
        String url = "https://nid.naver.com/oauth2.0/authorize?" +
                "response_type=code" +
                "&client_id=xQF6XDWPhMC665JO2kSq" +
                "&redirect_uri=https://www.fishkingapp.com/v2/api/naverAuthCode" +
                "&state=abcd";
        String method = "POST";
        Map<String,String> parameter = new HashMap<String, String>();

        String responseForAccessCode = memberService.sendRequest(url,method,parameter,"");

        return responseForAccessCode;
    }

    @GetMapping("/pass")
    @ResponseBody
    public String pass(HttpServletResponse response) throws IOException {
//        response.sendRedirect("http://www.naver.com");
        String url = "https://id.passlogin.com/oauth2/authorize?" +
                "response_type=code" +
                "&client_id=uWHHuitm5at159jXPlc5" +
                "&redirect_uri=https://www.fishkingapp.com/v2/api/passAuthCode" +
                "&state=abcd";
        String method = "POST";
        Map<String,String> parameter = new HashMap<String, String>();

        String responseForAccessCode = memberService.sendRequest(url,method,parameter,"");

        return responseForAccessCode;
    }

    @GetMapping("/kakao")
    @ResponseBody
    public String kakao(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://kauth.kakao.com/oauth/authorize?" +
                "response_type=code" +
                "&client_id=f0685b27f74d3f456d396195ca40796e" +
                "&redirect_uri=https://www.fishkingapp.com/v2/api/kakaoAuthCode");
        return "";
    }
    @GetMapping("/facebook")
    @ResponseBody
    public String facebook(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://www.facebook.com/v9.0/dialog/oauth?" +
                "client_id=697267061151978" +
                "&redirect_uri=https://www.fishkingapp.com/v2/api/facebookAuthCode" +
                "");

        return "";
    }

    @GetMapping("/apple")
    public String apple(ModelMap model){
        System.out.println("in apple()");
        model.addAttribute("client_id", "com.fishkingapp.nativeFishkingService");
        model.addAttribute("redirect_uri", "https://fishkingapp.com/v2/api/appleAuthCode");
        model.addAttribute("nonce", "aaa");

        return "noName2";
    }

    @GetMapping("/nice")
    public String nice(ModelMap model){
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

        String sSiteCode = "BT950";			// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";		// NICE로부터 부여받은 사이트 패스워드

        String sRequestNumber = "REQ0000000001";        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
        // 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
        sRequestNumber = niceCheck.getRequestNO(sSiteCode);
//        session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.

        String sAuthType = "";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

        String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
        String customize 	= "";		//없으면 기본 웹페이지 / Mobile : 모바일페이지

        String sGender = ""; 			//없으면 기본 선택 값, 0 : 여자, 1 : 남자

        // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
        //리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : http://www.~ 리턴 url : http://www.~
        String sReturnUrl = "https://www.fishkingapp.com/v2/api/niceSuccess";      // 성공시 이동될 URL
        String sErrorUrl = "https://www.fishkingapp.com/v2/api/niceFail";          // 실패시 이동될 URL

        // 입력될 plain 데이타를 만든다.
        String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                "7:ERR_URL" + sErrorUrl.getBytes().length + ":" + sErrorUrl +
                "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize +
                "6:GENDER" + sGender.getBytes().length + ":" + sGender;

        String sMessage = "";
        String sEncData = "";

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
        if( iReturn == 0 )
        {
            sEncData = niceCheck.getCipherData();
        }
        else if( iReturn == -1)
        {
            sMessage = "암호화 시스템 에러입니다.";
        }
        else if( iReturn == -2)
        {
            sMessage = "암호화 처리오류입니다.";
        }
        else if( iReturn == -3)
        {
            sMessage = "암호화 데이터 오류입니다.";
        }
        else if( iReturn == -9)
        {
            sMessage = "입력 데이터 오류입니다.";
        }
        else
        {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }

        model.addAttribute("sMessage",sMessage);
        model.addAttribute("sEncData",sEncData);

        return "niceREquest";
    }

}

