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

    @PostMapping("/noName")
    public String noName() throws ResourceNotFoundException, IOException, JCodecException {
        return "redirect:/v2/api/noName3?aaa=hello";

    }
    @PostMapping("/noName3")
    @ResponseBody
    public String noName3(@RequestParam("aaa") String aaa){
        return aaa;
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

    @GetMapping("/nice/{id}")
    @ResponseBody
    public String nice(@PathVariable("id") Long id, ModelMap model, HttpSession session) throws IOException {
        NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

        String sSiteCode = "BT950";			// NICE로부터 부여받은 사이트 코드
        String sSitePassword = "bG72MjEPkvjy";		// NICE로부터 부여받은 사이트 패스워드

        String sRequestNumber = id.toString();        	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
        // 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
//        sRequestNumber = niceCheck.getRequestNO(sSiteCode);
        session.setAttribute("REQ_SEQ" , sRequestNumber);	// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.

        String sAuthType = "M";      	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

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

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);//보낼 데이터 암호화.
        //암호화 결과 코드 확인.
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

        String url = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
        String method = "POST";
        Map<String,String> parameter = new HashMap<String, String>();
        parameter.put("EncodeData",sEncData);
        parameter.put("m","checkplusService");

        String responseForAccessCode = memberService.sendRequest(url,method,parameter,"");
        return responseForAccessCode;
    }

}

