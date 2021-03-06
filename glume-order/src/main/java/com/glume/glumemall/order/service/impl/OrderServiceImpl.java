package com.glume.glumemall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.glume.common.core.constant.OrderConstant;
import com.glume.common.core.exception.servlet.NoStockException;
import com.glume.common.core.to.MemberRespTo;
import com.glume.common.core.to.mq.OrderTo;
import com.glume.common.core.to.mq.SeckillOrderTo;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.RedisUtils;
import com.glume.common.core.utils.SpringUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.order.entity.OrderItemEntity;
import com.glume.common.core.enums.OrderStatusEnum;
import com.glume.glumemall.order.entity.PaymentInfoEntity;
import com.glume.glumemall.order.feign.CartFeignService;
import com.glume.glumemall.order.feign.MemberFeignService;
import com.glume.glumemall.order.feign.ProductFeignService;
import com.glume.glumemall.order.feign.WareFeignService;
import com.glume.glumemall.order.interceptor.LoginUserInterceptor;
import com.glume.glumemall.order.service.OrderItemService;
import com.glume.glumemall.order.service.PaymentInfoService;
import com.glume.glumemall.order.to.OrderCreateTo;
import com.glume.glumemall.order.vo.*;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
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
    PaymentInfoService paymentInfoService;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        OrderEntity orderEntity = baseMapper.selectOne(new QueryWrapper<OrderEntity>().eq("order_sn",orderSn));
        return orderEntity;
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public PageUtils queryPageWithItem(Map<String, Object> params) {
        MemberRespTo memberRespTo = LoginUserInterceptor.toThreadLocal.get();
        IPage<OrderEntity> orderEntityIPage = baseMapper.selectPage(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
                        .eq("member_id", memberRespTo.getId()).orderByDesc("id"));
        List<OrderEntity> entityList = orderEntityIPage.getRecords().stream().map(orderEntity -> {
            List<OrderItemEntity> orderItemEntityList = orderItemService.list(new QueryWrapper<OrderItemEntity>().eq("order_sn", orderEntity.getOrderSn()));
            orderEntity.setOrderItemEntities(orderItemEntityList);
            return orderEntity;
        }).collect(Collectors.toList());
        orderEntityIPage.setRecords(entityList);
        return new PageUtils(orderEntityIPage);
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public String handlerPayResult(PayAsyncVo payAsyncVo) {
        PaymentInfoEntity paymentInfoEntity = new PaymentInfoEntity();
        paymentInfoEntity.setAlipayTradeNo(payAsyncVo.getTrade_no());
        paymentInfoEntity.setOrderSn(payAsyncVo.getOut_trade_no());
        paymentInfoEntity.setPaymentStatus(payAsyncVo.getTrade_status());
        paymentInfoEntity.setCallbackTime(payAsyncVo.getNotify_time());
        // ??????????????????
        paymentInfoService.save(paymentInfoEntity);
        // ??????????????????
        if (payAsyncVo.getTrade_status().equals("TRADE_SUCCESS") ||
                payAsyncVo.getTrade_status().equals("TRADE_FINISHED")) {
            String orderSn = payAsyncVo.getOut_trade_no();
            baseMapper.updateOrderStatus(orderSn,OrderStatusEnum.PAYED.getCode());
        }
        return "success";
    }

    @Override
    public void closeOrder(OrderEntity orderEntity) {
        OrderEntity entity = baseMapper.selectById(orderEntity.getId());
        // ?????????????????????????????????????????????????????????
        if (entity.getStatus() == OrderStatusEnum.CREATE_NEW.getCode()) {
            OrderEntity newOrderData = new OrderEntity();
            newOrderData.setId(entity.getId());
            newOrderData.setStatus(OrderStatusEnum.CANCLED.getCode());
            baseMapper.updateById(newOrderData);
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(entity,orderTo);
            try {
                // TODO ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                rabbitTemplate.convertAndSend("order-event-exchange","order.release.other.#",orderTo);
            } catch (AmqpException e) {
                // TODO ?????????????????????????????????????????????
                e.printStackTrace();
            }
        }
    }

    @Override
    public PayVo getOrderPay(String orderSn) {
        PayVo payVo = new PayVo();
        OrderEntity orderByOrderSn = this.getOrderByOrderSn(orderSn);
        payVo.setOut_trade_no(orderByOrderSn.getOrderSn());
        // ??????????????????????????????????????????
        BigDecimal money = orderByOrderSn.getPayAmount().setScale(2, BigDecimal.ROUND_UP);
        payVo.setTotal_amount(money.toString());
        List<OrderItemEntity> orderItemEntities = orderItemService.list(new QueryWrapper<OrderItemEntity>().eq("order_sn", orderSn));
        payVo.setSubject(orderItemEntities.get(0).getSkuName());
        payVo.setBody(orderItemEntities.get(0).getSkuAttrsVals());
        return payVo;
    }

    /**
     * ????????????????????????????????????
     * @return
     */
    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        // TODO ?????????????????????????????????????????????
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        MemberRespTo memberRespTo = LoginUserInterceptor.toThreadLocal.get();
        /** ????????????????????????????????????????????????????????? */
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // ??????????????????????????????
        CompletableFuture<Void> feignMemberAddress = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> memberAddressList = memberFeignService.getMemberAddressList(memberRespTo.getId());
            orderConfirmVo.setAddress(memberAddressList);
        }, executor);

        // ???????????????????????????????????????
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

        // ????????????
        Integer integration = memberRespTo.getIntegration();
        orderConfirmVo.setIntegration(integration);

        // ????????????????????????
        String token = UUID.randomUUID().toString().replace("-", "");
        RedisUtils redisUtils = SpringUtils.getBean(RedisUtils.class);
        redisUtils.set(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespTo.getId(),token,60 * 30);
        orderConfirmVo.setOrderToken(token);
        CompletableFuture.allOf(feignMemberAddress,feignCartItem).get();
        return orderConfirmVo;
    }

    /**
     * ?????????????????? + ??????????????? ??????????????????????????????????????????
     */
    @Override
    @Transactional
