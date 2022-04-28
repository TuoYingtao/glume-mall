<template>
  <el-dialog title="" :visible.sync="dialogVisible" append-to-body :before-close="cancel">
    <el-descriptions class="margin-top" title="日志详情" :column="2" :size="size" border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-user"></i>任务名称
        </template>
        <span>{{formData.jobName}}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>任务分组
        </template>
        <span>{{formData.jobGroup}}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>Bean 名称
        </template>
        <span>{{formData.beanName}}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>调用方法
        </template>
        <span>{{formData.invokeTarget}}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>Cron 表达式
        </template>
        <span>{{formData.cronExpression}}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>Cron 计划策略
        </template>
        <el-tag v-if="formData.misfirePolicy == 0">默认</el-tag>
        <el-tag type="success" v-else-if="formData.misfirePolicy == 1">立即触发执行</el-tag>
        <el-tag type="warning" v-else-if="formData.misfirePolicy == 2">触发一次执行</el-tag>
        <el-tag type="info" v-else-if="formData.misfirePolicy == 3">不触发立即执行</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>并发执行
        </template>
        <el-tag type="success" v-if="formData.concurrent == 0">允许</el-tag>
        <el-tag type="danger" v-else-if="formData.concurrent == 1">禁止</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>状态
        </template>
        <el-tag type="success" v-if="formData.status == 0">正常</el-tag>
        <el-tag type="warning" v-else-if="formData.status == 1">暂停</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>下次执行时间
        </template>
        <span>{{formData.nextValidTime}}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>创建时间
        </template>
        <span>{{formData.createTime}}</span>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-office-building"></i>备注
        </template>
        <span>{{formData.remark}}</span>
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
