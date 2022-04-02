package com.glume.glumemall.seckill.service.impl;

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
import com.glume.glumemall.seckill.service.SeckillNoticeService;
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
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author tuoyingtao
 * @create 2022-04-01 15:31
 */
@Service
public class SeckillNoticeServiceImpl implements SeckillNoticeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeckillNoticeServiceImpl.class);

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

    private Integer expire = 60 * 60 * 12;

    private String notice_id = "NOTICE_ID";

    private String SECKILL_NOTICE_WARN = "seckill:notice:";

    private String SECKILL_NOTICE_INFO = "seckill:notice:info:";

    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void uploadSendNotice() throws ExecutionException, InterruptedException {
        // 获取未来三天的需要通知的数据
        R result = couponFeignService.currentSendNotice();
        if (result.getCode() == HttpStatus.SUCCESS) {
            List<SeckillSkuNoticeVo> resultData = result.getData(new TypeReference<List<SeckillSkuNoticeVo>>() {
            });
            if (StringUtils.isNotEmpty(resultData)) {
                for (SeckillSkuNoticeVo resultDatum : resultData) {
                    // 获取当前通知数据的用户信息与订阅商品的信息
                    asyncMemberAndSkuInfo(resultDatum);
                    // 设置缓存KEY 以当天发送通知的日期为KEY
                    String key = SECKILL_NOTICE_WARN + dateHnadler(resultDatum.getSendTime());
                    BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(key);
                    // 设置过期时间 12小时
                    hashOps.expire(expire, TimeUnit.SECONDS);
                    // 获取发送通知的时间
                    long keyTime = resultDatum.getSendTime().getTime();
                    // 判断当前发送通知的时间是否已在缓存中，保持幂等性
                    if (!hashOps.hasKey(String.valueOf(keyTime))) {
                        // 收集所有同一时间发送通知的ID
                        List<Long> idList = resultData.stream().filter(item -> item.getSendTime().getTime() == keyTime)
                                .map(SeckillSkuNoticeVo::getId).distinct().collect(Collectors.toList());
                        Map<String,List<Long>> data = new HashMap<>();
                        data.put(notice_id,idList);
                        // 缓存：已发送通知的时间为KEY 所有同一时间发送通知的ID为Value
                        hashOps.put(String.valueOf(keyTime), JSON.toJSONString(data));
                        // 缓存：通知信息 所有需要通知的数据
                        BoundHashOperations<String, String, String> infoHashOps = redisTemplate.boundHashOps(SECKILL_NOTICE_INFO);
                        infoHashOps.put(String.valueOf(resultDatum.getId()),JSON.toJSONString(resultDatum));
                    }
                }
            }
        }
    }

    @Override
    public void sendCurrentEmailNotice() {
        Date date = new Date();
        Instant currentTime = date.toInstant();
        // 获取当天的需要通知的缓存
        BoundHashOperations<String, String, String> currentHashOps = redisTemplate.boundHashOps(SECKILL_NOTICE_WARN + dateHnadler(date));
        // 当天需要通知的场次缓存
        Set<String> keys = currentHashOps.keys();
        if (StringUtils.isNotEmpty(keys)) {
            for (String key : keys) {
                // 计算当前时间与发送通知时间是否一致，默认提前1分钟发送邮件通知
                Long time = Long.valueOf(key);
                Instant sendTime = new Date(time).toInstant();
                Duration duration = Duration.between(currentTime, sendTime).abs();
                long minutes = duration.toMinutes();
                if (minutes == 1) {
                    // 开始发送通知， 获取缓存中需要通知的所有ID
                    String result = currentHashOps.get(key);
                    Map<String, List<String>> listMap = JSON.parseObject(result, new TypeReference<Map<String, List<String>>>() {
                    });
                    List<String> ids = listMap.get(notice_id);
                    // 获取缓存所有需要通知的信息
                    BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SECKILL_NOTICE_INFO);
                    List<String> list = hashOps.multiGet(ids);
                    for (String item : list) {
                        SeckillSkuNoticeVo seckillSkuNoticeVo = JSON.parseObject(item, SeckillSkuNoticeVo.class);
                        // 开始发送通知，判断当前是邮件还是短信 0-短信 1-邮件
                        Boolean isNotice = seckillSkuNoticeVo.getNoticeType() == 1 ? sendNoticeMail(seckillSkuNoticeVo) : false;
                        if (isNotice) {
                            // 发送成功，删除缓存中的通知信息与当天场次ID
                            currentHashOps.delete(key);
                            hashOps.delete(seckillSkuNoticeVo.getId().toString());
                        }
                    }
                }
            }
        }
    }

    private void asyncMemberAndSkuInfo(SeckillSkuNoticeVo seckillSkuNoticeVo) throws ExecutionException, InterruptedException {
        // 获取Sku信息，并设置商品标题
        CompletableFuture<Void> skuInfoFuture = CompletableFuture.runAsync(() -> {
            R result = productFeignService.getSkuInfo(seckillSkuNoticeVo.getSkuId());
            if (result.getCode() == HttpStatus.SUCCESS) {
                SkuInfoVo skuInfoVo = result.getData(new TypeReference<SkuInfoVo>() {
                });
                seckillSkuNoticeVo.setSkuInfoVo(skuInfoVo);
            }
        },executor);
        // 获取需要通知的用户信息
        CompletableFuture<Void> memberInfoFuture = CompletableFuture.runAsync(() -> {
            R result = memberFeignService.info(seckillSkuNoticeVo.getMemberId());
            if (result.getCode() == HttpStatus.SUCCESS) {
                MemberVo memberVo = result.getData(new TypeReference<MemberVo>() {
                });
                seckillSkuNoticeVo.setMemberVo(memberVo);
            }
        },executor);
        CompletableFuture.allOf(skuInfoFuture,memberInfoFuture).get();
    }

    /** 发送邮件通知 */
    private Boolean sendNoticeMail(SeckillSkuNoticeVo seckillSkuNoticeVo) {
        // 创建邮件实例
        MailTo mailTo = SpringUtils.getBean(MailTo.class);
        // 设置接收者邮箱
        mailTo.setMailNameTo(Arrays.asList(seckillSkuNoticeVo.getMemberVo().getEmail()));
        // 设置模板数据信息
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
        LocalDate parse = LocalDate.parse(DATEFORMAT.format(date));
        return parse.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
