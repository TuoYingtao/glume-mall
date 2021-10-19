import router from './router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'

NProgress.configure({ showSpinner: false })

router.beforeEach((to, from, next) => {
    NProgress.start()
    if (getToken()) {
      next()
    } else {
      // 没有token
      if (to.path == '/login') {
        next();
      } else {
        next(`/login`) // 否则全部重定向到登录页
        NProgress.done()
      }
    }
})

router.afterEach(() => {
  NProgress.done()
})
