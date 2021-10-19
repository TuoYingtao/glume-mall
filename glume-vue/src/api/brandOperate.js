import request from "@/utils/request"
import qs from "qs"

/* 获取品牌数据 */
export function getBrandTree() {
  return request({
    url: "/product",
    method: "GET"
  })
}
/* 修改品牌排序 */
export function amendBrandSort(param) {
  return request({
    url: "/product",
    method: "PUT",
    data: qs.stringify(param)
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

/** 设置价格 */
export function getPriceList(param) {
  return request({
    url: "/model_price",
    method: "GET",
    params: param
  })
}
/* 新增 */
export function addPriceItem(param) {
  return request({
    url: "/model_price",
    method: "POST",
    data: qs.stringify(param)
  })
}
/* 查询 */
export function selectPriceItem(param) {
  return request({
    url: "/model_price/detail",
    method: "GET",
    params: param
  })
}
/* 查询 */
export function amendPriceItem(param) {
  return request({
    url: "/model_price",
    method: "PUT",
    data: qs.stringify(param)
  })
}
/* 删除 */
export function deletePriceItem(id) {
  return request({
    url: "/model_price",
    method: "DELETE",
    data: qs.stringify(id)
  })
}
