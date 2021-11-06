import request from '@/utils/request'
import qs from 'qs'

// 获取菜单列表
export function menuList(userId) {
  return request({
    url: `/menu/list/${userId}`,
    method: 'GET',
  })
}

// 修改
export function updateMenu(data) {
  return request({
    url: `/menu/update`,
    method: 'PUT',
    data: qs.stringify(data)
  })
}

// 添加
export function addMenu(data) {
  return request({
    url: `/menu/save`,
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 查询
export function queryMenu(menuId) {
  return request({
    url: `/menu/info/${menuId}`,
    method: 'GET',
  })
}

// 删除
export function delMenu(userId) {
  return request({
    url: `/menu/delete/${userId}`,
    method: 'DELETE',
  })
}
