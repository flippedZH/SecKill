package com.flipped.seckill.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Description: Redis配置类
 * User: zh
 * Date: 2021/7/5
 * Time: 19:27
 */

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // key 序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // value 序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // hash 类型 key 序列化
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // hash 类型 value 序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 注入连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    //调用脚本的配置
    @Bean
    public DefaultRedisScript<Long> script() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // stock.lua 脚本位置 和 application.yaml 同级目录
        redisScript.setLocation(new ClassPathResource("stock.lua"));
        //返回值类型
        redisScript.setResultType(Long.class);
        return redisScript;
    }

}
