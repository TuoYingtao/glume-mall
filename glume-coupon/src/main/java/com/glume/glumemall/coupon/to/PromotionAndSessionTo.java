package com.glume.glumemall.coupon.to;

import com.glume.glumemall.coupon.entity.SeckillPromotionEntity;
import com.glume.glumemall.coupon.entity.SeckillSessionEntity;
import lombok.Data;

import java.util.List;

/**
 * @author TuoYingtao
 * @create 2022-03-20 14:48
 */
@Data
public class PromotionAndSessionTo {

    private List<SeckillPromotionEntity> promotionEntities;

    private List<SeckillSessionEntity> sessionEntities;
}
