import request from "@/utils/request"
import qs from "qs"

export function getData(param) {
  return request({
    url: "/coupon/seckillpromotion/list",
    method: "GET",
    params: param,
  })
}
export function save(data) {
  return request({
    url: "/coupon/seckillpromotion/save",
    method: "POST",
    data: qs.stringify(data),
  })
}
export function info(id) {
  return request({
    url: "/coupon/seckillpromotion/info/" + id,
    method: "GET",
  })
}
export function update(data) {
  return request({
    url: "/coupon/seckillpromotion/update",
    method: "PUT",
    data: qs.stringify(data),
  })
}
export function delData(id) {
  console.log(id)
  return request({
    url: "/coupon/seckillpromotion/delete/" + id,
    method: "DELETE",
  })
}
