package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.service.common.PayService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @PostMapping("/payresult")
    public String payResult(HttpServletRequest request) throws IOException {
        String rcid		= request.getParameter("reCommConId"		);
        String rctype	= request.getParameter("reCommType"		);
        String rhash	= request.getParameter("reHash"			);
        String rcancel  = request.getParameter("reCnclType"			);   // 1 취소응답, 0 정상응답

        Long orderId = payService.payResult(rcid, rctype, rhash, rcancel);
//        return "redirect:/cust/payment/result?orderId="+orderId.toString();
        return "redirect:https://fishkingapp.com/cust/reservation/my/detail/" + orderId.toString();
//        return "redirect:https://fishkingapp.com/cust/reservation/my/detail/2";
    }

    @ApiOperation(value = "주문 취소", notes= "주문 취소 " +
            "\n {" +
            "\n     orderId: 주문id" +
            "\n }" +
            "\n 방문횟수는 어복황제 이용 횟수 로 변경해주세요")
    @ResponseBody
    @PostMapping("/v2/api/cancel")
    public Map<String, Object> cancel(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody Map<String, Object> body
    ) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long orderId = Long.valueOf(body.get("orderId").toString());
            String msg = payService.cancelOrder(orderId, token);
            if (msg.contains("거절")) {
                result.put("status", "fail");
                result.put("message", "취소결제가 실패했습니다.");
            } else {
                result.put("status", "success");
                result.put("message", msg);
            }
    //        System.out.println(msg);
        } catch (Exception e) {
            result.put("status", "fail");
            result.put("message", "취소결제가 실패했습니다.");
        }
        return result;
    }

}
