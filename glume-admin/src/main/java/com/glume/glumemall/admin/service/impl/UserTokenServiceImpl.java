package com.glume.glumemall.admin.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.common.utils.mybatis.PageUtils;
import com.glume.glumemall.common.utils.mybatis.Query;

import com.glume.glumemall.admin.dao.UserTokenDao;
import com.glume.glumemall.admin.entity.UserTokenEntity;
import com.glume.glumemall.admin.service.UserTokenService;


@Service("userTokenService")
public class UserTokenServiceImpl extends ServiceImpl<UserTokenDao, UserTokenEntity> implements UserTokenService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserTokenEntity> page = this.page(
                new Query<UserTokenEntity>().getPage(params),
                new QueryWrapper<UserTokenEntity>()
        );

        return new PageUtils(page);
    }

}