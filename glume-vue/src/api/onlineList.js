import request from "@/utils/request";
import qs from "qs";

export function list(param) {
  return request({
    url: "/admin/login/online/list",
    method: "GET",
    params: param
  })
}
export function forceKikeOut(param) {
  return request({
    url: "/admin/login/online/forceKikeOut",
    method: "GET",
    params: param
  })
}
