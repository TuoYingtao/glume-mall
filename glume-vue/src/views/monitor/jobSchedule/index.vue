<template>
  <layout-container>
    <!--    搜索框-->
    <search-box v-show="showSearch" :is-select="true" :param-accept="queryParams" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
    <!--    全局按钮-->
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-col>
          <el-button icon="el-icon-plus" size="mini" @click="addJob">新增</el-button>
          <el-button icon="el-icon-delete" type="danger" size="mini" @click="batchDelete">删除</el-button>
          <el-button icon="el-icon-s-operation" type="info" size="mini" @click="toLogRouter">日志</el-button>
          <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getList" />
        </el-col>
      </el-row>
      <!--    内容展示-->
      <el-table v-loading="loading" :data="dataList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="任务名称" prop="jobName"/>
        <el-table-column label="任务分组" prop="jobGroup"/>
        <el-table-column label="Bean 名称" prop="beanName"/>
        <el-table-column label="调用方法" prop="invokeTarget" width="300">
          <template slot-scope="scope">
            <el-tooltip class="tooltip" effect="dark" :open-delay="1000" :content="scope.row.invokeTarget" placement="top-start">
              <span>{{scope.row.invokeTarget}}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="cron 表达式" prop="cronExpression"/>
        <el-table-column label="cron 计划策略" prop="misfirePolicy">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.misfirePolicy == 0">默认</el-tag>
            <el-tag type="success" v-else-if="scope.row.misfirePolicy == 1">立即触发执行</el-tag>
            <el-tag type="warning" v-else-if="scope.row.misfirePolicy == 2">触发一次执行</el-tag>
            <el-tag type="info" v-else-if="scope.row.misfirePolicy == 3">不触发立即执行</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="并发执行" prop="concurrent">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.concurrent == 0">允许</el-tag>
            <el-tag type="danger" v-else-if="scope.row.concurrent == 1">禁止</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" active-color="#13ce66" inactive-color="#ff4949"
              :active-value="0" :inactive-value="1" @change="onClickSwitch(scope.row)">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="remark"/>
        <el-table-column label="创建时间" align="center" prop="createTime">
          <template  slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300">
          <template slot-scope="scope">
            <el-button icon="el-icon-edit" size="mini" type="warning" @click="editClick(scope.row)">修改</el-button>
            <el-popconfirm class="el-popconfirm" icon-color="red" title="这是一段内容确定删除吗？" @confirm="deleteClick(scope.row)">
              <el-button icon="el-icon-delete" size="mini" type="danger" slot="reference">删除</el-button>
            </el-popconfirm>
            <el-dropdown size="mini" type="primary" @command="moreSelect">
              <el-button type="primary" size="mini"><i class="el-icon-d-arrow-right"/>更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{index: 1,row: scope.row}"><i class="el-icon-caret-right"/>执行一次</el-dropdown-item>
                <el-dropdown-item :command="{index: 2,row: scope.row}"><i class="el-icon-view"/>日志详情</el-dropdown-item>
                <el-dropdown-item :command="{index: 3,row: scope.row}"><i class="el-icon-s-operation"/>调度日志</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :current-page.sync="queryParams.page" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
    <dialog-box ref="dialogBox" :title="title" :data="formData" :groupList="groupList" @submitForm="submitForm" />
    <info-dialog-box ref="infoDialogBox" :data="formData" />
  </layout-container>
</template>

<script>
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
import {deleteData, editData, groupAll, infoData, listData, runJob, saveData, switchStatus} from "@/api/jobSchedule";
import DialogBox from "@/views/monitor/jobSchedule/component/DialogBox";
import InfoDialogBox from "@/views/monitor/jobSchedule/component/InfoDialogBox";

export default {
  name: "index",
  components: {InfoDialogBox, DialogBox, LayoutContainer},
  data() {
    return {
      ids: null,
      showSearch: true,
      loading: false,
      queryParams: {
        page: 1,
        limit: 20,
      },
      queryDataModel: [{type: "default",label: "任务名称",prop: "jobName"},
        {type: "select",label: "任务分组",prop: "jobGroup",data: [], field: {value: "jobGroup",label: "jobGroup"}},
        {type: "default",label: "Bean 名称",prop: "beanName"},
        {type: "select",label: "状态",prop: "status",data: [
            {id: 0,name: "正常"}, {id: 1,name: "暂停"}]},],
      dataList: [],
      total: 0,
      groupList: [],
      // form 表单
      title: null,
      formData: {},
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.groupAll();
      this.loading = true
      listData(this.queryParams).then(res => {
        this.dataList = res.data.list;
        this.total = res.data.totalCount;
        this.loading = false;
      });
    },
    groupAll() {
      groupAll().then(res => {
        this.groupList = res.data;
        this.queryDataModel[1].data = res.data;
      });
    },
    addJob() {
      this.title = "新增任务"
      this.$refs["dialogBox"].open();
    },
    submitForm(e) {
      if (e.jobId) {
        editData(e).then(res => {
          this.notSuccess(res.msg);
          this.$refs["dialogBox"].close();
          this.getList()
        })
      } else {
        saveData(e).then(res => {
          this.notSuccess(res.msg);
          this.$refs["dialogBox"].close();
          this.getList()
        })
      }
    },
    moreSelect(e) {
      switch (e.index) {
        case 1: runJob({jobId: e.row.jobId}).then(res => {
          this.notSuccess(res.msg);
        }); break;
        case 2: infoData(e.row.jobId).then(res => {
          this.formData = res.data;
          this.$refs["infoDialogBox"].open();
        }); break;
        case 3: this.toLogRouter(e.row); break;
      }

    },
    editClick(row) {
      this.title = "修改任务";
      infoData(row.jobId).then(res => {
        this.formData = res.data;
        this.$refs["dialogBox"].open();
      })
    },
    batchDelete() {
      if (this.ids && this.ids.length > 0) {
        this.$confirm("此操作将永久删除该任务, 是否继续?","提示", {
          cancelButtonText: "不用了",
          confirmButtonText: "确认",
          type: "warning",
        }).then(() => {
          this.deleteClick({jobId: this.ids.toString})
        })
      } else {
        this.notWarning("请选择需要删除的任务！")
      }
    },
    deleteClick(row) {
      deleteData(row.jobId).then(res => {
        this.notSuccess(res.msg);
        this.getList();
      })
    },
    onClickSwitch(row) {
      switchStatus(row.jobId).then(res => {
        this.notSuccess(res.msg);
        this.getList();
      })
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
    },
    toLogRouter(query) {
      this.$router.push({
        path: "/monitor/jobsechdulelog",
        query: query
      });
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
.tooltip {
  white-space: nowrap;
  text-overflow: ellipsis;
}
.el-dropdown {
  margin-left: 10px;
}
.el-popconfirm {
  margin-left: 10px;
}
</style>
