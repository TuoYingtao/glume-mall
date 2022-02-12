package com.glume.glumemall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.glume.common.core.to.MemberRespTo;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.order.feign.CartFeignService;
import com.glume.glumemall.order.feign.MemberFeignService;
import com.glume.glumemall.order.feign.WareFeignService;
import com.glume.glumemall.order.interceptor.LoginUserInterceptor;
import com.glume.glumemall.order.vo.MemberAddressVo;
import com.glume.glumemall.order.vo.OrderConfirmVo;
import com.glume.glumemall.order.vo.OrderItemVo;
import com.glume.glumemall.order.vo.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

        // TODO 防重令牌

        CompletableFuture.allOf(feignMemberAddress,feignCartItem).get();
        return orderConfirmVo;
    }

}
