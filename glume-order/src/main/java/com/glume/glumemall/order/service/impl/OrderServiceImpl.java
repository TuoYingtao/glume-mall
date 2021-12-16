package com.glume.glumemall.order.service.impl;

import com.glume.common.core.to.MemberRespTo;
import com.glume.glumemall.order.feign.CartFeignService;
import com.glume.glumemall.order.feign.MemberFeignService;
import com.glume.glumemall.order.interceptor.LoginUserInterceptor;
import com.glume.glumemall.order.vo.MemberAddressVo;
import com.glume.glumemall.order.vo.OrderConfirmVo;
import com.glume.glumemall.order.vo.OrderItemVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;

import com.glume.glumemall.order.dao.OrderDao;
import com.glume.glumemall.order.entity.OrderEntity;
import com.glume.glumemall.order.service.OrderService;

import javax.annotation.Resource;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Resource
    MemberFeignService memberFeignService;

    @Resource
    CartFeignService cartFeignService;

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
    public OrderConfirmVo confirmOrder() {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        MemberRespTo memberRespTo = LoginUserInterceptor.toThreadLocal.get();
        // TODO 订单确认页需要返回的数据待完成
        // 远程查询会员收货地址
        List<MemberAddressVo> memberAddressList = memberFeignService.getMemberAddressList(memberRespTo.getId());
        orderConfirmVo.setAddress(memberAddressList);

        // 远程查询购物车选中的购物项
        List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
        orderConfirmVo.setItems(currentUserCartItems);

        // 用户积分
        Integer integration = memberRespTo.getIntegration();
        orderConfirmVo.setIntegration(integration);


        return null;
    }

}