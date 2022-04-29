<template>
  <el-dialog title="" :visible.sync="dialogVisible" append-to-body :before-close="cancel">
    <el-descriptions class="margin-top" title="日志详情" :column="2" :size="size" border direction="vertical">
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>日志信息
        </template>
        <span>{{formData.jobMessage}}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>任务状态
        </template>
        <el-tag type="success" v-if="formData.status == 0">允许</el-tag>
        <el-tag type="danger" v-else-if="formData.status == 1">禁止</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>耗时(单位：毫秒)
        </template>
        <span>{{formData.times}}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>开始时间
        </template>
        <span>{{ parseTime(formData.startTime) }}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>停止时间
        </template>
        <span>{{ parseTime(formData.stopTime) }}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>创建时间
        </template>
        <span>{{ parseTime(formData.createTime) }}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>异常信息
        </template>
        <span>{{ formData.exceptionInfo }}</span>
      </el-descriptions-item>
    </el-descriptions>
  </el-dialog>
</template>

<script>
export default {
  name: "InfoDialogBox",
  props: {
    data: Object,
  },
  watch: {
    data(val) {
      console.log(val)
      this.formData = val;
    }
  },
  data() {
    return {
      dialogVisible: false,
      openCron: false,
      formData: {},
      size: "10",
    }
  },
  methods: {
    cancel() {
      this.close();
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

</style>
