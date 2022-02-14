package com.glume.glumemall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单结算地址、运费信息
 * @author tuoyingtao
 * @create 2022-02-12 14:51
 */
@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;
}
