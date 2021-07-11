package com.flipped.seckill;

import com.flipped.seckill.utils.UUIDUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private RedisScript<Boolean> redisScript;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void lock01() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 如果k1不存在，才可以设置成功--setIfAbsent
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        // 如果占位成功，进行正常操作 --成功写入值才算锁成功了
        if (isLock) {
            valueOperations.set("name","xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println(name);
            // 操作结束，删除锁
            redisTemplate.delete("k1");
        }else {
            System.out.println("有线程在使用，请稍后再试");
        }
    }
    @Test
    public void lock02() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 通过设置过期时间，锁仍然可以正常销毁
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1",10, TimeUnit.SECONDS);
        // 如果占位成功，进行正常操作
        if (isLock) {
            valueOperations.set("name","222");
            String name = (String) valueOperations.get("name");
//            System.out.println(2/0);
            System.out.println(name);
            // 处理锁-----获取锁，判断锁，删除锁--原子性
            redisTemplate.delete("k1");
        }else {
            System.out.println("有线程在使用，请稍后再试");
        }
    }




}
