package com.glume.glumemall.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.product.entity.CategoryBrandRelationEntity;

import java.util.Map;

/**
 * 品牌&分类关联
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 13:36:55
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateBrandName(Long brandId,String brandName);

    void updateCategoryName(Long catId,String categoryName);
}

