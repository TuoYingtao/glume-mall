package com.glume.glumemall.coupon.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.exception.servlet.FeignException;
import com.glume.common.core.utils.JwtUtils;
import com.glume.common.core.utils.R;
import com.glume.glumemall.coupon.feign.AdminFeignService;
import com.glume.glumemall.coupon.to.AdminUserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.coupon.dao.SeckillPromotionDao;
import com.glume.glumemall.coupon.entity.SeckillPromotionEntity;
import com.glume.glumemall.coupon.service.SeckillPromotionService;

import javax.servlet.http.HttpServletRequest;


@Service("seckillPromotionService")
public class SeckillPromotionServiceImpl extends ServiceImpl<SeckillPromotionDao, SeckillPromotionEntity> implements SeckillPromotionService {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AdminFeignService adminFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillPromotionEntity> page = this.page(
                new Query<SeckillPromotionEntity>().getPage(params),
                new QueryWrapper<SeckillPromotionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSeckillPromotion(SeckillPromotionEntity seckillPromotion, HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String userNameFromToken = jwtUtils.getUserNameFromToken(token);
        R result = adminFeignService.getByUserDetail(userNameFromToken);
        if (result.getCode() == HttpStatus.SUCCESS) {
            AdminUserTo adminUserTo = result.getData(new TypeReference<AdminUserTo>() {
            });
            seckillPromotion.setUsername(userNameFromToken);
            seckillPromotion.setUserId(adminUserTo.getUserId());
            seckillPromotion.setCreateTime(new Date());
            seckillPromotion.setStartTime(seckillPromotion.getDateTime()[0]);
            seckillPromotion.setEndTime(seckillPromotion.getDateTime()[1]);
            baseMapper.insert(seckillPromotion);
        } else {
            throw new FeignException(result.getCode(),"网络波动，秒杀活动主题保存失败！",result.get("msg").toString());
        }
    }

}
