import Vue from 'vue'
import Vuex from 'vuex'
import getters from "@/store/getters";
import app from '@/store/modules/app'
import user from '@/store/modules/user'
import tagsView from '@/store/modules/tagsView'
import settings from '@/store/modules/settings'
import permission from "@/store/modules/permission";
import issuedCommodity from "@/store/modules/issuedCommodity"

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    user,
    tagsView,
    settings,
    permission,
    issuedCommodity
  },
  getters
})

export default store
