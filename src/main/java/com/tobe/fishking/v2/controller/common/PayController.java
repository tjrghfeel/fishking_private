package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.service.common.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @PostMapping("/payresult")
    public String payResult(HttpServletRequest request) {
        String rcid		= request.getParameter("reCommConId"		);
        String rctype	= request.getParameter("reCommType"		);
        String rhash	= request.getParameter("reHash"			);
        String rcancel  = request.getParameter("reCnclType"			);   // 1 취소응답, 0 정상응답

        Long orderId = payService.payResult(rcid, rctype, rhash, rcancel);
//        return "redirect:/cust/payment/result?orderId="+orderId.toString();
        return "redirect:https://fishkingapp.com/cust/reservation/my/detail/" + orderId.toString();
//        return "redirect:https://fishkingapp.com/cust/reservation/my/detail/2";
    }

    @PostMapping("/cancel")
    public Map<String, Object> cancel(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody Map<String, Object> body
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            payService.cancelOrder((Long) body.get("orderId"), token);
            result.put("status", "success");
            result.put("message", "취소되었습니다.");
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("message", "취소결제가 실패했습니다.");
        }
        return result;
    }

}
