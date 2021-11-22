package com.glume.glumemall.product.service.impl;

import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.product.entity.SpuInfoEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.product.dao.SkuInfoDao;
import com.glume.glumemall.product.entity.SkuInfoEntity;
import com.glume.glumemall.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params),wrapper);
        if (StringUtils.isNotNull(params.get("key"))) {
            String key = params.get("key").toString();
            wrapper.and(w -> {
                w.eq("sku_id",key).or().like("sku_name",key);
            });
        }
        if (StringUtils.isNotNull(params.get("brandId")) && !"0".equalsIgnoreCase(params.get("brandId").toString())) {
            String brandId = params.get("brandId").toString();
            wrapper.eq("brand_id",brandId);
        }
        if (StringUtils.isNotNull(params.get("catelogId")) && !"0".equalsIgnoreCase(params.get("catelogId").toString())) {
            String catelogId = params.get("catelogId").toString();
            wrapper.eq("catelog_id",catelogId);
        }
        if (StringUtils.isNotNull(params.get("min"))) {
            String min = params.get("min").toString();
            wrapper.ge("price",min);
        }
        if (StringUtils.isNotNull(params.get("max"))) {
            try {
                String max = params.get("max").toString();
                BigDecimal bigDecimal = new BigDecimal(max);
                if (bigDecimal.compareTo(new BigDecimal("0")) == 1) {
                    wrapper.le("price",max);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        baseMapper.insert(skuInfoEntity);
    }

}