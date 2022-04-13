import request from '@/utils/request'
import qs from "qs"

/** 用户列表 */
export function userList(param) {
  return request({
    url: '/admin/user/list',
    method: 'GET',
    params: param
  })
}
/** 用户信息 */
export function getUserInfo(id) {
  return request({
    url: '/admin/user/info/' + id,
    method: 'GET'
  })
}
/** 保存 */
export function saveUserInfo(data) {
  return request({
    url: '/admin/user/save',
    method: 'POST',
    data: qs.stringify(data)
  })
}
/** 修改 */
export function updateUserInfo(data) {
  return request({
    url: '/admin/user/update',
    method: 'PUT',
    data: qs.stringify(data)
  })
}
/** 删除 */
export function deleteUserInfo(ids) {
  return request({
    url: '/admin/user/delete/' + ids,
    method: 'DELETE'
  })
}
/** 重置密码 */
export function resetPassword(userId,data) {
  return request({
    url: `/admin/user/reset/${userId}/password`,
    method: 'POST',
    data: qs.stringify(data)
  })
}
