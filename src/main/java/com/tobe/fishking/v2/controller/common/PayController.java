package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.service.common.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @PostMapping("/payresult")
    public void payResult(HttpServletRequest request) {
        String rcid		= request.getParameter("reCommConId"		);
        String rctype	= request.getParameter("reCommType"		);
        String rhash	= request.getParameter("reHash"			);
        String rcancel  = request.getParameter("reCnclType"			);   // 1 취소응답, 0 정상응답

        payService.payResult(rcid, rctype, rhash, rcancel);
    }

}
