import request from "@/utils/request"
import qs from "qs"

/* 获取属性列表 */
export function getAttr(catelogId,param) {
  return request({
    url: `/product/attr/list/${catelogId}`,
    method: "GET",
    params: param
  })
}
/* 添加属性列表 */
export function addAttr(data) {
  return request({
    url: "/product/attr/save",
    method: "POST",
    data: qs.stringify(data)
  })
}
/* 查询属性列表 */
export function queryAttr(attrId) {
  return request({
    url: `/product/attr/info/${attrId}`,
    method: "GET"
  })
}
/* 修改属性列表 */
export function amendAttr(data) {
  return request({
    url: "/product/attr/update",
    method: "PUT",
    data: qs.stringify(data)
  })
}
/* 删除属性列表 */
export function delAttr(data) {
  console.log(data)
  return request({
    url: `/product/attr/delete`,
    method: "DELETE",
    data: qs.stringify(data)
  })
}
