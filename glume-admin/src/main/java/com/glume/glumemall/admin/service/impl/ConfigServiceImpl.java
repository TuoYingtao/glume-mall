package com.glume.glumemall.admin.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.common.utils.Query;

import com.glume.glumemall.admin.dao.ConfigDao;
import com.glume.glumemall.admin.entity.ConfigEntity;
import com.glume.glumemall.admin.service.ConfigService;


@Service("configService")
public class ConfigServiceImpl extends ServiceImpl<ConfigDao, ConfigEntity> implements ConfigService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ConfigEntity> page = this.page(
                new Query<ConfigEntity>().getPage(params),
                new QueryWrapper<ConfigEntity>()
        );

        return new PageUtils(page);
    }

}