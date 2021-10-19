import request from '@/utils/request'

// 首页信息
export function getHome () {
  return request({
    url: '/index_info',
    method: 'GET'
  })
}

/* 订单信息 */
export function getOrderList (param) {
  return request({
    url: '/index_data',
    method: 'GET',
    params: param
  })
}
