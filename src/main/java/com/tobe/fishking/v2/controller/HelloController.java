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

        String url = "https://kapi.kakao.com/v1/api/story/post/link";

        String method = "POST";
        Map<String,String> parameter = new HashMap<String, String>();
        Map<String,Object> linkInfo = new HashMap<>();
        linkInfo.put("url","https://www.naver.com/");
        linkInfo.put("requested_url","http://www.naver.com");
        linkInfo.put("host","www.naver.com");
        linkInfo.put("title","네이버");
        String[] images = new String[0];
//        images[0]="https://scrap.kakaocdn.net/dn/nkXw7/hyJjUgljO0/zWVKuMmy1ofio3fKxU1G41/img.png?width=270&height=270&face=0_0_270_270";
//        images[1]="https://scrap.kakaocdn.net/dn/5uAY1/hyJitkkYyB/NRXglmbYeBm0kHoqHkJF50/img.png?width=270&height=270&face=0_0_270_270";
        linkInfo.put("image",images);
        linkInfo.put("description","네이버 메인에서 어쩌구");
        linkInfo.put("type","website");
        linkInfo.put("section","");
        parameter.put("link_info",linkInfo.toString());
        String responseForAccessCode = memberService.sendRequest(url,method,parameter,"");

        return responseForAccessCode;
//        model.addAttribute("hello","hello2");
//        return "kakaoStoryShare";

    }

    @GetMapping("/naver")
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
    public String facebook(HttpServletResponse response) throws IOException {
        response.sendRedirect("https://www.facebook.com/v9.0/dialog/oauth?" +
                "client_id=697267061151978" +
                "&redirect_uri=https://www.fishkingapp.com/v2/api/facebookAuthCode" +
                "");

        return "";
    }

}

