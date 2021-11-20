<template>
  <div>
    <el-upload ref="upload" :action="uploadParams.host"
               :file-list="fileList"
               :data="uploadParams"
               :on-success="uploadSuccess"
               :on-progress="onProgress"
               :before-upload="beforeUpload"
               list-type="picture-card"
               :auto-upload="true"
               :multiple="false">
      <i slot="default" class="el-icon-plus"></i>
      <div slot="trigger" slot-scope="{file}">
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
  </div>
</template>

<script>
import {getOSSPolicy} from "@/api/commodityManage/classify";

export default {
  name: "upload",
  data() {
    return {
      uploadParams: {
        host: "",
      },
      fileList: [],
      dialogImageUrl: '',
      uploadPath: null,
      dialogVisible: false,
    }
  },
  methods: {
    /**
     * 上传图片
     */
    submitUpload() {
      this.$refs.upload.submit();
    },
    /**
     * 上传图片之前
     */
    beforeUpload(file) {},
    /**
     * 上传图片中
     */
    onProgress(event, file, fileList) {
      console.log("onProgress",event, file, fileList);
    },
    /**
     * 上传图片完成
     */
    uploadSuccess(response, file, fileList) {
      console.log("uploadSuccess",response, file, fileList)
      this.$emit("uploadSuccess","https://glume-mall.oss-cn-shenzhen.aliyuncs.com/" + this.uploadPath)
      this.OSSPolicy();
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
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
    }
  }
}
</script>

<style scoped>

</style>
