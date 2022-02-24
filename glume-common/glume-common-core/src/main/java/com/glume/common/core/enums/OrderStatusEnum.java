package com.glume.common.core.enums;

/**
 * 订单状态枚举
 * @author tuoyingtao
 * @create 2022-02-12 17:41
 */
public enum OrderStatusEnum {

    CREATE_NEW(0,"待付款"),

    PAYED(1,"已付款"),

    SENDED(2,"已发货"),

    RECIEVED(3,"已完成"),

    CANCLED(4,"已取消"),

    SERVICING(5,"售后中"),

    SERVICED(6,"售后完成");

    private Integer code;

    private String msg;

    OrderStatusEnum(Integer code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
