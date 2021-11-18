import request from '@/utils/request'
import qs from 'qs'

// 获取角色列表
export function memberLevelList(param) {
  return request({
    url: `/member/memberlevel/list`,
    method: 'GET',
    params: param
  })
}

// 查询
export function queryMemberLevel(menuId) {
  return request({
    url: `/member/memberlevel/info/${menuId}`,
    method: 'GET',
  })
}

// 修改
export function updateMemberLevel(data) {
  return request({
    url: `/member/memberlevel/update`,
    method: 'PUT',
    data: qs.stringify(data)
  })
}

// 添加
export function addMemberLevel(data) {
  return request({
    url: `/member/memberlevel/save`,
    method: 'POST',
    data: qs.stringify(data)
  })
}

// 删除
export function delMemberLevel(id) {
  return request({
    url: `/member/memberlevel/delete`,
    method: 'DELETE',
    data: qs.stringify(id)
  })
}
