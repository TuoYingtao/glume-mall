package com.glume.glumemall.ware.service.impl;

import com.glume.common.core.enums.WareConstant;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.ware.entity.PurchaseDetailEntity;
import com.glume.glumemall.ware.service.PurchaseDetailService;
import com.glume.glumemall.ware.service.WareSkuService;
import com.glume.glumemall.ware.vo.MergeVo;
import com.glume.glumemall.ware.vo.PurchaseDoneVo;
import com.glume.glumemall.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.ware.dao.PurchaseDao;
import com.glume.glumemall.ware.entity.PurchaseEntity;
import com.glume.glumemall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService detailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status",0).or().eq("status",1);
        IPage<PurchaseEntity> page = this.page(new Query<PurchaseEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        // 没有ID新建一个
        if (!StringUtils.isNotNull(purchaseId)) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        // TODO 确认采购单的状态为：0、1 才能合并
        List<Long> item = mergeVo.getItem();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> detailEntities = item.stream().map(aLong -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(aLong);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.RECEIVE.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        detailService.updateBatchById(detailEntities);
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        baseMapper.updateById(purchaseEntity);
    }

    @Override
    public void received(List<Long> ids) {
        // 1.确认当前采购单是新建或已分配状态
        List<PurchaseEntity> purchaseEntities = ids.stream().map(aLong -> {
                    PurchaseEntity purchaseEntity = baseMapper.selectById(aLong);
                    return purchaseEntity;
                }).filter(purchaseEntity ->
                        purchaseEntity.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode()
                                || purchaseEntity.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode())
                .map(purchaseEntity -> {
                    purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
                    purchaseEntity.setUpdateTime(new Date());
                    return purchaseEntity;
                }).collect(Collectors.toList());
        // 2.改变采购单的状态
        this.updateBatchById(purchaseEntities);
        // 3.改变采购项的状态
        purchaseEntities.forEach(purchaseEntity -> {
            List<PurchaseDetailEntity> purchaseDetailEntities = detailService.listDetaliByPurchaseId(purchaseEntity.getId());
            List<PurchaseDetailEntity> entityList = purchaseDetailEntities.stream().map(purchaseDetailEntity -> {
                PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                detailEntity.setId(purchaseEntity.getId());
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return detailEntity;
            }).collect(Collectors.toList());
            detailService.updateBatchById(entityList);
        });
    }

    @Override
    public void purchaseDone(PurchaseDoneVo purchaseDoneVo) {
        // 改变采购项状态
        Boolean flag = true;
        List<PurchaseDetailEntity> doneVoList = new ArrayList<>();
        for (PurchaseItemDoneVo item : purchaseDoneVo.getItems()) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                flag = false;
                detailEntity.setStatus(item.getStatus());
            } else {
                // 将成功采购进行入库
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                PurchaseDetailEntity detail = detailService.getById(item.getItemId());
                wareSkuService.addStock(detail.getSkuId(),detail.getWareId(),detail.getSkuNum());
            }
            detailEntity.setId(item.getItemId());
            doneVoList.add(detailEntity);
        }
        detailService.updateBatchById(doneVoList);

        // 改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseDoneVo.getId());
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode()
            : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());

    }

}