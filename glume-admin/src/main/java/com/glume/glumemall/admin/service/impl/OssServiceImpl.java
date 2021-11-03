package com.glume.glumemall.admin.service.impl;

import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.glume.glumemall.admin.dao.OssDao;
import com.glume.glumemall.admin.entity.OssEntity;
import com.glume.glumemall.admin.service.OssService;


@Service("ossService")
public class OssServiceImpl extends ServiceImpl<OssDao, OssEntity> implements OssService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OssEntity> page = this.page(
                new Query<OssEntity>().getPage(params),
                new QueryWrapper<OssEntity>()
        );

        return new PageUtils(page);
    }

}