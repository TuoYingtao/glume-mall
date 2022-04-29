import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * visible                        // 0 为显示，当设置 1 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    noCache: true                // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '',
    component: Layout,
    redirect: 'index',
    meta: {visible: 0},
    children: [{
        path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: 'Home',
        meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true }
      }]
  }, {
      path: "visitGoodsInfo",
      meta: {title: "订单详情", tagName: '上门-订单详情', icon: '',noCache: true, affix: false, visible: 1},
      component: (resolve) => require(['@/views/order/orderInfo/index'],resolve),
  }, {
    path: '/user',
    component: Layout,
    redirect: 'noredirect',
    children: [{
        path: 'profile',
        component: (resolve) => require(['@/views/system/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user', visible: 1}
      }]
  }, {
    path: '/login',
    name: "login",
    component: (resolve) => require(['@/views/login'], resolve),
    meta: {visible: 1}
  }, {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    meta: {visible: 1}
  }, {
    path: '/lockScreen',
    name: "lockScreen",
    component: (resolve) => require(['@/views/error/lockScreen'], resolve),
    meta: {visible: 1}
  },{
    path: '*',
    component: (resolve) => require(['@/views/error/4042'], resolve),
    meta: {visible: 1}
  },
]

// 因为可以动态的挂载路由，但是不能动态删除路由。所以才考略到，
// 在需要动态清空动态挂载路由的话，直接将一个新的路由对象赋值给旧的路由对象，这样就可以达到动态清除的工作
const createRouter = () => new Router({
  mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
const router = createRouter()
export default router
