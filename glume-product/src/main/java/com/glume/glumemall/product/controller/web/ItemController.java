package com.glume.glumemall.product.controller.web;

import com.glume.glumemall.product.service.SkuInfoService;
import com.glume.glumemall.product.vo.item.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author TuoYingtao
 * @create 2021-12-02 23:16
 */
@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) {
        System.out.println("准备查询");
        SkuItemVo skuItemVo = skuInfoService.item(skuId);
        model.addAttribute("item",skuItemVo);
        return "item";
    }
}
