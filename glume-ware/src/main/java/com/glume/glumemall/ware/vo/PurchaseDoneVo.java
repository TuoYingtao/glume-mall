package com.glume.glumemall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 完成采购单
 * @author TuoYingtao
 * @create 2021-11-28 17:55
 */
@Data
public class PurchaseDoneVo {
    /**
     * 采购单ID
     */
    @NotNull(message = "采购单ID不能为空")
    private Long id;
    /**
     * 完成采购项集合
     */
    private List<PurchaseItemDoneVo> items;
}
