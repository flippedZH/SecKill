package com.flipped.seckill.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flipped.seckill.mapper.TUserMapper;
import com.flipped.seckill.pojo.TUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.flipped.seckill.vo.LoginVo;
import com.flipped.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author flipped
 * @since 2021-07-04
 */
public interface ITUserService  extends IService<TUser>  {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    TUser getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);

    RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response);
}
