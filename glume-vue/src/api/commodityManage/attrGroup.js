import request from "@/utils/request"
import qs from "qs"

/* 获取属性列表 */
export function getAttrGroup(catelogId,param) {
  return request({
    url: `/product/attrgroup/list/${catelogId}`,
    method: "GET",
    params: param
  })
}
/* 添加属性列表 */
export function addAttrGroup(data) {
  return request({
    url: "/product/category/save",
    method: "POST",
    data: qs.stringify(data)
  })
}
/* 查询属性列表 */
export function queryAttrGroup(attrGroupId) {
  return request({
    url: `/product/attrgroup/info/${attrGroupId}`,
    method: "GET"
  })
}
/* 修改属性列表 */
export function amendAttrGroup(data) {
  return request({
    url: "/product/category/update",
    method: "PUT",
    data: qs.stringify(data)
  })
}
/* 删除属性列表 */
export function delAttrGroup(catId) {
  return request({
    url: `/product/category/delete/${catId}`,
    method: "DELETE"
  })
}

/* 获取品牌型号数据 */
export function getBrandModel(param) {
  return request({
    url: `/models`,
    method: "GET",
    params: param
  })
}

/* 修改品牌型号数据 */
export function amendBrandModel(param) {
  return request({
    url: `/models`,
    method: "PUT",
    data: qs.stringify(param)
  })
}
