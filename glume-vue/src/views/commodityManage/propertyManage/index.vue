<template>
  <layout-container>
    <tree-box ref="treeBox" @setCatelongId="setCatelongId" @getAttrList="getAttrList" @reset="reset">
      <!--    搜索框-->
      <search-box class="search-box" v-show="showSearch" :is-select="true" :param-accept="queryParam" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
      <el-card class="box-card">
        <div class="table-title-text">{{ title }}</div>
        <el-row :gutter="10" class="mb8">
          <el-col :span="10">
            <el-button icon="el-icon-plus" size="mini" @click="addAttrGroup()">添加</el-button>
          </el-col>
          <right-toolbar :show-search.sync="showSearch" :is-flag-show="$route.meta.search" @queryTable="getAttrList"/>
        </el-row>
        <el-table v-loading="loading" :data="attrList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" :key="domKey2">
          <el-table-column type="selection" width="80" align="center"/>
          <el-table-column label="图标" prop="icon"/>
          <el-table-column label="组名" prop="attrGroupName"/>
          <el-table-column label="描述" prop="descript">
            <template slot-scope="scope">
              <span class="text-table">{{ scope.row.descript }}</span>
            </template>
          </el-table-column>
          <el-table-column label="排序" prop="sort">
            <template slot-scope="scope">
              <el-input class="sort-input" type="number" v-model="scope.row.sort" @blur="blurInputItem('is_sort',scope.row)"/>
            </template>
          </el-table-column>
          <el-table-column label="操作" prop="sort">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="mapping(scope.row)">属性关联</el-button>
              <el-button type="warning" size="mini" @click="amendAttrGroup(scope.row)">修改</el-button>
              <el-button type="danger" size="mini" @click="attrDelClick(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination :page-size="[10,20,30,40,50]" v-show="total > 0" :total="total" :page.sync="queryParam.page" :limit.sync="queryParam.limit" @pagination="getAttrList"/>
      </el-card>
    </tree-box>
    <price-dialog ref="RefDialog" :mobel-id="id"/>

    <!-- attr 弹窗 -->
    <el-dialog :title="dialogTitle" width="35%" :visible.sync="isAttrOpen" :before-close="attrFromHandleClose">
      <el-form ref="attrFrom" :model="attrFrom" :rules="TreeRules" size="medium" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="属性分组名称：" prop="attrGroupName">
              <el-input v-model="attrFrom.attrGroupName"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="属性分组描述：" prop="descript">
              <el-input v-model="attrFrom.descript" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="属性分组图标：" prop="icon">
              <el-input v-model="attrFrom.icon" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="属性分组排序：" prop="sort">
              <el-input-number v-model="attrFrom.sort" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <span class="fr">
            <el-button @click="attrFromHandleClose">取消</el-button>
            <el-button type="cyan" @click="attrFromSubmitForm">更新</el-button>
          </span>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 弹窗 -->
    <group-property-relation-dialog ref="groupPropertyRelationDialog"
                                    :attrGroupIds="attrGroupId"
                                    @addAttrGroupRelation="addAttrGroupRelation"/>
    <add-group-relation-dialog ref="addGroupRelationDialog"
                               :attrGroupId="attrGroupId"
                               @flushGroupRelation="flushGroupRelation"/>
  </layout-container>
</template>

<script>
import PriceDialog from '@/views/system/brandOperate/components/dialog'
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
import {MessageBox} from "element-ui";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {addAttrGroup, amendAttrGroup, delAttrGroup, getAttrGroup, queryAttrGroup} from "@/api/commodityManage/attrGroup";
import SearchBox from "@/components/searchBox";
import treeBox from "@/views/commodityManage/componet/treeBox";
import groupPropertyRelationDialog from "@/views/commodityManage/propertyManage/children/groupPropertyRelationDialog";
import addGroupRelationDialog from "@/views/commodityManage/propertyManage/children/addGroupRelationDialog";

