import request from '@/utils/request'
import qs from 'qs'

// 获取菜单列表
export function menuList(userId) {
  return request({
    url: `/menu/menu/${userId}`,
    method: 'GET',
  })
}

// 修改
export function updateMenu(userId) {
  return request({
    url: `/menu/menu/${userId}`,
    method: 'GET',
  })
}

// 添加
export function addMenu(userId) {
  return request({
    url: `/menu/menu/${userId}`,
    method: 'GET',
  })
}

// 删除
export function delMenu(userId) {
  return request({
    url: `/menu/menu/${userId}`,
    method: 'GET',
  })
}
