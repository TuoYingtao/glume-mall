package com.glume.glumemall.seckill.scheduled;

import com.glume.glumemall.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 秒杀商品定时上架
 *      每晚3点；上架最近三天需要秒杀的商品
 *      当天 00:00:00 - 23:59:59
 *      明天 00:00:00 - 23:59:59
 *      后天 00:00:00 - 23:59:59
 * @author tuoyingtao
 * @create 2022-03-01 16:57
 */
@Service
public class SeckillSkuScheduled {

    @Autowired
    SeckillService seckillService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadSeckillSku3Day() {
        seckillService.uploadSeckillSkuLatest3Day();
    }
}
