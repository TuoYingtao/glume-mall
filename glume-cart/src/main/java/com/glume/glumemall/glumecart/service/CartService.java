package com.glume.glumemall.glumecart.service;

import com.glume.glumemall.glumecart.vo.Cart;
import com.glume.glumemall.glumecart.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author tuoyingtao
 * @create 2021-12-13 10:04
 */
public interface CartService {

    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItem getCartItem(Long skuId);

    Cart getCart() throws ExecutionException, InterruptedException;

    void clearCart(String cartKey);

    void checkItem(Long skuId, Integer check);

    void countItem(Long skuId, Integer num);

    void deleteItem(Long deleteId);

    List<CartItem> getUserCartItems();
}
