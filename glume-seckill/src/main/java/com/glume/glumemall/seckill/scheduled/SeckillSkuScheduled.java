package com.glume.glumemall.seckill.scheduled;

import com.glume.glumemall.seckill.service.SeckillService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

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
    private final Logger LOGGER = LoggerFactory.getLogger(SeckillSkuScheduled.class);

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedissonClient redissonClient;

    private final String UPLOAD_LOCK = "seckill:upload:lock:";

    @Scheduled(cron = "0 0 1/1 * * ?")
    public void uploadSeckillSku3Day() {
        LOGGER.info("开始上架近三天的活动商品....");
        /** 分布式锁 */
        RLock lock = redissonClient.getLock(UPLOAD_LOCK);
        // 设置锁过期时间
        lock.lock(10, TimeUnit.SECONDS);
        try {
            seckillService.uploadSeckillSkuLatest3Day();
        } finally {
            lock.unlock();
        }
    }
}
