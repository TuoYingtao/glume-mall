import request from "@/utils/request"
import qs from "qs"

export function getPriceList() {
  return request({
    url: "/price",
    method: "GET"
  })
}
/* 新增 */
export function addPriceItem(param) {
  return request({
    url: "/price",
    method: "POST",
    data: qs.stringify(param)
  })
}
/* 查询 */
export function selectPriceItem(param) {
  return request({
    url: "/price/detail",
    method: "GET",
    params: param
  })
}
/* 查询 */
export function amendPriceItem(param) {
  return request({
    url: "/price",
    method: "PUT",
    data: qs.stringify(param)
  })
}
/* 删除 */
export function deletePriceItem(id) {
  return request({
    url: "/price",
    method: "DELETE",
    data: qs.stringify(id)
  })
}
/* 黄金数据 */
export function getGoldList() {
  return request({
    url: "/precious_metal",
    method: "GET",
  })
}
/* 修改黄金价位 */
export function amendGoldItem(id,data) {
  return request({
    url: `/precious_metal/${id}/adjust`,
    method: "PUT",
    data: qs.stringify(data)
  })
}
