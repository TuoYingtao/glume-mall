import Vue from "vue";

const issuedCommodity = {
  state: {
    baseInfoForm: {}
  },

  mutations: {
    SET_BASE_INFO_FORM: (state,baseInfoForm) => Vue.set(state,"baseInfoForm",baseInfoForm),
  },

  actions: {
    setBaseInfoForm({commit},baseInfoForm) {
      commit("SET_BASE_INFO_FORM",baseInfoForm);
    }
  }
}
export default issuedCommodity
