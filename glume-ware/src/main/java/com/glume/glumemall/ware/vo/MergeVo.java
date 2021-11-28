package com.glume.glumemall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * 整合采购单
 * @author TuoYingtao
 * @create 2021-11-28 16:46
 */
@Data
public class MergeVo {
    /**
     * 整单ID
     */
    private Long purchaseId;
    /**
     * 合并项整合
     */
    private List<Long> item;
}
