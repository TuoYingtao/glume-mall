import request from "@/utils/request"
import qs from "qs"

export function getFeedback() {
  return request({
    url: "/feedback",
    method: "GET"
  })
}

export function consultFeedback(param) {
  return request({
    url: "/feedback",
    method: "PUT",
    data: qs.stringify(param)
  })
}
