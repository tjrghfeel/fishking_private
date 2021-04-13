package com.tobe.fishking.v2.controller.jsp;

import net.minidev.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/boarding")
public class JspController {

    @GetMapping("/index")
    public ModelAndView index(ModelAndView mnv) {
        mnv.setViewName("jsp/index");
        return mnv;
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(ModelAndView mnv) {
        mnv.setViewName("jsp/dashboard");
        return mnv;
    }

}
