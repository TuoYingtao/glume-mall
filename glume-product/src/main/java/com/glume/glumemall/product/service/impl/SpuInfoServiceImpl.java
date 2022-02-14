package com.glume.glumemall.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.to.SkuReductionTo;
import com.glume.common.core.to.SpuBoundsTo;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.product.dao.SpuInfoDao;
import com.glume.glumemall.product.entity.*;
import com.glume.glumemall.product.feign.CouponFeignService;
import com.glume.glumemall.product.service.*;
import com.glume.glumemall.product.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpuInfoServiceImpl.class);

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotNull(params.get("key"))) {
            String key = params.get("key").toString();
            wrapper.and(w -> {
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        if (StringUtils.isNotNull(params.get("status"))) {
            String status = params.get("status").toString();
            wrapper.eq("publish_status",status);
        }
        if (StringUtils.isNotNull(params.get("brandId")) && !"0".equalsIgnoreCase(params.get("brandId").toString())) {
            String brandId = params.get("brandId").toString();
            wrapper.eq("brand_id",brandId);
        }
        if (StringUtils.isNotNull(params.get("catelogId")) && !"0".equalsIgnoreCase(params.get("catelogId").toString())) {
            String catelogId = params.get("catelogId").toString();
            wrapper.eq("catelog_id",catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(new Query<SpuInfoEntity>().getPage(params),wrapper);

        return new PageUtils(page);
    }

    // TODO 事务回滚、远程调用超时、远程服务异常待处理
    @Override
    @Transactional
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        // 1.保存SPU基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        // 2.保存SPU描述图片 pms_spu_info_desc
        List<String> decript = spuSaveVo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        // 3.保存SPU的图片集 pms_spu_images
        List<String> images = spuSaveVo.getImages();
        spuImagesService.saveSpuImages(spuInfoEntity.getId(),images);

        // 4.保存SPU的规格参数 pms_sku_sale_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> valueEntities = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(attr.getAttrId());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttrValueBatch(valueEntities);

        // 5.保存SPU的积分信息 glumemall_sms -> sms_sku_bounds
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds,spuBoundsTo);
        spuBoundsTo.setSpuId(spuInfoEntity.getId());
        R spuBounds = couponFeignService.saveSpuBounds(spuBoundsTo);
        if (spuBounds.getCode() != 200) {
            LOGGER.error("远程保存SPU积分信息失败！");
        }

        // 5.保存当前对应的所有SKU信息
        // 5.1 保存SKU信息 pms_sku_info
        List<Skus> skus = spuSaveVo.getSkus();
        if (StringUtils.isNotEmpty(skus) && skus.size() != 0) {
            skus.forEach(item -> {
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuSubtitle(spuInfoEntity.getBrandName());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);

                // 5.2 保存SKU图片信息 pms_sku_images
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> skuImagesEntityList = item.getImages().stream().map(images1 -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setDefaultImg(skuImagesEntity.getDefaultImg());
                    skuImagesEntity.setImgUrl(skuImagesEntity.getImgUrl());
                    return skuImagesEntity;
                }).filter(f -> !StringUtils.isEmpty(f.getImgUrl())).collect(Collectors.toList());
                skuImagesService.saveSkuImage(skuImagesEntityList);

                // 5.3 保存SKU销售属性信息 pms_sku_sale_attr_value
                List<SkuSaleAttrValueEntity> attrValueEntities = item.getAttr().stream().map(skuSaleAttr -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(skuSaleAttr, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setAttrId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveSkuSaleAttr(attrValueEntities);

                // 5.4 保存SKU优惠、满减信息 glumemall_sms -> sms_sku_ladder -> sms_sku_full_reduction
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0 ||
                        skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                    R reduction = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (reduction.getCode() != 200) {
                        LOGGER.error("远程保存SKU优惠信息失败！");
                    }
                }
            });
        }
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        SkuInfoEntity skuInfoEntity = skuInfoService.getById(skuId);
        SpuInfoEntity spuInfoEntity = this.getById(skuInfoEntity.getSpuId());
        return spuInfoEntity;
    }

}
