package com.flipped.seckill.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 消息发送者
 * User: zh
 * Date: 2021/7/9
 * Time: 10:59
 */

@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    public void send(Object msg){
//        log.info("发送消息："+msg);
//        rabbitTemplate.convertAndSend("fanoutExchange","",msg);
//    }
//
//    public void send01(Object msg){
//        log.info("发送red消息："+msg);
//        rabbitTemplate.convertAndSend("directExchange","queue.red",msg);
//    }
//
//    public void send02(Object msg){
//        log.info("发送green消息："+msg);
//        rabbitTemplate.convertAndSend("directExchange","queue.green",msg);
//    }
//
//    //匹配模式只能在交换机与消息队列之间 发消息时使用
//    //通配： *：至少有一个  #：任意
//    public void send_Topic01(Object msg){
//        log.info("发送消息：(queue01接收的生产出来的消息：)"+msg);
//        rabbitTemplate.convertAndSend("topicExchange","queue.red.msg",msg);
//    }
//
//    public void send_Topic02(Object msg){
//        log.info("发送消息：(两个queue都接收的生产出来的消息)："+msg);
//        rabbitTemplate.convertAndSend("topicExchange","green.queue.msg",msg);
//    }



        //发送秒杀信息
        public void sendSeckillMsg(String msg){
            System.out.println("消息："+msg);
        log.info("发送秒杀消息"+msg);

        rabbitTemplate.convertAndSend("seckillExchange","seckill.msg",msg);
    }

}
