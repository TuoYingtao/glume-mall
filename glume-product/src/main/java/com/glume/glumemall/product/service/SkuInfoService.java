package com.glume.glumemall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.product.entity.SkuInfoEntity;
import com.glume.glumemall.product.vo.item.SkuItemVo;

import java.util.Map;

/**
 * sku信息
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 13:36:55
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    SkuItemVo item(Long skuId);
}

