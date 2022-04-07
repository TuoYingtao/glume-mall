package com.glume.glumemall.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 黑名单表
 * @author tuoyingtao
 * @create 2022-04-06 11:34
 */
@Data
@TableName("sys_blacklist")
public class BlackListEntity {

    @TableId
    private Integer id;
    /**
     * 踢出者ID
     */
    private Integer kickOutId;
    /**
     * 被踢出用户名
     */
    private String username;
    /**
     * 用户令牌
     */
    private String token;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 被提出的时间
     */
    private Date loginOutTime;
}
