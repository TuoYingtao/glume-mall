import axios from 'axios'
import { Notification, MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken, removeToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import router from '../router'

let isMessageBox = false;
let cancelToken = axios.CancelToken;
const source = cancelToken.source();

axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

/** 创建axios实例 */
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: process.env.VUE_APP_BASE_API,
  // 超时
  timeout: 10000,
  cancelToken: source.token,
})

/** request拦截器 */
service.interceptors.request.use(config => {
  // 是否需要设置 token
  if (store.getters.token) {
    config.headers['Authorization'] = getToken();
  }
  return config
}, error => {
    Promise.reject(error)
})

/** 响应拦截器 */
service.interceptors.response.use(res => {
  // 未设置状态码则默认成功状态
  const code = res.data.code || 200;
  // 获取错误信息
  const message = errorCode[code] || res.data.msg || errorCode['default'];
  if (code === 200) {
    return res.data
  }else {
    Notification.error(message);
  }
},error => {
  // 未设置状态码则默认成功状态
  const code = error.response.status;
  // 获取错误信息
  const message = errorCode[code] || error.response.data.error || error.response.data.msg || errorCode['default'];
  switch (error.response.status) {
    case 401:
      source.cancel(error);
      removeToken();
      if (isMessageBox) {
        MessageBox.close();
      }
      MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        isMessageBox = false;
        MessageBox.close();
        router.push({path: "/login"})
      })
      break;
    default:
      Message({ message: message, type: 'error',duration: 5 * 1000})
      return Promise.reject(error)
  }
})

export default service
