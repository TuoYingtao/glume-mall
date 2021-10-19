import request from "@/utils/request"

export function getStatistics(param) {
  return request({
    url: "/statistics",
    method: "GET",
    params: param
  })
}
