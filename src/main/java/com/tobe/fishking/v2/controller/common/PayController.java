package com.tobe.fishking.v2.controller.common;

import com.tobe.fishking.v2.service.common.PayService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PayController {

//    private final PayService payService;

//    @ResponseBody
//    @GetMapping("/v2/api/ct")
//    public Map<String, Object> ct(@RequestParam Long number) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            String msg = payService.cancelOrder(number, "");
//            if (msg.contains("거절")) {
//                result.put("status", "fail");
//                result.put("message", "취소결제가 실패했습니다.");
//            } else {
//                result.put("status", "success");
//                result.put("message", msg);
//            }
//        } catch (Exception e) {
//            result.put("status", "fail");
//            result.put("message", "취소결제가 실패했습니다.");
//        }
//        return result;
//    }

}
