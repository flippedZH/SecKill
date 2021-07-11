package com.flipped.seckill.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipped.seckill.pojo.TUser;
import com.flipped.seckill.service.impL.TUserServiceImpl;
import com.flipped.seckill.utils.CookieUtil;
import com.flipped.seckill.vo.RespBean;
import com.flipped.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TUserServiceImpl userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        ///handler--指要拦截的对象

        //如果拦截器处理的对象--是一个方法
        if (handler instanceof HandlerMethod) {
            //在拦截器中 获取当前user：  取代之前的参数拦截
            TUser user = getUser(request, response);
            //通过拦截器获取的参数放入UserContext中  用于参数解析器解析 用于mvc请求方法统一使用
            UserContext.setUser(user);
            //强转  ---方法处理器
            HandlerMethod hm = (HandlerMethod) handler;
            //获取方法上面的注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            //没有注解--无需拦截  直接返回
            if (accessLimit == null) {
                return true;
            }
            //有注解 --获取参数
            int second = accessLimit.second();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();

            //处理注解逻辑
            String key = request.getRequestURI();
            if (needLogin) {
                if (user == null) {
                    //构建返回对象 --返回json数据类型
                    render(response, RespBeanEnum.SESSION_ERROR);
                    return false;
                }
                key += ":" + user.getId();
            }

            // 限制访问次数
            ValueOperations valueOperations = redisTemplate.opsForValue();
            Integer count = (Integer) valueOperations.get(key);
            if (null == count) {
                valueOperations.set(key, 1, second, TimeUnit.SECONDS);
            } else if (count < maxCount) {
                valueOperations.increment(key);
            } else {
                render(response, RespBeanEnum.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    /**
     * 构建返回对象
     *
     * @param response
     * @param respBeanEnum
     */
    private void render(HttpServletResponse response, RespBeanEnum respBeanEnum) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        RespBean respBean = RespBean.error(respBeanEnum);
        out.write(new ObjectMapper().writeValueAsString(respBean));
        out.flush();
        out.close();
    }

    /**
     * 功能描述：获取当前登录用户
     *
     * @param request
     * @param response
     */
    private TUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String ticket = CookieUtil.getCookieValue(request, "userTicket");
        if (StringUtils.isEmpty(ticket)) {
            return null;
        }
        return userService.getUserByCookie(ticket, request, response);

    }
}
