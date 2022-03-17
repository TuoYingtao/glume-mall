package com.glume.glumemall.admin.to;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.glume.common.core.annotation.valid.AddGroup;
import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.annotation.valid.UpdateGroup;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 秒杀活动主题To
 * @author TuoYingtao
 * @create 2022-03-14 14:23
 */
@Data
public class SeckillPromotionTo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
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
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;
    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
    /**
     * 上下线状态 0-禁用 1-启用
     */
    @NotNull(message = "上下线状态不能为空:0-禁用 1-启用")
    private Integer status;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 创建人
     */
    private Long userId;
    /**
     * 创建人用户名
     */
    private String username;
    /**
     * 活动日期范围
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotEmpty(message = "活动日期范围不能为空")
    private Date[] dateTime;
}
