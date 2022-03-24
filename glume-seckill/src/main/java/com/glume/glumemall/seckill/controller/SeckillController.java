package com.glume.glumemall.seckill.controller;

import com.glume.common.core.utils.R;
import com.glume.glumemall.seckill.service.SeckillService;
import com.glume.glumemall.seckill.to.SeckillSkuRedisTo;
import com.glume.glumemall.seckill.vo.SeckillSessionsWithSkusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author TuoYingtao
 * @create 2022-03-02 22:11
 */
@Controller
public class SeckillController {

    @Autowired
    SeckillService seckillService;

    /**
     * 返回当前时间可以参与的秒杀商品
     * @return
     */
    @ResponseBody
    @GetMapping("/getCurrentSeckillSkus")
    public R getCurrentSeckillSkus() {
        List<SeckillSkuRedisTo> vos = seckillService.getCurrentSeckillSkus();
        return R.ok().put("data",vos);
    }

    /**
     * 返回即将参与的秒杀商品
     */
    @ResponseBody
    @GetMapping("/getNotCurrentSeckillSkus")
    public R getNotCurrentSeckillSkus() {
        List<SeckillSkuRedisTo> data = seckillService.getNotCurrentSeckillSkus();
        return R.ok().put("data",data);
    }

    /**
     * 获取某一个商品的秒杀信息
     * @return
     */
    @ResponseBody
    @GetMapping("/sku/seckill/{skuId}")
    public R getSkuSeckillInfo(@PathVariable("skuId") Long skuId) {
        SeckillSkuRedisTo to = seckillService.getSkuSeckillInfo(skuId);
        return R.ok().put("data",to);
    }

    @GetMapping("/kill")
    public String seckill(@RequestParam("killId") String killId,
                          @RequestParam("key") String key,
                          @RequestParam("num") Integer num, Model model) {
        String orderSn = seckillService.kill(killId,key,num);
        model.addAttribute("orderSn",orderSn);
        return "success";
    }
}
