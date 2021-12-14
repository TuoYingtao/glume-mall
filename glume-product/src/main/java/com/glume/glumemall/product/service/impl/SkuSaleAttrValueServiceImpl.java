package com.glume.glumemall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.product.dao.SkuSaleAttrValueDao;
import com.glume.glumemall.product.entity.SkuSaleAttrValueEntity;
import com.glume.glumemall.product.service.SkuSaleAttrValueService;
import com.glume.glumemall.product.vo.item.ItemSaleAttrsVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuSaleAttr(List<SkuSaleAttrValueEntity> attrValueEntities) {
        this.saveBatch(attrValueEntities);
    }

    @Override
    public List<ItemSaleAttrsVo> getSaleAttrsBySpuId(Long spuId) {
        List<ItemSaleAttrsVo> saleAttrsVos = baseMapper.getSaleAttrsBySpuId(spuId);
        return saleAttrsVos;
    }

    @Override
    public List<String> getSkuSaleAttrValueAsStringList(Long skuId) {
        return baseMapper.getSkuSaleAttrValueAsStringList(skuId);
    }

}