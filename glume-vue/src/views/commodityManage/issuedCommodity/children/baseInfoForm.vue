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
            <el-input v-model="form.name" placeholder="请输入商品描述"/>
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
            <el-select style="width: 100%" v-model="form.brandId" filterable @change="brandChange" placeholder="请选择品牌">
              <el-option v-for="(item,index) in classifyBrandList" :key="index" :label="item.name" :value="item.brandId"/>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="商品重量（Kg）">
            <el-input-number style="width: 100%" :precision="3"
                             v-model="form.freeFreightPoint"
                             controls-position="right"
                             :min="0" placeholder="请输入商品重量（Kg）"/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="设置金币">
            <el-input-number style="width: 100%" v-model="form.freeFreightPoint"
                             controls-position="right"
                             :min="0" placeholder="请输入设置金币"/>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="设置成长值">
            <el-input-number style="width: 100%" v-model="form.freeFreightPoint"
                             controls-position="right"
                             :min="0" placeholder="请输入设置成长值"/>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="商品介绍">
            <el-upload ref="upload" :action="uploadParams.host"
                       :file-list="fileList"
                       :data="uploadParams"
                       :on-success="uploadSuccess"
                       :on-progress="onProgress"
                       :before-upload="beforeUpload"
                       list-type="picture-card"
                       :auto-upload="false"
                       :multiple="true">
              <i slot="default" class="el-icon-plus"></i>
              <div slot="trigger" slot-scope="{file}" v-if="imageShow">
                <img class="el-upload-list__item-thumbnail" :src="file.url">
              </div>
              <div slot="file" slot-scope="{file}" v-if="!imageShow">
                <img class="el-upload-list__item-thumbnail" :src="file.url">
                <span class="el-upload-list__item-actions">
                  <span class="el-upload-list__item-preview"@click="handlePictureCardPreview(file)">
                    <i class="el-icon-zoom-in"></i>
                  </span>
                </span>
              </div>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item>
            <el-button style="margin-top: 12px;" @click="next">下一步</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import {getBrandTree, getOSSPolicy} from "@/api/commodityManage/classify";
import {classifyBrand} from "@/api/commodityManage/brand";

export default {
  name: "baseInfoForm",
  data() {
    return {
      form: {},
      classifyTreeList: [],
      classifyBrandList: [],
      attrProps: {
        value: "catId",
        children: "children",
        label: "name",
      },
      uploadParams: {
        host: "",
      },
      dialogImageUrl: '',
      disabled: false,
      dialogVisible:false,
      imageShow: true,
      fileList: [],
      updateImagePathName: []
    }
  },
  created() {
    this.getClassifyTree();
    this.OSSPolicy()
  },
  methods: {
    next() {
      this.submitUpload();
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
    },
    uploadSuccess(response, file, fileList) {
      console.log("uploadSuccess",response, file, fileList)
      this.treeFrom.icon = "https://glume-mall.oss-cn-shenzhen.aliyuncs.com/" + this.uploadPath;
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    onProgress(event, file, fileList) {
      console.log("onProgress",event, file, fileList);
      // this.imageShow = false
    },
    /**
     * 上传图片之前
     */
    beforeUpload(file) {
      console.log("beforeUpload",file)
    },
    /**
     * 上传图片
     */
    submitUpload() {
      this.$refs.upload.submit();
    },
    OSSPolicy() {
      getOSSPolicy().then(response => {
        this.updateImagePathName.push(response.data.dir + "/" + new Date().getTime())
        let len = this.updateImagePathName - 1;
        this.uploadParams = {
          host: response.data.host,
          OSSAccessKeyId: response.data.accessid,
          key: this.updateImagePathName[len],
          signature: response.data.signature,
          policy: response.data.policy,
          // 设置服务端返回状态码为200，不设置则默认返回状态码204。
          success_action_status: 200,
        }
      })
    }
  }
}
</script>

<style scoped>
.baseInfoForm {
  padding: 0 25%;
}
</style>
