package com.glume.common.core.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 积分信息
 * @author TuoYingtao
 * @create 2021-11-22 21:12
 */
@Data
public class SpuBoundsTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;

}
