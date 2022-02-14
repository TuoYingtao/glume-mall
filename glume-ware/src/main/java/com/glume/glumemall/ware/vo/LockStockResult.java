package com.glume.glumemall.ware.vo;

import lombok.Data;

/**
 * 库存锁定结果信息
 * @author tuoyingtao
 * @create 2022-02-14 15:25
 */
@Data
public class LockStockResult {

    public Long skuId;
    /**
     * 几件商品
     */
    private Integer num;
    /**
     * 是否锁定成功
     */
    private Boolean locked;
}
