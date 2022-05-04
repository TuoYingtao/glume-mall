package com.glume.glumemall.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.glume.common.mybatis.PageUtils;
import com.glume.glumemall.admin.entity.RoleEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色
 *
 * @author tuoyingtao
 * @email tuoyingtao@163.com
 * @date 2021-10-18 09:31:33
 */
public interface RoleService extends IService<RoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    RoleEntity getRoleDetail(Long role_id);

    HashMap<String,Object> getInfoById(Long roleId);

    void updateRoleById(RoleEntity role);

    void removeRoleByIds(List<Long> roleIds);

    void saveMyRole(RoleEntity roleEntity);

    HashMap<String, Object> menuTreeService(Long roleId);

    RoleEntity getRoleDetail(String roleTag);

    List<RoleEntity> getRoleByIdBatchList(List<Long> ids);
}

