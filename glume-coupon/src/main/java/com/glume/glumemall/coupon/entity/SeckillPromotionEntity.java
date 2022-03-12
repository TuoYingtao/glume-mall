package com.glume.glumemall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.glume.common.core.annotation.valid.AddGroup;
import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.valid.UpdateGroup;
import io.swagger.annotations.Api;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;

/**
 * 秒杀活动
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:13:53
 */
@Data
@TableName("sms_seckill_promotion")
public class SeckillPromotionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	@NotNull(message = "id不能为空",groups = {IDGroup.class, UpdateGroup.class})
	private Long id;
	/**
	 * 活动标题
	 */
	@NotBlank(message = "活动标题不能为空")
	private String title;
	/**
	 * 开始日期
	 */
	private Date startTime;
	/**
	 * 结束日期
	 */
	private Date endTime;
	/**
	 * 上下线状态
	 */
	@NotNull(message = "上下线状态不能为空")
	private Integer status;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	/**
	 * 创建人
	 */
	private Long userId;
	/**
	 * 创建人用户名
	 */
	@TableField(exist = false)
	private String username;
	/**
	 * 活动日期范围
	 */
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotEmpty(message = "活动日期范围不能为空")
	@Size(message = "活动日期范围格式不正确",min = 1,max = 2)
	@Future(message = "活动日期范围不能小于今日",groups = AddGroup.class)
	private Date[] dateTime;

}
