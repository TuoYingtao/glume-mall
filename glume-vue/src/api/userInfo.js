import request from '@/utils/request'
import qs from "qs"

/* 用户列表 */
export function userList(param) {
  return request({
    url: '/admin/users',
    method: 'GET',
    params: param
  })
}
/* 用户添加店员 */
export function setCompany(param) {
  return request({
    url: '/admin/users',
    method: 'PATCH',
    data: qs.stringify(param)
  })
}
