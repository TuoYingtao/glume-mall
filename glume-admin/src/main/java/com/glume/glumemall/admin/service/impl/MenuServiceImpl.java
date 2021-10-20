package com.glume.glumemall.admin.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glume.glumemall.common.utils.PageUtils;
import com.glume.glumemall.common.utils.Query;

import com.glume.glumemall.admin.dao.MenuDao;
import com.glume.glumemall.admin.entity.MenuEntity;
import com.glume.glumemall.admin.service.MenuService;


@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuDao, MenuEntity> implements MenuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MenuEntity> page = this.page(
                new Query<MenuEntity>().getPage(params),
                new QueryWrapper<MenuEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取菜单项
     * @param menuId
     * @return
     */
    @Override
    public MenuEntity getMenuDetail(Long menuId) {
        MenuEntity menuEntity = baseMapper.selectById(menuId);
        return menuEntity;
    }

}