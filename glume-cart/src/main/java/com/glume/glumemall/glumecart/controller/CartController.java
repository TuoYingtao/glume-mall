package com.glume.glumemall.glumecart.controller;

import com.glume.glumemall.glumecart.service.CartService;
import com.glume.glumemall.glumecart.vo.Cart;
import com.glume.glumemall.glumecart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        Cart cart = cartService.getCart();
        model.addAttribute("cart",cart);
        return "cartList";
    }

    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num,
                            RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
        cartService.addToCart(skuId,num);
        redirectAttributes.addAttribute("skuId",skuId);
        return "redirect:http://cart.glumemall.com/addToCartSuccess.html";
    }

    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,Model model) {
        // 查询购物车数据
        CartItem item = cartService.getCartItem(skuId);
        model.addAttribute("item",item);
        return "success";
    }

    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId, @RequestParam("check") Integer check) {
        cartService.checkItem(skuId,check);
        return "redirect:http://cart.glumemall.com/cart.html";
    }

    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) {
        cartService.countItem(skuId,num);
        return "redirect:http://cart.glumemall.com/cart.html";
    }

    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("deleteId") Long deleteId) {
        cartService.deleteItem(deleteId);
        return "redirect:http://cart.glumemall.com/cart.html";
    }
}