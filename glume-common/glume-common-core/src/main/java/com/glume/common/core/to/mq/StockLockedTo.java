package com.glume.common.core.to.mq;

import lombok.Data;

/**
 * 库存工作单To
 * @author tuoyingtao
 * @create 2022-02-23 18:23
 */
@Data
public class StockLockedTo {

    /**
     * 库存工作单ID
     */
    private Long id;
    /**
     * 库存工作单详情
     */
    private StockDetailTo stockDetailTo;
}
