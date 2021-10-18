package com.glume.glumemall.admin.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.common.utils.Query;

import com.glume.glumemall.admin.dao.RoleMenuDao;
import com.glume.glumemall.admin.entity.RoleMenuEntity;
import com.glume.glumemall.admin.service.RoleMenuService;


@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDao, RoleMenuEntity> implements RoleMenuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RoleMenuEntity> page = this.page(
                new Query<RoleMenuEntity>().getPage(params),
                new QueryWrapper<RoleMenuEntity>()
        );

        return new PageUtils(page);
    }

}