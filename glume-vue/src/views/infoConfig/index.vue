<template>
  <layout-container>
    <!-- 搜索 -->
    <search-box v-show="showSearch" :param-accept="queryParams" :search-data="searchData" @queryParams="queryData" @resetData="resetQuery" />

    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-col :span="0.5">
          <el-button size="mini" icon="el-icon-plus" @click="addUserInfo">新增</el-button>
        </el-col>
        <el-col :span="0.5">
          <el-popover placement="right" trigger="click">
            <el-input class="el-input" v-model="mobile" @blur="handlerService"/>
            <el-button size="mini" type="warning" slot="reference">客服电话</el-button>
          </el-popover>
        </el-col>
        <right-toolbar :show-search.sync="showSearch" :is-flag-show="$route.meta.search" @queryTable="getList"/>
      </el-row>
      <!-- 表格 -->
      <el-table v-loading="loading" :data="infoDataList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="姓名" prop="name"/>
        <el-table-column label="联系方式" prop="mobile"/>
        <el-table-column label="分公司" prop="store_id"/>
        <el-table-column label="创建时间">
          <template slot-scope="scope">
            <span type="success">{{ parseTime(scope.row.create_time) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="infoConfigDetail(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="deleteDetail(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-size="[20,40,60,80,100]" v-show="total > 0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
    <el-dialog :title="title" :visible.sync="dialogVisible" width="25%" :before-close="handleClose">
      <el-form ref="elForm" :model="form" :rules="rules" size="medium" label-width="100px">
        <el-form-item label="姓名：" prop="name">
          <el-col :span="18"><el-input style="width: 90%;" type="text" placeholder="请输入联系方式" v-model="form.name"/></el-col>
        </el-form-item>
        <el-form-item label="联系方式：" prop="mobile">
          <el-col :span="18"><el-input style="width: 90%;" type="number" placeholder="请输入联系方式" v-model="form.mobile"/></el-col>
        </el-form-item>
        <el-form-item label="分公司：" prop="store_id">
            <el-select clearable v-model="form.store_id" filterable placeholder="请选择或手动输入">
              <el-option v-for="item in searchData[1].data" :key="item.id" :label="item.name" :value="item.id"/>
            </el-select>
        </el-form-item>
        <el-form-item>
          <span class="fr">
            <el-button @click="handleClose">取消</el-button>
            <el-button type="cyan" @click="affirmActive">确认</el-button>
          </span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </layout-container>
</template>

<script>
  import { getInfoConfigList,getInfoDetail,addInfoConfigList,amendInfoConfigList,deleteInfoConfigList,getReception,amendReception } from '@/api/infoConfig'
  import SearchBox from '@/components/searchBox/index'
  import RightToolbar from '@/components/RightToolbar/index'
  import { MessageBox } from "element-ui"
  import { arrayFun } from '@/utils/tuo'
  import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
  export default {
    name: 'index',
    components: { LayoutContainer, RightToolbar, SearchBox },
    data() {
      return {
        id: null,
        loading: false,
        showSearch: true,
        dialogVisible: false,
        total: 0,
        title: "",
        searchData: [{type: "default",label: "联系方式",prop: "mobile"},
          {type: "select",label: "分公司",prop: "store_id",input: true,data:[]}],
        queryParams: {
          page: 1,
          limit: 20,
          status: 7
        },
        form: {status: 1,},
        infoDataList: [],
        rules: {
          name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
          mobile: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
          store_id: [{ required: true, message: '请选择或输入分公司', trigger: 'blur' },],
        },
        mobile: null,
      }
    },
    created() {
      this.getList();
      this.getReception();
    },
    methods: {
      getList() {
        this.loading = true;
        getInfoConfigList(this.queryParams).then(response => {
          this.infoDataList = response.data;
          this.searchData[1].data = response.company;
          this.total = response.total;
          this.loading = false;
        })
      },
      getReception() {
        getReception().then(response => {
          this.mobile = response.data.mobile;
        })
      },
      addUserInfo() {
        this.title = "添加员工";
        this.dialogVisible = true;
        this.flushStore();
      },
      handleSelectionChange(selection) {
        console.log(selection)
      },
      queryData(args) {
        this.getList();
      },
      resetQuery() {
        this.reset();
        this.getList();
      },
      flushStore() {
        getInfoConfigList(this.queryParams).then(response => {
          this.searchData[1].data = response.company;
        })
      },
      deleteDetail(row) {
        MessageBox.confirm("确认删除《" + row.name + "》这条记录吗？","系统提示", {
          confirmButtonText: "确认",
          cancelButtonText: "取消"
        }).then(() => {
          deleteInfoConfigList({id: row.id}).then(response => {
            this.notSuccess("成功删除此记录？")
            arrayFun.deleteList(this.infoDataList,"id",row.id);
          })
        })
      },
      infoConfigDetail(row){
        this.id = row.id;
		    this.title = "修改员工";
        getInfoDetail({id: row.id}).then(response => {
          console.log(response)
          this.form = response.data;
          this.dialogVisible = true;
        })
      },
      affirmActive() {
        this.$refs['elForm'].validate(valid => {
          if (!valid) return false;
          if (this.id == null) {
            addInfoConfigList(this.form).then(response => {
              this.dialogVisible = false;
              this.getList();
              this.reset();
            })
          } else {
            this.form.id = this.id;
            amendInfoConfigList(this.form).then(response => {
              this.dialogVisible = false;
              this.getList();
              this.reset();
              this.notSuccess("修改成功！！")
            })
          }
        })
      },
      handlerService() {
        if (/^1(3|4|5|6|7|8|9)\d{9}$/.test(this.mobile)) {
          amendReception({mobile: this.mobile}).then(response => {
            if (response.code == 200) {
              this.notSuccess("已修改客服号码！");
            }
          })
        } else {
          this.notWarning("手机号格式有误！");
          this.mobile = null;
        }
      },
      handleClose() {
        this.$refs['elForm'].resetFields();
        this.dialogVisible = false;
        this.reset();
      },
      reset() {
        this.id = null;
        this.form = {status: 1}
      }
    }
  }
</script>

<style scoped>
.service-text {
  padding-left: 20px;
  font-size: 14px;
}
.el-input {
  width: 400px;
}
</style>
