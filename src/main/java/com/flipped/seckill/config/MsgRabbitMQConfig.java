package com.flipped.seckill.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Description: 秒杀消息配置
 * User: zh
 * Date: 2021/7/9
 * Time: 21:06
 */

@Configuration
public class MsgRabbitMQConfig {

    private static  final  String QUEUE="seckillQueue";
    private static  final  String EXCHANGE="seckillExchange";

    //队列对象获取方法
    @Bean
    public Queue queue(){
        return new  Queue(QUEUE);
    }

    //交换机对象获取方法
    @Bean
    public TopicExchange seckillExchange()
    {
        return new TopicExchange(EXCHANGE);
    }

    //队列与交换器的绑定
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(seckillExchange()).with("seckill.#");
    }
}