export default {
  name: 'index',
  components: {
    LayoutContainer,
    PriceDialog,
    SearchBox,
    treeBox,
    Treeselect,
    groupPropertyRelationDialog,
    addGroupRelationDialog,
  },
  data() {
    return {
      loading: false,
      queryDataModel: [{type: "default",label: "关键字查询",prop: "key"}],
      showSearch: true,
      queryParam: {page: 1, limit: 10},
      id: null,
      catId: null,
      is_price: false,
      TreeRules: {
        sort: [{ required: true, message: "排序不能为空", trigger: "blur" }],
        icon: [{ required: true, message: "图标地址不能为空", trigger: "blur" }],
        attrGroupName: [{ required: true, message: "属性分组名称不能为空", trigger: "blur" }],
        descript: [{ required: true, message: "属性分组描述不能为空", trigger: "blur" }],
      },
      attrList: [],
      /* 属性分组数组 */
      attrFrom: {},
      isAttrOpen: false,
      attrProps: {
        label: "name",
        value: "catId",
      },
      attrFromCatId: [2,34,225],
      dialogTitle: "",
      domKey2: 0,
      isSwitchAmand: false,
      attrGroupId: null,
      total: null,
      title: "",
    }
  },
  directives: {
    focus: {
      inserted: function(el) {
        el.querySelector("input").focus()
      }
    }
  },
  methods: {
    setCatelongId(e) {
      this.catId = e;
    },
    getAttrList() {
      this.loading = true;
      getAttrGroup(this.catId,this.queryParam).then(response => {
        this.attrList = response.data.page.list;
        this.total = response.data.page.totalCount;
        this.categoryPathHandler(response.data.categoryPath);
        this.loading = false;
        this.handlerBrandModel(this.attrList);
      })
    },
    handlerBrandModel(brandModel) {
      if (brandModel && brandModel.length > 0) {
        brandModel.forEach(item => {
          item.is_name = true;
          item.is_sort = true;
          item.is_ave_price = true;
        })
      }
    },
    mapping(row) {
      this.attrGroupId = row.attrGroupId;
      this.$refs['groupPropertyRelationDialog'].open(this.attrGroupId);
    },
    addAttrGroupRelation() {
      this.$refs['addGroupRelationDialog'].open();
    },
    flushGroupRelation() {
      this.$refs['groupPropertyRelationDialog'].getRelationList()
    },
    categoryPathHandler(categoryPath) {
      this.attrFrom.categoryPath = []
      this.title = "";
      categoryPath.forEach(item => {
        if (this.title == "") {
          this.title = item.name;
        } else {
          this.title = this.title + " > " + item.name;
        }
        this.attrFrom.categoryPath.push(item.catId)
      })
    },
    onInput(node,data) {
      this.handlerInput(node,data);
    },
    blurInputItem(key,row) {
      this.handlerItem(key,row);
      amendAttrGroup(row).then(response => {
        if (response.code == 200) {
          this.notSuccess("修改成功!")
        }
      })
    },
    handlerInput(node,data) {
      this.brandTreeList.forEach(item => {
        if (item.id == node.parent.data.id) item.children.forEach(child => { if (child.id == data.id) child.is_sort = !child.is_sort;})
      })
      this.domKey += 1;
    },
    cascaderChange(value) {
      this.attrFrom.catelogId = value;
    },
    queryAttrGroupInfo(attrGroupId) {
      queryAttrGroup(attrGroupId).then(response => {
        this.attrFrom = response.data
      })
    },
    addAttrGroup() {
      this.attrReset()
      this.dialogTitle = "添加属性"
      this.isAttrOpen = true;
    },
    amendAttrGroup(row) {
      this.attrReset()
      this.dialogTitle = "修改属性"
      this.queryAttrGroupInfo(row.attrGroupId);
      this.isAttrOpen = true;
    },
    attrDelClick: function (row) {
      MessageBox.confirm('是否确认删除名称为"' + row.attrGroupName + '"的数据项？').then(function () {
        return delAttrGroup({attrGroupIds: row.attrGroupId});
      }).then(() => {
        this.getAttrList();
        this.notSuccess("删除成功");
      }).catch(() => {
      });
    },
    attrFromSubmitForm() {
      this.$refs["attrFrom"].validate(valid => {
        if (valid) {
          if (this.attrFrom.attrGroupId != undefined) {
            this.attrFrom.categoryPath = []
            amendAttrGroup(this.attrFrom).then(response => {
              this.notSuccess("修改成功");
              this.isAttrOpen = false;
              this.getAttrList();
            });
          } else {
            if (this.catId == 0) return this.notError("请选择一种分类在来添加");
            this.attrFrom.catelogId = this.catId;
            addAttrGroup(this.attrFrom).then(response => {
              this.notSuccess("新增成功");
              this.isAttrOpen = false;
              this.getAttrList();
            });
          }
        }
      });
    },
    attrFromHandleClose () {
      this.isAttrOpen = false
      this.reset()
    },
    handlerItem(key,row) {
      this.attrList.forEach(item => {
        if (item.id == row.id) {
          item[key] = !item[key];
        }
      })
      this.domKey2 += 1;
    },
    handleQuery() {
      this.getAttrList();
    },
    resetQuery() {
      this.catId = 0;
      this.reset();
      this.getAttrList();
    },
    attrReset() {
      this.reset();
    },
    reset() {
      this.dialogTitle = "";
      this.queryParam = {
        page: 1,
        limit: 10
      }
      this.attrFrom = {};
    },
    openPrice(row) {
      this.$refs["RefDialog"].open();
    }
  }
}
</script>

<style lang="scss" scoped>
.table-title-text {
  font-size: 14px;
  color: #999;
  padding-bottom: 10px;
}
.text-table {
  padding: 8px 20px;
}
.price-input {
  width: 300px;
}
.sort-input {
  width: 150px;
}
</style>
