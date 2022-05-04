import request from '@/utils/request'
import qs from 'qs'

// 登录方法
export function login(login) {
  return request({
    url: '/admin/user/login',
    method: 'POST',
    data: qs.stringify(login)
  })
}

export function loginOut() {
  return request({
    url: '/admin/user/logout',
    method: 'GET',
  })
}

export function captcha() {
  return request({
    url: '/admin/user/captcha',
    method: 'GET',
  })
}
// 用户信息
export function getInfo () {
  return request({
    url: '/admin/user/info',
    method: 'GET'
  })
}
