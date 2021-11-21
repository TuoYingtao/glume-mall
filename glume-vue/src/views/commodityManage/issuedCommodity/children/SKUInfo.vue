<template>
  <div class="skuinfo">
    <div v-if="false"></div>
    <el-empty v-else description="当前分类没有可选的规格参数"/>
    <div class="button-box">
      <el-button class="nextButton" type="info" @click="back">上一步</el-button>
      <el-button class="nextButton" type="primary" @click="next">下一步</el-button>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: "SKUInfo",
  data() {
    return {
      SKUInfoData: []
    }
  },
  computed: {
    ...mapGetters(['baseInfoForm','sizeParamData','marketPropertyData'])
  },
  methods: {
    next() {
      this.$emit("next");
    },
    back() {
      this.$emit("back");
    },
    tableHandlerData() {
      console.log(this.baseInfoForm)
      // console.log(this.sizeParamData)
      console.log(this.marketPropertyData)
      let SKUInfoData = [];
      for (let i = 0; i < this.marketPropertyData[0].newTempSaleAttrs.length; i++) {
        for (let j = 0; j < this.marketPropertyData[2].newTempSaleAttrs.length; j++) {
          SKUInfoData[i] = [];
          SKUInfoData[i].push({color: this.marketPropertyData[0].newTempSaleAttrs[i],
            version: this.marketPropertyData[2].newTempSaleAttrs[j],
            productName: this.baseInfoForm.name,
            title: this.baseInfoForm.name,
            title2: "",
            price: 0})
        }
      }
      this.SKUInfoData = SKUInfoData;
      console.log(SKUInfoData)
    }
  }
}
</script>

<style scoped>
  .skuinfo {
    padding: 0 30%;
  }
  .button-box {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .nextButton {
    width: 200px;
    margin-top: 12px;
  }
</style>
