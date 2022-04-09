package com.glume.glumemall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.common.mybatis.PageUtils;
import com.glume.common.mybatis.Query;
import com.glume.glumemall.admin.dao.LoginDao;
import com.glume.glumemall.admin.entity.LoginEntity;
import com.glume.glumemall.admin.service.LoginService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 在线列表
 * @author tuoyingtao
 * @create 2022-04-06 15:27
 */
@Service("loginService")
public class LoginServiceImpl extends ServiceImpl<LoginDao, LoginEntity> implements LoginService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LoginEntity> page = this.page(
                new Query<LoginEntity>().getPage(params),
                new QueryWrapper<LoginEntity>());

        return new PageUtils(page);
    }
}
