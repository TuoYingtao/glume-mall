package com.glume.glumemall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 13:36:55
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> catIds);
}

