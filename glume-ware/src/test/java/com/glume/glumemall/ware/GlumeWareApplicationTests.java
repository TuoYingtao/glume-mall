package com.glume.glumemall.ware;

import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.utils.R;
import com.glume.glumemall.ware.feign.MemberFeignService;
import com.glume.glumemall.ware.vo.MemberAddressVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
class GlumeWareApplicationTests {

    @Resource
    MemberFeignService memberFeignService;

    @Test
    void contextLoads() {
        R info = memberFeignService.info(1L);
        Map<String, Object> map = info.getData(new TypeReference<Map<String,Object>>(){});
        MemberAddressVo data = (MemberAddressVo) map.get("memberReceiveAddress");
        System.out.println(data);
    }

}
