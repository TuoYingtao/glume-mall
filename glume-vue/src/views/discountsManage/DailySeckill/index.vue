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
  </layout-container>
</template>

<script>
  import SearchBox from '@/components/searchBox/index'
  import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
  import {MessageBox} from "element-ui";
  import {getData} from "@/api/DiscountsManage/DailySeckill";
  export default {
    name: 'index',
    components: { LayoutContainer, SearchBox },
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
          {type: "datetime",label: "活动时间范围",prop: "dateTime"}],
        title: "",
        dialogVisible: false,
        form: {},
        rules: {
          roleName: [{ required: true, message: "菜单名称不能为空", trigger: "blur" }],
          roleTag: [{ required: true, message: "菜单顺序不能为空", trigger: "blur" }],
        },
        dataList: [],
        menuExpand: false,
        menuNodeAll: false,
        menuIds: [],
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
        this.title = "添加角色";
        this.dialogVisible = true;
        this.treeSelect();
      },
      amendRole(row) {
        this.reset();
        this.treeSelect();
        this.form.roleId = row.role;
        queryRole(row.roleId).then(response => {
          this.form = response.data.info;
          this.menuIds = response.data.menuIds;
          this.dialogVisible = true;
          this.title = "修改角色";
        });
      },
      treeSelect() {
        getTreeSelect().then(response => {
          this.menuOptions = response.data.menus;
        })
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["elForm"].validate(valid => {
          if (valid) {
            if (this.form.roleId != undefined) {
              if (this.form.roleTag == "admin") {
                this.dialogVisible = false;
                return this.notError("超级管理员工不能修改！");
              }
              this.form.menuIds = this.getMenuAllCheckedKeys() || [];
              updateRole(this.form).then(response => {
                this.notSuccess("修改成功");
                this.dialogVisible = false;
                this.getList();
              });
            } else {
              this.form.menuIds = this.getMenuAllCheckedKeys() || [];
              addRole(this.form).then(response => {
                this.notSuccess("新增成功");
                this.dialogVisible = false;
                this.getList();
              });
            }
          }
        });
      },
      deleteRole(row) {
        if (row.roleTag == "admin") return this.notError("超级管理员工不能删除！");
        MessageBox.confirm('是否确认删除名称为"' + row.roleName + '"的数据项？').then(function() {
          return delRole(row.roleId);
        }).then(() => {
          this.getList();
          this.notSuccess("删除成功");
        }).catch(() => {});
      },
      // 所有菜单节点数据
      getMenuAllCheckedKeys() {
        // 目前被选中的菜单节点
        let checkedKeys = this.$refs.menu.getCheckedKeys();
        // 半选中的菜单节点
        let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
        checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
        return checkedKeys;
      },
      // 树权限（展开/折叠）
      handleCheckedTreeExpand(value, type) {
        if (type == 'menu') {
          let treeList = this.menuOptions;
          for (let i = 0; i < treeList.length; i++) {
            this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
          }
        } else if (type == 'dept') {
          let treeList = this.deptOptions;
          for (let i = 0; i < treeList.length; i++) {
            this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
          }
        }
      },
      // 树权限（全选/全不选）
      handleCheckedTreeNodeAll(value, type) {
        if (type == 'menu') {
          this.$refs.menu.setCheckedNodes(value ? this.menuOptions: []);
        } else if (type == 'dept') {
          this.$refs.dept.setCheckedNodes(value ? this.deptOptions: []);
        }
      },
      // 树权限（父子联动）
      handleCheckedTreeConnect(value, type) {
        if (type == 'menu') {
          this.form.menuCheckStrictly = value ? true: false;
        } else if (type == 'dept') {
          this.form.deptCheckStrictly = value ? true: false;
        }
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
      handleClose() {
        this.dialogVisible = false;
        this.reset();
      },
      reset() {
        this.title = "";
        this.menuExpand = false;
        this.menuNodeAll = false;
        this.menuIds = [];
        this.form = {};
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
