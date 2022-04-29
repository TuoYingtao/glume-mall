<template>
  <layout-container>
    <el-card class="box-card min-h10">
      <el-empty v-if="is_empty" class="empty" :image-size="300" description="暂无数据"></el-empty>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button type="info" icon="el-icon-sort" size="mini" @click="toggleExpandAll">展开/折叠</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getList" />
      </el-row>
      <!--   表格   -->
      <el-table v-if="refreshTable" v-loading="is_loading" :data="menuList" row-key="menuId" :default-expand-all="isExpandAll"
                :row-style="rowStyle" :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
                :header-cell-style="{'text-align':'justify-all'}" :cell-style="{'text-align':'justify-all'}">
        <el-table-column prop="name" label="菜单名称" :show-overflow-tooltip="true"/>
        <el-table-column prop="icon" label="图标" align="center">
          <template slot-scope="scope">
            <svg-icon :icon-class="scope.row.icon" />
          </template>
        </el-table-column>
        <el-table-column prop="menuType" label="菜单级别" :show-overflow-tooltip="true"/>
        <el-table-column prop="orderNum" label="排序"/>
        <el-table-column prop="perms" label="权限标识" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.perms" type="warning">{{scope.row.perms}}</el-tag>
            <el-tag type="info" v-else>-------</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.component">{{scope.row.component}}</el-tag>
            <el-tag type="info" v-else>-------</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="query" label="请求参数" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.query" type="info">{{scope.row.query}}</el-tag>
            <el-tag type="info" v-else>-------</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="显示/隐藏">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.visible == 0">显示</el-tag>
            <el-tag v-else type="info">隐藏</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status == 0" type="success">正常</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime,'{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-plus" @click="handleAdd(scope.row)">新增</el-button>
            <el-button size="mini" type="warning" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="38%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <treeselect v-model="form.parentId" :options="menuOptions" :normalizer="normalizer" :show-count="true" placeholder="选择上级菜单" @select="treeselectSelect"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio :label="'M'">目录</el-radio>
                <el-radio :label="'C'">菜单</el-radio>
                <el-radio :label="'F'">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item v-if="form.menuType != 'F'" label="菜单图标">
              <el-popover placement="bottom-start" width="460" trigger="click" @show="$refs['iconSelect'].reset()">
                <IconSelect ref="iconSelect" @selected="selected" />
                <el-input slot="reference" v-model="form.icon" placeholder="点击选择图标" readonly>
                  <svg-icon v-if="form.icon" slot="prefix" :icon-class="form.icon" class="el-input__icon" style="height: 32px;width: 16px;"/>
                  <i v-else slot="prefix" class="el-icon-search el-input__icon" />
                </el-input>
              </el-popover>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
<!--          <el-col :span="12">-->
<!--            <el-form-item v-if="form.menuType != 'F'">-->
<!--              <span slot="label">-->
<!--                <el-tooltip content="选择是外链则路由地址需要以`http(s)://`开头" placement="top">-->
<!--                  <i class="el-icon-question"></i>-->
<!--                </el-tooltip>-->
<!--                是否外链-->
<!--              </span>-->
<!--              <el-radio-group v-model="form.isFrame">-->
<!--                <el-radio label="0">是</el-radio>-->
<!--                <el-radio label="1">否</el-radio>-->
<!--              </el-radio-group>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'" prop="path">
              <span slot="label">
                <el-tooltip content="访问的路由地址，如：`/system`、`/system/index`，默认在`views`目录下" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                路由地址
              </span>
              <el-input v-model="form.path" placeholder="请输入路由地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType == 'C'">
            <el-form-item prop="component">
              <span slot="label">
                <el-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                组件路径
              </span>
              <el-input v-model="form.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'M'">
              <el-input v-model="form.perms" placeholder="请输入权限标识" maxlength="100" />
              <span slot="label">
                <el-tooltip content="控制器中定义的权限字符，如：@PreAuthorize(`@ss.hasPermi('system:user:list')`)" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                权限字符
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType == 'C'">
              <el-input v-model="form.query" placeholder="请输入路由参数" maxlength="255" />
              <span slot="label">
                <el-tooltip content='访问路由的默认传递参数，如：`{"id": 1, "name": "ry"}`' placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                路由参数
              </span>
            </el-form-item>
          </el-col>
