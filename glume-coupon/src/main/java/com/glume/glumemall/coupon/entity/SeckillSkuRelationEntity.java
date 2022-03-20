package com.glume.glumemall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 秒杀活动商品关联
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:13:53
 */
@Data
@TableName("sms_seckill_sku_relation")
public class SeckillSkuRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 活动id
	 */
	private Long promotionId;
	/**
	 * 活动名称
	 */
	@TableField(exist = false)
	private String promotionName;
	/**
	 * 活动场次id
	 */
	private Long promotionSessionId;
	/**
	 * 活动场次名称
	 */
	@TableField(exist = false)
	private String promotionSessionName;
	/**
	 * 商品id
	 */
	private Long skuId;
	/**
	 * 商品标题
	 */
	@TableField(exist = false)
	private String skuTitle;
	/**
	 * 秒杀价格
	 */
	private BigDecimal seckillPrice;
	/**
	 * 秒杀总量
	 */
	private BigDecimal seckillCount;
	/**
	 * 每人限购数量
	 */
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
