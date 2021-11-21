package com.glume.glumemall.product.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.product.entity.SkuSaleAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-11-21 19:54:42
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuSaleAttr(List<SkuSaleAttrValueEntity> attrValueEntities);
}

