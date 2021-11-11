<template>
  <layout-container>
    <div class="box-content">
      <el-card class="box-card">
        <div class="tree-input-box">
          <el-input class="tree-input" placeholder="输入品牌名称进行过滤" v-model="filterText" />
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
            <span class="el-tree-text" v-show="(data.level == 2) && data.is_sort" @dblclick="onInput(node,data)" ref="refTree">{{ data.sort }}</span>
            <el-input class="el-tree-input" type="number" ref="treeInput" v-focus v-show="(data.level == 2) && !data.is_sort" v-model="data.sort" @blur="blurInputTree(node,data)" />
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
  </layout-container>
</template>

<script>
import { getBrandTree, amendBrandSort, getBrandModel, amendBrandModel } from "@/api/commodityManage/classify"
import PriceDialog from '@/views/system/brandOperate/components/dialog'
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'

export default {
  name: 'index',
  components: {
    LayoutContainer,
    PriceDialog
  },
  data() {
    return {
      id: null,
      is_price: false,
      domKey: 0,
      domKey2: 0,
      showSearch: true,
      loading: false,
      filterText: "",
      expanded: [1],
      defaultProps: {
        label: "name",
        children: "children"
      },
      brandTreeList: [],
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
  },
  methods: {
    getBrandTree() {
      getBrandTree().then(response => {
        this.brandTreeList = response.data;
        this.handlerBrandTreeList(this.brandTreeList);
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
      this.title = `${brandTreeList[0].name} > ${brandTreeList[0].children[0].name}`;
      this.getBrandModel();
      brandTreeList.forEach(item => {
        item.level = 1;
        item.children.forEach(child => {
          child.level = 2;
          child.is_sort = true;
        })
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
    handlerItem(key,row) {
      this.modelList.forEach(item => {
        if (item.id == row.id) {
          item[key] = !item[key];
        }
      })
      this.domKey2 += 1;
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

<style scoped>
.box-content .box-card:nth-child(1) /deep/ .el-card__body {
  padding: 0 10px;
}
.box-content .box-card:nth-child(1) {
  float: left;
  display: inline-block;
  width: 12%;
}
.box-content .box-card:nth-child(2) {
  width: 88%;
}
.tree-input-box {
  padding-top: 10px;
  background-color: #FFFFFF;
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
