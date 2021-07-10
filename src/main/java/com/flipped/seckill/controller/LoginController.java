package com.flipped.seckill.controller;

import com.flipped.seckill.service.ITUserService;
import com.flipped.seckill.service.impL.TUserServiceImpl;
import com.flipped.seckill.vo.LoginVo;
import com.flipped.seckill.vo.RespBean;
import com.sun.deploy.net.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Classname LoginController
 * @Description TODO
 * @Date 2021/7/4 11:42
 * @Created by zh
 */

@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private ITUserService userService;

    @RequestMapping("toLogin")
    public  String toLogin(){
        return "login";
    }

    @ResponseBody
    @RequestMapping("/doLogin")
    //接收前段传递过来的参数
    public RespBean dologin(@Valid LoginVo loginVo, HttpServletResponse response, HttpServletRequest request){

//        System.out.println("????"+loginVo);
        return userService.doLogin(loginVo,request,response);
    }
}
