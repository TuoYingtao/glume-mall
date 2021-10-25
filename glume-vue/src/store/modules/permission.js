import { constantRoutes } from '@/router'
import Layout from '@/layout/index'

const permission  = {
  state: {
    routes: [],
    addRoutes: [],
    defaultRoutes: [],
    topbarRouters: [],
    sidebarRouters: []
  },

  mutations: {
    SET_TOKEN: (state, token) => state.token = token,
    SET_NAME: (state, name) => state.name = name,
    SET_AVATAR: (state, avatar) => state.avatar = avatar,
    SET_ROLES: (state, roles) => state.roles = roles,
  },

  actions: {
    // 生成路由
    GenerateRoutes({ commit },menus) {
      return new Promise(resolve => {
        // const sdata = JSON.parse(JSON.stringify(res.data))
        // const rdata = JSON.parse(JSON.stringify(res.data))
        // const sidebarRoutes = filterAsyncRouter(sdata)
        // const rewriteRoutes = filterAsyncRouter(rdata, false, true)
        // rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })
        // commit('SET_ROUTES', rewriteRoutes)
        // commit('SET_SIDEBAR_ROUTERS', constantRoutes.concat(sidebarRoutes))
        // commit('SET_DEFAULT_ROUTES', sidebarRoutes)
        // commit('SET_TOPBAR_ROUTES', sidebarRoutes)
        menuModelHandler(menus)
        resolve(constantRoutes)
      })
    },
  }
}

function menuModelHandler(menus) {
  menus.forEach(item => {
    // item.meta = { title: "", icon: "", visible: "", perms: "", menuType: "" }
    for (let key of Object.keys(item)) {
      item.meta = {}
      item.meta[key] = ""
    }
  })
  menus.map(menu => {
    menu.component = Layout;
    // menu.meta.title = menu.name;
    // delete menu.name;
    // menu.meta.icon = menu.icon;
    // delete menu.icon;
    // menu.meta.visible = menu.visible;
    // delete menu.visible;
    // menu.meta.perms = menu.perms;
    // delete menu.perms;
    // menu.meta.menuType = menu.menuType;
    // delete menu.menuType;
    console.log(menu)
  })
}
export default permission

