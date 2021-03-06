package com.glume.glumemall.product.service.impl;


import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.product.dao.ProductAttrValueDao;
import com.glume.glumemall.product.entity.ProductAttrValueEntity;
import com.glume.glumemall.product.service.ProductAttrValueService;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveProductAttrValueBatch(List<ProductAttrValueEntity> valueEntities) {
        this.saveBatch(valueEntities);
    }

    @Override
    public List<ProductAttrValueEntity> baseAttrListforSPU(Long spuId) {
        List<ProductAttrValueEntity> entityList = baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("sup_id", spuId));
        return entityList;
    }

}