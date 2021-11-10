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
      <el-card class="box-card">
        <div class="table-title-text">{{ title }}</div>
        <el-row :gutter="10" class="mb8">
          <el-col :span="10">
          </el-col>
          <right-toolbar :show-search.sync="showSearch" :is-flag-show="$route.meta.search" @queryTable="getBrandModel"/>
        </el-row>
        <el-table v-loading="loading" :data="modelList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" :key="domKey2">
          <el-table-column type="selection" width="80" align="center"/>
          <el-table-column label="图片">
            <template slot-scope="scope">
              <img class="img_box" alt="loading" :src="scope.row.image_url"/>
            </template>
          </el-table-column>
          <el-table-column label="型号" prop="name">
            <template slot-scope="scope">
              <span class="text-table" v-show="scope.row.is_name" @dblclick="handlerItem('is_name',scope.row)">{{ scope.row.name }}</span>
              <el-input class="name-input" v-focus type="text" v-show="!scope.row.is_name" v-model="scope.row.name" @blur="blurInputItem('is_name',scope.row)"/>
            </template>
          </el-table-column>
          <el-table-column label="价格" prop="ave_price">
            <template slot-scope="scope">
              <span class="text-table" v-show="scope.row.is_ave_price" @dblclick="handlerItem('is_ave_price',scope.row)">{{ scope.row.ave_price }}</span>
              <el-input class="price-input" v-focus type="number" v-show="!scope.row.is_ave_price" v-model="scope.row.ave_price" @blur="blurInputItem('is_ave_price',scope.row)"/>
            </template>
          </el-table-column>
          <el-table-column label="排序" prop="sort">
            <template slot-scope="scope">
              <el-input class="sort-input" type="number" v-model="scope.row.sort" @blur="blurInputItem('is_sort',scope.row)"/>
            </template>
          </el-table-column>
          <el-table-column label="操作" prop="sort">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="openPrice(scope.row)">价位设置</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination :page-size="[10,20,30,40,50]" v-show="total > 0" :total="total" :page.sync="queryParam.page" :limit.sync="queryParam.limit" @pagination="getBrandModel"/>
      </el-card>
    </div>
    <price-dialog ref="RefDialog" :mobel-id="id"/>

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
  getBrandModel,
  amendBrandModel,
  getOSSPolicy,
  addBrandTree, amendBrandTree, delBrandTree, queryBrandTree
} from "@/api/brandOperate"
import PriceDialog from '@/views/system/brandOperate/components/dialog'
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {MessageBox} from "element-ui";

export default {
  name: 'index',
  components: {
    LayoutContainer,
    PriceDialog,
    Treeselect
  },
  data() {
    return {
      id: null,
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

      domKey: 0,
      domKey2: 0,
      showSearch: true,
      isSwitchAmand: false,
      loading: false,
      filterText: "",
      expanded: [1],
      defaultProps: {
        label: "name",
        children: "children"
      },
      modelList: [],
      title: "",
      total: null,
      queryParam: {
        brand_id: null,
        page: 1,
        limit: 10
      }
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
    getBrandModel() {
      this.loading = true;
      getBrandModel(this.queryParam).then(response => {
        this.modelList = response.product;
        this.total = response.total;
        this.loading = false;
        this.handlerBrandModel(this.modelList);
      })
    },
    handlerBrandTreeList(brandTreeList) {
      this.queryParam.brand_id = brandTreeList[0].children[0].id;
      this.title = `${brandTreeList[0].name} > ${brandTreeList[0].children[0].name} > ${brandTreeList[0].children[0].children[0].name}`;
      this.getBrandModel();
      brandTreeList.forEach(item => {
        item.level = 1;
        if (item.children != undefined || item.children != null) {
          this.levelChildren(item,2)
        }
      })
    },
    levelChildren(item,level) {
      item.children.forEach(child => {
        child.level = level;
        child.is_sort = true;
      })
    },
    handlerBrandModel(brandModel) {
      brandModel.forEach(item => {
        item.is_name = true;
        item.is_sort = true;
        item.is_ave_price = true;
      })
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
      if (!node.parent.data.length) {
        this.title = `${node.parent.data.name} > ${data.name}`;
      }
      this.queryParam.brand_id = data.id;
      this.getBrandModel();
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
      this.modelList.forEach(item => {
        if (item.id == row.id) {
          item[key] = !item[key];
        }
      })
      this.domKey2 += 1;
    },
    treeReset() {
      this.treeFrom = {}
    },
    reset() {
      this.queryParam = {
        brand_id: null,
        page: 1,
        limit: 10
      }
    },
    openPrice(row) {
      this.id = row.id;
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
