import request from '@/utils/request'
import qs from 'qs'

// 获取角色列表
export function roleList(param) {
  return request({
    url: `/role/list`,
    method: 'GET',
    params: param
  })
}

// 修改
export function updateMenu(data) {
  return request({
    url: `/role/update`,
    method: 'PUT',
    data: qs.stringify(data)
  })
}

// 添加
export function addMenu(data) {
  return request({
    url: `/role/save`,
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 查询
export function queryMenu(menuId) {
  return request({
    url: `/role/info/${menuId}`,
    method: 'GET',
  })
}

// 删除
export function delMenu(userId) {
  return request({
    url: `/role/delete/${userId}`,
    method: 'DELETE',
  })
}
