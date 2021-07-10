//package com.flipped.seckill.config;
//
//
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Description: no desc
// * User: zh
// * Date: 2021/7/9
// * Time: 16:14
// */
//
//@Configuration
//public class RabbitMQTopicConfig {
//
//    private static  final  String QUEUE01="queue_top01";
//    private static  final  String QUEUE02="queue_top02";
//    private static  final  String EXCHANGE="topicExchange";
//    private static  final  String ROUTINGKEY01="#.queue.#";
//    private static  final  String ROUTINGKEY02="*.queue.#";
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
//    public TopicExchange topicExchange(){
//        return new TopicExchange(EXCHANGE);
//    }
//
//    @Bean
//    public Binding binding01(){
//        return  BindingBuilder.bind(queue01()).to(topicExchange()).with(ROUTINGKEY01);
//    }
//
//    @Bean
//    public Binding binding02(){
//        return BindingBuilder.bind(queue02()).to(topicExchange()).with(ROUTINGKEY02);
//    }
//
//}
//
