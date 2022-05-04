package com.glume.glumemall.admin.controller.feign;

import com.glume.common.core.annotation.valid.IDGroup;
import com.glume.common.core.utils.R;
import com.glume.glumemall.admin.feign.CouponFeignService;
import com.glume.glumemall.admin.feign.ProductFeignService;
import com.glume.glumemall.admin.service.CouponRelationService;
import com.glume.glumemall.admin.to.SeckillRelationTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author TuoYingtao
 * @create 2022-03-17 11:54
 */
@RestController
@RequestMapping("admin/coupon")
public class FeignCouponRelationController {

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    CouponRelationService couponRelationService;

    /**
     * 所有产品列表
     */
    @GetMapping("/list/all/product")
    public R listProductAll() {
        R reuslt = productFeignService.listAll();
        return reuslt;
    }

    /**
     * 分类列表
     */
    @GetMapping("/list/category/product")
    public R listProductCategory() {
        R reuslt = productFeignService.listCategory();
        return reuslt;
    }

    /**
     * 活动主题与场次
     */
    @GetMapping("/list/promotionandsession")
    public R promotionAndSession() {
        R reuslt = couponFeignService.promotionAdnSessionList();
        return reuslt;
    }

    /**
     * 列表
     */
    @GetMapping("/list/seckillRelation")
    public R listSeckillRelation(@RequestParam Map<String,Object> params) {
        R result = couponFeignService.skuRelationList(params);
        return result;
    }

    /**
     * 保存
     */
    @PostMapping("/save/seckillRelation")
    public R saveSeckillRelation(@Valid SeckillRelationTo relationTo) {
        R result = couponFeignService.skuRelationSave(relationTo);
        return result;
    }

    /**
     * 信息
     */
    @GetMapping("/info/seckillRelation/{id}")
    public R infoSeckillRelation(@Validated(IDGroup.class) @PathVariable("id") Long id) {
        R result = couponFeignService.skuRelationInfo(id);
        return result;
    }

    /**
     * 修改
     */
    @PutMapping("/update/seckillRelation")
    public R updateSeckillRelation(@Validated(IDGroup.class) SeckillRelationTo seckillRelationTo) {
        R result = couponFeignService.skuRelationUpdate(seckillRelationTo);
        return result;
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/seckillRelation/{id}")
    public R deleteSeckillRElation(@PathVariable("id") Long[] ids) {
        R result = couponFeignService.skuRelationDelete(ids);
        return result;
    }
}
