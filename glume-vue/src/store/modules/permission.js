import { constantRoutes } from '@/router'
import Layout from '@/layout/index'

const permission  = {
  state: {
    menuRoutes: []
  },

  mutations: {
    SET_ROUTES: (state, routes) => state.menuRoutes = routes,
  },

  actions: {
    // 生成路由
    GenerateRoutes({ commit },menus) {
      return new Promise(resolve => {
        let mane = menuModelHandler(menus);
        let routes = [...mane,...constantRoutes];
        commit('SET_ROUTES',routes)
        resolve(routes)
      })
    },
  }
}

function menuModelHandler(menus) {
  menus.forEach(item => {
    item.meta = {};
    (item.component == null || item.component == "") ? item.component = Layout : item.component = loadView(item.component);
    let keys = Object.keys(item);
    keys.forEach(key => {
      if (key == "component" || key == "meta" || key == "path" || key == "children") return
      if (key == "name") {
        item.meta.title = item[key];
      } else {
        item.meta[key] = item[key];
      }
      delete item[key]
    })
  })
  menus.map(menu => {
    if (menu.children && menu.children.length > 0) {
      menu.children = menuModelHandler(menu.children)
    }
  })
  return menus
}

const loadView = (view) => {
  if (process.env.NODE_ENV === 'development') {
    return (resolve) => require([`@/views/${view}`], resolve);
  } else {
    // 使用 import 实现生产环境的路由懒加载
    return () => import(`@/views/${view}`);
  }
}

export default permission

