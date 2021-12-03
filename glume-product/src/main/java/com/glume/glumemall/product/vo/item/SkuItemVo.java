package com.glume.glumemall.product.vo.item;

import com.glume.glumemall.product.entity.SkuImagesEntity;
import com.glume.glumemall.product.entity.SkuInfoEntity;
import com.glume.glumemall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * 商品详情
 *
 * @author tuoyingtao
 * @create 2021-12-03 9:54
 */
@Data
public class SkuItemVo {
    /**
     * sku基本信息
     */
    SkuInfoEntity info;
    /**
     * 是否有库存
     */
    Boolean hasStock = true;
    /**
     * sku图片信息
     */
    List<SkuImagesEntity> images;
    /**
     * spu销售属性组合
     */
    List<ItemSaleAttrsVo> saleAttr;
    /**
     * spu介绍
     */
    SpuInfoDescEntity desp;

    /**
     * spu规格参数
     */
    List<SpuItemAttrGroupVo> groupAttrs;

}
