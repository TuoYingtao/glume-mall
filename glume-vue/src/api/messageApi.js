import request from "@/utils/request"
import qs from "qs"

/* 获取通知 */
export function getMessageList(param) {
  return request({
    url: "/notice",
    method: "GET",
    params: param
  })
}
/* 添加通知 */
export function addMessageItem(param) {
  return request({
    url: "/notice",
    method: "POST",
    data: qs.stringify(param)
  })
}
/* 查看通知 */
export function queryMessageItem(id) {
  return request({
    url: "/notice/detail",
    method: "GET",
    params: id
  })
}
/* 修改通知 */
export function amendMessageItem(param) {
  return request({
    url: "/notice",
    method: "PUT",
    data: qs.stringify(param)
  })
}
/* 删除通知 */
export function deleteMessageItem(param) {
  return request({
    url: "/notice",
    method: "DELETE",
    data: qs.stringify(param)
  })
}
