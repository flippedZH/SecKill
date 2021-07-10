package com.flipped.seckill.controller;


import com.flipped.seckill.pojo.TUser;
import com.flipped.seckill.service.impL.TGoodsServiceImpl;
import com.flipped.seckill.service.impL.TUserServiceImpl;
import com.flipped.seckill.vo.DetailVo;
import com.flipped.seckill.vo.GoodsVo;
import com.flipped.seckill.vo.RespBean;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * Description: no desc
 * User: zh
 * Date: 2021/7/5
 * Time: 11:04
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private TGoodsServiceImpl goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, TUser user, HttpServletResponse response, HttpServletRequest request){
        //从Redis中取页面，如果不为空 直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();

        //从redis中取值（获取页面）
        String html = ((String) valueOperations.get("goodsList"));
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);;
        model.addAttribute("goodsList",goodsService.getGoodsVo());
        //创建WebContext,渲染页面需要的参数（是为了获取页面中的参数？）
        WebContext webContext = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        //手动渲染出html页面 进行存储
        html=thymeleafViewResolver.getTemplateEngine().process("goodsList",webContext);

        if(!StringUtils.isEmpty(html)){
            //存储页面到redis 并 设置失效时间（网页中动态部分应该不影响，所以应该不影响秒杀）
            valueOperations.set("goodslist",html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "/toDetail0/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    //这里不应该用请求参数吗？              --------确实是路径参数
    public String toDetail0(Model model,@PathVariable()Long goodsId,TUser user,HttpServletRequest request
    ,HttpServletResponse response){
        GoodsVo goodsVo = goodsService.getGoodsById(goodsId);

        //从Redis中取页面，如果不为空 直接返回页面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //从redis中取值（获取页面）
        String html = ((String) valueOperations.get("goodsDetail:"+goodsId));
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        Date startTime=goodsVo.getStartDate();
        Date endTime=goodsVo.getEndDate();
        Date nowDate=new Date();
        //秒杀进行状态
        int seckillStatus=0;
        //秒杀倒计时
        int remainSeconds=0;

        //秒杀未开始
        if(nowDate.before(startTime)){
            //距离秒杀开始的时间
            remainSeconds=(int)((startTime.getTime()-nowDate.getTime())/1000);
            //秒杀已结束
        }else if(nowDate.after(endTime)){
            seckillStatus=2;
            //秒杀进行中
        }else{
            seckillStatus=1;
        }

        model.addAttribute("user",user);
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("seckillStatus",seckillStatus);
        model.addAttribute("goods",goodsVo);

        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        //手动渲染出html页面 进行存储  --goodsDetail为文件夹
        html=thymeleafViewResolver.getTemplateEngine().process("goodsDetail",webContext);

        //文件名--"goodsDetail"+goodsId
        if(!StringUtils.isEmpty(html)){
            //存储页面到redis 并 设置失效时间（网页中动态部分应该不影响，所以应该不影响秒杀）
            valueOperations.set("goodsDetail:"+goodsId,html,60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "/detail/{goodsId}")
    @ResponseBody
    //这里不应该用请求参数吗？              --------确实是路径参数
    public RespBean toDetail(Model model, @PathVariable()Long goodsId, TUser user, HttpServletRequest request
            , HttpServletResponse response){

        GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
        Date startTime=goodsVo.getStartDate();
        Date endTime=goodsVo.getEndDate();
        Date nowDate=new Date();
        //秒杀进行状态
        int seckillStatus=0;
        //秒杀倒计时
        int remainSeconds=0;

        //秒杀未开始
        if(nowDate.before(startTime)){
            //距离秒杀开始的时间
            remainSeconds=(int)((startTime.getTime()-nowDate.getTime())/1000);
            //秒杀已结束
        }else if(nowDate.after(endTime)){
            seckillStatus=2;
            remainSeconds=-1;
            //秒杀进行中
        }else{
            seckillStatus=1;
            remainSeconds=0;
        }

        DetailVo detailVo=new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setRemainSecond(remainSeconds);
        detailVo.setSecKillStatus(seckillStatus);
        //返回了一个携带状态信息的 对象--detailVo
        return RespBean.success(detailVo);
    }


}
