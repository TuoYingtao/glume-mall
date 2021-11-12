import request from "@/utils/request"
import qs from "qs"

/* 获取品牌列表 */
export function brandList(param) {
  return request({
    url: `/product/brand/list`,
    method: "GET",
    params: param
  })
}
/* 添加品牌列表 */
export function addbrand(data) {
  return request({
    url: "/product/brand/save",
    method: "POST",
    data: qs.stringify(data)
  })
}
/* 查询品牌列表 */
export function querybrand(brandId) {
  return request({
    url: `/product/brand/info/${brandId}`,
    method: "GET"
  })
}
/* 修改品牌列表 */
export function amendbrand(data) {
  return request({
    url: "/product/brand/update",
    method: "PUT",
    data: qs.stringify(data)
  })
}
/* 删除品牌列表 */
export function delbrand(brandId) {
  return request({
    url: `/product/brand/delete/${brandId}`,
    method: "DELETE",
  })
}
