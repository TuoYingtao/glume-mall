package com.glume.glumemall.ware.service.impl;

import com.glume.common.core.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.ware.dao.PurchaseDetailDao;
import com.glume.glumemall.ware.entity.PurchaseDetailEntity;
import com.glume.glumemall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotNull(params.get("key"))) {
            String key = params.get("key").toString();
            wrapper.and(wr -> {
                wr.eq("purchase_id",key)
                        .or().eq("sku_id",key)
                        .or().eq("sku_name",key)
                        .or().eq("sku_price",key);
            });
        }
        if (StringUtils.isNotNull("status")){
            wrapper.eq("status",params.get("status"));
        }
        if (StringUtils.isNotNull("wareId")) {
            wrapper.eq("ware_id",params.get("wareId"));
        }
        IPage<PurchaseDetailEntity> page = this.page(new Query<PurchaseDetailEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> listDetaliByPurchaseId(Long id) {

        List<PurchaseDetailEntity> detailEntities = this.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id));
        return detailEntities;
    }

}