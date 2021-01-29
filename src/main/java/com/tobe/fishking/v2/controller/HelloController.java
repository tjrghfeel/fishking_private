package com.tobe.fishking.v2.controller;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.HelloResponseDto;
import com.tobe.fishking.v2.model.NoNameDTO;
import com.tobe.fishking.v2.model.auth.SignUpDto;
import com.tobe.fishking.v2.service.HelloService;
import com.tobe.fishking.v2.service.auth.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jcodec.api.JCodecException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;


@Api(tags = {"Hello Test"})
@RequiredArgsConstructor
@RestController
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
    public HelloResponseDto helloDto(
            HttpSession session,
            @RequestParam("name") String name,
            @RequestParam("amount") int amount
    ) {
        return new HelloResponseDto(name, amount);
    }

    @GetMapping("/noName")
    public String noName() throws ResourceNotFoundException, IOException, JCodecException {
//        helloService.noName();

        URL url = new URL("http://www.naver.com");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("GET"); // PUT is another valid option

        http.setRequestProperty("Authorization","aaahello");
        http.connect();
        System.out.println("http response : "+http);
//            try(OutputStream os = http.getOutputStream()) {
//                os.write(out);
//            }
        // Do something with http.getInputStream()
        int responseCode = http.getResponseCode();
        BufferedReader br;
        System.out.println("response code : "+responseCode);

        if(responseCode == 200) { // 정상 호출
            br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        } else {  // 에러 발생
            br = new BufferedReader(new InputStreamReader(http.getInputStream()));
        }
        String inputLine;
        StringBuffer res = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
        }
        br.close();
        return res.toString();

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
                "&state=abcd");

        return "";
    }

}

