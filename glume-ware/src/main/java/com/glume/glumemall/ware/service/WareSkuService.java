package com.glume.glumemall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.core.to.mq.StockLockedTo;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.ware.entity.WareSkuEntity;
import com.glume.glumemall.ware.vo.SkuHasStockVo;
import com.glume.glumemall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:42:52
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);

    Boolean orderSkuLockStock(WareSkuLockVo wareSkuLockVo);

    void handlerStockLockRelease(StockLockedTo stockLockedTo);
}

