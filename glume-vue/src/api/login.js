import request from '@/utils/request'
import qs from 'qs'

// 登录方法
export function login(login) {
  return request({
    url: '/user/login',
    method: 'POST',
    data: qs.stringify(login)
  })
}

export function captcha() {
  return request({
    url: '/user/captcha',
    method: 'GET',
  })
}
