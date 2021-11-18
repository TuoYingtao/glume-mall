<template>
  <div>
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
        <slot></slot>
      </div>
    </div>
    <!-- tree 弹窗 -->
    <el-dialog :title="dialogTitle" width="35%" :visible.sync="isTreeOpen" :before-close="treeFromHandleClose">
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
  </div>
</template>

<script>
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {
  addBrandTree,
  amendBrandTree,
  delBrandTree,
  getBrandTree,
  getOSSPolicy,
  queryBrandTree
} from "@/api/commodityManage/classify";
import {MessageBox} from "element-ui";

export default {
  name: "treeBox",
  components: {Treeselect},
  data() {
    return {
      filterText: "",
      isAddCheckbox: false,
      isAmendCheckbox: false,
      isDelCheckbox: false,
      brandTreeList: [],
      defaultProps: {
        label: "name",
        children: "children",
      },
      expanded: [1],
      loading: false,
      title: "",
      total: null,
      domKey: 0,
      dialogTitle: "",
      treeFrom: {},
      isTreeOpen: false,
      TreeRules: {
        name: [{ required: true, message: "分类名称不能为空", trigger: "blur" }],
        catLevel: [{ required: true, message: "层级不能为空", trigger: "blur" }],
        sort: [{ required: true, message: "排序不能为空", trigger: "blur" }],
        icon: [{ required: true, message: "图标地址不能为空", trigger: "blur" }],
        productCount: [{ required: true, message: "商品数量不能为空", trigger: "blur" }],
      },
      treeOptions: [],
      uploadParams: {},
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      uploadPath: "",
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
    handlerBrandTreeList(brandTreeList) {
      this.$emit("setCatelongId",0)
      this.$emit('getAttrList')
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
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    handleNodeClick(data,node,component) {
      this.$emit('reset')
      this.$emit("setCatelongId",data.catId)
      this.$emit('getAttrList')
    },
    expandedMenu(data) {
      if (data.level != 1) return;
      let index = this.expanded.findIndex(item => item == data.id);
      index != -1 ? this.expanded.splice(index,1) : this.expanded.push(data.id);
    },
    treeAddClick(row) {
      this.treeReset()
      this.dialogTitle = "添加分类"
      this.treeFrom.parentCid = row.catId
      this.getTreeselect()
      this.isTreeOpen = true
    },
    /** 查询Tree下拉树结构 */
    getTreeselect() {
      console.log(this.treeFrom)
      this.treeOptions = [];
      const tree = { catId: 0, name: '主类目', children: [] };
      tree.children = this.handleTree(this.brandTreeList, "catId","parentCid");
      this.treeOptions.push(tree);
    },
    treeAmendClick(row) {
      this.treeReset()
      this.dialogTitle = "修改分类"
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
    treeFromHandleClose() {
      this.isTreeOpen = false
      this.treeReset()
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
    treeselectSelect(e) {
      this.treeFrom.parentCid = e.catId;
    },
    uploadSuccess(response, file, fileList) {
      this.treeFrom.icon = "https://glume-mall.oss-cn-shenzhen.aliyuncs.com/" + this.uploadPath;
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleRemove(file) {
      console.log(file);
    },
    handleDownload(file) {
      console.log(file);
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
    treeReset() {
      this.treeFrom = {}
    },
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
