package com.glume.glumemall.coupon.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.coupon.entity.SeckillPromotionEntity;
import com.glume.glumemall.coupon.entity.SeckillSessionEntity;
import com.glume.glumemall.coupon.feign.ProductFeignService;
import com.glume.glumemall.coupon.service.SeckillPromotionService;
import com.glume.glumemall.coupon.service.SeckillSessionService;
import com.glume.glumemall.coupon.to.SkuInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.coupon.dao.SeckillSkuRelationDao;
import com.glume.glumemall.coupon.entity.SeckillSkuRelationEntity;
import com.glume.glumemall.coupon.service.SeckillSkuRelationService;


@Service("seckillSkuRelationService")
public class SeckillSkuRelationServiceImpl extends ServiceImpl<SeckillSkuRelationDao, SeckillSkuRelationEntity> implements SeckillSkuRelationService {

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    SeckillPromotionService seckillPromotionService;

    @Autowired
    SeckillSessionService seckillSessionService;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SeckillSkuRelationEntity> wrapper = new QueryWrapper<>();
        String promotionId = (String) params.get("promotionId");
        String sessionId = (String) params.get("sessionId");
        String seckillPrice = (String) params.get("seckillPrice");
        String seckillLimit = (String) params.get("seckillLimit");
        if (StringUtils.isNotEmpty(promotionId)) {
            wrapper.eq("promotion_id",promotionId);
        }
        if (StringUtils.isNotEmpty(sessionId)) {
            wrapper.eq("promotion_session_id",sessionId);
        }
        if (StringUtils.isNotEmpty(seckillPrice)) {
            wrapper.eq("seckill_price", seckillPrice);
        }
        if (StringUtils.isNotEmpty(seckillLimit)) {
            wrapper.eq("seckill_limit",seckillLimit);
        }
        wrapper.orderBy(true,true,"promotion_id","promotion_session_id","seckill_sort");

        IPage<SeckillSkuRelationEntity> page = this.page(
                new Query<SeckillSkuRelationEntity>().getPage(params), wrapper);

        List<SeckillSkuRelationEntity> entityList = null;
        try {
            entityList = seckillSkuRelationHandler(page.getRecords());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        page.setRecords(entityList);

        return new PageUtils(page);
    }

    private List<SeckillSkuRelationEntity> seckillSkuRelationHandler(List<SeckillSkuRelationEntity> records) throws ExecutionException, InterruptedException {
        List<Long> promotionIds = records.stream().map(SeckillSkuRelationEntity::getPromotionId).distinct().collect(Collectors.toList());
        List<Long> sessionIds = records.stream().map(SeckillSkuRelationEntity::getPromotionSessionId).distinct().collect(Collectors.toList());
        List<Long> skuIds = records.stream().map(SeckillSkuRelationEntity::getSkuId).distinct().collect(Collectors.toList());

        Map<Long,String>  promotionMap = new HashMap<>();
        Map<Long,String> sessionMap = new HashMap<>();
        Map<Long,String> productMap = new HashMap<>();

        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
            promotionIds.forEach(id -> {
                SeckillPromotionEntity seckillPromotionEntity = seckillPromotionService.getById(id);
                promotionMap.put(id,seckillPromotionEntity.getTitle());
            });
        }, executor);

        CompletableFuture<Void> runAsync1 = CompletableFuture.runAsync(() -> {
            sessionIds.forEach(id -> {
                SeckillSessionEntity sessionEntity = seckillSessionService.getById(id);
                sessionMap.put(id,sessionEntity.getName());
            });
        }, executor);

        CompletableFuture<Void> runAsync2 = CompletableFuture.runAsync(() -> {
            Long[] ids = skuIds.toArray(new Long[0]);
            R result = productFeignService.infoList(ids);
            List<SkuInfoTo> resultData = result.getData(new TypeReference<List<SkuInfoTo>>() {});
            for (SkuInfoTo skuInfoTo : resultData) {
                productMap.put(skuInfoTo.getSkuId(), skuInfoTo.getSkuName());
            }
        }, executor);

        CompletableFuture.allOf(runAsync,runAsync1,runAsync2).get();

        records.stream().map(skuRelationEntity -> {
            skuRelationEntity.setPromotionName(promotionMap.get(skuRelationEntity.getPromotionId()));
            skuRelationEntity.setSkuTitle(productMap.get(skuRelationEntity.getSkuId()));
            skuRelationEntity.setPromotionSessionName(sessionMap.get(skuRelationEntity.getPromotionSessionId()));
            return skuRelationEntity;
        }).collect(Collectors.toList());

        return records;
    }

    @Override
    public Map<String, Object> promotionAdnSessionList() {
        Map<String, Object> map = new HashMap<>();
        List<SeckillPromotionEntity> promotionEntities = seckillPromotionService.list();
        List<SeckillSessionEntity> sessionEntities = seckillSessionService.list();
        map.put("promotions",promotionEntities);
        map.put("sessions",sessionEntities);
        return map;
    }

    /**
     * 批量删除：根据主题ID或场次ID作为条件
     * @param fieid 字段
     * @param ids id
     */
    @Override
    public void deleteBatchSkuRelation(String fieid, List<Long> ids) {
        baseMapper.deleteBatchSkuRelation(fieid,ids);
    }

    @Override
    public List<SeckillPromotionEntity> getLates3DaySeckill() {
        QueryWrapper<SeckillPromotionEntity> wrapper = new QueryWrapper<>();
        wrapper.between("start_time",startTime(),endTime());
        List<SeckillPromotionEntity> entities = seckillPromotionService.list(wrapper);
        if (StringUtils.isNotEmpty(entities)) {
            List<Long> promotionId = entities.stream().map(SeckillPromotionEntity::getId).collect(Collectors.toList());
            List<SeckillPromotionEntity> promotionSessionRelation = seckillPromotionSessionHanlder(promotionId);
            return promotionSessionRelation;
        }
        return null;
    }

    public List<SeckillPromotionEntity> seckillPromotionSessionHanlder(List<Long> ids) {
        List<SeckillPromotionEntity> promotionSessionRelation = baseMapper.selectPromotionSessionRelation(ids);
        return promotionSessionRelation;
    }

    private String startTime() {
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        String format = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return format;
    }

    private String endTime() {
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.MAX);
        String format = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return format;
    }
}
