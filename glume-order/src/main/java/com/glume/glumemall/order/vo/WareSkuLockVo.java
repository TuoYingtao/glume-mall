package com.glume.glumemall.order.vo;

import lombok.Data;

import java.util.List;

/**
 * 库存锁定Vo
 * @author tuoyingtao
 * @create 2022-02-14 15:15
 */
@Data
public class WareSkuLockVo {

    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 需要锁住的库存信息
     */
    private List<OrderItemVo> locks;
}
