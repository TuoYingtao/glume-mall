<template>
  <layout-container class="dashboard-editor-container">
    <panel-group :project-total="projectTotal" @handleSetLineChartData="handleSetLineChartData" />

    <el-dialog class="order-dialog" :title="title" :visible.sync="dialogVisible" width="70%" :before-close="handleClose">
      <el-table class="el-table" v-loading="loading" :data="orderList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="订单编号" prop="id"/>
        <el-table-column label="图片">
          <template slot-scope="scope">
            <img class="img_box" alt="loading" v-lazy="scope.row.model_image"/>
          </template>
        </el-table-column>
        <el-table-column label="品牌名称" prop="model_name"/>
        <el-table-column label="订单所属">
          <el-table-column label="分公司">
            <template slot-scope="scope">
              <span>{{scope.row.branch_id}}</span>
            </template>
          </el-table-column>
          <el-table-column label="用户ID">
            <template slot-scope="scope">
              <span>{{scope.row.user_id}}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="预估价" prop="evaluate_price"/>
        <el-table-column label="成交价" prop="end_price"/>
        <el-table-column label="创建日期" align="center">
          <template  slot-scope="scope">
            <span>{{ parseTime(scope.row.create_time) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="汇款日期" align="center">
          <template  slot-scope="scope">
            <span>{{ parseTime(scope.row.remit_time) || '处理中' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" align="center">
          <template slot-scope="scope">
            <el-tag type="success">已完成</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-size="[10,20,30,40,50]" v-show="total > 0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="dataOrderHandler(type)"/>
    </el-dialog>
  </layout-container>
</template>

<script>
import { getHome, getOrderList } from '@/api/home'
import PanelGroup from './dashboard/PanelGroup'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import GradualStackMap from './dashboard/GradualStackMap'

import BarChart from './dashboard/BarChart'
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'

export default {
  name: 'Index',
  components: {
    LayoutContainer,
    PanelGroup,
    RaddarChart,
    PieChart,
    BarChart,
    GradualStackMap,
  },
  data() {
    return {
      projectTotal:{},
      type: 1,
      title: "",
      dialogVisible: false,
      orderList: [],
      loading: false,
      total: null,
      queryParams: {
        page: 1,
        limit: 5
      }
    }
  },
  mounted(){
    this.getHomeData()
  },
  methods: {
    handleSetLineChartData(type) {
      this.type = type;
      this.title = this.textTitleHandler(type);
      this.dialogVisible = true
      this.dataOrderHandler();
    },
    getHomeData() {
      getHome().then(response => {
        this.projectTotal = response.data
      })
    },
    dataOrderHandler() {
      this.loading = true;
      let param = this.queryParams;
      param.type = this.type;
      getOrderList(param).then(response => {
        this.orderList = response.data;
        this.total = response.total;
        this.loading = false;
      })
    },
    textTitleHandler(type) {
      switch (type) {
        case 1:
          return "今日订单";
        case 2:
          return "今日订单";
        case 3:
          return "所有订单";
        case 4:
          return "所有成交";
      }
    },
    handleClose() {
      this.dialogVisible = false;
    }
  }
}
</script>

<style lang="scss" scoped>
.order-dialog /deep/.el-dialog__wrapper .el-dialog {
  margin-top: 0 !important;
}
.dashboard-editor-container {
  padding: 32px;
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}

</style>
