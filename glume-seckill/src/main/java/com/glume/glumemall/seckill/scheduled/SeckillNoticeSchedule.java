package com.glume.glumemall.seckill.scheduled;

import com.glume.glumemall.seckill.service.SeckillNoticeService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * @author tuoyingtao
 * @create 2022-03-24 14:08
 */
@Service
public class SeckillNoticeSchedule {
    private final Logger LOGGER = LoggerFactory.getLogger(SeckillNoticeSchedule.class);

    @Autowired
    SeckillNoticeService seckillNoticeService;

    @Autowired
    RedissonClient redissonClient;

    private final String REDISSON_LOCK_UPLOAD = "seckill:notice:lock:upload:";

    private final String REDISSON_LOCK_SEND = "seckill:notice:lock:send:";

    @Async("MyThreadPoolTaskExecutor")
    @Scheduled(cron = "59 59 23 * * *")
    public void noticeSendEmail() {
        RLock lock = redissonClient.getLock(REDISSON_LOCK_UPLOAD);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            seckillNoticeService.uploadSendNotice();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Async("MyThreadPoolTaskExecutor")
    @Scheduled(cron = "0/10 * * * * *")
    public void sendNoticeEmail() {
        RLock lock = redissonClient.getLock(REDISSON_LOCK_SEND);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            seckillNoticeService.sendCurrentEmailNotice();
        } finally {
            lock.unlock();
        }
    }

}
