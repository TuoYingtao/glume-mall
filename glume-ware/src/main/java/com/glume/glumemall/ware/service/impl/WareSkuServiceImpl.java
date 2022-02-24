package com.glume.glumemall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.enums.OrderStatusEnum;
import com.glume.common.core.to.mq.StockDetailTo;
import com.glume.common.core.to.mq.StockLockedTo;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.common.core.exception.servlet.NoStockException;
import com.glume.glumemall.ware.entity.WareOrderTaskDetailEntity;
import com.glume.glumemall.ware.entity.WareOrderTaskEntity;
import com.glume.glumemall.ware.feign.OrderFeignService;
import com.glume.glumemall.ware.feign.ProductFeignService;
import com.glume.glumemall.ware.service.WareOrderTaskDetailService;
import com.glume.common.core.to.mq.OrderTo;
import com.glume.glumemall.ware.vo.OrderItemVo;
import com.glume.glumemall.ware.vo.SkuHasStockVo;
import com.glume.glumemall.ware.vo.WareSkuLockVo;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
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

    @Autowired
    OrderFeignService orderFeignService;

    @Autowired
    WareOrderTaskServiceImpl wareOrderTaskService;

    @Autowired
    WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    RabbitTemplate rabbitTemplate;

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
        // 库存工作单
        WareOrderTaskEntity wareOrderTaskEntity = saveWareOrderTaskEntity(wareSkuLockVo);

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
                    // 库存工作单
                    WareOrderTaskDetailEntity wareOrderTaskDetailEntity = new WareOrderTaskDetailEntity();
                    wareOrderTaskDetailEntity.setSkuId(skuId);
                    wareOrderTaskDetailEntity.setSkuNum(wareHasStock.getNum());
                    wareOrderTaskDetailEntity.setTaskId(wareOrderTaskEntity.getId());
                    wareOrderTaskDetailEntity.setWareId(warId);
                    wareOrderTaskDetailEntity.setLockStatus(1);
                    wareOrderTaskDetailService.save(wareOrderTaskDetailEntity);
                    // 发送MQ
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    stockLockedTo.setId(wareOrderTaskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(wareOrderTaskDetailEntity,stockDetailTo);
                    stockLockedTo.setStockDetailTo(stockDetailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked",stockLockedTo);
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
     * 库存工作单保存
     */
    private WareOrderTaskEntity saveWareOrderTaskEntity(WareSkuLockVo wareSkuLockVo) {
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(wareSkuLockVo.getOrderSn());
        wareOrderTaskService.save(wareOrderTaskEntity);
        return wareOrderTaskEntity;
    }

    @Override
    public void handlerStockLockRelease(StockLockedTo stockLockedTo) {
        // 解锁库存：首先查询是否有库存锁定数据；避免库存回滚，没有库存锁定数据，这种情况下无需解锁。
        StockDetailTo stockDetailTo = stockLockedTo.getStockDetailTo();
        WareOrderTaskDetailEntity byId = wareOrderTaskDetailService.getById(stockDetailTo.getId());
        if (StringUtils.isNotNull(byId)) {
            // 开始解锁：解锁前要查询订单；如果没有，必须解锁。若有这个订单，则查看订单状态（已取消-解锁库存  没取消-不能解锁库存）
            WareOrderTaskEntity taskEntity = wareOrderTaskService.getById(stockLockedTo.getId());
            R result = orderFeignService.getOrderStatus(taskEntity.getOrderSn());
            if (result.getCode() == 200) {
                OrderTo resultData = result.getData(new TypeReference<OrderTo>() {});
                if (StringUtils.isNull(resultData.getOrderSn()) ||  resultData.getStatus() == OrderStatusEnum.CANCLED.getCode()) {
                    // 只有库存工作单详情状态为锁定时，才能进行解锁
                    if (byId.getLockStatus() == 1) {
                        // 订单不存在或已经取消，开始解锁
                        unLockStock(byId.getId(),stockDetailTo.getSkuId(),stockDetailTo.getWareId(),stockDetailTo.getSkuNum());
                    }
                }
            } else {
                throw new RuntimeException("远程服务调用失败...");
            }
        } else { /* 无需解锁 */ }
    }

    /**
     * 订单关闭主动解锁库存，
     * 防止订单服务卡顿导致订单状态消息一直改不了，而库存消息优先到期。此时查看订单状态为新建，然后什么都不做就走了，
     * 最终导致卡顿的订单，永远不能解锁
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockStockOrder(OrderTo order) {
        WareOrderTaskEntity wareOrderTaskEntity = wareOrderTaskService.getOrderTaskByOrderSn(order.getOrderSn());
        List<WareOrderTaskDetailEntity> wareOrderTaskDetailEntities = wareOrderTaskDetailService.list(
                new QueryWrapper<WareOrderTaskDetailEntity>()
                        .eq("task_id", wareOrderTaskEntity.getId())
                        .eq("lock_status", 1));
        for (WareOrderTaskDetailEntity item : wareOrderTaskDetailEntities) {
            unLockStock(item.getId(), item.getSkuId(), item.getWareId(), item.getSkuNum());
        }
    }

    /**
     * 订单库存解锁
     */
    public void unLockStock(Long taskDetailId, Long skuId, Long wareId, Integer skuNum) {
        wareSkuDao.unLockStock(skuId,wareId,skuNum);
        // 修改库存工作单状态
        WareOrderTaskDetailEntity wareOrderTaskDetailEntity = new WareOrderTaskDetailEntity();
        wareOrderTaskDetailEntity.setId(taskDetailId);
        wareOrderTaskDetailEntity.setLockStatus(2); // 已解锁
        wareOrderTaskDetailService.updateById(wareOrderTaskDetailEntity);
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
