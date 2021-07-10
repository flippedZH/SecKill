package com.flipped.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Classname DemoController
 * @Description TODO
 * @Date 2021/7/3 23:46
 * @Created by zh
 */

@Controller
public class DemoController {

    @RequestMapping("hello")
    public String hello(Model model){
        model.addAttribute("name","yes");
        return "hello";
    }
}
