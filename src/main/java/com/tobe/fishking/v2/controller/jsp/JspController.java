package com.tobe.fishking.v2.controller.jsp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping("/fingerprint")
    public ModelAndView fingerprint(ModelAndView mnv) {
        mnv.setViewName("jsp/fingerprint");
        return mnv;
    }

    @GetMapping("/fingerprintregist")
    public ModelAndView fingerprintregist(ModelAndView mnv) {
        mnv.setViewName("jsp/fingerprintregist");
        return mnv;
    }

    @GetMapping("/fingerprintother")
    public ModelAndView fingerprintother(ModelAndView mnv) {
        mnv.setViewName("jsp/fingerprintother");
        return mnv;
    }

    @GetMapping("/camera")
    public ModelAndView camera(ModelAndView mnv) {
        mnv.setViewName("jsp/camera");
        return mnv;
    }

    @GetMapping("/cameradd")
    public ModelAndView cameradd(ModelAndView mnv) {
        mnv.setViewName("jsp/cameradd");
        return mnv;
    }

}
