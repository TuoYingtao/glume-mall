package com.glume.glumemall.coupon.dao;

import com.glume.glumemall.coupon.entity.SeckillPromotionEntity;
import com.glume.glumemall.coupon.entity.SeckillSessionEntity;
import com.glume.glumemall.coupon.entity.SeckillSkuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 秒杀活动商品关联
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:13:53
 */
@Mapper
public interface SeckillSkuRelationDao extends BaseMapper<SeckillSkuRelationEntity> {

    void deleteBatchSkuRelation(@Param("field") String field, @Param("ids") List<Long> ids);

    List<SeckillPromotionEntity> selectPromotionSessionRelation(@Param("ids") List<Long> ids);
}
