import request from "@/utils/request"
import qs from "qs"

export function getData(param) {
  return request({
    url: "/admin/coupon/list/seckillRelation",
    method: "GET",
    params: param,
  })
}
export function getPrductData() {
  return request({
    url: "/admin/coupon/list/all/product",
    method: "GET",
  })
}
export function listProductCategory() {
  return request({
    url: "/admin/coupon/list/category/product",
    method: "GET",
  })
}
export function promotionAndSession() {
  return request({
    url: "/admin/coupon/list/promotionandsession",
    method: "GET",
  })
}
export function save(data) {
  return request({
    url: "/admin/coupon/save/seckillRelation",
    method: "POST",
    data: qs.stringify(data),
  })
}
export function info(id) {
  return request({
    url: "/admin/coupon/info/seckillRelation/" + id,
    method: "GET",
  })
}
export function update(data) {
  return request({
    url: "/admin/coupon/update/seckillRelation",
    method: "PUT",
    data: qs.stringify(data),
  })
}
export function delData(id) {
  console.log(id)
  return request({
    url: "/admin/coupon/delete/seckillRelation/" + id,
    method: "DELETE",
  })
}
