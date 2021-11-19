<template>
  <div class="sizeParams">
      <el-tabs tab-position="left" style="height: 200px;">
      <el-tab-pane label="用户管理">用户管理</el-tab-pane>
      <el-tab-pane label="配置管理">配置管理</el-tab-pane>
      <el-tab-pane label="角色管理">角色管理</el-tab-pane>
      <el-tab-pane label="定时任务补偿">定时任务补偿</el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {getAttrGroupWithAttrs} from "@/api/commodityManage/issuedCommodity";
import {mapGetters} from "vuex";

export default {
  name: "sizeParams",
  data() {
    return {

    }
  },
  computed: {
    ...mapGetters(['baseInfoForm']),
  },
  methods: {
    attrGroupWithAttrs() {
      console.log(this.baseInfoForm)
      getAttrGroupWithAttrs(this.baseInfoForm.catelogId).then(response => {
        console.log(response)
      })
    }
  }
}
</script>

<style scoped>
  .sizeParams {
    padding: 0 25%;
  }
</style>
