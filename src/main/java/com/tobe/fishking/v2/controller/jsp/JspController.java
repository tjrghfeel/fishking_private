package com.tobe.fishking.v2.controller.jsp;

import com.tobe.fishking.v2.enums.IEnumModel;
import com.tobe.fishking.v2.enums.board.QuestionType;
import com.tobe.fishking.v2.exception.ResourceNotFoundException;
import com.tobe.fishking.v2.model.EnumValueDTO;
import com.tobe.fishking.v2.service.board.PostService;
import com.tobe.fishking.v2.service.smartfishing.FishingShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/boarding")
public class JspController {

    @Autowired private FishingShipService fishingShipService;
    @Autowired private PostService postService;

    @GetMapping("/{viewName}")
    public ModelAndView view(ModelAndView mnv, @PathVariable("viewName") String viewName, @RequestParam Map<String, String> params) throws ResourceNotFoundException {
        if ("cameradd".equals(viewName)) {
            String shipId = params.get("shipId");
            mnv.addObject("result", fishingShipService.getShipCamera(Long.valueOf(shipId)));
        }else if ("csnoticedetail".equals(viewName)) {
            String postId = params.get("postId");
            mnv.addObject("result", postService.getNoticeDetail(Long.valueOf(postId)));
        }else if ("csfaq".equals(viewName)) {
            mnv.addObject("result", postService.getNoticeList(0, "shipowner", null, null));
        }else if ("csqna".equals(viewName)) {
            mnv.addObject("questionType", toEnumValues(QuestionType.class));
        }else if ("csqnadetail".equals(viewName)) {
            String postId = params.get("postId");
            mnv.addObject("result", postService.getQnADetail(Long.valueOf(postId)));
        }

        mnv.setViewName("jsp/" + viewName);
        return mnv;
    }

    private List<EnumValueDTO> toEnumValues(Class<? extends IEnumModel> e) {
        return Arrays
                .stream(e.getEnumConstants())
                .map(EnumValueDTO::new)
                .collect(Collectors.toList());
    }

}
