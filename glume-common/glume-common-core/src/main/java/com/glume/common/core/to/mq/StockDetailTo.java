package com.glume.common.core.to.mq;

import lombok.Data;

/**
 * 库存工作单详情To
 * @author tuoyingtao
 * @create 2022-02-24 9:35
 */
@Data
public class StockDetailTo {

    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * 购买个数
     */
    private Integer skuNum;
    /**
     * 工作单id
     */
    private Long taskId;
    /**
     * 仓库id
     */
    private Long wareId;
    /**
     * 1-锁定  2-解锁  3-扣减
     */
    private Long lockStatus;
}
