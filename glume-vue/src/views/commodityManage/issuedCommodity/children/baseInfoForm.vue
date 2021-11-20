<template>
  <div class="baseInfoForm">
    <el-form ref="form" :model="form" label-width="120px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="商品名称">
            <el-input v-model="form.name" placeholder="请输入商品名称"/>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="商品描述">
            <el-input v-model="form.describe" placeholder="请输入商品描述"/>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="选择分类">
            <el-cascader style="width: 100%" ref="cascader" v-model="form.catelogId"
                         :options="classifyTreeList"
                         :props="attrProps"
                         @change="handleClassifyChange"
                         placeholder="请选择分类"/>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="选择品牌">
            <el-select style="width: 100%" v-model="form.brandName" filterable @change="brandChange" placeholder="请选择品牌">
              <el-option v-for="(item,index) in classifyBrandList" :key="index" :label="item.name" :value="item.brandId"/>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="商品重量（g）">
            <el-input-number style="width: 100%" :precision="3"
                             v-model="form.weight"
                             controls-position="right"
                             :min="0" placeholder="请输入商品重量（g）"/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="设置金币">
            <el-input-number style="width: 100%" v-model="form.gold"
                             controls-position="right"
                             :min="0" placeholder="请输入设置金币"/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="设置成长值">
            <el-input-number style="width: 100%" v-model="form.growthValue"
                             controls-position="right"
                             :min="0" placeholder="请输入设置成长值"/>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="商品介绍">
            <upload ref="productUpload" @uploadSuccess="productImage"/>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="图集">
            <upload ref="imagesUpload" @uploadSuccess="images"/>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item>
            <div class="button-box">
              <el-button class="nextButton" type="primary" @click="next">下一步</el-button>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import {getBrandTree} from "@/api/commodityManage/classify";
import {classifyBrand} from "@/api/commodityManage/brand";
import {mapActions} from "vuex";
import upload from "@/components/Upload/upload";

export default {
  name: "baseInfoForm",
  components: {upload},
  data() {
    return {
      form: {
        productImage: [],
        images: [],
      },
      classifyTreeList: [],
      classifyBrandList: [],
      attrProps: {
        value: "catId",
        children: "children",
        label: "name",
      },
    }
  },
  created() {
    this.getClassifyTree();
  },
  mounted() {
    this.$refs['productUpload'].OSSPolicy();
    this.$refs['imagesUpload'].OSSPolicy();
  },
  methods: {
    ...mapActions(["setBaseInfoForm"]),
    next() {
      this.setBaseInfoForm(this.form);
      this.$emit("next")
    },
    getClassifyTree() {
      getBrandTree().then(response => {
        this.classifyTreeList = response.data;
      })
    },
    getClassifyBrand() {
      classifyBrand(this.form.catelogId).then(response => {
        this.classifyBrandList = response.data;
      })
    },
    handleClassifyChange(e) {
      let num = e.length - 1;
      this.form.catelogId = e[num];
      this.getClassifyBrand();
      this.form.brandId = null;
    },
    brandChange(e) {
      this.form.brandId = e;
      this.classifyBrandList.forEach(item => {
        if (item.brandId == e) {
          this.form.brandId = item.brandName;
        }
      })
    },
    productImage(e) {
      this.form.productImage.push(e);
    },
    images(e) {
      this.form.images.push(e);
    }
  }
}
</script>

<style scoped>
.baseInfoForm {
  padding: 0 25%;
}
.button-box {
  display: flex;
  justify-content: center;
  align-items: center;
}
.nextButton {
  width: 200px;
  margin-top: 12px;
}
</style>
