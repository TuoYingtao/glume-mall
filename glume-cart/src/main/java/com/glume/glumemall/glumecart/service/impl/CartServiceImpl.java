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
import com.glume.glumemall.glumecart.vo.CartItem;
import com.glume.glumemall.glumecart.vo.SkuInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

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

