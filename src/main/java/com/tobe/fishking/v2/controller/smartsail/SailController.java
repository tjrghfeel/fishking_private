package com.tobe.fishking.v2.controller.smartsail;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.service.auth.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"스마트승선 승선관리 및 대시보드"})
@RequiredArgsConstructor
public class SailController {

    private final MemberService memberService;

    @ApiOperation(value = "대시보드", notes= "대시보드 {" +
            "\n accountNum: 계좌번호" +
            "\n bankCode: 은행코드" +
            "\n name: 예금주명 } " +
            "")
    @GetMapping("/sail/dashboard")
    public Map<String, Object> dashboard(@RequestHeader(name = "Authorization") String token) throws ResourceNotFoundException, NotAuthException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        Map<String, Object> response = new HashMap<>();
        return response;
    }

}
