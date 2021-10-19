<template>
  <layout-container>
    <div class="content-box">
      <el-card class="order-info box-card " style="width: 25%">
        <div class="header">
          <svg-icon icon-class="table"/>
          <span>基本信息</span>
        </div>
        <div class="content-text">
          <img class="image" v-lazy="orderInfo.model_image" />
          <el-descriptions class="el-descriptions" :column="1" :size="size" border :labelStyle="{'width':'120px'}">
            <el-descriptions-item>
              <template slot="label"><i class="el-icon-mobile-phone"/>&nbsp;设备名称:</template>
              <span>{{isVerify(orderInfo.model_name)}}</span>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label"><i class="el-icon-user"/>&nbsp;发货人:</template>
              <span>{{isVerify(orderInfo.contact)}}</span>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label"><i class="el-icon-phone-outline"/>&nbsp;联系方式:</template>
              <span>{{isVerify(orderInfo.mobile)}}</span>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label"><i class="el-icon-map-location"/>&nbsp;发货省:</template>
              <span>{{isVerify(orderInfo.province)}}</span>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label"><i class="el-icon-map-location"/>&nbsp;发货市:</template>
              <span>{{isVerify(orderInfo.city)}}</span>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label"><i class="el-icon-map-location"/>&nbsp;发货区:</template>
              <span>{{isVerify(orderInfo.area)}}</span>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label"><i class="el-icon-house"/>&nbsp;详细地址:</template>
              <span>{{isVerify(orderInfo.address)}}</span>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </el-card>
      <div class="order-info-right">
        <el-card class="order-info02 box-card">
          <div class="header">
            <svg-icon icon-class="table"/>
            <span>订单信息</span>
          </div>
          <div class="content-text">
            <el-descriptions class="el-descriptions" :column="5" :size="size" border>
              <el-descriptions-item>
                <template slot="label"><i class="el-icon-document"/>&nbsp;订单编号:</template>
                <span>{{isVerify(orderInfo.id)}}</span>
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label"><i class="el-icon-school"/>&nbsp;订单所属:</template>
                <span>{{isVerify(orderInfo.company)}}{{company}}</span>
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label"><i class="el-icon-files"/>&nbsp;订单类型:</template>
                <span>{{isVerify(orderType)}}</span>
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label"><i class="el-icon-document-checked"/>&nbsp;订单状态:</template>
                <span>{{orderStatusHandler(orderInfo.status)}}</span>
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label"><i class="el-icon-price-tag"/>&nbsp;预估价:</template>
                <el-tag type="danger" size="medium">￥{{isVerify(orderInfo.evaluate_price)}}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item>
                <template slot="label"><i class="el-icon-money"/>&nbsp;成交价:</template>
                <el-tag type="danger" size="medium">￥{{isVerify(orderInfo.end_price)}}</el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
        <el-card class="order-info02 box-card">
          <div class="header">
            <svg-icon icon-class="table"/>
            <span>设备问题单选项</span>
          </div>
          <div class="content-text">
            <el-descriptions class="el-descriptions" :column="4" :size="size" border>
              <el-descriptions-item v-for="(item,index) in info_for" :key="index">
                <template slot="label">{{item.title}}:</template>
                <span>{{isVerify(item.content)}}</span>
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
        <el-card class="order-info02 box-card">
          <div class="header">
            <svg-icon icon-class="table"/>
            <span>设备问题多选项</span>
          </div>
          <div class="content-text">
            <el-descriptions class="el-descriptions" :column="4" :size="size" border v-if="info_diff_more != null">
              <el-descriptions-item v-for="(item,index) in info_diff_more.content" :key="index">
                <template slot="label">多选项:</template>
                <span>{{item}}</span>
              </el-descriptions-item>
            </el-descriptions>
            <el-row v-else>
              <el-col :span="2">
                <el-row>
                  <el-col class="el-col-checkbox" :span="24">暂无问题多选项</el-col>
                </el-row>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </div>
    </div>
  </layout-container>
</template>

<script>
  import { getOrderInfo } from "@/api/orderList"
  import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
  export default {
    components: {
      LayoutContainer
    },
    name: 'index',
    data() {
      return {
        size: "medium",
        is_flog: true,
        orderInfo: [],
        info_for: [],
        attr_info_diff: [],
        info_diff_more: {},
      }
    },
    created() {
      if (this.$route.path == "/shopOrder/shopGoodsInfo" || this.$route.path == "/visitOrder/visitGoodsInfo") this.is_flog = false;
      this.$store.dispatch("tagsView/addVisitedView",this.$route)
      let info = JSON.parse(decodeURIComponent(this.$route.query.info));
      this.getInfo(info.id);
    },
    computed: {
      company:function() {
        if (this.isVerify(this.orderInfo.company) == '暂无数据') return '';
        return this.orderInfo.company == "深圳" ? "总部" : "分公司";
      },
      orderType:function() {
        switch (this.orderInfo.type) {
          case 1:
            return '上门回收';
          case 2:
            return '邮寄总部';
          case 3:
            return '线下门店';
        }
      }
    },
    methods: {
      getInfo(id) {
        getOrderInfo({id: id}).then(response => {
          this.orderInfo = response.data;
          this.info_for = response.data.info_for;
          this.attr_info_diff = response.data.attr_info_diff;
          this.info_diff_more = response.data.info_diff_more;
        })
      },
      orderStatusHandler(status) {
        switch (status) {
          case 1:
            return "已预约";
          case 2:
            return "已受理";
          case 3:
            return "已取消";
          case 4:
            return "已完成";
          case 5:
            return "已取件";
          case 6:
            return "已出价";
          case 7:
            return "已同意";
          case 8:
            return "待退回";
          case 9:
            return "已签收";
          case 10:
            return "已退回";
          default: "错误订单";
        }
      },
      isVerify(str) {
        if (str == 0) return  str;
        if (str == null || str == '' || str == undefined) {
          return "暂无数据"
        }
        return str;
      },
    }
  }
</script>

<style scoped>
  .content-box {
    display: flex;
    justify-content: flex-start;
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
  .order-info {
    margin: 0 16px;
    height: 30%;
  }
  .order-info .content-text {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  .order-info .content-text .image {
    width: 160px;
    height: 160px;
    padding-top: 10px;
  }

  .el-descriptions {
    width: 100%;
    padding-top: 8px;
  }

  .order-info-right {
    width: 100%;
    height: 10%;
    position: relative;
    display: flex;
    flex-direction: column;
  }

  .order-info02 .content-text {
    padding-top: 10px;
  }

  .el-col-price-estimate {
    color: #C03639;
    font-size: 15px;
    font-weight: bold;
  }

  .el-col-price-end {
    color: red;
    font-size: 15px;
    font-weight: bold;
  }

  .order-info02 .content-text .el-col-checkbox {
    font-size: 14px !important;
    font-weight: 500 !important;
  }
</style>
