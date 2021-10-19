import Vue from 'vue'
import Cookies from 'js-cookie'
import 'normalize.css/normalize.css' // a modern alternative to CSS resets
import Element from 'element-ui'
import './assets/styles/element-variables.scss'
import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import '@/assets/styles/layout.scss' // 布局样式
import '@/assets/styles/animate.min.css'
import App from './App'
import store from './store'
import router from './router'
import './assets/icons' // icon
import './permission' // permission control
import "./commonFun"
/* 解决：Chrome 51 版本以后增加的新事件捕获机制－Passive Event Listeners；添加事件管理者'passive'，以使页面更加流畅。 */
import "default-passive-events"
const defaultSettings = require('@/settings.js')

import Directives from '@/utils/directives'//引入自定义指令
Vue.use(Directives);// 全局注册指令
import astrict from '@/utils/astrict'
Vue.use(astrict)
import VueLazyload  from "vue-lazyload"
Vue.use(VueLazyload,{
  error: require('@/assets/image/error_image.jpg'),
  loading: require('@/assets/image/loding_image02.gif'),
  dispatchEvent: true,
  preLoad: 3,
  attempt: 2,
})

/**
 * @param 其组件目录的相对路径(组件目录相对于当前js文件的路径)
 * @param 是否查询其子目录
 * @param 匹配基础组件文件名的正则表达式(因此要注册为全局组件的组件名称约定很重要)
 */
// 全局组件挂载
const componentsContext = require.context('@/components', true, /index.vue$/);
componentsContext.keys().forEach(component => {
  // 获取每个index.vue文件 default 模块
  const componentConfig = componentsContext(component).default;
  if (componentConfig.name != null) {
    Vue.component(componentConfig.name, componentConfig);
  }
});

Vue.prototype.defaultSettings = defaultSettings;

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
