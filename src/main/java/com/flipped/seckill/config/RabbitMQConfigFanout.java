package com.flipped.seckill.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

/**
 * Description: no desc
 * User: zh
 * Date: 2021/7/9
 * Time: 15:41
 */

//public class RabbitMQConfigFanout {

    //典型的生产者（发消息）与消费者（处理消息）的模型
    //中间就是队列
//
//    private static  final  String QUEUE01="queue_fanout01";
//    private static  final  String QUEUE02="queue_fanout02";
//    private static  final  String EXCHANGE="fanoutExchange";
//
//    @Bean
//    public Queue queue(){
//        return new Queue("queue",true);
//    }
//
//    @Bean
//    public Queue queue01(){
//        return new Queue(QUEUE01);
//    }
//
//    @Bean
//    public Queue queue02(){
//        return new Queue(QUEUE02);
//    }
//
//    @Bean
//    public FanoutExchange fanoutExchange(){
//        return new  FanoutExchange(EXCHANGE);
//    }
////
//    @Bean
//    public Binding binding01(){
//        return  BindingBuilder.bind(queue01()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding binding02(){
//        return BindingBuilder.bind(queue02()).to(fanoutExchange());
//    }

//}
