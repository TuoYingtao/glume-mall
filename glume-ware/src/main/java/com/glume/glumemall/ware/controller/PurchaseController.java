package com.glume.glumemall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.glume.glumemall.ware.vo.MergeVo;
import com.glume.glumemall.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.ware.entity.PurchaseEntity;
import com.glume.glumemall.ware.service.PurchaseService;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.core.utils.R;



/**
 * 采购信息
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:42:52
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 查询新建与未分配的采购单
     */
    @GetMapping("/unreceive/list")
    public R unreceivelist(@RequestParam Map<String,Object> params) {
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("data",page);
    }

    /**
     * 整合采购单
     */
    @PostMapping("/merge")
    public R merge(MergeVo mergeVo) {
        purchaseService.mergePurchase(mergeVo);
        return R.ok("整合采购单成功！");
    }

    /**
     * 领取采购单
     */
    @PostMapping("/received")
    public R received(List<Long> ids) {
        purchaseService.received(ids);
        return R.ok();
    }

    /**
     * 完成采购单
     */
    @PostMapping("/done")
    public R finish(PurchaseDoneVo purchaseDoneVo) {
        purchaseService.purchaseDone(purchaseDoneVo);
        return R.ok();
    }
}
