package com.glume.glumemall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.glume.glumemall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝模板
 * @author tuoyingtao
 * @create 2022-2-25 16:38
 */
@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private String app_id = "2021000117624891";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCV6NsqMYpFfBQjZmh5gzWVhaNXBED4qyqoXGkwBppJoJlbRePIiw+0K/0yREV8KHNZE+WxLKPLXCL/06TK6fUQjwPM9zbx/n0Mn3+GLYpQE+GB6GfcjAbaUUxPTKXdmQaeKHrhqm1RBamsMFjWG8YrHuvvvPRl7GXamDYoqWkpR24QHEPNLLd7rGI72eKlTSSrmY9KE88Rut2uOxTJayb2xTakeJWR0XlTFPEuK6tMZFEMc65dEHkYp+ZFZeU825v0ZiXjJR/g7/kMAvchSKc1hBePqBmtaAxispUAAuD50SAW7pHy6DvNMpc6MZCKYA8qRfxjSknavJikUqlbAIaBAgMBAAECggEAE3TnjTBk2+Pg0T9na+4FtGRl8NNSg3Pj03o7BMq2l7vKa7hEejDiVuYmtLF7u0OcxQD6HObIU4qf2Ehl6Rg4gG+vdJjZfssi+9JvwMCEZSRPbob1hACAmzFWECzIklxcKYbjfAqcqZlsjjawMQnlzI703GbP6bKm/Zon3VsvlPXHMYfmyNVYXnSv8o9LzwgGONNKlZAOg3Sp/FPyNTJTSfCTs7OnGr9uqwfpfTlQdPdo9vTwxn44WLLNjFki54AlDoe9vZHHnfbVNo5rk3l27yBjjnvYWhRjsLBr3CURT+pyfzeNUSl/DbNltmw0f/XDhSaU8WFeROy6W1Rk5sdH4QKBgQDc1RZdu7sny1TGAPlnCTXzox6SEDsEGUAQ9wINXjppK24kJM9dcfdJx9wgPlnDPTX16mRDWleNy83AOfZ6A6BaqEAnAB1fpjp/FN8vG60LAzrwZOZNophO2hIQ76MXL4Ry4ryrvpIhl6zcUPEcemw9nubJulUsjwTjqa6HdgMf1QKBgQCtyGILXZVmZw5IhkLTqXVbBap/wioNBE1e9FINlYzSX0xO++sbKN10JlJDFp7AkHquutUZZ/8y9lILldurAj5tsw3ZpfLy2/OuK8JiqQ9op1IpTsUz7/F0jCzPDBKTxIc7VEa1uk0RJOVGFRMZtTvijSyxfMQ/mMtpYAsdhntN/QKBgQCDfp8C0XZwC7M+8/WOVP27RbY+Y0vA9QghEfQ24ZUGasX637BahDIrzMZWrMnmZ9xpwlhRgsn/tQufGuSZYu9pDCanEnprKiBK67K56lDv7cx6uJoqQFdRHKkFTKxgcfr8LecKnT+GoTpdNhVcT8mEJ8pk9H8jOUZAChnSr4LMwQKBgDfnvbrASV6fCpZEMt1ap3k3smjVMqfrWWSRno/mK15F8At8uV6MX+mMpmwUq89w6zaovgMlVJC1cyFs/TTfTOwHbQ42d5ivL445X5hToLRfF2xYfdI+f2Op8IRO8u78izCH5RNzMnWwc91+zB06E2DHX7hdVdjLs6xbjFfcOnCVAoGBAIttgHQmd7d+HWB/qzVymFxj7IMQ8U6aNRIcWom5emM4TIiG1IR6CKV5CS4Olg/WBnKRXu97SBWUr47E7zF2FY1SllY8Q5TuUamFQ7JMBEwZfzOIgorjkD2epjxANY2zdQjxXVYZ7/Ym97UDnZi4H0IPN83i9Sy97YdPeh1l/jQ2";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkeLPwsCvxVInzK1sOw7SzQyZ+pi11NA3PLbuq5xoJkqoYw+tLnNo2kgN6l0v0nCvzfqpCBLmLEb7ZK/VDk+/IA7OGnfD4wkF5AqMvUI+tTupOx71rs+llhgNixGVNl9tj+r8TKL7CCk0PWA1C+xiXyRVSXuucAZ1T2a/TQ18NM+AaMBb3F4qOXT/W4jNo75RTSCRjfFBPLVzI9XzQu0CtQXs4CaH0AAXCtRU6lU2/qhw+AzkGZ4Q+dqeg8nsvizKhfYQ6MfY4XagCQio+1v/+1MtDpNicfywbdkUnE95MO3MXS0gJwmsxy/iZag/Y0vS3H7k4sM0G+zVk8GjHKx42QIDAQAB";

    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private String notify_url = "http://2956no5633.wicp.vip/payed/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private String return_url = "http://member.glumemall.com/memberOrder.html";

    // 签名方式
    private String sign_type = "RSA2";

    // 字符编码格式
    private String charset = "utf-8";

    // 过期时间
    private String timeout = "30m";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+ timeout +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
