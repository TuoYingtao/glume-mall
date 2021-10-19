<template>
  <layout-container>
    <!-- 表格 -->
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button icon="el-icon-plus" size="mini" @click="addData">新增</el-button>
        </el-col>
        <right-toolbar :show-search.sync="showSearch" :is-flag-show="$route.meta.search" @queryTable="getList"/>
      </el-row>
      <!-- 表格 -->
      <el-table v-loading="loading" :data="massageDataList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="内容" prop="content" />
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleAmend(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-sizes="[20,40,60,80,100]" v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList" />
    </el-card>
    <!-- 弹出层 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="25%" :before-close="handleClose">
      <el-form ref="elForm" :model="form" :rules="rules" size="medium" label-width="80px">
        <el-form-item label="内容：" prop="content">
          <el-col :span="18"><el-input v-model="form.content"/></el-col>
        </el-form-item>
        <el-form-item>
          <span class="fr">
            <el-button @click="handleClose">取消</el-button>
            <el-button type="cyan" @click="handleAdd">添加</el-button>
          </span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </layout-container>
</template>

<script>
  import { getMessageList,addMessageItem,queryMessageItem,amendMessageItem,deleteMessageItem } from "@/api/messageApi"
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
        total: null,
        title: null,
        queryParams: {
          page: 1,
          limit: 20,
        },
        form: {content: null},
        massageDataList: [],
        rules: {
          content: [{ required: true, message: '请输入内容名称!', trigger: 'blur' }],
        },
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        this.loading = true;
        getMessageList(this.queryParams).then(response =>{
          this.massageDataList = response.data;
          this.total = response.total;
          this.loading = false;
        })
      },
      queryData(args) {
        console.log(args);
      },
      resetQuery() {
        console.log(this.queryParams);
      },
      handleSelectionChange(selection) {
        console.log(selection);
      },
      addData() {
        this.title = "新增通知";
        this.dialogVisible = true;
      },
      handleAmend(row) {
        this.title = "修改通知";
        this.dialogVisible = true;
        this.id = row.id;
        this.queryMessageData(row.id);
      },
      queryMessageData(id) {
        queryMessageItem({id:id}).then(response => {
          this.form.content = response.data.content;
        })
      },
      handleAdd() {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) return;
          if (this.id) {
            this.form.id = this.id;
            amendMessageItem(this.form).then(() => {
              this.dialogVisible = false;
              this.getList();
              this.reset();
            })
          } else {
            addMessageItem(this.form).then(() => {
              this.dialogVisible = false;
              this.getList();
              this.reset();
            })
          }
        })
      },
      handleDelete(row) {
        MessageBox.confirm("确认删除《" + row.content + "》此通知吗？","系统提示",{
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.form.id = row.id;
          deleteMessageItem(this.form).then(() => {
            arrayFun.deleteList(this.massageDataList,"id",row.id);
          })
        })
      },
      handleClose() {
        this.$refs['elForm'].resetFields();
        this.dialogVisible = false;
        this.reset();
      },
      reset() {
        this.id = null;
        this.form = {
          content: null
        }
      }
    }
  }
</script>

<style scoped>

</style>
