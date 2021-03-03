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

//        String url = "https://kauth.kakao.com/oauth/authorize?" +
//                "response_type=code" +
//                "&client_id=f0685b27f74d3f456d396195ca40796e" +
//                "&redirect_uri=https://www.fishkingapp.com/v2/api/kakaoAuthCode";
//        String method = "GET";
//        Map<String,String> parameter = new HashMap<String, String>();
//
//        String responseForAccessCode = memberService.sendRequest(url,method,parameter,"");

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
        model.addAttribute("nonce", "20B20D-0S8-1K8");

        return "noName2";
    }

}

