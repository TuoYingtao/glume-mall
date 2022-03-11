package com.glume.glumemall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Null;

/**
 * 秒杀活动场次
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:13:53
 */
@Data
@TableName("sms_seckill_session")
public class SeckillSessionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 场次名称
	 */
	private String name;
	/**
	 * 每日开始时间
	 */
	@JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
	private Date startTime;
	/**
	 * 每日结束时间
	 */
	@JsonFormat(pattern = "HH:mm:ss",timezone = "GMT+8")
	private Date endTime;
	/**
	 * 启用状态
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 活动时间范围
	 */
	@TableField(exist = false)
	@JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "HH:mm:ss")
	private Date[] dateTime;
	/**
	 * 所有优惠秒杀商品
	 */
	@TableField(exist = false)
	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private List<SeckillSkuRelationEntity> relationSkus;
}
