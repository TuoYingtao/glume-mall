package com.glume.glumemall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.admin.dao.BlackListDao;
import com.glume.glumemall.admin.entity.BlackListEntity;
import com.glume.glumemall.admin.service.BlackListService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 黑名单表
 * @author tuoyingtao
 * @create 2022-04-06 15:14
 */
@Service("blackListService")
public class BlackListServiceImpl extends ServiceImpl<BlackListDao, BlackListEntity> implements BlackListService {

    @Override
    public void userTokenVerify(String token) {
        BlackListEntity blackListEntity = baseMapper.selectOne(new QueryWrapper<BlackListEntity>().eq("token", token));
        if (StringUtils.isNotNull(blackListEntity)) {
            throw new RuntimeException();
        }
    }
}
