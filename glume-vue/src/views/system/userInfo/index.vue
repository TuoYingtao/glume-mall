<template>
  <layout-container>
    <!--    搜索框-->
    <search-box v-show="showSearch" :is-select="true" :param-accept="queryParams" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
    <!--    全局按钮-->
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-col>
          <el-button icon="el-icon-plus" size="mini" @click="addHandler">新增</el-button>
          <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getList" />
        </el-col>
      </el-row>
      <!--    内容展示-->
      <el-table v-loading="loading" :data="userList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="用户编号" prop="userId"/>
        <el-table-column label="角色" prop="roleName">
          <template slot-scope="scope">
            <el-tag type="primary">{{ scope.row.roleName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="用户名称" prop="username"/>
        <el-table-column label="手机号" prop="mobile">
          <template slot-scope="scope">
            <span>{{ scope.row.mobile ? scope.row.mobile : "暂无" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="邮箱" prop="email">
          <template slot-scope="scope">
            <span>{{ scope.row.email ? scope.row.email : "暂无" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.status == 0">启用</el-tag>
            <el-tag type="warning" v-if="scope.row.status == 1">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="注册日期" align="center" prop="createTime">
          <template  slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="380">
          <template slot-scope="scope">
            <el-button size="mini" type="warning" @click="amendHandler(scope.row)">编辑</el-button>
            <el-button size="mini" type="primary" @click="resetPasswordHandler(scope.row)">重置密码</el-button>
            <el-popconfirm style="padding-left: 10px" confirm-button-text='好的' cancel-button-text='不用了'
                           icon="el-icon-info" icon-color="red"
                           :title="`确定删除${scope.row.username}内容吗？`"
                           @confirm="deleteHandler(scope.row)">
              <el-button size="mini" type="danger" slot="reference">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :current-page.sync="queryParams.page" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
    <dialog-model ref="dialog" :role-list="roleAllList" :title="title" :form="form" @affirmActive="affirmActive" @handleClose="handleClose"/>
  </layout-container>
</template>

<script>
import {deleteUserInfo, getUserInfo, resetPassword, updateUserInfo, userList, saveUserInfo} from '@/api/userInfo.js'
import SearchBox from '@/components/searchBox/index'
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
import DialogModel from "@/views/system/userInfo/component/DialogModel";
import {roleListAll} from "@/api/role";

export default {
  components: { LayoutContainer, SearchBox, DialogModel },
  name: 'orderList',
  data() {
    return {
      id: null,
      loading: true,
      total: 0,
      userList: [],
      roleAllList: [],
      showSearch: true,
      queryDataModel: [{type: "default",label: "用户名称",prop: "username"},
        {type: "default",label: "手机号",prop: "mobile"},
        {type: "select",label: "角色",data: [],
          prop: "roleId",input: false,field: {label: "roleName",value: "roleId"}},
        {type: "select",label: "状态",data: [{id: 0,name: "启用"},{id: 1,name: "禁用"}],prop: "status",input: false}],
      queryParams: {
        page: 1,
        limit: 20,
      },
      title: "",
      form: {
        username: "",
        mobile: "",
        email: "",
        status: 0,
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {
    roleList() {
      roleListAll().then(res => {
        this.roleAllList = res.data;
        this.queryDataModel[2].data = res.data;
      });
    },
    getList() {
      this.loading = true;
      userList(this.queryParams).then(response => {
        this.userList = response.data.list;
        this.total = response.data.totalCount;
        this.loading = false;
      }).then(() => {
        this.roleList();
      })
    },
    addHandler() {
      this.title = "添加用户信息";
      this.$refs['dialog'].open();
    },
    amendHandler(row) {
      getUserInfo(row.userId).then(res => {
        this.form = res.data;
        this.title = "修改用户信息";
        this.$refs['dialog'].open();
      });
    },
    resetPasswordHandler(row) {
      this.$prompt("输入新密码！", "重置", {
        confirmButtonText: "确认",
        cancelButtonText: "不用了",
        inputErrorMessage: '新密码不能为空！'
      }).then(({ value }) => {
        resetPassword(row.userId,{password: value}).then(res => {
          this.notSuccess(res.msg);
        })
      });
    },
    deleteHandler(row) {
      deleteUserInfo(row.userId).then(rse => {
        this.getList();
      });
    },
    affirmActive(data) {
      if (data.userId) {
        updateUserInfo(data).then(rse => {
          this.notSuccess(res.msg);
          this.$refs['dialog'].close();
          this.reset();
        });
      } else {
        saveUserInfo(data).then(res => {
          this.notSuccess(res.msg);
          this.$refs['dialog'].close();
          this.reset();
        });
      }
    },
    handleClose() {
      this.reset();
    },
    handleQuery() {
      this.queryParams.page = 1;
      this.queryParams.limit = 20;
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
      this.form = {
        username: "",
        mobile: "",
        email: "",
        status: 0,
      };
      this.queryParams = {
        page: 1,
        limit: 20,
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
