package com.glume.glumemall.coupon.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.JwtUtils;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.coupon.entity.SeckillSkuRelationEntity;
import com.glume.glumemall.coupon.service.SeckillSkuRelationService;
import com.glume.glumemall.coupon.to.AdminUserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.coupon.dao.SeckillPromotionDao;
import com.glume.glumemall.coupon.entity.SeckillPromotionEntity;
import com.glume.glumemall.coupon.service.SeckillPromotionService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;


@Service("seckillPromotionService")
public class SeckillPromotionServiceImpl extends ServiceImpl<SeckillPromotionDao, SeckillPromotionEntity> implements SeckillPromotionService {

    @Autowired
    SeckillSkuRelationService seckillSkuRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SeckillPromotionEntity> wrapper = new QueryWrapper<>();
        String title = (String) params.get("title");
        String status = (String) params.get("status");
        String dateTime = (String) params.get("dateTime");
        if (StringUtils.isNotEmpty(title)) {
            wrapper.like("title",title);
        }
        if (StringUtils.isNotEmpty(status)) {
            wrapper.eq("status", status);
        }
        if (StringUtils.isNotEmpty(dateTime)) {
            String[] time = dateTime.split(",");
            wrapper.between("start_time",time[0],time[1]);
        }
        IPage<SeckillPromotionEntity> page = this.page(new Query<SeckillPromotionEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public void saveSeckillPromotion(SeckillPromotionEntity seckillPromotion) {
        baseMapper.insert(seckillPromotion);
    }

    @Override
    @Transactional
    public void removePromotionById(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        seckillSkuRelationService.deleteBatchSkuRelation("promotion_id",ids);
    }

}
