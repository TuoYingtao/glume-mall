<template>
  <layout-container>
    <!--    搜索框-->
    <search-box v-show="showSearch" :is-select="true" :param-accept="queryParams" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
    <!--    全局按钮-->
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-col>
          <el-button :class="[parentArray.length <= 1 ? 'is-disabled' : '']" type="primary" size="mini" @click="bakeInviteData">返回上一级</el-button>
          <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getList" />
        </el-col>
      </el-row>
      <!--    内容展示-->
      <el-table v-loading="loading" :data="userList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="用户编号" prop="id"/>
        <el-table-column label="手机号" prop="mobile">
          <template slot-scope="scope">
            <span>{{ scope.row.mobile ? scope.row.mobile : "暂无" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="定位地址" prop="location">
          <template slot-scope="scope">
            <span>{{ scope.row.location ? scope.row.location : "暂无" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="父级">
          <template slot-scope="scope">
            <span>{{ scope.row.parent_id ? scope.row.parent_id : "默认" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="邀请数" prop="invite">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" plain @click="getInviteData(scope.row)" v-if="scope.row.lowe_nums">{{ scope.row.lowe_nums }}</el-button>
            <span v-else>暂无</span>
          </template>
        </el-table-column>
        <el-table-column label="注册日期" align="center" prop="create_time">
          <template  slot-scope="scope">
            <span>{{ parseTime(scope.row.create_time) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :current-page.sync="queryParams.page" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
  </layout-container>
</template>

<script>
import { userList,setCompany } from '@/api/userInfo.js'
import SearchBox from '@/components/searchBox/index'
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'

export default {
  components: { LayoutContainer, SearchBox },
  name: 'orderList',
  data() {
    return {
      id: null,
      parentArray: [],
      loading: true,
      total: 0,
      userList: [],
      showSearch: true,
      queryDataModel: [{type: "default",label: "用户编号",prop: "id"},
        {type: "default",label: "手机号",prop: "mobile"},
        {type: "select",label: "邀请排序",data: [{id: 1,name: "降序"},{id: 2,name: "升序"}],prop: "sorts",input: false}],
      queryParams: {
        page: 1,
        limit: 20,
        parent_id: null,
        sorts: 1,
      },
    }
  },
  watch: {
    ["queryParams.page"]: {
      handler(val) {
        let parentObj = this.parentArray[this.parentArray.length - 1];
        if (parentObj) {
          parentObj.page = val;
        }
      }
    },
    ["queryParams.limit"]: {
      handler(val) {
        let parentObj = this.parentArray[this.parentArray.length - 1];
        if (parentObj) {
          parentObj.limit = val;
        }
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      userList(this.queryParams).then(response => {
        this.userList = response.data;
        this.total = response.total;
        this.loading = false;
      })
    },
    bakeInviteData() {
      this.parentArray.pop();
      if (this.parentArray.length == 0) return;
      let parentObj = this.parentArray[this.parentArray.length - 1];
      this.queryParams.parent_id = parentObj.parent_id;
      this.queryParams.page = parentObj.page;
      this.queryParams.limit = parentObj.limit;
      this.queryParams.sorts = parentObj.sorts;
      console.log(this.parentArray[0].page,parentObj.page,this.queryParams.page)
      this.getList();
    },
    async getInviteData(row) {
      console.log(this.parentArray)
      if (this.parentArray.length == 0) await this.handlerInviteParam();
      let param = {
        page: 1,
        limit: 20,
        parent_id: row.id,
        sorts: 1
      };
      this.parentArray.push(param);
      this.queryParams = param;
      this.getList();
    },
    handlerInviteParam() {
      this.parentArray.push({
        parent_id: null,
        page: this.queryParams.page,
        limit:  this.queryParams.limit,
        sorts: this.queryParams.sorts
      });
    },
    handleQuery() {
      this.queryParams.parent_id = null;
      this.queryParams.page = 1;
      this.queryParams.limit = 20;
      this.queryParams.sorts = 1;
      this.getList()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
    },
    resetQuery() {
      this.reset();
      this.getList();
    },
    reset() {
      this.id = "";
      this.queryParams = {
        page: 1,
        limit: 20,
        parent_id: null,
        sorts: 1,
      };
    },
  },
}
</script>

<style lang="scss" scoped>
  .el-dropdown {
    margin: 0 2px;
  }
  .integralInput /deep/ input::-webkit-inner-spin-button {
    -webkit-appearance: none;
  }
  .integralInput /deep/ input[type="number"] {
    -moz-appearance: textfield;
  }
</style>
