package com.glume.glumemall.product.service.impl;

import com.glume.glumemall.product.service.SkuSaleAttrValueService;
import com.glume.glumemall.product.vo.item.ItemSaleAttrsVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tuoyingtao
 * @create 2021-12-03 14:28
 */
@SpringBootTest
class SkuSaleAttrValueServiceImplTest {

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Test
    void getSaleAttrsBySpuId() {
        List<ItemSaleAttrsVo> saleAttrsBySpuId = skuSaleAttrValueService.getSaleAttrsBySpuId(13L);
        System.out.println(saleAttrsBySpuId);
    }
}