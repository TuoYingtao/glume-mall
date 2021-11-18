/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.glume.common.core.constant;

/**
 * 常量
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Constants {

	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;

    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 当前页码
     */
    public static final String PAGE = "page";

    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";

    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";

    /**
     * 排序方式
     */
    public static final String ORDER = "order";

    /**
     *  升序
     */
    public static final String ASC = "asc";

    /**
     * 菜单类型
     */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG('M'),
        /**
         * 菜单
         */
        MENU('C'),
        /**
         * 按钮
         */
        BUTTON('F');

        private Character value;

        MenuType(Character value) {
            this.value = value;
        }

        public Character getValue() {
            return value;
        }
    }

    /**
     * 属性类型
     */
    public enum AttrType {

        MARKET_ATTR_TYPE(0, "销售属性"),

        BASE_ATTR_TYPE(1, "基本属性"),

        MARKET_BASE_TYPE(2,"既是销售属性又是基本属性");

        private Integer value;

        private String msg;

        AttrType(Integer value,String msg) {
            this.value = value;
            this.msg = msg;
        }

        public Integer getValue() {
            return value;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 定时任务状态
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
