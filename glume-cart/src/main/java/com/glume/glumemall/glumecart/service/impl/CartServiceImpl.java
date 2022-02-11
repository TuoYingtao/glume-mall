package com.glume.glumemall.glumecart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.constant.CartConstant;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.glumecart.feign.ProductFeignService;
import com.glume.glumemall.glumecart.interceotir.CartInterceptor;
import com.glume.glumemall.glumecart.service.CartService;
import com.glume.glumemall.glumecart.to.UserInfoTo;
import com.glume.glumemall.glumecart.vo.Cart;
import com.glume.glumemall.glumecart.vo.CartItem;
import com.glume.glumemall.glumecart.vo.SkuInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author tuoyingtao
 * @create 2021-12-13 10:05
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        // 商品添加到购物车
        CartItem cartItem = new CartItem();
        String str = (String) cartOps.get(skuId.toString());
        // 判断当前购物车是否包含此类商品
        if (StringUtils.isNull(str)) {
            // 没有此类商品则添加
            CompletableFuture<Void> skuInfo1 = CompletableFuture.runAsync(() -> {
                // 远程查询当前要添加的商品SKU信息
                R skuInfo = productFeignService.info(skuId);
                SkuInfoVo data = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setTitle(data.getSkuTitle());
                cartItem.setPrice(data.getPrice());
                cartItem.setSkuId(data.getSkuId());
            }, executor);
            CompletableFuture<Void> skuInfo2 = CompletableFuture.runAsync(() -> {
                // 远程查询当前要添加的商品SKU的组合信息
                List<String> skuSaleAttrValue = productFeignService.getSkuSaleAttrValue(skuId);
                cartItem.setSkuAttr(skuSaleAttrValue);
            }, executor);
            CompletableFuture.allOf(skuInfo1,skuInfo2).get();
            String s = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(),s);
            return cartItem;
        } else {
            // 有此类商品则修改数量
            CartItem item = JSON.parseObject(str, CartItem.class);
            item.setCount(item.getCount() + num);
            String s = JSON.toJSONString(item);
            cartOps.put(skuId.toString(),s);
            return item;
        }
    }

    /**
     * 获取购物车中的购物项
     * @param skuId
     * @return
     */
    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String item = (String) cartOps.get(skuId.toString());
        if (StringUtils.isNotNull(item)) {
            CartItem cartItem = JSON.parseObject(item, CartItem.class);
            return cartItem;
        }
        return null;
    }

    /**
     * 获取购物车列表
     * @return
     */
    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        Cart cart = new Cart();
        // 判断当前用户是否登录
        if (StringUtils.isNotNull(userInfoTo.getUserId())) {
            // 如果临时购物车的数据还没有进行合并【合并购物车】
            String tempCartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> tempCartItems = getCartItems(tempCartKey);
            if (StringUtils.isNotEmpty(tempCartItems)) {
                for (CartItem item : tempCartItems) {
                    addToCart(item.getSkuId(), item.getCount());
                }
                clearCart(tempCartKey);
            }
            // 获取登录后的购物车的数据【包含临时购物车数据与登录后的购物车的数据】
            List<CartItem> cartItems = getCartItems(CartConstant.CART_PREFIX + userInfoTo.getUserId());
            cart.setItems(cartItems);
        } else {
            String userKey = userInfoTo.getUserKey();
            List<CartItem> cartItems = getCartItems(CartConstant.CART_PREFIX + userKey);
            cart.setItems(cartItems);
        }
        return cart;
    }

    /**
     * 清空购物车
     * @param cartKey
     */
    @Override
    public void clearCart(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    /**
     * 选中购物项
     * @param skuId
     * @param check
     */
    @Override
    public void checkItem(Long skuId, Integer check) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check == 1 ? true : false);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String s = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),s);
    }

    /**
     * 改变商品项的数量
     * @param skuId
     * @param num
     */
    @Override
    public void countItem(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String s = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),s);
    }

    /**
     * 删除商品项
     * @param deleteId
     */
    @Override
    public void deleteItem(Long deleteId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(deleteId.toString());
    }

    /**
     * 当前用户选中的购物车项
     * @return
     */
    @Override
    public List<CartItem> getUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        if (StringUtils.isNotNull(userInfoTo.getUserId())) {
            String userKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
            List<CartItem> cartItems = getCartItems(userKey);
            return cartItems.stream().filter(cartItem -> cartItem.getCheck())
                    .map(cartItem -> {
                        // 更新最新价格
                        R productData = productFeignService.getPrice(cartItem.getSkuId());
                        String price = (String) productData.get("price");
                        cartItem.setPrice(new BigDecimal(price));
                        return cartItem;
                    }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 获取购物车中的所有购物项
     * @param cartKey
     * @return
     */
    private List<CartItem> getCartItems(String cartKey) {
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        if (StringUtils.isNotEmpty(values)) {
            List<CartItem> cartItems = values.stream().map(obj -> {
                String s = obj.toString();
                CartItem cartItem = JSON.parseObject(s, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            return cartItems;
        }
        return null;
    }

    /**
     * 获取需要操作的购物车
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.toThreadLocal.get();
        // 判断当前用户是否登录，是否使用临时购物车
        String cartKey = "";
        if (StringUtils.isNotNull(userInfoTo.getUserId())) {
            cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserId();
        } else {
            cartKey = CartConstant.CART_PREFIX + userInfoTo.getUserKey();
        }
        BoundHashOperations<String, Object, Object> stringObjectObjectBoundHashOperations = redisTemplate.boundHashOps(cartKey);
        return stringObjectObjectBoundHashOperations;
    }
}

