package com.glume.glumemall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;

import com.glume.common.core.annotation.Logic;
import com.glume.common.core.annotation.valid.AddGroup;
import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.valid.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 会员等级
 * 
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:23:38
 */
@Data
@TableName("ums_member_level")
public class MemberLevelEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	@NotNull(message = "等级ID不能为空",
			groups = {UpdateGroup.class, IDGroup.class})
	@Null(message = "不能指定ID值",groups = AddGroup.class)
	private Long id;
	/**
	 * 等级名称
	 */
	@NotBlank(message = "等级名称不能为空",groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * 等级需要的成长值
	 */
	@NotNull(message = "等级需要的成长值不能为空",
			groups = {AddGroup.class, UpdateGroup.class})
	@Min(value = 0,message = "等级需要的成长值不能小于0",
			groups = {AddGroup.class,UpdateGroup.class})
	private Integer growthPoint;
	/**
	 * 是否为默认等级[0->不是；1->是]
	 */
	@NotNull(message = "是否为默认等级不能为空",
			groups = {AddGroup.class, UpdateGroup.class})
	@Logic(vals = {0,1}, message = "是否为默认等级[0->不是；1->是]",
			groups = {AddGroup.class, UpdateGroup.class})
	private Integer defaultStatus;
	/**
	 * 免运费标准
	 */
	@NotNull(message = "免运费标准不能为空",
			groups = {AddGroup.class, UpdateGroup.class})
	@Min(value = 0,message = "免运费标准不能小于0")
	private BigDecimal freeFreightPoint;
	/**
	 * 每次评价获取的成长值
	 */
	@NotNull(message = "每次评价获取的成长值不能为空",
			groups = {AddGroup.class, UpdateGroup.class})
	@Min(value = 0,message = "每次评价获取的成长值不能小于0")
	private Integer commentGrowthPoint;
	/**
	 * 是否有免邮特权[0->没有；1->有]
	 */
	@NotNull(message = "是否有免邮特权不能为空",
			groups = {AddGroup.class, UpdateGroup.class})
	@Logic(vals = {0,1}, message = "是否有免邮特权[0->没有；1->有]",
			groups = {AddGroup.class, UpdateGroup.class})
	private Integer priviledgeFreeFreight;
	/**
	 * 是否有会员价格特权[0->没有；1->有]
	 */
	@NotNull(message = "是否有会员价格特权不能为空",
			groups = {AddGroup.class, UpdateGroup.class})
	@Logic(vals = {0,1}, message = "是否有会员价格特权[0->没有；1->有]",
			groups = {AddGroup.class, UpdateGroup.class})
	private Integer priviledgeMemberPrice;
	/**
	 * 是否有生日特权[0->没有；1->有]
	 */
	@NotNull(message = "是否有生日特权不能为空",
			groups = {AddGroup.class, UpdateGroup.class})
	@Logic(vals = {0,1}, message = "是否有生日特权[0->没有；1->有]",
			groups = {AddGroup.class, UpdateGroup.class})
	private Integer priviledgeBirthday;
	/**
	 * 备注
	 */
	private String note;

}
