package com.glume.glumemall.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.constant.RedisConstant;
import com.glume.glumemall.admin.dao.BlackListDao;
import com.glume.glumemall.admin.entity.BlackListEntity;
import com.glume.glumemall.admin.service.BlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 黑名单表
 * @author tuoyingtao
 * @create 2022-04-06 15:14
 */
@Service("blackListService")
public class BlackListServiceImpl extends ServiceImpl<BlackListDao, BlackListEntity> implements BlackListService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void userTokenVerify(String token, String username) {
        BoundSetOperations setOps = redisTemplate.boundSetOps(RedisConstant.BLACKLIST_KEY + username);
        if (setOps.isMember(token)) {
            throw new RuntimeException();
        }
//        BlackListEntity blackListEntity = baseMapper.selectOne(new QueryWrapper<BlackListEntity>().eq("token", token));
//        if (StringUtils.isNotNull(blackListEntity)) {
//            throw new RuntimeException();
//        }
    }
}
