<!-- TODO 每日秒杀待完成 -->
<template>
  <layout-container>
    <!--    搜索框-->
    <search-box v-show="showSearch" :is-select="true" :param-accept="queryParams" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
    <!--    全局按钮-->
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-button icon="el-icon-plus" size="mini" @click="addData">新增</el-button>
        <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getList" />
      </el-row>
      <!--    内容展示-->
      <el-table v-loading="loading" :data="dataList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="场次名称" prop="name"/>
        <el-table-column label="每日开始时间" prop="startTime"/>
        <el-table-column label="每日结束时间" prop="endTime"/>
        <el-table-column label="状态" prop="status">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status == 1" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime"/>
        <el-table-column label="操作" prop="sort">
          <template slot-scope="scope">
            <el-button icon="el-icon-edit" type="warning" size="mini" @click="amendRole(scope.row)">修改</el-button>
            <el-button icon="el-icon-delete" type="danger" size="mini" @click="deleteRole(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
    <!-- 弹出层 -->
    <my-dialog ref="dialog" :title="title" :form="form" @submitForm="submitForm"/>
  </layout-container>
</template>

<script>
  import SearchBox from '@/components/searchBox/index'
  import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
  import {MessageBox} from "element-ui";
  import {getData,save,info,update,delData} from "@/api/DiscountsManage/SessionSeckill";
  import MyDialog from "@/views/discountsManage/SessionSeckill/component/MyDialog";
  export default {
    name: 'index',
    components: {MyDialog, LayoutContainer, SearchBox },
    data() {
      return {
        total: 0,
        showSearch: true,
        loading: false,
        queryParams: {
          page: 1,
          limit: 20,
        },
        queryDataModel: [{type: "default",label: "场次名称",prop: "name"},
          {type: "select",label: "状态",prop: "status",data: [
              {id: 0,name: "禁用"}, {id: 1,name: "启用"}]},
          {type: "datetime",label: "活动时间范围",prop: "dateTime",format: "HH:mm:ss"}],
        title: "",
        form: {
          status: 1,
        },
        dataList: [],
        menuOptions: [],
        defaultProps: {
          children: "children",
          label: "label"
        },
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        this.loading = true;
        getData(this.queryParams).then(response => {
          this.dataList = response.data.list;
          this.total = response.data.totalCount;
          this.loading = false;
        })
      },
      addData() {
        this.reset();
        this.title = "添加场次信息";
        this.$refs["dialog"].open();
      },
      amendRole(row) {
        this.reset();
        this.form.id = row.role;
        info(row.id).then(response => {
          this.form = response.data;
          if (response.data.startTime && response.data.endTime) {
            this.form.dateTime = [response.data.startTime, response.data.endTime];
          }
          this.title = "修改场次信息";
          this.$refs["dialog"].open();
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        if (this.form.id != undefined) {
          update(this.form).then(response => {
            this.notSuccess("修改成功");
            this.$refs["dialog"].close();
            this.getList();
          });
        } else {
          save(this.form).then(response => {
            this.notSuccess("新增成功");
            this.$refs["dialog"].close();
            this.getList();
          });
        }
      },
      deleteRole(row) {
        MessageBox.confirm('是否确认删除名称为"' + row.name + '"的数据项？').then(function() {
          return delData(row.id);
        }).then(() => {
          this.getList();
          this.notSuccess("删除成功");
        }).catch(() => {});
      },
      handleSelectionChange(e) {
        console.log(e)
      },
      handleQuery(param) {
        this.getList();
      },
      resetQuery() {
        this.reset();
        this.getList();
      },
      reset() {
        this.title = "";
        this.form = {
          status: 1
        };
        this.queryParams = {
          page: 1,
          limit: 20,
        };
      }
    }
  }
</script>

<style scoped>
</style>
