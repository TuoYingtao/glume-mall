import { login, getInfo } from '@/api/login'
import { getToken, setToken, removeToken } from '@/utils/auth'

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    roles: {},
    permissions: [],
  },

  mutations: {
    SET_TOKEN: (state, token) => state.token = token,
    SET_NAME: (state, name) => state.name = name,
    SET_AVATAR: (state, avatar) => state.avatar = avatar,
    SET_ROLES: (state, roles) => state.roles = roles,
  },

  actions: {
    /* 登录 */
    Login({ commit }, userInfo) {
      const username = userInfo.name
      const password = userInfo.password
      const code = userInfo.code
      const key = userInfo.key
      return new Promise((resolve, reject) => {
        login({username, password,code,key}).then(res => {
          const token = res.data.token;
          setToken(token)
          commit('SET_TOKEN', token)
          resolve(token)
        }).catch(error => {
          reject(error)
        })
      })
    },
    /* 获取用户信息 */
    GetInfo({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(res => {
          if(res.code == 200) {
            commit('SET_AVATAR', res.data.avatar)
            commit('SET_NAME', res.data.name)
            commit('SET_ROLES', res.data)
            resolve(res)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    /* 退出系统 */
    LogOut({ commit }) {
      return new Promise((resolve, reject) => {
        removeToken()
        commit('SET_ROLES', {})
        commit('SET_AVATAR', '')
      })
    },
  }
}
export default user
