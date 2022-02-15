package com.glume.glumemall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.glume.common.core.constant.OrderConstant;
import com.glume.common.core.exception.servlet.NoStockException;
import com.glume.common.core.to.MemberRespTo;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.RedisUtils;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.order.entity.OrderItemEntity;
import com.glume.glumemall.order.enume.OrderStatusEnum;
import com.glume.glumemall.order.feign.CartFeignService;
import com.glume.glumemall.order.feign.MemberFeignService;
import com.glume.glumemall.order.feign.ProductFeignService;
import com.glume.glumemall.order.feign.WareFeignService;
import com.glume.glumemall.order.interceptor.LoginUserInterceptor;
import com.glume.glumemall.order.service.OrderItemService;
import com.glume.glumemall.order.to.OrderCreateTo;
import com.glume.glumemall.order.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private ThreadLocal<OrderSubmitVo> confirmVoThreadLocal = new ThreadLocal<>();

    @Resource
    MemberFeignService memberFeignService;

    @Resource
    CartFeignService cartFeignService;

    @Resource
    WareFeignService wareFeignService;

    @Resource
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    OrderItemService orderItemService;

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
    @Transactional
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo) {
        confirmVoThreadLocal.set(orderSubmitVo);
        SubmitOrderResponseVo submitOrderResponseVo = new SubmitOrderResponseVo();
        MemberRespTo memberRespTo = LoginUserInterceptor.toThreadLocal.get();
        submitOrderResponseVo.setCode(0);
        /** 验证令牌 */
        String orderToken = orderSubmitVo.getOrderToken();
        RedisUtils redisUtils = SpringUtils.getBean(RedisUtils.class);
        long result = redisUtils.executeLuaDel(Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespTo.getId()), orderToken);
        if (result == 0) {
            // 令牌验证不通过
            submitOrderResponseVo.setCode(1);
        } else {
            /** 创建订单 */
            // 1、令牌验证通过
            OrderCreateTo order = createOrder();
            // 2、验价
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrice = orderSubmitVo.getPayPrice();
            if (Math.abs(payAmount.subtract(payPrice).doubleValue()) < 0.01) {
                // 3、保存订单
                saveOrder(order);
                // 4、库存锁定。只要有异常回滚订单数据
                WareSkuLockVo wareSkuLockVo = new WareSkuLockVo();
                wareSkuLockVo.setOrderSn(order.getOrder().getOrderSn());
                List<OrderItemVo> lockItems = order.getOrderItems().stream().map(orderItemEntity -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setSkuId(orderItemEntity.getSkuId());
                    orderItemVo.setTitle(orderItemEntity.getSkuName());
                    orderItemVo.setCount(orderItemEntity.getSkuQuantity());
                    orderItemVo.setPrice(orderItemEntity.getSkuPrice());
                    return orderItemVo;
                }).collect(Collectors.toList());
                wareSkuLockVo.setLocks(lockItems);
                R orderLockStockResult = wareFeignService.orderLockStock(wareSkuLockVo);
                if (orderLockStockResult.getCode() == 200) {
                    submitOrderResponseVo.setOrder(order.getOrder());
                } else {
                    // 库存锁定失败
                    submitOrderResponseVo.setCode(3);
                    throw new NoStockException();
                }
            } else {
                // 金额对比失败
                submitOrderResponseVo.setCode(2);
            }
        }
        return submitOrderResponseVo;
    }

    /** 保存订单数据 */
    private void saveOrder(OrderCreateTo orderCreateTo) {
        OrderEntity orderEntity = orderCreateTo.getOrder();
        orderEntity.setModifyTime(new Date());
        this.save(orderEntity);
        List<OrderItemEntity> orderItemEntities = orderCreateTo.getOrderItems()
                .stream().map(orderItemEntity -> {
                    orderItemEntity.setOrderId(orderEntity.getId());
                    return orderItemEntity;
        }).collect(Collectors.toList());
        orderItemService.saveBatch(orderItemEntities);
    }

    /** 创建订单 */
    public OrderCreateTo createOrder() {
        OrderCreateTo orderCreateTo = new OrderCreateTo();
        // 生成订单号
        String orderSn = IdWorker.getTimeId();
        // 创建订单
        OrderEntity orderEntity = buildOrder(orderSn);
        // 创建订单项
        List<OrderItemEntity> orderItemEntities = buildOrderItems(orderSn);
        // 计算价格、积分等信息
        computePrice(orderEntity,orderItemEntities);
        // 设置订单
        orderCreateTo.setOrder(orderEntity);
        // 设置订单项
        orderCreateTo.setOrderItems(orderItemEntities);

        return orderCreateTo;
    }

    /** 计算价格 */
    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        BigDecimal total = new BigDecimal("0");
        BigDecimal coupon = new BigDecimal("0");
        BigDecimal promotion = new BigDecimal("0");
        BigDecimal integration = new BigDecimal("0");
        BigDecimal giveIntegration = new BigDecimal("0");
        BigDecimal giveGrowth = new BigDecimal("0");
        /* 获取所有订单项的总价格、优惠卷总价、促销总价、积分总价、总赠送积分、总赠送成长值 */
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            // 订单项总价格
            total = total.add(orderItemEntity.getRealAmount());
            // 优惠卷总价
            coupon = coupon.add(orderItemEntity.getCouponAmount());
            // 促销总价
            promotion = promotion.add(orderItemEntity.getPromotionAmount());
            // 积分总价
            integration = integration.add(orderItemEntity.getIntegrationAmount());
            // 总赠送积分
            giveIntegration = giveIntegration.add(new BigDecimal(orderItemEntity.getGiftIntegration().toString()));
            // 总赠送成长值
            giveGrowth = giveGrowth.add(new BigDecimal(orderItemEntity.getGiftGrowth().toString()));
        }
        // 设置订单总额
        orderEntity.setTotalAmount(total);
        // 设置订单应付总额
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        // 设置优惠卷总额
        orderEntity.setCouponAmount(coupon);
        // 设置促销总额
        orderEntity.setPromotionAmount(promotion);
        // 设置积分总额
        orderEntity.setIntegrationAmount(integration);
        // 设置赠送积分
        orderEntity.setIntegration(giveIntegration.intValue());
        // 设置赠送成长值
        orderEntity.setGrowth(giveGrowth.intValue());
    }

    /** 构建订单信息 */
    private OrderEntity buildOrder(String orderSn) {
        MemberRespTo memberRespTo = LoginUserInterceptor.toThreadLocal.get();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(orderSn);
        orderEntity.setMemberId(memberRespTo.getId());
        // 获取收货地址信息
        OrderSubmitVo orderSubmitVo = confirmVoThreadLocal.get();
        R fare = wareFeignService.getFare(orderSubmitVo.getAddrId());
        FareVo fareResp = fare.getData(new TypeReference<FareVo>() {
        });
        // 设置运费信息
        orderEntity.setFreightAmount(fareResp.getFare());
        // 设置收货人信息
        orderEntity.setReceiverCity(fareResp.getAddress().getCity());
        orderEntity.setReceiverDetailAddress(fareResp.getAddress().getDetailAddress());
        orderEntity.setReceiverName(fareResp.getAddress().getName());
        orderEntity.setReceiverPhone(fareResp.getAddress().getPhone());
        orderEntity.setReceiverPostCode(fareResp.getAddress().getPostCode());
        orderEntity.setReceiverProvince(fareResp.getAddress().getProvince());
        orderEntity.setReceiverRegion(fareResp.getAddress().getRegion());
        // 设置订单状态
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        // 设置自动确认收货时间
        orderEntity.setAutoConfirmDay(7);
        // 删除状态
        orderEntity.setDeleteStatus(0);

        return orderEntity;
    }

    /** 构建所有订单项信息 */
    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
        if (StringUtils.isNotNull(currentUserCartItems) && currentUserCartItems.size() > 0) {
            List<OrderItemEntity> orderItemEntityList = currentUserCartItems.stream().map(orderItemVo -> {
                OrderItemEntity orderItemEntity = buildOrderItem(orderItemVo);
                orderItemEntity.setOrderSn(orderSn);
                return orderItemEntity;
            }).collect(Collectors.toList());
            return orderItemEntityList;
        }
        return null;
    }

    /** 构建某一个订单项信息 */
    private OrderItemEntity buildOrderItem(OrderItemVo orderItemVo) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        // 商品SPU信息
        Long skuId = orderItemVo.getSkuId();
        R spuInfoResp = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo spuInfoRespData = spuInfoResp.getData(new TypeReference<SpuInfoVo>() {
        });
        orderItemEntity.setSpuId(spuInfoRespData.getId());
        orderItemEntity.setSpuName(spuInfoRespData.getSpuName());
        orderItemEntity.setSpuBrand(spuInfoRespData.getBrandId().toString());
        orderItemEntity.setCategoryId(spuInfoRespData.getCatalogId());
        // 商品SKU信息
        orderItemEntity.setSkuId(orderItemVo.getSkuId());
        orderItemEntity.setSkuName(orderItemVo.getTitle());
        orderItemEntity.setSkuPic(orderItemVo.getImage());
        orderItemEntity.setSkuPrice(orderItemVo.getPrice());
        String skuAttr = org.springframework.util.StringUtils.collectionToDelimitedString(orderItemVo.getSkuAttr(), ";");
        orderItemEntity.setSkuAttrsVals(skuAttr);
        orderItemEntity.setSkuQuantity(orderItemVo.getCount());
        // 积分信息
        BigDecimal count = new BigDecimal(orderItemVo.getCount().toString());
        orderItemEntity.setGiftGrowth(orderItemVo.getPrice().multiply(count).intValue());
        orderItemEntity.setGiftIntegration(orderItemVo.getPrice().multiply(count).intValue());
        // 订单项价格信息
        orderItemEntity.setPromotionAmount(new BigDecimal("0"));
        orderItemEntity.setCouponAmount(new BigDecimal("0"));
        orderItemEntity.setIntegrationAmount(new BigDecimal("0"));
        // 计算实际金额
        BigDecimal originPrice = orderItemEntity.getSkuPrice()
                .multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        // 总额减去各种优惠
        BigDecimal decimal = originPrice.subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getPromotionAmount())
                .subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(decimal);
        return orderItemEntity;
    }

}
