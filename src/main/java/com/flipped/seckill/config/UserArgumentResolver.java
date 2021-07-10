package com.flipped.seckill.config;


import com.flipped.seckill.pojo.TUser;
import com.flipped.seckill.service.impL.TUserServiceImpl;
import com.flipped.seckill.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description: no desc
 * User: zh
 * Date: 2021/7/5
 * Time: 20:04
 */

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    TUserServiceImpl userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //判断参数类型
        Class<?> parameterType = methodParameter.getParameterType();
        //返回的是user才会执行下面的resolveArgument(保证只校验user)

        ////一直为true 为啥会出问题
        return TUser.class.equals(parameterType);
//        System.out.println("yes");
//        return true;
    }

        @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String ticket = CookieUtil.getCookieValue(request, "userTicket");

        if(StringUtils.isEmpty(ticket)){
            return "login";
        }
        return  userService.getUserByCookie(ticket,request,response);
//            return UserContext.getUser();

    }
}
