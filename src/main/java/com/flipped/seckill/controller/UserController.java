package com.flipped.seckill.controller;


import com.flipped.seckill.rabbitmq.MQSender;
import com.flipped.seckill.vo.RespBean;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description: no desc
 * User: zh
 * Date: 2021/7/9
 * Time: 11:05
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqsender;



//    //消息发送测试
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq(){
//        mqsender.send("hello");
//    }
//
//    //fanout 模式
//    //消息发送测试
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public void mq_fanout(){
//        mqsender.send("hello_fanout");
//    }
//
//
//    //direct模式
//    @RequestMapping("/mq/direct01")
//    @ResponseBody
//    public void mq_direct01(){
//        mqsender.send01("hello_direct_red");
//    }
//
//    //direct模式
//    @RequestMapping("/mq/direct02")
//    @ResponseBody
//    public void mq_direct02(){
//        mqsender.send02("hello_direct_green");
//    }
//
//    //topic模式
//    @RequestMapping("/mq/top01")
//    @ResponseBody
//    public void mq_top01(){
//        mqsender.send_Topic01("被hello_一号消费者收到！");
//    }
//
//    @RequestMapping("/mq/top02")
//    @ResponseBody
//    public void mq_top02(){
//        mqsender.send_Topic02("被hello_二号消费者收到！");
//    }




}
