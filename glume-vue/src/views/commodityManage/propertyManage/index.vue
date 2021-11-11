<template>
  <layout-container>
    <div class="box-content">
      <el-card class="box-card">
        <div class="tree-input-box">
          <el-input class="tree-input" placeholder="输入品牌名称进行过滤" v-model="filterText" />
        </div>
        <div class="operation">
          <el-checkbox v-model="isAddCheckbox">添加</el-checkbox>
          <el-checkbox v-model="isAmendCheckbox">修改</el-checkbox>
          <el-checkbox v-model="isDelCheckbox">删除</el-checkbox>
        </div>
        <el-tree class="filter-tree" ref="tree" node-key="id"
                 :data="brandTreeList" :props="defaultProps"
                 :default-expanded-keys="expanded"
                 :filter-node-method="filterNode"
                 :default-expand-all="false"
                 :expand-on-click-node="false"
                 @node-click="handleNodeClick">
        <span class="custom-tree-node" slot-scope="{ node, data }" :key="domKey">
          <span class="el-tree-text" @click="expandedMenu(data)">{{ node.label }}</span>
          <template>
            <span>
              <el-button class="add" v-show="isAddCheckbox" type="text" size="mini" @click.stop="treeAddClick(data)">添加</el-button>
              <el-button class="amend" v-show="isAmendCheckbox" type="text" size="mini" @click.stop="treeAmendClick(data)">修改</el-button>
              <el-button class="del" v-show="isDelCheckbox" type="text" size="mini" @click.stop="treeDelClick(data)">删除</el-button>
            </span>
          </template>
        </span>
        </el-tree>
      </el-card>
      <div class="box-right">
        <!--    搜索框-->
        <search-box class="search-box" v-show="showSearch" :is-select="true" :param-accept="queryParam" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
        <el-card class="box-card">
          <div class="table-title-text">{{ title }}</div>
          <el-row :gutter="10" class="mb8">
            <el-col :span="10">
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
                <el-button type="warning" size="mini" @click="amendAttrGroup(scope.row)">修改</el-button>
                <el-button type="danger" size="mini" @click="delAttrGroup(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <pagination :page-size="[10,20,30,40,50]" v-show="total > 0" :total="total" :page.sync="queryParam.page" :limit.sync="queryParam.limit" @pagination="getAttrList"/>
        </el-card>
      </div>
    </div>
    <price-dialog ref="RefDialog" :mobel-id="id"/>

    <!-- attr 弹窗 -->
    <el-dialog :title="title" :visible.sync="isAttrOpen" width="28%" :before-close="treeFromHandleClose">
      <el-form ref="treeFrom" :model="attrFrom" :rules="TreeRules" size="medium" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="属性分组名称：" prop="attrGroupName">
              <el-input v-model="attrFrom.attrGroupName"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
          <el-col :span="12">
            <el-form-item label="属性分组路径：">
              <el-cascader v-model="attrFrom.catelogId" :options="treeOptions" :props="attrProps" @change="cascaderChange"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <span class="fr">
            <el-button @click="treeFromHandleClose">取消</el-button>
            <el-button type="cyan" @click="treeFromSubmitForm">添加</el-button>
          </span>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- tree 弹窗 -->
    <el-dialog :title="title" :visible.sync="isTreeOpen" width="28%" :before-close="treeFromHandleClose">
      <el-form ref="treeFrom" :model="treeFrom" :rules="TreeRules" size="medium" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级商品：">
              <treeselect v-model="treeFrom.parentCid" :options="treeOptions" :normalizer="normalizer" :show-count="true" placeholder="选择上级菜单" @select="treeselectSelect"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="分类名称：" prop="name">
              <el-input v-model="treeFrom.name"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序：" prop="catLevel">
              <el-input-number v-model="treeFrom.catLevel" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品排序：" prop="sort">
              <el-input-number v-model="treeFrom.sort" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计量单位：">
              <el-input v-model="treeFrom.productUnit"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品数量：" prop="productCount">
              <el-input-number v-model="treeFrom.productCount" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="图标：" prop="icon">
              <el-upload :action="uploadParams.host"
                         :data="uploadParams"
                         :on-success="uploadSuccess"
                         list-type="picture-card"
                         :auto-upload="true">
                  <i slot="default" class="el-icon-plus"></i>
                  <div slot="file" slot-scope="{file}">
                    <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
                    <span class="el-upload-list__item-actions">
                      <span class="el-upload-list__item-preview"@click="handlePictureCardPreview(file)">
                        <i class="el-icon-zoom-in"></i>
                      </span>
                      <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleDownload(file)">
                        <i class="el-icon-download"></i>
                      </span>
                      <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
                        <i class="el-icon-delete"></i>
                      </span>
                    </span>
                </div>
              </el-upload>
              <el-dialog :visible.sync="dialogVisible">
                <img width="100%" :src="dialogImageUrl" alt="">
              </el-dialog>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <span class="fr">
            <el-button @click="treeFromHandleClose">取消</el-button>
            <el-button type="cyan" @click="treeFromSubmitForm">添加</el-button>
          </span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </layout-container>
