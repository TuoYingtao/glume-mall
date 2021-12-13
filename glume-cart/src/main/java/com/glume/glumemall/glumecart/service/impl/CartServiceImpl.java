package com.glume.glumemall.glumecart.service.impl;

import com.glume.glumemall.glumecart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author tuoyingtao
 * @create 2021-12-13 10:05
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;
}

