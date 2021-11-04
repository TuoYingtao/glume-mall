package com.glume.glumemall.thirdparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

}
