package com.glume.glumemall.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.product.dao.CategoryBrandRelationDao;
import com.glume.glumemall.product.entity.CategoryBrandRelationEntity;
import com.glume.glumemall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceimpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        boolean brandId = StringUtils.isNotNull(params.get("brandId"));
        boolean catelogId = StringUtils.isNotNull(params.get("catelogId"));
        if (brandId && catelogId) {
            wrapper.eq("brand_id",params.get("brandId"))
                    .and(obj -> obj.eq("catelog_id",params.get("catelogId")));
        } else {
            if (brandId) wrapper.eq("brand_id",params.get("brandId"));
            if (catelogId) wrapper.eq("catelog_id",params.get("catelogId"));
        }

        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),wrapper);

        return new PageUtils(page);
    }

}