import request from "@/utils/request"
import qs from "qs"

/* 列表 */
export function getInfoConfigList(param) {
  return request({
    url: "/clerk",
    method: "GET",
    params: param
  })
}
/* 查询 */
export function getInfoDetail(param) {
  return request({
    url: "/clerk/detail",
    method: "GET",
    params: param
  })
}
/* 添加 */
export function addInfoConfigList(param) {
  return request({
    url: "/clerk",
    method: "POST",
    data: qs.stringify(param)
  })
}
/* 修改 */
export function amendInfoConfigList(param) {
  return request({
    url: "/clerk",
    method: "PUT",
    data: qs.stringify(param)
  })
}
/* 删除 */
export function deleteInfoConfigList(param) {
  return request({
    url: "/clerk",
    method: "DELETE",
    data: qs.stringify(param)
  })
}

/* 获取客服号码 */
export function getReception() {
  return request({
    url: "/reception",
    method: "GET"
  })
}

/* 修改客服号码 */
export function amendReception(pram) {
  return request({
    url: "/reception",
    method: "PUT",
    data: qs.stringify(pram)
  })
}
