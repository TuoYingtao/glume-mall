package com.glume.glumemall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.glume.common.core.constant.OrderConstant;
import com.glume.common.core.to.MemberRespTo;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.RedisUtils;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.order.feign.CartFeignService;
import com.glume.glumemall.order.feign.MemberFeignService;
import com.glume.glumemall.order.feign.WareFeignService;
import com.glume.glumemall.order.interceptor.LoginUserInterceptor;
import com.glume.glumemall.order.to.OrderCreateTo;
import com.glume.glumemall.order.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.order.dao.OrderDao;
import com.glume.glumemall.order.entity.OrderEntity;
import com.glume.glumemall.order.service.OrderService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Resource
    MemberFeignService memberFeignService;

    @Resource
    CartFeignService cartFeignService;

    @Resource
    WareFeignService wareFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 订单确认页需要返回的数据
     * @return
     */
    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        // TODO 订单确认页需要返回的数据待完成
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        MemberRespTo memberRespTo = LoginUserInterceptor.toThreadLocal.get();
        /** 异步多线程：远程调用解决请求头丢失问题 */
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 远程查询会员收货地址
        CompletableFuture<Void> feignMemberAddress = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> memberAddressList = memberFeignService.getMemberAddressList(memberRespTo.getId());
            orderConfirmVo.setAddress(memberAddressList);
        }, executor);

        // 远程查询购物车选中的购物项
        CompletableFuture<Void> feignCartItem = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
            orderConfirmVo.setItems(currentUserCartItems);
        }, executor).thenRunAsync(() -> {
            List<OrderItemVo> items = orderConfirmVo.getItems();
            List<Long> collect = items.stream().map(OrderItemVo::getSkuId).collect(Collectors.toList());
            R skusHasStock = wareFeignService.getSkusHasStock(collect);
            List<SkuHasStockVo> data = skusHasStock.getData(new TypeReference<List<SkuHasStockVo>>() {
            });
            if (StringUtils.isNotNull(data)) {
                Map<Long, Boolean> map = data.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
                orderConfirmVo.setStocks(map);
            }
        });

        // 用户积分
        Integer integration = memberRespTo.getIntegration();
        orderConfirmVo.setIntegration(integration);

        // 幂等性：防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        RedisUtils redisUtils = SpringUtils.getBean(RedisUtils.class);
        redisUtils.set(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespTo.getId(),token,60 * 30);
        orderConfirmVo.setOrderToken(token);
        CompletableFuture.allOf(feignMemberAddress,feignCartItem).get();
        return orderConfirmVo;
    }

    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo) {
        SubmitOrderResponseVo submitOrderResponseVo = new SubmitOrderResponseVo();
        MemberRespTo memberRespTo = LoginUserInterceptor.toThreadLocal.get();
        /** 验证令牌 */
        String orderToken = orderSubmitVo.getOrderToken();
        RedisUtils redisUtils = SpringUtils.getBean(RedisUtils.class);
        long result = redisUtils.executeLuaDel(Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespTo.getId()), orderToken);
        if (result == 0) {
            // 令牌验证不通过
            submitOrderResponseVo.setCode(1);
            return submitOrderResponseVo;
        } else {
            // 令牌验证通过

        }

        return null;
    }

    /** 创建订单 */
    public OrderCreateTo createOrder() {
        OrderCreateTo orderCreateTo = new OrderCreateTo();
        // 生成订单号
        String orderSn = IdWorker.getTimeId();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(orderSn);
        // TODO 创建订单待完成 22-2-12
        return orderCreateTo;
    }

}
