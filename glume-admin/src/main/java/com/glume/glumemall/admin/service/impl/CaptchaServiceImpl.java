package com.glume.glumemall.admin.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.common.utils.mybatis.PageUtils;
import com.glume.glumemall.common.utils.mybatis.Query;

import com.glume.glumemall.admin.dao.CaptchaDao;
import com.glume.glumemall.admin.entity.CaptchaEntity;
import com.glume.glumemall.admin.service.CaptchaService;


@Service("captchaService")
public class CaptchaServiceImpl extends ServiceImpl<CaptchaDao, CaptchaEntity> implements CaptchaService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CaptchaEntity> page = this.page(
                new Query<CaptchaEntity>().getPage(params),
                new QueryWrapper<CaptchaEntity>()
        );

        return new PageUtils(page);
    }

}