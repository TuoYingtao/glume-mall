package com.glume.glumemall.admin.to;

import com.baomidou.mybatisplus.annotation.TableField;
import com.glume.common.core.annotation.valid.IDGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 秒杀活动商品关联To
 * @author TuoYingtao
 * @create 2022-03-17 14:37
 */
@Data
public class SeckillRelationTo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "活动关联品牌ID不能为空",groups = IDGroup.class)
    private Long id;
    /**
     * 活动id
     */
    @NotNull(message = "活动id不能为空")
    private Long promotionId;
    /**
     * 活动名称
     */
    private String promotionName;
    /**
     * 活动场次id
     */
    @NotNull(message = "活动场次id不能为空")
    private Long promotionSessionId;
    /**
     * 活动场次名称
     */
    private String promotionSessionName;
    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    private Long skuId;
    /**
     * 商品标题
     */
    private String skuTitle;
    /**
     * 秒杀价格
     */
    @NotNull(message = "秒杀价格不能为空")
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    @NotNull(message = "秒杀总量不能为空")
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    @NotNull(message = "每人限购数量不能为空")
    private BigDecimal seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

    public BigDecimal getSeckillPrice() {
        return seckillPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public void setSeckillPrice(BigDecimal seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public BigDecimal getSeckillCount() {
        return seckillCount.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public void setSeckillCount(BigDecimal seckillCount) {
        this.seckillCount = seckillCount;
    }

    public BigDecimal getSeckillLimit() {
        return seckillLimit.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public void setSeckillLimit(BigDecimal seckillLimit) {
        this.seckillLimit = seckillLimit;
    }
}
