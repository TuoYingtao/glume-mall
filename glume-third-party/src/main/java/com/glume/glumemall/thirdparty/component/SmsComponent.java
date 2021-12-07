package com.glume.glumemall.thirdparty.component;

import com.alibaba.fastjson.JSON;
import com.glume.common.core.constant.HttpStatus;
import com.glume.glumemall.thirdparty.util.HttpUtils;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里短信服务
 * @author tuoyingtao
 * @create 2021-12-06 13:24
 */
@ConfigurationProperties(prefix = "alibaba.cloud.sms")
@Data
@Component
public class SmsComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsComponent.class);

    /**
     * 请求地址
     */
    private String host;
    /**
     * 请求路径
     */
    private String path;
    /**
     * 模板ID
     */
    private String templateId = "M72CB42894";
    /**
     * APPCODE
     */
    private String appcode;

    public void sendSmsCode(String mobile,String code) {
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", mobile);
        querys.put("templateId", templateId);
        querys.put("value", code);
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            String body = EntityUtils.toString(response.getEntity());
            Map map = JSON.parseObject(body, Map.class);
            if (HttpStatus.SUCCESS != Integer.parseInt(map.get("code").toString())) {
                LOGGER.error("阿里短信服务=>body：{}",body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