</template>

<script>
import {
  getBrandTree,
  amendBrandModel,
  getOSSPolicy,
  addBrandTree, amendBrandTree, delBrandTree, queryBrandTree
} from "@/api/commodityManage/classify"
import PriceDialog from '@/views/system/brandOperate/components/dialog'
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {MessageBox} from "element-ui";
import {getAttrGroup, queryAttrGroup} from "@/api/commodityManage/attrGroup";
import SearchBox from "@/components/searchBox";

export default {
  name: 'index',
  components: {
    LayoutContainer,
    PriceDialog,
    Treeselect,
    SearchBox
  },
  data() {
    return {
      queryDataModel: [{type: "default",label: "关键字查询",prop: "key"}],
      showSearch: true,
      queryParam: {page: 1, limit: 10},
      id: null,
      catId: null,
      is_price: false,
      isAddCheckbox: false,
      isAmendCheckbox: false,
      isDelCheckbox: false,
      isTreeOpen: false,
      treeFrom: {},
      treeOptions: [],
      brandTreeList: [],
      TreeRules: {
        name: [{ required: true, message: "分类名称不能为空", trigger: "blur" }],
        catLevel: [{ required: true, message: "层级不能为空", trigger: "blur" }],
        sort: [{ required: true, message: "排序不能为空", trigger: "blur" }],
        icon: [{ required: true, message: "图标地址不能为空", trigger: "blur" }],
        productCount: [{ required: true, message: "商品数量不能为空", trigger: "blur" }],
      },
      uploadPath: "",
      uploadParams: {},
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,

      /* 属性分组数组 */
      attrFrom: {},
      isAttrOpen: false,
      attrProps: {},

      domKey: 0,
      domKey2: 0,
      isSwitchAmand: false,
      loading: false,
      filterText: "",
      expanded: [1],
      defaultProps: {
        label: "name",
        children: "children"
      },
      attrList: [],
      title: "",
      total: null,
    }
  },
  directives: {
    focus: {
      inserted: function(el) {
        el.querySelector("input").focus()
      }
    }
  },
  watch: {
    filterText: {
      handler(val) {
        this.$refs.tree.filter(val);
      }
    }
  },
  created() {
    this.getBrandTree();
    this.OSSPolicy();
  },
  methods: {
    getBrandTree() {
      getBrandTree().then(response => {
        this.brandTreeList = response.data;
        this.handlerBrandTreeList(this.brandTreeList);
      })
    },
    OSSPolicy() {
      getOSSPolicy().then(response => {
        this.uploadPath = response.data.dir + "/" + new Date().getTime();
        this.uploadParams = {
          host: response.data.host,
          OSSAccessKeyId: response.data.accessid,
          key: this.uploadPath,
          signature: response.data.signature,
          policy: response.data.policy,
          // 设置服务端返回状态码为200，不设置则默认返回状态码204。
          success_action_status: 200,
        }
      })
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
    handlerBrandTreeList(brandTreeList) {
      this.handlerClassifyProperty(brandTreeList[0])
      this.getAttrList();
      brandTreeList.forEach(item => {
        item.level = 1;
        if (item.children != undefined || item.children != null) {
          this.levelChildren(item,2)
        }
      })
    },
    categoryPathHandler(categoryPath) {
      categoryPath.forEach(item => {
        if (this.title == "") {
          this.title = item.name;
        } else {
          this.title = this.title + " > " + item.name;
        }
      })
    },
    handlerClassifyProperty(brandTree) {
      if (brandTree.children && brandTree.children.length > 0) {
        this.handlerClassifyProperty(brandTree.children[0])
      } else {
        this.catId = brandTree.catId
      }
    },
    levelChildren(item,level) {
      item.children.forEach(child => {
        child.level = level;
        child.is_sort = true;
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
    expandedMenu(data) {
      if (data.level != 1) return;
      let index = this.expanded.findIndex(item => item == data.id);
      index != -1 ? this.expanded.splice(index,1) : this.expanded.push(data.id);
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    handleNodeClick(data,node,component) {
      this.reset();
      this.catId = data.catId;
      this.getAttrList();
    },
    onInput(node,data) {
      this.handlerInput(node,data);
    },
    blurInputTree(node,data) {
      this.handlerInput(node,data);
      amendBrandSort({id: data.id,sort: data.sort}).then(response => {
        if (response.code == 200) {
          this.notSuccess("修改成功!")
        }
      })
    },
    blurInputItem(key,row) {
      this.handlerItem(key,row);
      amendBrandModel(row).then(response => {
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
    amendAttrGroup(row) {
      this.attrReset()
      this.queryAttrGroupInfo(row.attrGroupId);
      this.isAttrOpen = true;
    },
    treeAddClick(row) {
      this.treeReset()
      this.treeFrom.parentCid = row.catId
      this.getTreeselect()
      this.isTreeOpen = true
    },
    treeAmendClick(row) {
      this.treeReset()
      this.treeFrom.catId = row.catId
      this.getTreeselect()
      this.getQueryTreeInfo(row.catId)
      this.isTreeOpen = true
    },
    getQueryTreeInfo(catId) {
      queryBrandTree(catId).then(response => {
        this.treeFrom = response.data.category;
      });
    },
    treeDelClick(row) {
      MessageBox.confirm('是否确认删除名称为"' + row.name + '"的数据项？').then(function() {
        return delBrandTree(row.catId);
      }).then(() => {
        this.getBrandTree();
        this.notSuccess("删除成功");
      }).catch(() => {});
    },
    treeFromSubmitForm() {
      this.$refs["treeFrom"].validate(valid => {
        if (valid) {
          if (this.treeFrom.catId != undefined) {
            amendBrandTree(this.treeFrom).then(response => {
              this.notSuccess("修改成功");
              this.isTreeOpen = false;
              this.getBrandTree();
            });
          } else {
            addBrandTree(this.treeFrom).then(response => {
              this.notSuccess("新增成功");
              this.isTreeOpen = false;
              this.getBrandTree();
            });
          }
        }
      });
    },
    treeFromHandleClose() {
      this.isTreeOpen = false
      this.treeReset()
    },
    treeselectSelect(e) {
      this.treeFrom.parentCid = e.catId;
    },
    /** 转换Tree数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.catId,
        label: node.name,
        children: node.children
      };
    },
    /** 查询Tree下拉树结构 */
    getTreeselect() {
      console.log(this.treeFrom)
      this.treeOptions = [];
      const tree = { catId: 0, name: '主类目', children: [] };
      tree.children = this.handleTree(this.brandTreeList, "catId","parentCid");
      this.treeOptions.push(tree);
    },
    handleRemove(file) {
      console.log(file);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleDownload(file) {
      console.log(file);
    },
    uploadSuccess(response, file, fileList) {
      console.log("上传成功！")
      this.treeFrom.icon = "https://glume-mall.oss-cn-shenzhen.aliyuncs.com/" + this.uploadPath;
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
      this.reset();
      this.getAttrList();
    },
    attrReset() {
      this.reset();
    },
    treeReset() {
      this.treeFrom = {}
    },
    reset() {
      this.title = "";
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
.box-content .box-card:nth-child(1) /deep/ .el-card__body {
  padding: 0 10px;
}
.box-content .box-card:nth-child(1) {
  float: left;
  display: inline-block;
  width: 14%;
}
.box-content .box-card:nth-child(2) {
  width: 86%;
}
.box-content .search-box {
  width: 86% !important;
  margin-bottom: 10px;
}
.tree-input-box {
  padding-top: 10px;
  background-color: #FFFFFF;
}
.switchButton {
  font-size: 14px;
  padding-top: 10px;
  line-height: 25px;
  .switchBox {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
.operation {
  padding-top: 10px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.amend {
  color: #ffba00;
}
.del {
  color: #ff4949;
}
.filter-tree {
  margin-top: 10px;
  max-height: 60vh;
  overflow: auto;
}
.filter-tree /deep/ .el-tree-node {
  padding: 6px 0;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
}
.el-tree-text {
  font-size: 14px;
  color: #000;
}
.el-tree-text:nth-child(2) {
  padding: 5px 10px;
  color: #999;
}
.el-tree-input {
  width: 60px;
}

.table-title-text {
  font-size: 14px;
  color: #999;
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

/* 设置滚动条的样式 */
::-webkit-scrollbar {
  width: 6px;
}
/* 滚动槽 */
::-webkit-scrollbar-track {
  -webkit-box-shadow: inset006pxrgba(0,0,0,0.3);
  border-radius:10px;
}
/* 滚动条滑块 */
::-webkit-scrollbar-thumb {
  border-radius:10px;
  background:rgba(0,0,0,0.1);
  -webkit-box-shadow: inset006pxrgba(0,0,0,0.5);
}
::-webkit-scrollbar-thumb:window-inactive {
  background: rgba(255,0,0,0.4);
}
</style>
