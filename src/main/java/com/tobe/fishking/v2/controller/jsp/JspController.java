package com.tobe.fishking.v2.controller.jsp;

import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.board.NoticeDetailDto;
import com.tobe.fishking.v2.service.board.PostService;
import com.tobe.fishking.v2.service.smartfishing.FishingShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/boarding")
public class JspController {

    @Autowired private FishingShipService fishingShipService;
    @Autowired private PostService postService;

    @GetMapping("/share")
    public ModelAndView share(ModelAndView mnv) {
        mnv.setViewName("jsp/share");
        return mnv;
    }

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
    public ModelAndView cameradd(ModelAndView mnv, @RequestParam("shipId") Long shipId) {
        mnv.addObject("result", fishingShipService.getShipCamera(shipId));
        mnv.setViewName("jsp/cameradd");
        return mnv;
    }

    @GetMapping("/csnotice")
    public ModelAndView csnotice(ModelAndView mnv) {
        mnv.setViewName("jsp/csnotice");
        return mnv;
    }

    @GetMapping("/csnoticedetail")
    public ModelAndView csnoticedetail(ModelAndView mnv, @RequestParam("postId") Long postId) throws ResourceNotFoundException {
        mnv.addObject("result", postService.getNoticeDetail(postId));
        mnv.setViewName("jsp/csnoticedetail");
        return mnv;
    }

    @GetMapping("/csfaq")
    public ModelAndView csfaq(ModelAndView mnv) {
        mnv.addObject("result", postService.getNoticeList(0, "shipowner", null, null));
        mnv.setViewName("jsp/csfaq");
        return mnv;
    }

}
