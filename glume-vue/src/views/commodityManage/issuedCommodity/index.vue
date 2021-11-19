<template>
  <layout-container>
    <el-card class="box-card steps-box">
      <el-steps :active="stepsActive" simple>
        <el-step title="基本信息" icon="el-icon-edit"></el-step>
        <el-step title="规格参数" icon="el-icon-s-order"></el-step>
        <el-step title="销售属性" icon="el-icon-s-ticket"></el-step>
        <el-step title="SKU信息" icon="el-icon-s-goods"></el-step>
        <el-step title="保存完成" icon="el-icon-success"></el-step>
      </el-steps>
    </el-card>
    <el-card class="box-card">
      <base-info-form v-show="stepsActive == 0" ref="baseInfoForm" @next="next"/>
      <size-params v-show="stepsActive == 1" ref="sizeParams"/>
    </el-card>
  </layout-container>
</template>

<script>
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
import baseInfoForm from "@/views/commodityManage/issuedCommodity/children/baseInfoForm";
import sizeParams from "./children/sizeParams";
export default {
  name: "index",
  components: {LayoutContainer,baseInfoForm,sizeParams},
  data() {
    return {
      stepsActive: 0,
    }
  },
  watch: {
    stepsActive: {
      handler(val) {
        switch (val) {
          case 1:
            return this.$refs["sizeParams"].attrGroupWithAttrs();
          default: false;
        }
      }
    }
  },
  methods: {
    next() {
      if (this.stepsActive++ > 5) this.stepsActive = 0;
    }
  }
}
</script>

<style scoped>
.steps-box /deep/ .el-step__head.is-finish,
.steps-box /deep/ .el-step__title.is-finish {
  color: #00bb00;
}
.steps-box /deep/ .el-step__head.is-process,
.steps-box /deep/ .el-step__title.is-process {
  color: #e6a700;
}
</style>
