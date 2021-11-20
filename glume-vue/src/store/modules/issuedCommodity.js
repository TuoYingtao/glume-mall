import Vue from "vue";

const issuedCommodity = {
  state: {
    baseInfoForm: {},
    sizeParamData: [],
    marketPropertyData: []
  },

  mutations: {
    SET_BASE_INFO_FORM: (state,baseInfoForm) => Vue.set(state,"baseInfoForm",baseInfoForm),
    SET_SIZE_PARAM_DATA: (state,sizeParamData) => Vue.set(state,"sizeParamData",sizeParamData),
    SET_MARKET_PROPERTY_DATA: (state,marketPropertyData) => Vue.set(state,"marketPropertyData",marketPropertyData),
  },

  actions: {
    setBaseInfoForm({commit},baseInfoForm) {
      commit("SET_BASE_INFO_FORM",baseInfoForm);
    },
    setSizeParamData({commit},sizeParamData) {
      commit("SET_SIZE_PARAM_DATA",sizeParamData);
    },
    setMarketPropertyData({commit},marketPropertyData) {
      commit("SET_MARKET_PROPERTY_DATA",marketPropertyData);
    }
  }
}
export default issuedCommodity
