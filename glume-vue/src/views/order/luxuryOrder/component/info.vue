<template>
  <layout-container>
    <el-card class="order-info box-card">
      <div class="header">
        <svg-icon icon-class="table"/>
        <span>基本信息</span>
      </div>
      <div class="content-text">
        <el-descriptions class="el-descriptions" :column="1" :size="size" border>
          <el-descriptions-item>
            <template slot="label">订单编号:</template>
            <span>{{info.id}}</span>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">联系方式:</template>
            <span>{{info.mobile}}</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
    <div class="content-box">
      <el-card class="order-info box-card" style="width: 25%" v-for="(item,index) in info.imgs" :key="index">
        <div class="header">
          <svg-icon icon-class="table"/>
          <span>预览</span>
        </div>
        <div class="content-text">
          <el-image class="el-image" :preview-src-list="info.imgs" :src="item"/>
        </div>
      </el-card>
    </div>
  </layout-container>
</template>

<script>
import { luxuryInfo } from "@/api/orderList"
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
export default {
  name: 'info',
  components: { LayoutContainer },
  data() {
    return {
      size: "",
      info: {}
    }
  },
  created() {
    let info = JSON.parse(decodeURIComponent(this.$route.query.info));
    this.getInfo(info);
  },
  methods: {
    getInfo(info) {
      luxuryInfo(info).then(response => {
        this.info = response.data;
      })
    },
  }
}
</script>

<style scoped>
.content-box {
  display: flex;
  justify-content: flex-start;
}
.content-box .box-card {
  margin: 10px 30px;
}
.header {
  padding-bottom: 8px;
  border-bottom: 1px solid #CCC;
}
.header span{
  padding-left: 9px;
  font-size: 16px;
  font-weight: bold;
  letter-spacing: 1px;
  color: #525252;
}
.order-info .content-text {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.el-descriptions {
  width: 100%;
  padding-top: 8px;
}
.el-image {
  height: 600px;
  border: 1px solid #000;
}

</style>
