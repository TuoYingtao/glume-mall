package com.glume.glumemall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.product.entity.AttrEntity;
import com.glume.glumemall.product.entity.BrandEntity;
import com.glume.glumemall.product.service.BrandService;
import com.glume.glumemall.product.service.impl.AttrServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@SpringBootTest
class GlumeProductApplicationTests {

    @Resource
    BrandService brandService;

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setName("华为");
//        brandService.save(brandEntity);
//        System.out.println("保存成功");

//        brandEntity.setBrandId(1L);
//        brandEntity.setDescript("华为手机");
//        brandService.updateById(brandEntity);

        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach(item -> {
            System.out.println(item);
        });
    }

    @Test
    void notRelationAttr() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("page",1);
        params.put("limit",10);
        PageUtils notRelationAttr = SpringUtils.getBean(AttrServiceImpl.class).getNotRelationAttr(params, 17L);
        List<AttrEntity> list = (List<AttrEntity>) notRelationAttr.getList();
        Iterator<AttrEntity> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Test
    void addRelationAttr() {
        Long[] longs = new Long[]{20L,22L};
        SpringUtils.getBean(AttrServiceImpl.class).saveAttrGroupRelationBatch(longs,17L);
    }

}
