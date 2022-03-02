package com.glume.glumemall.seckill.controller;

import com.glume.common.core.utils.R;
import com.glume.glumemall.seckill.service.SeckillService;
import com.glume.glumemall.seckill.to.SeckillSkuRedisTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author TuoYingtao
 * @create 2022-03-02 22:11
 */
@RestController
public class SeckillController {

    @Autowired
    SeckillService seckillService;

    /**
     * 返回当前时间可以参与的秒杀商品
     * @return
     */
    @GetMapping("/getCurrentSeckillSkus")
    public R getCurrentSeckillSkus() {
        List<SeckillSkuRedisTo> vos = seckillService.getCurrentSeckillSkus();
        return R.ok().put("data",vos);
    }
}
