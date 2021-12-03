package com.glume.glumemall.product.service.impl;

import com.glume.glumemall.product.service.AttrGroupService;
import com.glume.glumemall.product.vo.item.SpuItemAttrGroupVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


/**
 * @author tuoyingtao
 * @create 2021-12-03 11:59
 */
@SpringBootTest
class AttrGroupServiceImplTest {

    @Autowired
    AttrGroupService attrGroupService;

    @Test
    void getAttrGroupWithAttrsBySpuId() {
        List<SpuItemAttrGroupVo> vos = attrGroupService.getAttrGroupWithAttrsBySpuId(13L, 225L);
        System.out.println(vos);
    }
}