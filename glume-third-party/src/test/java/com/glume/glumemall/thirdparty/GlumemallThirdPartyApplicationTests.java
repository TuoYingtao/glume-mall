package com.glume.glumemall.thirdparty;

import com.aliyun.oss.OSSClient;
import com.glume.glumemall.thirdparty.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class GlumemallThirdPartyApplicationTests {

    @Autowired
    OSSClient ossClient;

    @Test
    void contextLoads() {
    }

    @Test
    void testOss() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("C:\\Users\\Administrator\\Desktop\\1590252779188 (1).jpg");
        ossClient.putObject("glume-mall", "AAA.jpg", inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传完成....");
    }

    @Test
    public void sendSms() {
        String host = "https://jumsendsms.market.alicloudapi.com";
        String path = "/sms/send-upgrade";
        String method = "POST";
        String appcode = "e4ea93264877482cb458447c5ef8b830";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", "15207447965");
        querys.put("templateId", "M72CB42894");
        querys.put("value", "15786");
        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
