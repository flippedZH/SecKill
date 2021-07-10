package com.flipped.seckill;



import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.tools.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description: no desc
 * User: zh
 * Date: 2021/7/10
 * Time: 10:12
 */



@Data
@AllArgsConstructor
class msg{
    private String username="1";
    private Integer id=1;
}

public class teset {

    public static void main(String[] args) {
        msg msg = new msg("邹恒", 1);
        System.out.println(msg);
        String s = JSONObject.toJSONString(msg);
        System.out.println(s);
    }
}
