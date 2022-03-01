import request from "@/utils/request"
import qs from "qs"

export function getData(param) {
  return request({
    url: "/coupon/seckillsession/list",
    method: "GET",
    params: param,
  })
}
