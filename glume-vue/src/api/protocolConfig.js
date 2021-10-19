import request from "@/utils/request"
import qs from "qs"

export function getProtocolConfig() {
  return request({
    url: '/agreement',
    method: "GET",
  })
}
/* 修改 */
export function amendProtocolConfig(param) {
  return request({
    url: '/agreement',
    method: "PUT",
    data: qs.stringify(param)
  })
}
