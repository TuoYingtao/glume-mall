package com.glume.glumemall.glumecart.controller;

import com.glume.glumemall.glumecart.interceotir.CartInterceptor;
import com.glume.glumemall.glumecart.to.UserInfoTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author tuoyingtao
 * @create 2021-12-13 10:25
 */
@Controller
public class CartController {

    @GetMapping("/cart.html")
    public String cartListPage() {
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        System.out.println(userInfoTo);
        return "cartList";
    }
}
