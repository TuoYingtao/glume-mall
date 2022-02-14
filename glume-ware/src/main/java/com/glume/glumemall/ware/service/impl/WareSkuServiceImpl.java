package com.glume.glumemall.ware.service.impl;

import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.core.exception.servlet.NoStockException;
import com.glume.glumemall.ware.feign.ProductFeignService;
import com.glume.glumemall.ware.vo.OrderItemVo;
import com.glume.glumemall.ware.vo.SkuHasStockVo;
import com.glume.glumemall.ware.vo.WareSkuLockVo;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.ware.dao.WareSkuDao;
import com.glume.glumemall.ware.entity.WareSkuEntity;
import com.glume.glumemall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WareSkuServiceImpl.class);

    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotNull(params.get("skuId"))) {
            wrapper.eq("sku_id",params.get("skuId")).or();
        }
        if (StringUtils.isNotNull(params.get("wareId"))) {
            wrapper.eq("ware_id",params.get("wareId"));
        }
        IPage<WareSkuEntity> page = this.page(new Query<WareSkuEntity>().getPage(params), wrapper);

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (!StringUtils.isNotNull(wareSkuEntities) || wareSkuEntities.size() == 0) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            // TODO 处理远程查询SKU名称异常，无需回滚事务
            try {
                R info = productFeignService.info(skuId);
                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");
                if (info.getCode() == 200) {
                    wareSkuEntity.setSkuName(data.get("skuName").toString());
                }
            } catch (Exception e) {
                LOGGER.warn("查询远程SKU名称失败！");
            }
            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.addStock(skuId,wareId,skuNum);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            Long count = baseMapper.getSkuStock(skuId);
            skuHasStockVo.setSkuId(skuId);
            skuHasStockVo.setHasStock(count == null ? false : count > 0);
            return skuHasStockVo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    @Transactional(rollbackFor = NoStockException.class)
    public Boolean orderSkuLockStock(WareSkuLockVo wareSkuLockVo) {
        List<OrderItemVo> locks = wareSkuLockVo.getLocks();
        List<SkuWareHasStock> wareHasStocks = locks.stream().map(item -> {
            SkuWareHasStock skuWareHasStock = new SkuWareHasStock();
            skuWareHasStock.setSkuId(item.getSkuId());
            skuWareHasStock.setNum(item.getCount());
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(item.getSkuId());
            skuWareHasStock.setWarId(wareIds);
            return skuWareHasStock;
        }).collect(Collectors.toList());
        for (SkuWareHasStock wareHasStock : wareHasStocks) {
            Long skuId = wareHasStock.getSkuId();
            List<Long> warIds = wareHasStock.getWarId();
            if (StringUtils.isEmpty(warIds) || warIds.size() == 0) {
                // 没有任何仓库有这个商品的库存
                throw new NoStockException(skuId);
            }
            // 开始锁定库存
            // 当前商品是否在某一个库存中成功锁定
            Boolean skuStocked = false;
            for (Long warId : warIds) {
                //成功返回-1，失败则是-0
                Long row = wareSkuDao.lockSkuStock(skuId,warId,wareHasStock.getNum());
                if (row == 1) {
                    // 当前仓库锁定成功，跳出循环；开始下一个商品的库存锁定
                    skuStocked = true;
                    break;
                } else {
                    // 当前仓库锁定失败，重试下一个仓库
                }
            }
            if (!skuStocked) {
                // 当前商品在所有仓库中都没有锁定成功
                throw new NoStockException(skuId);
            }
        }
        return true;
    }

    /**
     * 库存仓库信息
     * 用于查看当前 SKU 在那个仓库有库存
     */
    @Data
    class SkuWareHasStock {
        private Long skuId;
        private Integer num;
        private List<Long> warId;
    }

}
