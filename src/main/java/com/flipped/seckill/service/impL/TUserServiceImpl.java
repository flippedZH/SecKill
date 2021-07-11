package com.flipped.seckill.service.impL;

import com.flipped.seckill.config.RedisConfig;
import com.flipped.seckill.exception.GlobalException;
import com.flipped.seckill.pojo.TUser;
import com.flipped.seckill.mapper.TUserMapper;
import com.flipped.seckill.service.ITUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flipped.seckill.utils.CookieUtil;
import com.flipped.seckill.utils.MD5Util;
import com.flipped.seckill.utils.UUIDUtil;
import com.flipped.seckill.utils.ValidatorUtils;
import com.flipped.seckill.vo.LoginVo;
import com.flipped.seckill.vo.RespBean;
import com.flipped.seckill.vo.RespBeanEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author flipped
 * @since 2021-07-04
 */

@Service
public class TUserServiceImpl   extends ServiceImpl<TUserMapper, TUser> implements ITUserService {

    @Autowired
    private TUserMapper userMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String frontPwd = loginVo.getPassword();

        //先写完所有的错误情况

//        //判空
//        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(frontPwd)){
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        //判断格式是不是正确
//        if(!ValidatorUtils.isMobile(mobile)){
//            System.out.println(RespBean.error(RespBeanEnum.MOBILE_ERROR).toString());
//            return  RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }

        //根据手机号获取用户  (本项目 手机号就是id)
        TUser user = userMapper.selectById(mobile);
        //数据库中没有找到用户
        if (null == user) {
//            System.out.println("没找到");
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
//        判断密码是否正确 (每个用户有一个单独的盐)
        if (!MD5Util.frontPwdToDBPwd(frontPwd, user.getSalt()).equals(user.getPassword())) {
//            System.out.println(RespBean.error(RespBeanEnum.LOGIN_ERROR));
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        //生成cookie
        String ticket = UUIDUtil.uuid();

        //方式一：操作String--将用户信息存入redis  注意key的定义方式
        redisTemplate.opsForValue().set("user:" + ticket, user);

        CookieUtil.setCookie(request, response, "userTicket", ticket);
        //方式二：request.getSession().setAttribute(ticket,user);
        return RespBean.success(ticket);
    }


    @Override
    public TUser getUserByCookie(String userTicket, HttpServletRequest request, HttpServletResponse response) {
        //判空
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        TUser user = (TUser) redisTemplate.opsForValue().get("user:" + userTicket);

        //继续添加一次cookie,以防万一
        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }

    //更新密码
    //通过每次在对数据库操作之前完成 对Redis数据的删除 就可以保持数据的一致性
    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {
        TUser user = getUserByCookie(userTicket, request, response);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPwd2DBPwd(password, user.getSalt()));
        int result = userMapper.updateById(user);
        if (result == 1) {
            // 删除 Redis
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}
