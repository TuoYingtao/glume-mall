package com.glume.glumemall.ware.exception;

/**
 * 没有库存异常
 * @author tuoyingtao
 * @create 2022-02-14 15:59
 */
public class NoStockException extends RuntimeException {
    private Long skuId;

    public NoStockException(Long skuId) {
        super("商品ID:" + skuId + "；没有足够的库存了");
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
