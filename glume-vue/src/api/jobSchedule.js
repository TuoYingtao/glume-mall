import request from "@/utils/request";
import qs from "qs"

export function listData(param) {
  return request({
    url: "/admin/schedulejob/list",
    method: "GET",
    params: param
  })
}

export function infoData(id) {
  return request({
    url: "/admin/schedulejob/info/" + id,
    method: "GET",
  })
}

export function saveData(data) {
  return request({
    url: "/admin/schedulejob/save",
    method: "POST",
    data: qs.stringify(data)
  })
}

export function editData(data) {
  return request({
    url: "/admin/schedulejob/update",
    method: "PUT",
    data: qs.stringify(data)
  })
}

export function deleteData(ids) {
  return request({
    url: "/admin/schedulejob/delete/" + ids,
    method: "DELETE",
  })
}

export function switchStatus(id) {
  return request({
    url: "/admin/schedulejob/status/" + id,
    method: "GET",
  })
}

export function runJob(id) {
  return request({
    url: "/admin/schedulejob/run",
    method: "GET",
    params: id
  })
}
export function groupAll() {
  return request({
    url: "/admin/schedulejob/group/all",
    method: "GET",
  })
}
