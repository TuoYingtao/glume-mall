import axios from 'axios'
import { Notification, MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken, removeToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import router from '../router'

let isMessageBox = false;

axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: process.env.VUE_APP_BASE_API,
  // 超时
  timeout: 10000,
  // headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'}
})

// request拦截器
service.interceptors.request.use(config => {
  // 是否需要设置 token
  if (store.getters.token) {
    config.headers['Authorization'] = getToken();
  }
  return config
}, error => {
    Promise.reject(error)
})
// 响应拦截器
service.interceptors.response.use(res => {
  // 未设置状态码则默认成功状态
  const code = res.data.code || 200;
  // 获取错误信息
  const msg = errorCode[code] || res.data.data || errorCode['default']
  if (code === 200) {
    return res.data
  }else if (code == 500) {
    Notification.error({ title: res.data.msg })
  } else {
    Notification.error({ title: msg })
  }
},error => {
  switch (error.response.status) {
    case 401:
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
    case 408:
      Message({ message: "系统接口请求超时", type: 'error',duration: 5 * 1000})
      return Promise.reject(error)
    case 500:
      Message({ message: "后端接口连接异常", type: 'error',duration: 5 * 1000})
      return Promise.reject(error)
    case 501:
      Message({ message: error.response.data.msg, type: 'error',duration: 5 * 1000})
      return Promise.reject(error)
    default: return Promise.reject(error)
  }
})

export default service
