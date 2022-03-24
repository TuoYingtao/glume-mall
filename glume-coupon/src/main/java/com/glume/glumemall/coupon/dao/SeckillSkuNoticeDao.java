package com.glume.glumemall.coupon.dao;

import com.glume.glumemall.coupon.entity.SeckillSkuNoticeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 秒杀商品通知订阅
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:13:53
 */
@Mapper
public interface SeckillSkuNoticeDao extends BaseMapper<SeckillSkuNoticeEntity> {

    List<SeckillSkuNoticeEntity> selectNearDay3();
}
