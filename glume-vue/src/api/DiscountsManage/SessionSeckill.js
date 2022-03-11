import request from "@/utils/request"
import qs from "qs"

export function getData(param) {
  return request({
    url: "/coupon/seckillsession/list",
    method: "GET",
    params: param,
  })
}
export function save(data) {
  return request({
    url: "/coupon/seckillsession/save",
    method: "POST",
    data: qs.stringify(data),
  })
}
export function info(id) {
  return request({
    url: "/coupon/seckillsession/info/" + id,
    method: "GET",
  })
}
export function update(data) {
  return request({
    url: "/coupon/seckillsession/update",
    method: "PUT",
    data: qs.stringify(data),
  })
}
export function delData(id) {
  return request({
    url: "/coupon/seckillsession/delete/" + id,
    method: "DELETE",
  })
}
