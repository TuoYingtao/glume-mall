package com.glume.glumemall.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 在线列表
 * @author tuoyingtao
 * @create 2022-04-06 11:27
 */
@Data
@TableName("sys_login")
public class LoginEntity {

    @TableId
    private Integer id;
    /**
     * 用户名
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
     * 登录时间
     */
    private Date createTime;
}
