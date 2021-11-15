package com.glume.glumemall.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.product.dao.CategoryBrandRelationDao;
import com.glume.glumemall.product.entity.CategoryBrandRelationEntity;
import com.glume.glumemall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public void updateBrandName(Long brandId,String brandName) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandName(brandName);
        baseMapper.update(categoryBrandRelationEntity,new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
    }

    @Override
    public void updateCategoryName(Long catId, String categoryName) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setCatelogName(categoryName);
        baseMapper.updateCategoryName(catId,categoryName);
    }

    /**
     * 根据品牌ID做删除
     * @param brandIds
     */
    @Override
    public void removeBrandRelationById(List<Long> brandIds) {
        brandIds.forEach(brandId -> baseMapper.delete(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId)));
    }

    /**
     * 根据分类ID做删除
     * @param catIds
     */
    @Override
    public void removeCategoryRelationById(List<Long> catIds) {
        catIds.forEach(catId -> baseMapper.delete(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id",catId)));
    }


}