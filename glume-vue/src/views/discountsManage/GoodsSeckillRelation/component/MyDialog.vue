<template>
  <el-dialog :title="title" :visible.sync="dialogVisible" width="78%" :before-close="close">
    <div class="context-box">
      <div class="category">
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
        </span>
        </el-tree>
      </div>
      <el-form ref="elForm" :model="form" :rules="rules" size="medium" label-width="120px">
      <el-row>
        <el-col :span="12">
          <el-form-item label="活动名称：" prop="promotionId">
            <el-select v-model="form.promotionId" placeholder="请选择活动名称">
              <el-option
                v-for="item in promotionAndSession.promotions"
                :key="item.id"
                :label="item.title"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="活动场次名称：" prop="promotionSessionId">
            <el-select v-model="form.promotionSessionId" placeholder="请选择活动场次名称">
              <el-option
                v-for="item in promotionAndSession.sessions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="商品标题：" prop="skuId">
            <el-select v-model="form.skuId" placeholder="请选择商品标题">
              <el-option
                v-for="item in productList"
                :key="item.skuId"
                :label="item.skuTitle"
                :value="item.skuId">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="秒杀价格：" prop="seckillPrice">
            <el-input v-model="form.seckillPrice" placeholder="请输入秒杀价格"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="秒杀总量：" prop="seckillCount">
            <el-input v-model="form.seckillCount" placeholder="请输入秒杀总量"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="每人限购数量：" prop="seckillLimit">
            <el-input v-model="form.seckillLimit" placeholder="请输入每人限购数量"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="排序：" prop="seckillSort">
            <el-input-number v-model="form.seckillSort" placeholder="请输入排序"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
          <span class="fr">
            <el-button @click="close">取消</el-button>
            <el-button type="cyan" @click="submitForm">添加</el-button>
          </span>
      </el-form-item>
    </el-form>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "MyDialog",
  props: {
    title: String,
    form: Object,
    productList: Array,
    brandTreeList: Array,
    promotionAndSession: Object
  },
  data() {
    return {
      dialogVisible: false,
      filterText: "",
      domKey: 0,
      expanded: [1],
      defaultProps: {
        label: "name",
        children: "children",
      },
      rules: {
        promotionId: [{ required: true, message: "活动名称不能为空", trigger: "blur" }],
        promotionSessionId: [{ required: true, message: "活动场次名称不能为空", trigger: "blur" }],
        skuId: [{ required: true, message: "商品标题不能为空", trigger: "blur" }],
        seckillPrice: [{ required: true, message: "秒杀价格不能为空", trigger: "blur" }],
        seckillCount: [{ required: true, message: "秒杀总量不能为空", trigger: "blur" }],
        seckillLimit: [{ required: true, message: "每人限购数量不能为空", trigger: "blur" }],
      },
    }
  },
  watch: {
    filterText: {
      handler(val) {
        this.$refs.tree.filter(val);
      }
    },
  },
  methods: {
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    handleNodeClick(data,node,component) {
      console.log(data,node,component)
      this.$emit("onCategory",data)
    },
    expandedMenu(data) {
      if (data.level -= 1) return;
    },
    submitForm() {
      this.$refs["elForm"].validate(valid => {
        if (valid) {
          this.$emit("submitForm")
        }
      })
    },
    open() {
      this.dialogVisible = true;
    },
    close() {
      this.dialogVisible = false;
    }
  }
}
</script>

<style scoped>
  .el-select {
    width: 100%;
  }
  .context-box {
    display: flex;
    justify-content: space-between;
  }
  .category {
    display: flex;
    flex-direction: column;
  }
  .filter-tree {
    margin-top: 10px;
    max-height: 50vh;
    overflow: auto;
  }
  .filter-tree /deep/ .el-tree-node {
    padding: 6px 0;
  }
</style>
