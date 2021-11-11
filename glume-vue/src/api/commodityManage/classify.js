import request from "@/utils/request"
import qs from "qs"

/* 获取分类数据 */
export function getBrandTree() {
  return request({
    url: "/product/category/list/tree",
    method: "GET",
  })
}
/* 添加分类数据 */
export function addBrandTree(data) {
  return request({
    url: "/product/category/save",
    method: "POST",
    data: qs.stringify(data)
  })
}
/* 查询分类数据 */
export function queryBrandTree(catId) {
  return request({
    url: `/product/category/info/${catId}`,
    method: "GET"
  })
}
/* 修改分类数据 */
export function amendBrandTree(data) {
  return request({
    url: "/product/category/update",
    method: "PUT",
    data: qs.stringify(data)
  })
}
/* 删除分类数据 */
export function delBrandTree(catId) {
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

/* 获取云存储 */
export function getOSSPolicy() {
  return request({
    url: "/thirdparty/oss/policy",
    method: "GET",
  })
}