//    @GlobalTransactional // seata ????????????????????????????????????
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo) {
        confirmVoThreadLocal.set(orderSubmitVo);
        SubmitOrderResponseVo submitOrderResponseVo = new SubmitOrderResponseVo();
        MemberRespTo memberRespTo = LoginUserInterceptor.toThreadLocal.get();
        submitOrderResponseVo.setCode(0);
        /** ???????????? */
        String orderToken = orderSubmitVo.getOrderToken();
        RedisUtils redisUtils = SpringUtils.getBean(RedisUtils.class);
        long result = redisUtils.executeLuaDel(Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespTo.getId()), orderToken);
        if (result == 0) {
            // ?????????????????????
            submitOrderResponseVo.setCode(1);
        } else {
            /** ???????????? */
            // 1?????????????????????
            OrderCreateTo order = createOrder();
            // 2?????????
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrice = orderSubmitVo.getPayPrice();
            if (Math.abs(payAmount.subtract(payPrice).doubleValue()) < 0.01) {
                // 3???????????????
                saveOrder(order);
                // 4???????????????????????????????????????????????????
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
                    // ??????????????????????????????????????????
//                    int i = 10/0;
                    // TODO ??????????????????
                    rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",order.getOrder());
                } else {
                    // ??????????????????
                    submitOrderResponseVo.setCode(3);
                    throw new NoStockException();
                }
            } else {
                // ??????????????????
                submitOrderResponseVo.setCode(2);
            }
        }
        return submitOrderResponseVo;
    }

    /** ?????????????????? */
    @Override
    @Transactional
    public void createSeckillOrder(SeckillOrderTo seckillOrderTo) {
        // TODO ????????????????????????????????????
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(seckillOrderTo.getOrderSn());
        orderEntity.setMemberId(seckillOrderTo.getMemberId());
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        BigDecimal num = new BigDecimal(seckillOrderTo.getNum().toString());
        BigDecimal payAmount = seckillOrderTo.getSeckillPrice().multiply(num);
        orderEntity.setPayAmount(payAmount);
        baseMapper.insert(orderEntity);

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderSn(seckillOrderTo.getOrderSn());
        orderItemEntity.setRealAmount(payAmount);
        orderItemEntity.setSkuQuantity(seckillOrderTo.getNum());
        orderItemService.save(orderItemEntity);
    }

    /** ?????????????????? */
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

    /** ???????????? */
    public OrderCreateTo createOrder() {
        OrderCreateTo orderCreateTo = new OrderCreateTo();
        // ???????????????
        String orderSn = IdWorker.getTimeId();
        // ????????????
        OrderEntity orderEntity = buildOrder(orderSn);
        // ???????????????
        List<OrderItemEntity> orderItemEntities = buildOrderItems(orderSn);
        // ??????????????????????????????
        computePrice(orderEntity,orderItemEntities);
        // ????????????
        orderCreateTo.setOrder(orderEntity);
        // ???????????????
        orderCreateTo.setOrderItems(orderItemEntities);

        return orderCreateTo;
    }

    /** ???????????? */
    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        BigDecimal total = new BigDecimal("0");
        BigDecimal coupon = new BigDecimal("0");
        BigDecimal promotion = new BigDecimal("0");
        BigDecimal integration = new BigDecimal("0");
        BigDecimal giveIntegration = new BigDecimal("0");
        BigDecimal giveGrowth = new BigDecimal("0");
        /* ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????? */
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            // ??????????????????
            total = total.add(orderItemEntity.getRealAmount());
            // ???????????????
            coupon = coupon.add(orderItemEntity.getCouponAmount());
            // ????????????
            promotion = promotion.add(orderItemEntity.getPromotionAmount());
            // ????????????
            integration = integration.add(orderItemEntity.getIntegrationAmount());
            // ???????????????
            giveIntegration = giveIntegration.add(new BigDecimal(orderItemEntity.getGiftIntegration().toString()));
            // ??????????????????
            giveGrowth = giveGrowth.add(new BigDecimal(orderItemEntity.getGiftGrowth().toString()));
        }
        // ??????????????????
        orderEntity.setTotalAmount(total);
        // ????????????????????????
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        // ?????????????????????
        orderEntity.setCouponAmount(coupon);
        // ??????????????????
        orderEntity.setPromotionAmount(promotion);
        // ??????????????????
        orderEntity.setIntegrationAmount(integration);
        // ??????????????????
        orderEntity.setIntegration(giveIntegration.intValue());
        // ?????????????????????
        orderEntity.setGrowth(giveGrowth.intValue());
    }

    /** ?????????????????? */
    private OrderEntity buildOrder(String orderSn) {
        MemberRespTo memberRespTo = LoginUserInterceptor.toThreadLocal.get();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(orderSn);
        orderEntity.setMemberId(memberRespTo.getId());
        // ????????????????????????
        OrderSubmitVo orderSubmitVo = confirmVoThreadLocal.get();
        R fare = wareFeignService.getFare(orderSubmitVo.getAddrId());
        FareVo fareResp = fare.getData(new TypeReference<FareVo>() {
        });
        // ??????????????????
        orderEntity.setFreightAmount(fareResp.getFare());
        // ?????????????????????
        orderEntity.setReceiverCity(fareResp.getAddress().getCity());
        orderEntity.setReceiverDetailAddress(fareResp.getAddress().getDetailAddress());
        orderEntity.setReceiverName(fareResp.getAddress().getName());
        orderEntity.setReceiverPhone(fareResp.getAddress().getPhone());
        orderEntity.setReceiverPostCode(fareResp.getAddress().getPostCode());
        orderEntity.setReceiverProvince(fareResp.getAddress().getProvince());
        orderEntity.setReceiverRegion(fareResp.getAddress().getRegion());
        // ??????????????????
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        // ??????????????????????????????
        orderEntity.setAutoConfirmDay(7);
        // ????????????
        orderEntity.setDeleteStatus(0);

        return orderEntity;
    }

    /** ??????????????????????????? */
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

    /** ?????????????????????????????? */
    private OrderItemEntity buildOrderItem(OrderItemVo orderItemVo) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        // ??????SPU??????
        Long skuId = orderItemVo.getSkuId();
        R spuInfoResp = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo spuInfoRespData = spuInfoResp.getData(new TypeReference<SpuInfoVo>() {
        });
        orderItemEntity.setSpuId(spuInfoRespData.getId());
        orderItemEntity.setSpuName(spuInfoRespData.getSpuName());
        orderItemEntity.setSpuBrand(spuInfoRespData.getBrandId().toString());
        orderItemEntity.setCategoryId(spuInfoRespData.getCatalogId());
        // ??????SKU??????
        orderItemEntity.setSkuId(orderItemVo.getSkuId());
        orderItemEntity.setSkuName(orderItemVo.getTitle());
        orderItemEntity.setSkuPic(orderItemVo.getImage());
        orderItemEntity.setSkuPrice(orderItemVo.getPrice());
        String skuAttr = org.springframework.util.StringUtils.collectionToDelimitedString(orderItemVo.getSkuAttr(), ";");
        orderItemEntity.setSkuAttrsVals(skuAttr);
        orderItemEntity.setSkuQuantity(orderItemVo.getCount());
        // ????????????
        BigDecimal count = new BigDecimal(orderItemVo.getCount().toString());
        orderItemEntity.setGiftGrowth(orderItemVo.getPrice().multiply(count).intValue());
        orderItemEntity.setGiftIntegration(orderItemVo.getPrice().multiply(count).intValue());
        // ?????????????????????
        orderItemEntity.setPromotionAmount(new BigDecimal("0"));
        orderItemEntity.setCouponAmount(new BigDecimal("0"));
        orderItemEntity.setIntegrationAmount(new BigDecimal("0"));
        // ??????????????????
        BigDecimal originPrice = orderItemEntity.getSkuPrice()
                .multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        // ????????????????????????
        BigDecimal decimal = originPrice.subtract(orderItemEntity.getCouponAmount())
                .subtract(orderItemEntity.getPromotionAmount())
                .subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(decimal);
        return orderItemEntity;
    }

}
