package com.glume.glumemall.product.service.impl;

import com.glume.glumemall.product.vo.item.SkuItemVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tuoyingtao
 * @create 2021-12-04 11:22
 */
@SpringBootTest
class SkuInfoServiceImplTest {

    @Autowired
    SkuInfoServiceImpl skuInfoService;

    @Test
    void item() {
        SkuItemVo item = skuInfoService.item(1L);
        System.out.println(item);
    }
}
