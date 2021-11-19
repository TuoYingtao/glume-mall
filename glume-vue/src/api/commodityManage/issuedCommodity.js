import request from "@/utils/request"
import qs from "qs"

/* 获取分类下的所有分组以及它的属性 */
export function getAttrGroupWithAttrs(catelogId) {
  return request({
    url: `/product/attrgroup/${catelogId}/withattr`,
    method: "GET",
  })
}
