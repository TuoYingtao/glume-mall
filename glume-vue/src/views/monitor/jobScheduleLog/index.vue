<template>
  <layout-container>
    <!--    搜索框-->
    <search-box v-show="showSearch" :is-select="true" :param-accept="queryParams" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
    <!--    全局按钮-->
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-col>
          <el-button icon="el-icon-delete" type="danger" size="mini" @click="batchDelete">删除</el-button>
          <el-button icon="el-icon-close" type="info" size="mini" @click="closeLogRouter">关闭</el-button>
          <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getList" />
        </el-col>
      </el-row>
      <!--    内容展示-->
      <el-table v-loading="loading" :data="dataList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="日志信息" prop="jobMessage"/>
        <el-table-column label="任务状态" prop="status">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.status == 0">成功</el-tag>
            <el-tag type="danger" v-else-if="scope.row.status == 1">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="耗时(单位：毫秒)" prop="times"/>
        <el-table-column label="开始时间" align="center" prop="startTime">
          <template  slot-scope="scope">
            <span>{{ parseTime(scope.row.startTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="停止时间" align="center" prop="stopTime">
          <template  slot-scope="scope">
            <span>{{ parseTime(scope.row.stopTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime">
          <template  slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="异常信息" prop="exceptionInfo" :show-overflow-tooltip="true" width="400" />
        <el-table-column label="操作" width="300">
          <template slot-scope="scope">
            <el-button icon="el-icon-view" size="mini" type="info" @click="jobLogInfo(scope.row)">详情</el-button>
            <el-popconfirm class="el-popconfirm" icon-color="red" title="这是一段内容确定删除吗？" @confirm="deleteClick(scope.row)">
              <el-button icon="el-icon-delete" size="mini" type="danger" slot="reference">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :current-page.sync="queryParams.page" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
    <info-dialog-box ref="infoDialogBox" :data="formData" />
  </layout-container>
</template>

<script>
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
import {deleteData, emptyLogData, infoData, listData} from "@/api/jobLogSchedule";
import InfoDialogBox from "@/views/monitor/jobScheduleLog/component/InfoDialogBox";
export default {
  name: "index",
  components: {InfoDialogBox, LayoutContainer},
  data() {
    return {
      ids: null,
      showSearch: true,
      loading: false,
      queryParams: {
        page: 1,
        limit: 20,
      },
      queryDataModel: [{type: "default",label: "日志信息",prop: "jobMessage"},
        {type: "select",label: "任务状态",prop: "status",data: [
            {id: 0,name: "正常"}, {id: 1,name: "暂停"}]},
        {type: "datetime",label: "日期范围",prop: "dateTime",format: "yyyy-MM-dd HH:mm:ss"}],
      dataList: [],
      total: 0,
      // form
      formData: {}
    }
  },
  created() {
    if (this.$route.query.jobId) {
      this.queryParams.jobId = this.$route.query.jobId;
    }
    this.getList();
  },
  methods: {
    getList() {
      listData(this.queryParams).then(res => {
        this.dataList = res.data.list;
        this.total = res.data.totalCount;
      })
    },
    jobLogInfo(row) {
      infoData(row.logId).then(res => {
        this.formData = res.data;
        this.$refs["infoDialogBox"].open();
      })
    },
    batchDelete() {
      this.$confirm("此操作将会清空所有任务记录, 是否继续?","提示", {
        cancelButtonText: "不用了",
        confirmButtonText: "确认",
        type: "warning",
      }).then(() => {
        emptyLogData().then(res => {
          this.notSuccess(res.msg);
          this.getList();
        })
      })
    },
    deleteClick(row) {
      deleteData(row.logId).then(res => {
        this.notSuccess(res.msg);
        this.getList();
      })
    },
    closeLogRouter() {
      this.$router.go(-1);
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
    },
    handleQuery() {
      this.queryParams.page = 1;
      this.queryParams.limit = 20;
      this.getList()
    },
    resetQuery() {
      this.reset();
      this.getList();
    },
    reset() {
      this.queryParams = {
        page: 1,
        limit: 20,
      };
    },
  }
}
</script>

<style scoped>
.el-popconfirm {
  margin-left: 10px;
}
</style>
