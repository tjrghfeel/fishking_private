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
    public ModelAndView index() {
        System.out.println("index");
        ModelAndView mnv = new ModelAndView("jsp/index");

        Map<String, Object> results = new HashMap<>();
        Map<String, Object> maps = new HashMap<>();
        List<String> lists = new ArrayList<>();
        lists.add("a");
        lists.add("b");
        maps.put("test", "test");
        maps.put("list", lists);

        mnv.addObject("name", "테스트");
        mnv.addObject("maps", maps);
        mnv.addObject("lists", lists);

        return mnv;
    }

}
