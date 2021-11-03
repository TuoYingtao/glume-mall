package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.MenuEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
public interface MenuService extends IService<MenuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    MenuEntity getMenuDetail(Long menuId);

    void addMenuItem(MenuEntity menuEntity,String username);

    void updateMenuItem(MenuEntity menuEntity, String username);

    List<MenuEntity> getMenuList(Long userId, @NotNull Boolean specific);
}

