package com.glume.glumemall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glume.glumemall.product.entity.SkuSaleAttrValueEntity;
import com.glume.glumemall.product.vo.item.ItemSaleAttrsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-11-21 19:54:42
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<ItemSaleAttrsVo> getSaleAttrsBySpuId(@Param("spuId") Long spuId);

    List<String> getSkuSaleAttrValueAsStringList(@Param("skuId") Long skuId);
}
