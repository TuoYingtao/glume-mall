package com.glume.glumemall.seckill.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.seckill.feign.CouponFeignService;
import com.glume.glumemall.seckill.feign.MemberFeignService;
import com.glume.glumemall.seckill.feign.ProductFeignService;
import com.glume.glumemall.seckill.feign.ThirdPartyFeignService;
import com.glume.glumemall.seckill.to.MailTo;
import com.glume.glumemall.seckill.vo.MemberVo;
import com.glume.glumemall.seckill.vo.SeckillSkuNoticeVo;
import com.glume.glumemall.seckill.vo.SkuInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


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
    ProductFeignService productFeignService;

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    ThirdPartyFeignService thirdPartyFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    SchedulingTaskExecutor executor;

    private String SECKILL_NOTICE_WARN = "seckill:notice:";

    @Async("MyThreadPoolTaskExecutor")
    @Scheduled(cron = "0/10 * * * * *")
    public void noticeSendEmail() {
        R result = couponFeignService.currentSendNotice();
        LOGGER.info("需要通知的数据：{}",result);
        if (result.getCode() == HttpStatus.SUCCESS) {
            List<SeckillSkuNoticeVo> resultData = result.getData(new TypeReference<List<SeckillSkuNoticeVo>>() {
            });
            if (StringUtils.isNotEmpty(resultData)) {
                resultData.forEach(seckillSkuNoticeVo -> {
                    // 设置SKU、用户 信息
                    try {
                        asyncMemberAndSkuInfo(seckillSkuNoticeVo);
                        // 缓存
                        long time = dateHnadler(seckillSkuNoticeVo.getSendTime());
                        String key = SECKILL_NOTICE_WARN + time;
                        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
                        if (!hashOps.hasKey(seckillSkuNoticeVo.getId().toString())) {
                            hashOps.put(seckillSkuNoticeVo.getId().toString(),JSON.toJSONString(seckillSkuNoticeVo));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private void asyncMemberAndSkuInfo(SeckillSkuNoticeVo seckillSkuNoticeVo) throws ExecutionException, InterruptedException {
        // 获取Sku信息，并设置商品标题
        CompletableFuture<Void> skuInfoFuture = CompletableFuture.runAsync(() -> {
            R skuInfoResult = productFeignService.getSkuInfo(seckillSkuNoticeVo.getSkuId());
            SkuInfoVo skuInfoVo = skuInfoResult.getData(new TypeReference<SkuInfoVo>() {
            });
            seckillSkuNoticeVo.setSkuInfoVo(skuInfoVo);
        },executor);
        // 获取需要通知的用户信息
        CompletableFuture<Void> memberInfoFuture = CompletableFuture.runAsync(() -> {
            R memberInfoResult = memberFeignService.info(seckillSkuNoticeVo.getMemberId());
            MemberVo memberVo = memberInfoResult.getData(new TypeReference<MemberVo>() {
            });
            seckillSkuNoticeVo.setMemberVo(memberVo);
        },executor);
        CompletableFuture.allOf(skuInfoFuture,memberInfoFuture).get();
    }

    @Async("MyThreadPoolTaskExecutor")
    @Scheduled(cron = "0/10 * * * * *")
    public void sendNoticeEmail() {
        long currentDate = dateHnadler(new Date());
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SECKILL_NOTICE_WARN + currentDate);
        Set<String> keys = hashOps.keys();
        if (StringUtils.isNotEmpty(keys)) {
            for (String key : keys) {
                String result = hashOps.get(key);
                SeckillSkuNoticeVo seckillSkuNoticeVo = JSON.parseObject(result, SeckillSkuNoticeVo.class);
                Instant sendTime = seckillSkuNoticeVo.getSendTime().toInstant();
                Instant currentTime = new Date().toInstant();
                Duration between = Duration.between(sendTime, currentTime).abs();
                long minutes = between.toMinutes();
                if (minutes == 1) {
                    // 开始发送邮箱提醒
                    Boolean isNoticeMail = sendNoticeMail(seckillSkuNoticeVo);
                    if (isNoticeMail) {
                        hashOps.delete(key);
                    }
                }
            }
        }
    }

    /** 发送邮件通知 */
    private Boolean sendNoticeMail(SeckillSkuNoticeVo seckillSkuNoticeVo) {
        MailTo mailTo = SpringUtils.getBean(MailTo.class);
        mailTo.setMailNameTo(Arrays.asList(seckillSkuNoticeVo.getMemberVo().getEmail()));
        mailTo.setVariable(new HashMap<String,String>(){{
            put("title",seckillSkuNoticeVo.getSkuInfoVo().getSkuTitle());
        }});
        R result = thirdPartyFeignService.send(mailTo);
        if (result.getCode() == HttpStatus.SUCCESS) {
            return true;
        }
        return false;
    }

    /** 日期转时间戳 */
    private long dateHnadler(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate parse = LocalDate.parse(dateFormat.format(date));
        return parse.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
