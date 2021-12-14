package com.glume.glumemall.glumecart.controller;

import com.glume.glumemall.glumecart.interceotir.CartInterceptor;
import com.glume.glumemall.glumecart.service.CartService;
import com.glume.glumemall.glumecart.to.UserInfoTo;
import com.glume.glumemall.glumecart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutionException;

/**
 * @author tuoyingtao
 * @create 2021-12-13 10:25
 */
@Controller
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/cart.html")
    public String cartListPage() {
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        System.out.println(userInfoTo);
        return "cartList";
    }

    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num,
                            Model model) throws ExecutionException, InterruptedException {

        CartItem cartItem = cartService.addToCart(skuId,num);
        model.addAttribute("item",cartItem);
        return "success";
    }
}