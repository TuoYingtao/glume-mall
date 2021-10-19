import Vue from "vue"
import { parseTime, resetForm, addDateRange, download, handleTree, HTMLTitle } from "@/utils/tuo.js";

// 全局方法挂载
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.download = download
Vue.prototype.handleTree = handleTree
Vue.prototype.HTMLTitle = HTMLTitle

Vue.prototype.msgSuccess = function (msg) {
  this.$message({ showClose: true, message: msg, type: "success",  duration: 1500 });
}
Vue.prototype.msgError = function (msg) {
  this.$message({ showClose: true, message: msg, type: "error",  duration: 1500 });
}
Vue.prototype.msgInfo = function (msg) {
  this.$message.info(msg);
}
Vue.prototype.notSuccess = function(not) {
  this.$notify({ showClose: true, title: "成功", message: not, type: "success", duration: 1500 });
}
Vue.prototype.notError = function (not) {
  this.$notify({ showClose: true, title: "失败", message: not, type: "error", duration: 1500 })
}
Vue.prototype.notWarning = function (not) {
  this.$notify({ title: '警告', message: not, type: 'warning', duration: 1500 });
}
Vue.prototype.notInfo = function (not) {
  this.$notify.info({ title: '消息', message: not, duration: 1500 });
}
