import request from "@/utils/request";
import qs from "qs";

export function listData(param) {
  return request({
    url: "/admin/taskLog/list",
    method: "GET",
    params: param
  })
}

export function infoData(logId) {
  return request({
    url: "/admin/taskLog/info/" + logId,
    method: "GET",
  })
}

export function deleteData(ids) {
  return request({
    url: "/admin/taskLog/delete/" + ids,
    method: "DELETE",
  })
}

export function emptyLogData() {
  return request({
    url: "/admin/taskLog/emptyLog",
    method: "GET",
  })
}
