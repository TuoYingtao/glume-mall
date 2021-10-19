import request from "@/utils/request"
import qs from "qs"

export function getOrderList(param) {
  return request({
    url: "/orders",
    method: "GET",
    params: param
  })
}
/* 详情 */
export function getOrderInfo(param) {
  return request({
    url: "/orders/detail",
    method: "GET",
    params: param
  })
}
/* 汇款信息 */
export function getRemitInfo(param) {
  return request({
    url: "/payment",
    method: "GET",
    params: param
  })
}
/* 确认汇款 */
export function affirmRemit(param) {
  return request({
    url: "/orders",
    method: "PUT",
    data: qs.stringify(param)
  })
}
/* 奢侈品确认处理 */
export function affirmOrder(param) {
  return request({
    url: "/acceptance",
    method: "PUT",
    data: qs.stringify(param)
  })
}
/* 奢侈品详情 */
export function luxuryInfo(param) {
  return request({
    url: "/luxury",
    method: "GET",
    params: param
  })
}
/* 确认完成 */
export function affirmFinish(param) {
  return request({
    url: "/orders",
    method: "PUT",
    data: qs.stringify(param)
  })
}
