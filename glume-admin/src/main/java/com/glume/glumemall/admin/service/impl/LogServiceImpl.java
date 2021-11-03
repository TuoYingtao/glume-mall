package com.glume.glumemall.admin.service.impl;

import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.admin.dao.LogDao;
import com.glume.glumemall.admin.entity.LogEntity;
import com.glume.glumemall.admin.service.LogService;


@Service("logService")
public class LogServiceImpl extends ServiceImpl<LogDao, LogEntity> implements LogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LogEntity> page = this.page(
                new Query<LogEntity>().getPage(params),
                new QueryWrapper<LogEntity>()
        );

        return new PageUtils(page);
    }

}