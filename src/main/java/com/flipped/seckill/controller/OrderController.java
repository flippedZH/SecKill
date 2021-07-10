package com.flipped.seckill.controller;


import com.flipped.seckill.pojo.TUser;
import com.flipped.seckill.service.impL.TOrderServiceImpl;
import com.flipped.seckill.vo.OrderDetailVo;
import com.flipped.seckill.vo.RespBean;
import com.flipped.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description: no desc
 * User: zh
 * Date: 2021/7/8
 * Time: 11:02
 */

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private TOrderServiceImpl orderService;

    @RequestMapping("/detail")
    @ResponseBody
    public RespBean order(TUser user, Long orderId){  //orderId是 那个44  这里一定是orderid 才不会错

        //用户未登录
        if(null==user){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo orderDetailVo= orderService.getOrdeDetail( orderId);

        return RespBean.success(orderDetailVo);
    }

}
