package com.glume.glumemall.order.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 支付宝-支付异步通知参数实体Vo
 * @author tuoyingtao
 * @create 2022-2-28 15:24
 */
@ToString
@Data
public class PayAsyncVo {

    /**
     * 该笔交易创建的时间。格式为 yyyy-MM-dd HH:mm:ss。
     */
    private String gmt_create;
    /**
     * 编码格式，如 utf-8、gbk、gb2312 等。
     */
    private String charset;
    /**
     * 该笔交易 的买家付款时间。格式为 yyyy-MM-dd HH:mm:ss。
     */
    private String gmt_payment;
    /**
     * 通知的发送时间。格式为 yyyy-MM-dd HH:mm:ss。
     */
    private Date notify_time;
    /**
     * 商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来。
     */
    private String subject;
    /**
     * 签名。详见下文 异步返回结果的验签。
     */
    private String sign;
    /**
     * 买家支付宝账号对应的支付宝唯一用户号。以 2088 开头的纯 16 位数字。
     */
    private String buyer_id;
    /**
     * 订单的备注、描述、明细等。对应请求时的 body 参数，原样通知回来。
     */
    private String body;
    /**
     * 用户在交易中支付的可开发票的金额。
     */
    private String invoice_amount;
    /**
     * 调用的接口版本，固定为：1.0。
     */
    private String version;
    /**
     * 通知校验 ID。
     */
    private String notify_id;
    /**
     * 支付成功的各个渠道金额信息
     */
    private String fund_bill_list;
    /**
     * 通知的类型。trade_status_sync
     */
    private String notify_type;
    /**
     * 原支付请求的商户订单号。
     */
    private String out_trade_no;
    /**
     * 本次交易支付的订单金额，单位为人民币（元）。
     */
    private String total_amount;
    /**
     * 交易目前所处的状态。 TRADE_SUCCESS
     */
    private String trade_status;
    /**
     * 支付宝交易凭证号。
     */
    private String trade_no;

    private String auth_app_id;
    /**
     * 商家在收益中实际收到的款项，单位人民币（元）。
     */
    private String receipt_amount;
    /**
     * 使用集分宝支付的金额。
     */
    private String point_amount;
    /**
     * 支付宝分配给开发者的应用 ID。
     */
    private String app_id;
    /**
     * 最终支付的金额；用户在交易中支付的金额。
     */
    private String buyer_pay_amount;
    /**
     * 商户生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2。
     */
    private String sign_type;
    /**
     * 卖家支付宝用户号。
     */
    private String seller_id;

}
