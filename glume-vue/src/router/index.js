import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
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
    children: [{
        path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: 'Home',
        meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true }
      }]
  },
  // {
  //   path: "/userInfo",
  //   component: Layout,
  //   meta: {title: "用户列表", icon: 'user'},
  //   children:[{
  //     path: "user",
  //     name: "UserManager",
  //     component: (resolve) => require(['@/views/system/userInfo/index'],resolve),
  //     meta: { title: '用户管理', icon: 'user', noCache: true, affix: false }
  //   }],
  // }, {
  //   path: "/visitOrder",
  //   component: Layout,
  //   meta: { title: '上门订单', icon: 'fl-jia', noCache: true, affix: false },
  //   children:[{
  //     path: "visitOrderAll",
  //     name: "VisitAll",
  //     component: (resolve) => require(['@/views/order/visitOrder/visitOrderAll'],resolve),
  //     meta: { title: '全部订单', tagName: '上门-全部订单', icon: 'order', noCache: true, affix: false },
  //   },{
  //     path: "visitAppointGoods",
  //     name: "VisitAppoint",
  //     component: (resolve) => require(['@/views/order/visitOrder/visitAppointGoods'],resolve),
  //     meta: { title: '已预约', tagName: '上门-已预约', icon: 'fuwu', noCache: true, affix: false },
  //   },{
  //     path: "visitAcceptGoods",
  //     name: "VisitAccept",
  //     component: (resolve) => require(['@/views/order/visitOrder/visitAcceptGoods'],resolve),
  //     meta: { title: '已受理', tagName: '上门-已受理', icon: 'yanzheng', noCache: true, affix: false },
  //   },{
  //     path: "visitFinishGoods",
  //     name: "VisitFinish",
  //     component: (resolve) => require(['@/views/order/visitOrder/visitFinishGoods'],resolve),
  //     meta: { title: '已完成', tagName: '上门-已完成', icon: 'chengjiao', noCache: true, affix: false },
  //   },{
  //     hidden: true,
  //     path: "visitGoodsInfo",
  //     meta: {title: "订单详情", tagName: '上门-订单详情', icon: '',noCache: true, affix: false},
  //     component: (resolve) => require(['@/views/order/orderInfo/index'],resolve),
  //   }]
  // }, {
  //   path: "/shopOrder",
  //   component: Layout,
  //   meta: { title: '门店订单', icon: 'dianpu', noCache: true, affix: false },
  //   children:[{
  //     path: "shopOrderAll",
  //     name: "ShopAll",
  //     component: (resolve) => require(['@/views/order/shopOrder/shopOrderAll'],resolve),
  //     meta: { title: '全部订单', tagName: '门店-全部订单', icon: 'order', noCache: true, affix: false },
  //   },{
  //     path: "shopAppointGoods",
  //     name: "ShopAppoint",
  //     component: (resolve) => require(['@/views/order/shopOrder/shopAppointGoods'],resolve),
  //     meta: { title: '已预约', tagName: '门店-已预约', icon: 'fuwu', noCache: true, affix: false },
  //   },{
  //     path: "shopAcceptGoods",
  //     name: "ShopAccept",
  //     component: (resolve) => require(['@/views/order/shopOrder/shopAcceptGoods'],resolve),
  //     meta: { title: '已受理', tagName: '门店-已受理', icon: 'daiban', noCache: true, affix: false },
  //   },{
  //     path: "finishGoods",
  //     name: "ShopFinish",
  //     component: (resolve) => require(['@/views/order/shopOrder/finishGoods'],resolve),
  //     meta: { title: '已完成', tagName: '门店-已完成', icon: 'chengjiao', noCache: true, affix: false },
  //   },{
  //     hidden: true,
  //     path: "shopGoodsInfo",
  //     meta: {title: "订单详情", tagName: '门店-订单详情', icon: '',noCache: true, affix: false},
  //     component: (resolve) => require(['@/views/order/orderInfo/index'],resolve),
  //   }]
  // }, {
  //   path: "/mailOrder",
  //   component: Layout,
  //   meta: { title: '邮寄订单', icon: 'kuaidi', noCache: true, affix: false },
  //   children:[{
  //     path: "mailOrderAll",
  //     name: "MailAll",
  //     component: (resolve) => require(['@/views/order/mailOrder/mailOrderAll'],resolve),
  //     meta: { title: '全部订单', tagName: '邮寄-全部订单', icon: 'order', noCache: true, affix: false },
  //   },{
  //     path: "mailAppointGoods",
  //     name: "MailAppoint",
  //     component: (resolve) => require(['@/views/order/mailOrder/mailAppointGoods'],resolve),
  //     meta: { title: '已预约', tagName: '邮寄-已预约', icon: 'yuyue', noCache: true, affix: false },
  //   },{
  //     path: "mailTakeGoods",
  //     name: "MailTake",
  //     component: (resolve) => require(['@/views/order/mailOrder/mailTakeGoods'],resolve),
  //     meta: { title: '已取件', tagName: '邮寄-已取件', icon: 'express-model', noCache: true, affix: false },
  //   },{
  //     path: "mailSignGoods",
  //     name: "MailSign",
  //     component: (resolve) => require(['@/views/order/mailOrder/mailSignGoods'],resolve),
  //     meta: { title: '已签收', tagName: '邮寄-已签收', icon: 'daifahuo', noCache: true, affix: false },
  //   },{
  //     path: "mailPriceGoods",
  //     name: "MailPrice",
  //     component: (resolve) => require(['@/views/order/mailOrder/mailPriceGoods'],resolve),
  //     meta: { title: '已出价', tagName: '邮寄-已出价', icon: 'baojiadan', noCache: true, affix: false },
  //   },{
  //     path: "mailBackGoods",
  //     name: "MailBack",
  //     component: (resolve) => require(['@/views/order/mailOrder/mailBackGoods'],resolve),
  //     meta: { title: '被退回', tagName: '邮寄-被退回', icon: 'jujue', noCache: true, affix: false },
  //   },{
  //     path: "mailRemitGoods",
  //     name: "MailRemit",
  //     component: (resolve) => require(['@/views/order/mailOrder/mailRemitGoods'],resolve),
  //     meta: { title: '待打款', tagName: '邮寄-待打款', icon: 'huikuanjiaojie', noCache: true, affix: false },
  //   },{
  //     path: "finishGoods",
  //     name: "MailFinish",
  //     component: (resolve) => require(['@/views/order/mailOrder/finishGoods'],resolve),
  //     meta: { title: '已完成', tagName: '邮寄-已完成', icon: 'chengjiao', noCache: true, affix: false },
  //   },{
  //     path: "mailSendBackGoods",
  //     name: "MailSendBack",
  //     component: (resolve) => require(['@/views/order/mailOrder/mailSendBackGoods'],resolve),
  //     meta: { title: '已退回', tagName: '邮寄-已退回', icon: 'huowu-tuihui', noCache: true, affix: false },
  //   },{
  //     hidden: true,
  //     path: "mailGoodsInfo",
  //     meta: {title: "订单详情", tagName: '邮寄-订单详情', icon: '',noCache: true, affix: false},
  //     component: (resolve) => require(['@/views/order/orderInfo/index'],resolve),
  //   }]
  // }, {
  //   path: "/luxuryOrder",
  //   component: Layout,
  //   meta: { title: '奢侈品订单', icon: 'theme', noCache: true, affix: false },
  //   children:[{
  //     path: "luxuryOrderAll",
  //     name: "LuxuryAll",
  //     component: (resolve) => require(['@/views/order/luxuryOrder/luxuryOrderAll'],resolve),
  //     meta: { title: '全部订单', tagName: '奢侈品-全部订单', icon: 'order', noCache: true, affix: false },
  //   },{
  //     path: "luxuryAppointGoods",
  //     name: "LuxuryAppoint",
  //     component: (resolve) => require(['@/views/order/luxuryOrder/luxuryAppointGoods'],resolve),
  //     meta: { title: '已预约', tagName: '奢侈品-已预约', icon: 'fuwu', noCache: true, affix: false },
  //   },{
  //     path: "luxuryAcceptGoods",
  //     name: "LuxuryAccept",
  //     component: (resolve) => require(['@/views/order/luxuryOrder/luxuryAcceptGoods'],resolve),
  //     meta: { title: '已受理', tagName: '奢侈品-已受理', icon: 'yanzheng', noCache: true, affix: false },
  //   },{
  //     path: "luxuryCallGoods",
  //     name: "LuxuryCall",
  //     component: (resolve) => require(['@/views/order/luxuryOrder/luxuryCallGoods'],resolve),
  //     meta: { title: '已取消', tagName: '奢侈品-已取消', icon: 'jujue', noCache: true, affix: false },
  //   },{
  //     path: "luxuryFinishGoods",
  //     name: "LuxuryFinish",
  //     component: (resolve) => require(['@/views/order/luxuryOrder/luxuryFinishGoods'],resolve),
  //     meta: { title: '已完成', tagName: '奢侈品-已完成', icon: 'chengjiao', noCache: true, affix: false },
  //   },{
  //     hidden: true,
  //     path: "luxuryGoodsInfo",
  //     meta: {title: "订单详情", tagName: '奢侈品-订单详情', icon: '',noCache: true, affix: false},
  //     component: (resolve) => require(['@/views/order/luxuryOrder/component/info'],resolve),
  //   }]
  // }, {
  //   path: "/oneKeyOrder",
  //   component: Layout,
  //   meta: { title: '一键订单', icon: 'button', noCache: true, affix: false },
  //   children:[{
  //     path: "oneKeyOrderAll",
  //     name: "OneKeyAll",
  //     component: (resolve) => require(['@/views/order/oneKeyOrder/oneKeyOrderAll'],resolve),
  //     meta: { title: '全部订单', tagName: '一键-全部订单', icon: 'order', noCache: true, affix: false },
  //   },{
  //     path: "oneKeyAppointGoods",
  //     name: "OneKeyAppoint",
  //     component: (resolve) => require(['@/views/order/oneKeyOrder/oneKeyAppointGoods'],resolve),
  //     meta: { title: '已预约', tagName: '一键-已预约', icon: 'fuwu', noCache: true, affix: false },
  //   },{
  //     path: "oneKeyAcceptGoods",
  //     name: "OneKeyAccept",
  //     component: (resolve) => require(['@/views/order/oneKeyOrder/oneKeyAcceptGoods'],resolve),
  //     meta: { title: '已受理', tagName: '一键-已受理', icon: 'yanzheng', noCache: true, affix: false },
  //   },{
  //     path: "oneKeyCallGoods",
  //     name: "OneKeyCall",
  //     component: (resolve) => require(['@/views/order/oneKeyOrder/oneKeyCallGoods'],resolve),
  //     meta: { title: '已取消', tagName: '一键-已取消', icon: 'jujue', noCache: true, affix: false },
  //   },{
  //     path: "oneKeyFinishGoods",
  //     name: "OneKeyFinish",
  //     component: (resolve) => require(['@/views/order/oneKeyOrder/oneKeyFinishGoods'],resolve),
  //     meta: { title: '已完成', tagName: '一键-已完成', icon: 'chengjiao', noCache: true, affix: false },
  //   }]
  // }, {
  //   path: "/goldOrder",
  //   component: Layout,
  //   meta: { title: '黄金订单', icon: 'gold', noCache: true, affix: false },
  //   children:[{
  //     path: "goldOrderAll",
  //     name: "GoldAll",
  //     component: (resolve) => require(['@/views/order/goldOrder/goldOrderAll'],resolve),
  //     meta: { title: '全部订单', tagName: '黄金-全部订单', icon: 'order', noCache: true, affix: false },
  //   },{
  //     path: "goldAppointGoods",
  //     name: "GoldAppoint",
  //     component: (resolve) => require(['@/views/order/goldOrder/goldAppointGoods'],resolve),
  //     meta: { title: '已预约', tagName: '黄金-已预约', icon: 'fuwu', noCache: true, affix: false },
  //   },{
  //     path: "goldAcceptGoods",
  //     name: "GoldAccept",
  //     component: (resolve) => require(['@/views/order/goldOrder/goldAcceptGoods'],resolve),
  //     meta: { title: '已受理', tagName: '黄金-已受理', icon: 'yanzheng', noCache: true, affix: false },
  //   },{
  //     path: "goldCallGoods",
  //     name: "GoldCall",
  //     component: (resolve) => require(['@/views/order/goldOrder/goldCallGoods'],resolve),
  //     meta: { title: '已取消', tagName: '黄金-已取消', icon: 'jujue', noCache: true, affix: false },
  //   },{
  //     path: "goldFinishGoods",
  //     name: "GoldFinish",
  //     component: (resolve) => require(['@/views/order/goldOrder/goldFinishGoods'],resolve),
  //     meta: { title: '已完成', tagName: '黄金-已完成', icon: 'chengjiao', noCache: true, affix: false },
  //   }]
  // }, {
  //   path: "/OtherOrder",
  //   component: Layout,
  //   meta: { title: '其它订单', icon: 'other', noCache: true, affix: false },
  //   children:[{
  //     path: "otherOrderAll",
  //     name: "OtherAll",
  //     component: (resolve) => require(['@/views/order/otherOrder/otherOrderAll'],resolve),
  //     meta: { title: '全部订单', tagName: '其它-全部订单', icon: 'order', noCache: true, affix: false },
  //   },{
  //     path: "otherAppointGoods",
  //     name: "OtherAppoint",
  //     component: (resolve) => require(['@/views/order/otherOrder/otherAppointGoods'],resolve),
  //     meta: { title: '已预约', tagName: '其它-已预约', icon: 'fuwu', noCache: true, affix: false },
  //   },{
  //     path: "otherAcceptGoods",
  //     name: "OtherAccept",
  //     component: (resolve) => require(['@/views/order/otherOrder/otherAcceptGoods'],resolve),
  //     meta: { title: '已受理', tagName: '其它-已受理', icon: 'yanzheng', noCache: true, affix: false },
  //   },{
  //     path: "otherCallGoods",
  //     name: "OtherCall",
  //     component: (resolve) => require(['@/views/order/otherOrder/otherCallGoods'],resolve),
  //     meta: { title: '已取消', tagName: '其它-已取消', icon: 'jujue', noCache: true, affix: false },
  //   },{
  //     path: "otherFinishGoods",
  //     name: "OtherFinish",
  //     component: (resolve) => require(['@/views/order/otherOrder/otherFinishGoods'],resolve),
  //     meta: { title: '已完成', tagName: '其它-已完成', icon: 'chengjiao', noCache: true, affix: false },
  //   }]
  // }, {
  //   path: '/systemConfig',
  //   component: Layout,
  //   meta: {title: "系统配置",icon: 'shezhi'},
  //   children:[{
  //     path: "brandOperate",
  //     name: "BrandManager",
  //     component: (resolve) => require(['@/views/system/brandOperate/index'],resolve),
  //     meta: { title: '品牌管理', icon: 'pad-full', noCache: true, affix: false },
  //   }, {
  //     path: "infoConfig",
  //     name: "UserConfig",
  //     component: (resolve) => require(['@/views/infoConfig/index'],resolve),
  //     meta: { title: '员工配置', icon: 'international', noCache: true, affix: false },
  //   }, {
  //     path: "price",
  //     name: "PriceManager",
  //     component: (resolve) => require(['@/views/system/reportPrice/price'],resolve),
  //     meta: { title: '报价管理', icon: 'jiageprice1', noCache: true, affix: false },
  //   }, {
  //     path: "statistics",
  //     name: "UserStatistics",
  //     component: (resolve) => require(['@/views/system/statistics/index'],resolve),
  //     meta: { title: '用户统计', icon: 'chart', noCache: true, affix: false },
  //   }, {
  //     path: "feedback",
  //     name: "Feedback",
  //     component: (resolve) => require(['@/views/system/feedback/index'],resolve),
  //     meta: { title: '意见反馈', icon: 'message', noCache: true, affix: false },
  //   }, {
  //     path: "messageConfig",
  //     name: "MessageManager",
  //     component: (resolve) => require(['@/views/system/messageConfig/index'],resolve),
  //     meta: { title: '通知管理', icon: 'scrollimgs', noCache: true, affix: false },
  //   }, {
  //     path: "sloganConfig",
  //     name: "SloganManager",
  //     hidden: true,
  //     component: (resolve) => require(['@/views/system/sloganConfig/index'],resolve),
  //     meta: { title: '广告管理', icon: 'advertisings', noCache: true, affix: false },
  //   }, {
  //     path: "protocolConfig",
  //     name: "ProtocolConfig",
  //     component: (resolve) => require(['@/views/system/protocolConfig/index'],resolve),
  //     meta: { title: '协议配置', icon: 'xieyi', noCache: true, affix: false }
  //   }],
  // },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/system/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  }, {
    path: '/login',
    name: "login",
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  }, {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    hidden: true
  }, {
    path: '/lockScreen',
    name: "lockScreen",
    component: (resolve) => require(['@/views/error/lockScreen'], resolve),
    hidden: true
  },{
    path: '*',
    component: (resolve) => require(['@/views/error/4042'], resolve),
    hidden: true
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