<!--          <el-col :span="12">-->
<!--            <el-form-item v-if="form.menuType == 'C'">-->
<!--              <span slot="label">-->
<!--                <el-tooltip content="选择是则会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致" placement="top">-->
<!--                <i class="el-icon-question"></i>-->
<!--                </el-tooltip>-->
<!--                是否缓存-->
<!--              </span>-->
<!--              <el-radio-group v-model="form.isCache">-->
<!--                <el-radio label="0">缓存</el-radio>-->
<!--                <el-radio label="1">不缓存</el-radio>-->
<!--              </el-radio-group>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'">
              <span slot="label">
                <el-tooltip content="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                显示状态
              </span>
              <el-radio-group v-model="form.visible">
                <el-radio :label="0">显示</el-radio>
                <el-radio :label="1">隐藏</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item v-if="form.menuType != 'F'">
              <span slot="label">
                <el-tooltip content="选择停用则路由将不会出现在侧边栏，也不能被访问" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                菜单状态
              </span>
              <el-radio-group v-model="form.status">
                <el-radio :label="0">正常</el-radio>
                <el-radio :label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </layout-container>
</template>

<script>
import { mapGetters } from "vuex"
import LayoutContainer from "@/components/LayoutContainer/LayoutContainer";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import IconSelect from "@/components/IconSelect";
import {menuList, updateMenu, addMenu, delMenu, queryMenu} from "@/api/menu";
import {MessageBox} from "element-ui";

export default {
  components: {LayoutContainer,Treeselect,IconSelect},
  name: "index",
  data () {
    return {
      showSearch: true,
      is_empty: false,
      refreshTable: true,
      is_loading: false,
      menuList: [],
      // 是否展开，默认全部折叠
      isExpandAll: false,
      // 菜单树选项
      menuOptions: [],
      // 是否显示弹出层
      open: false,
      // 弹出层标题
      title: "",
      // 表单参数
      form: {
        menuType: "M",
        icon: '',
        visible: 0,
        status: 0,
      },
      // 表单校验
      rules: {
        name: [
          { required: true, message: "菜单名称不能为空", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "菜单顺序不能为空", trigger: "blur" }
        ],
        path: [
          { required: true, message: "路由地址不能为空", trigger: "blur" }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(["info"]),
  },
  created() {
    this.getList();
  },
  methods: {
    // 选择图标
    selected(name) {
      this.form.icon = name;
    },
    getList() {
      this.is_loading = true
      console.log(this.info);
      menuList(this.info.userId).then(response => {
        if (response.data.menus && response.data.menus.length == 0) {
          this.is_empty = true;
        } else {
          this.menuList = response.data.menus;
        }
        this.is_loading = false
      })
    },
    rowStyle({row, rowIndex}) {
      if (row.menuType == "M") {
        return {"background": "#FFF"}
      } else if (row.menuType == "C") {
        return {"background": "#F7F7F7"}
      } else {
        return {"background": "#F2F2F2"}
      }
    },
    treeselectSelect(e) {
      this.form.parentId = e.menuId;
    },
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.menuId,
        label: node.name,
        children: node.children
      };
    },
    /** 查询菜单下拉树结构 */
    getTreeselect() {
      menuList(this.info.userId).then(response => {
        this.menuOptions = [];
        const menu = { menuId: 0, name: '主类目', children: [] };
        menu.children = this.handleTree(response.data.menus, "menuId");
        this.menuOptions.push(menu);
      });
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      if (row != null && row.menuId) {
        this.form.parentId = row.menuId;
      } else {
        this.form.parentId = 0;
      }
      this.open = true;
      this.title = "添加菜单";
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable  = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable  = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      queryMenu(row.menuId).then(response => {
        this.form = response.data.menuInfo;
        this.open = true;
        this.title = "修改菜单";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.menuId != undefined) {
            updateMenu(this.form).then(response => {
              this.notSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMenu(this.form).then(response => {
              this.notSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      MessageBox.confirm('是否确认删除名称为"' + row.name + '"的数据项？').then(function() {
        return delMenu(row.menuId);
      }).then(() => {
        this.getList();
        this.notSuccess("删除成功");
      }).catch(() => {});
    },
    cancel() {
      this.reset();
      this.open = false;
    },
    reset() {
      this.title = "";
      this.form = {
        menuType: "M",
        icon: '',
        visible: 0,
        status: 0,
      };
    }
  }
}
</script>

<style scoped>
.empty {
  margin-top: 10%;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: justify-all;
}
</style>
