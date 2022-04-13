<template>
  <layout-container>
    <!--    搜索框-->
    <search-box v-show="showSearch" :is-select="true" :param-accept="queryParams" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
    <!--    全局按钮-->
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-col>
          <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getList" />
        </el-col>
      </el-row>
      <!--    内容展示-->
      <el-table v-loading="loading" :data="dataList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="用户名" prop="username"/>
        <el-table-column label="IP地址" prop="ip"/>
        <el-table-column label="令牌" prop="token">
          <template slot-scope="scope">
            <el-tooltip class="tooltip" effect="dark" :content="scope.row.token" placement="top">
              <span>{{scope.row.token}}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="登录时间" align="center" prop="createTime">
          <template  slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="forceKikeOut(scope.row)">强制下线</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :current-page.sync="queryParams.page" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
  </layout-container>
</template>

<script>
import {list, forceKikeOut} from "@/api/onlineList";

export default {
  name: "index.vue",
  data() {
    return {
      ids: null,
      showSearch: true,
      queryParams: {
        page: 1,
        limit: 20,
      },
      queryDataModel: [{type: "default",label: "用户名",prop: "username"}],
      dataList: [],
      total: 0,
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      list(this.queryParams).then(res => {
        this.dataList = res.data.list;
        console.log(res)
      })
    },
    forceKikeOut(row) {
      forceKikeOut({id: row.id}).then(res => {
        this.notSuccess(res.msg);
      });
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

<style lang="scss" scoped>
  .tooltip {
    word-break: keep-all;
    white-space: nowrap;
  }
</style>
