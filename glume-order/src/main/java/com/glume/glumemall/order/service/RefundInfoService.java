package com.glume.glumemall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-13 15:34:34
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

