package com.glume.glumemall.order.vo;

import lombok.Data;

/**
 * Sku库存信息
 * @author tuoyingtao
 * @create 2022-02-12 10:16
 */
@Data
public class SkuHasStockVo {

    private Long skuId;

    private Boolean hasStock;
}
