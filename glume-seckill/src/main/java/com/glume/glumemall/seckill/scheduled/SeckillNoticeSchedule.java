package com.glume.glumemall.seckill.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.seckill.feign.CouponFeignService;
import com.glume.glumemall.seckill.vo.SeckillSkuNoticeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @author tuoyingtao
 * @create 2022-03-24 14:08
 */
@Service
public class SeckillNoticeSchedule {
    private final Logger LOGGER = LoggerFactory.getLogger(SeckillNoticeSchedule.class);

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    private Integer ttl = 60 * 3 * 1000;

    private String SECKILL_NOTICE_WARN = "seckill:notice:";

    @Async
    @Scheduled(cron = "0 0/30 * * * *")
    public void noticeSendEmail() {
        R result = couponFeignService.currentSendNotice();
        LOGGER.info("需要通知的数据：{}",result);
        //            long currentTime = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        if (result.getCode() == HttpStatus.SUCCESS) {
            List<SeckillSkuNoticeVo> resultData = result.getData(new TypeReference<List<SeckillSkuNoticeVo>>() {
            });
            if (StringUtils.isNotEmpty(resultData)) {
                resultData.forEach(seckillSkuNoticeVo -> {
                    long sendTime = seckillSkuNoticeVo.getSendTime().getTime();
                    String key = SECKILL_NOTICE_WARN + sendTime;
                    BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
                    hashOps.expire(sendTime + ttl, TimeUnit.MICROSECONDS);
                    if (!hashOps.hasKey(seckillSkuNoticeVo.getId().toString())) {
                        hashOps.put(seckillSkuNoticeVo.getId().toString(),JSON.toJSONString(seckillSkuNoticeVo));
                    }
                });
            }
        }
    }
}
