package com.glume.glumemall.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.JwtUtils;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.feign.CouponFeignService;
import com.glume.glumemall.admin.service.CouponPromotionService;
import com.glume.glumemall.admin.service.UserService;
import com.glume.glumemall.admin.to.SeckillPromotionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author TuoYingtao
 * @create 2022-03-15 17:01
 */
@Service("couponPromotionServiceImpl")
public class CouponPromotionServiceImpl implements CouponPromotionService {

    @Value("${jwt.header}")
    String tokenHeader;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    UserService userService;

    @Override
    public R saveSeckillPromotion(HttpServletRequest request, SeckillPromotionTo seckillPromotionTo) {
        String token = request.getHeader(tokenHeader);
        String name = jwtUtils.getUserNameFromToken(token);
        UserEntity userDetail = userService.getByUserDetail(name);
        seckillPromotionTo.setUserId(userDetail.getUserId());
        seckillPromotionTo.setUsername(name);
        seckillPromotionTo.setStartTime(seckillPromotionTo.getDateTime()[0]);
        seckillPromotionTo.setEndTime(seckillPromotionTo.getDateTime()[1]);
        seckillPromotionTo.setCreateTime(new Date());
        R result = couponFeignService.promotionSave(seckillPromotionTo);
        return result;
    }

    @Override
    public R updateSeckillPromotion(SeckillPromotionTo seckillPromotionTo) {
        seckillPromotionTo.setStartTime(seckillPromotionTo.getDateTime()[0]);
        seckillPromotionTo.setEndTime(seckillPromotionTo.getDateTime()[1]);
        R result = couponFeignService.promotionUpdate(seckillPromotionTo);
        return result;
    }

    @Override
    public PageUtils listSeckillPromotion(Map<String, Object> params) {
        R list = couponFeignService.promotionList(params);
        if (list.getCode() == HttpStatus.SUCCESS) {
            PageUtils pageUtils = list.getData(new TypeReference<PageUtils>() {
            });
            List<SeckillPromotionTo> promotionToList = JSON.parseArray(pageUtils.getList().toString(),SeckillPromotionTo.class);
            if (StringUtils.isNotNull(promotionToList)) {
                Map<Long,String> map = new HashMap<Long,String>();
                List<Long> ids = promotionToList.stream().map(SeckillPromotionTo::getUserId).distinct().collect(Collectors.toList());
                if (ids.size() != 0) {
                    ids.forEach(id -> {
                        UserEntity userEntity = userService.getById(id);
                        map.put(id,userEntity.getUsername());
                    });
                    List<SeckillPromotionTo> newPromotionList = promotionToList.stream().map(seckillPromotionTo -> {
                        seckillPromotionTo.setUsername(map.get(seckillPromotionTo.getUserId()));
                        return seckillPromotionTo;
                    }).collect(Collectors.toList());
                    pageUtils.setList(newPromotionList);
                }
            }
            return pageUtils;
        }
        return null;
    }
}
