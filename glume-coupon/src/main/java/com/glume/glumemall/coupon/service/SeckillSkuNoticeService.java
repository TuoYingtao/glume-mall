package com.glume.glumemall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.coupon.entity.SeckillSkuNoticeEntity;

import java.util.List;
import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:13:53
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SeckillSkuNoticeEntity> currentSendNotice();
}

