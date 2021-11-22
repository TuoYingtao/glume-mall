package com.glume.common.core.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 满减信息
 * @author TuoYingtao
 * @create 2021-11-22 21:21
 */
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
