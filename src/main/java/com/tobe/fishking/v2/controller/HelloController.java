package com.tobe.fishking.v2.controller;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.HelloResponseDto;
import com.tobe.fishking.v2.model.auth.SignUpDto;
import com.tobe.fishking.v2.service.HelloService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = {"Hello Test"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v2/api")
public class HelloController {
    @Autowired
    HelloService helloService;

    @ApiOperation(value = "Hello", notes = "Hello Test")

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name,
                                     @RequestParam("amount") int amount) {
        return new HelloResponseDto(name, amount);
    }

    @PostMapping("/noName")
    public void noName(@RequestBody String dto) throws ResourceNotFoundException {
        System.out.println("hello >>> "+dto);
        helloService.noName();
    }

}

