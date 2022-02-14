package com.glume.glumemall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.exception.servlet.NoStockException;
import com.glume.glumemall.ware.vo.SkuHasStockVo;
import com.glume.glumemall.ware.vo.WareSkuLockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.glume.glumemall.ware.entity.WareSkuEntity;
import com.glume.glumemall.ware.service.WareSkuService;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.core.utils.R;



/**
 * 商品库存
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:42:52
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 锁定库存
     */
    @PostMapping("/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVo wareSkuLockVo) {
        try {
            Boolean lockStockResults = wareSkuService.orderSkuLockStock(wareSkuLockVo);
            return R.ok();
        } catch (NoStockException e) {
            return R.error(HttpStatus.BizCodeEnum.NO_STOCK_EXCEPTION.getCode(), HttpStatus.BizCodeEnum.NO_STOCK_EXCEPTION.getMsg());
        }
    }

    /**
     *  获取SKU库存
     */
    @PostMapping("/hasStock")
    public R getSkusHasStock(@RequestBody List<Long> skuIds) {
        List<SkuHasStockVo> skusHasStock = wareSkuService.getSkusHasStock(skuIds);
        return R.ok().put("data",skusHasStock);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
