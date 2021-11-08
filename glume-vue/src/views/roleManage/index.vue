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
      <table-box :loading="loading" :data="roleList" :tableColumn="tableColumn" @jumpPage="jumpPage" @selection-change="handleSelectionChange"/>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
    <!-- 弹出层 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="25%" :before-close="handleClose">
      <el-form ref="elForm" :model="form" :rules="rules" size="medium" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="角色名称：" prop="roleName">
              <el-input v-model="form.roleName"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="角色标签：" prop="roleTag">
              <el-input v-model="form.roleTag"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述：">
              <el-input type="textarea" :rows="3"placeholder="请输入内容" v-model="form.remark"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单权限：">
              <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠</el-checkbox>
              <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">全选/全不选</el-checkbox>
              <el-checkbox v-model="form.menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">父子联动</el-checkbox>
              <el-tree class="tree-border" :data="menuOptions" show-checkbox ref="menu" node-key="id"
                       :check-strictly="!form.menuCheckStrictly" empty-text="加载中，请稍候" :props="defaultProps"></el-tree>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <span class="fr">
            <el-button @click="handleClose">取消</el-button>
            <el-button type="cyan" @click="submitForm">添加</el-button>
          </span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </layout-container>
</template>

<script>
import {addRole, getTreeSelect, roleList, updateRole} from "@/api/role"
  import SearchBox from '@/components/searchBox/index'
  import TableBox from '@/components/TableBox'
  import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
  export default {
    name: 'roleList',
    components: { LayoutContainer, SearchBox,TableBox },
    data() {
      return {
        tableColumn: [
          {label: '角色ID',prop: 'roleId'},
          {label: '角色名称',prop: 'roleName'},
          {label: '角色标签',prop: 'roleTag'},
          {label: '描述',prop: 'remark'},
          {label: '操作',size: 'mini',model: [
              {name: '修改',color: 'warning',onClick: 'amendRole',icon: "el-icon-edit"},
              {name: '删除',color: 'warning',onClick: 'deleteRole',icon: "el-icon-edit"}],type: 'bottom'},
        ],
        total: 0,
        showSearch: true,
        loading: false,
        queryParams: {
          page: 1,
          limit: 20,
        },
        queryDataModel: [{type: "default",label: "角色ID",prop: "roleId"},
          {type: "default",label: "角色名称",prop: "roleName"},
          {type: "datetime",label: "日期范围",prop: "date_time"}],
        title: "",
        dialogVisible: false,
        form: {},
        rules: {
          roleName: [{ required: true, message: "菜单名称不能为空", trigger: "blur" }],
          roleTag: [{ required: true, message: "菜单顺序不能为空", trigger: "blur" }],
        },
        roleList: [],
        menuExpand: false,
        menuNodeAll: false,
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
        roleList(this.queryParams).then(response => {
          this.roleList = response.data.list;
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
              this.form.menuIds = this.getMenuAllCheckedKeys();
              updateRole(this.form).then(response => {
                this.notSuccess("修改成功");
                this.dialogVisible = false;
                this.getList();
              });
            } else {
              this.form.menuIds = this.getMenuAllCheckedKeys();
              addRole(this.form).then(response => {
                this.notSuccess("新增成功");
                this.dialogVisible = false;
                this.getList();
              });
            }
          }
        });
      },
      amendRole(row) {

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
        console.log(param)
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
