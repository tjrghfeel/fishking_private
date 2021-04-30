package com.tobe.fishking.v2.controller.smartfishing;

import com.tobe.fishking.v2.entity.auth.Member;
import com.tobe.fishking.v2.exception.NotAuthException;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.smartfishing.AccountDTO;
import com.tobe.fishking.v2.service.auth.MemberService;
import com.tobe.fishking.v2.service.fishking.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v2/api")
@Api(tags = {"스마트출조 설정"})
@RequiredArgsConstructor
public class SettingController {

    private final MemberService memberService;
    private final CompanyService companyService;

    @ApiOperation(value = "정산 계좌 설정", notes= "정산 계좌 설정 {" +
            "\n accountNum: 계좌번호" +
            "\n bankCode: 은행코드" +
            "\n name: 예금주명 } " +
            "")
    @PutMapping("/fishing/account/update")
    public Map<String, Object> updateAccount(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody AccountDTO accountDTO) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        Map<String, Object> response = new HashMap<>();
        try {
            companyService.updateAccount(accountDTO, member);
            response.put("status", "success");
            response.put("message", "성공했습니다.");
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "실패했습니다.");
        }
        return response;
    }

    @ApiOperation(value = "정산 계좌 정보", notes= "정산 계좌 정보 {" +
            "\n accountNum: 계좌번호" +
            "\n bankCode: 은행코드" +
            "\n bank: 은행명" +
            "\n name: 예금주명 } " +
            "")
    @GetMapping("/fishing/account")
    public Map<String, Object> getAccount(
            @RequestHeader(name = "Authorization") String token) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        return companyService.getAccount(member);
    }

    @ApiOperation(value = "알림 설정 정보", notes= " {" +
            "\n alarm: 설정 정보  } " +
            "")
    @GetMapping("/fishing/alarm")
    public Map<String, Object> getAlarmSetting(
            @RequestHeader(name = "Authorization") String token) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        return companyService.getAlarmSetting(member);
    }

    @ApiOperation(value = "알림 설정", notes= "알림 설정 {" +
            "\n alarm: 설정 정보 } " +
            "")
    @PutMapping("/fishing/alarm/update")
    public Map<String, Object> updateAlarmSetting(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody Map<String, Object> body) throws NotAuthException, ResourceNotFoundException {
        if (!memberService.checkAuth(token)) {
            throw new NotAuthException("권한이 없습니다.");
        }
        Member member = memberService.getMemberBySessionToken(token);
        Map<String, Object> response = new HashMap<>();
        try {
            companyService.updateAlarmSetting(member, (Boolean) body.get("alarm"));
            response.put("status", "success");
            response.put("message", "성공했습니다.");
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", "실패했습니다.");
        }
        return response;
    }
}
