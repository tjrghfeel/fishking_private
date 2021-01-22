package com.tobe.fishking.v2.controller;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.HelloResponseDto;
import com.tobe.fishking.v2.model.NoNameDTO;
import com.tobe.fishking.v2.model.auth.SignUpDto;
import com.tobe.fishking.v2.service.HelloService;
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
import java.io.IOException;


@Api(tags = {"Hello Test"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class HelloController {
    @Autowired
    HelloService helloService;

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

    @PostMapping("/noName")
    public String noName(MultipartFile file) throws ResourceNotFoundException, IOException, JCodecException {
        helloService.noName(file);

        return "asbdsf";
    }

}

