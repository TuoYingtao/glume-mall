package com.glume.glumemall.ware.vo;

import lombok.Data;

/**
 * 完成采购项
 * @author TuoYingtao
 * @create 2021-11-28 17:57
 */
@Data
public class PurchaseItemDoneVo {
    /**
     * 采购项ID
     */
    private Long itemId;
    /**
     * 采购项状态
     */
    private Integer status;
    /**
     * 原因
     */
    private String describe;
}
