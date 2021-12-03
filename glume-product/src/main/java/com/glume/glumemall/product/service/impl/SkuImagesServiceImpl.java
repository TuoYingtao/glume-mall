package com.glume.glumemall.product.service.impl;

import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.product.dao.SkuImagesDao;
import com.glume.glumemall.product.entity.SkuImagesEntity;
import com.glume.glumemall.product.service.SkuImagesService;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuImagesEntity> page = this.page(
                new Query<SkuImagesEntity>().getPage(params),
                new QueryWrapper<SkuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuImage(List<SkuImagesEntity> skuImagesEntityList) {
        this.saveBatch(skuImagesEntityList);
    }

    @Override
    public List<SkuImagesEntity> getImagesBySkuId(Long skuId) {
        List<SkuImagesEntity> imagesEntities = baseMapper.selectList(new QueryWrapper<SkuImagesEntity>().eq("sku_id", skuId));
        return imagesEntities;
    }

}