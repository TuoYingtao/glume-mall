package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.RoleMenuEntity;

import java.util.List;
import java.util.Map;

/**
 * 角色与菜单对应关系
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
public interface RoleMenuService extends IService<RoleMenuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<RoleMenuEntity> getRoleMenuEntity(Long roleId);

    List<Long> getRoleMenuIds(Long roleId);

    List<String> getRoleAuthorityInfo(Long roleId);

    void removeMenuRoleByIds(List<Long> menu_id);

    void removeRoleMenuByIds(List<Long> roleIds);

    void saveMyBatch(Long roleId, Long[] menuIds);

    void updateBatchRoleMenu(Long roleId,Long[] menuIds);

    List<RoleMenuEntity> getRoleMenuBatch(List<Long> roleIds);
}

