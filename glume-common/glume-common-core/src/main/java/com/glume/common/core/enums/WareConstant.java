package com.glume.common.core.enums;

/**
 * @author TuoYingtao
 * @create 2021-11-28 16:57
 */
public class WareConstant {
    public enum PurchaseStatusEnum {
        CREATED(0,"新建"),
        ASSIGNED(1,"乙分配"),
        RECEIVE(2,"已领取"),
        FINISH(3,"已完成"),
        HASERROR(4,"有异常");

        private Integer code;
        private String msg;

        private PurchaseStatusEnum(Integer code,String msg){
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

    public enum PurchaseDetailStatusEnum {
        CREATED(0,"新建"),
        BUYING(1,"正在采购"),
        RECEIVE(2,"已分配"),
        FINISH(3,"已完成"),
        HASERROR(4,"采购失败");

        private Integer code;
        private String msg;

        private PurchaseDetailStatusEnum(Integer code,String msg){
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
}
