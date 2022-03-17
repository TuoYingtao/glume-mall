package com.glume.glumemall.admin.service;

import com.glume.common.core.utils.R;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.to.SeckillPromotionTo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author TuoYingtao
 * @create 2022-03-15 16:59
 */
public interface CouponPromotionService {

    R saveSeckillPromotion(HttpServletRequest request, SeckillPromotionTo seckillPromotionTo);

    R updateSeckillPromotion(SeckillPromotionTo seckillPromotionTo);

    PageUtils listSeckillPromotion(Map<String, Object> params);
}
