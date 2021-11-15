package com.glume.glumemall.product.service.impl;

import com.glume.common.core.exception.servlet.ServiceException;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.product.dao.BrandDao;
import com.glume.glumemall.product.entity.BrandEntity;
import com.glume.glumemall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotNull(params.get("key"))) {
            String key = params.get("key").toString();
            wrapper.and(obj -> {
                obj.eq("name",key)
                    .or().eq("brand_id",key)
                    .or().eq("first_letter",key)
                    .or().like("descript",key);
            });
        }
        if (StringUtils.isNotNull(params.get("name"))) {
            String name = params.get("name").toString();
            wrapper.like("name",name);
        }
        IPage<BrandEntity> page = this.page(new Query<BrandEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public BrandEntity getBrandById(Long brandId) {
        BrandEntity brandEntity = baseMapper.selectById(brandId);
        if (!StringUtils.isNotNull(brandEntity)) {
            throw new ServiceException("没有当前" + brandId + "信息.");
        }
        return brandEntity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBrandByIds(List<Long> brandId) {
        //TODO 1. 检测当前删除的品牌信息，是否被别的地方引用

        baseMapper.deleteBatchIds(brandId);
        categoryBrandRelationService.removeBrandRelationById(brandId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBrandById(BrandEntity brand) {
        baseMapper.updateById(brand);
        if (StringUtils.isNotEmpty(brand.getName())) {
            categoryBrandRelationService.updateBrandName(brand.getBrandId(), brand.getName());
        }
    }

}