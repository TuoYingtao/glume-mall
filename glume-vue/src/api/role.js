import request from '@/utils/request'
import qs from 'qs'

// 获取角色列表
export function roleList(param) {
  return request({
    url: `/admin/role/list`,
    method: 'GET',
    params: param
  })
}

// 角色下拉菜单权限列表
export function getTreeSelect(roleId) {
  return request({
    url: `/admin/role/menuTree`,
    method: 'GET',
    params: roleId
  })
}

// 修改
export function updateRole(data) {
  return request({
    url: `/admin/role/update`,
    method: 'PUT',
    data: qs.stringify(data)
  })
}

// 添加
export function addRole(data) {
  return request({
    url: `/admin/role/save`,
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 查询
export function queryRole(menuId) {
  return request({
    url: `/admin/role/info/${menuId}`,
    method: 'GET',
  })
}

// 删除
export function delRole(userId) {
  return request({
    url: `/admin/role/delete/${userId}`,
    method: 'DELETE',
  })
}
