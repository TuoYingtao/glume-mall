import request from "@/utils/request"
import qs from "qs"

export function getData(param) {
  return request({
    url: "/admin/coupon/list/seckillPromotion",
    method: "GET",
    params: param,
  })
}
export function save(data) {
  return request({
    url: "/admin/coupon/save/seckillPromotion",
    method: "POST",
    data: qs.stringify(data),
  })
}
export function info(id) {
  return request({
    url: "/admin/coupon/info/seckillPromotion/" + id,
    method: "GET",
  })
}
export function update(data) {
  return request({
    url: "/admin/coupon/update/seckillPromotion",
    method: "PUT",
    data: qs.stringify(data),
  })
}
export function delData(id) {
  console.log(id)
  return request({
    url: "/admin/coupon/delete/seckillPromotion/" + id,
    method: "DELETE",
  })
}
