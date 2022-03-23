<template>
  <el-dialog :title="title" :visible.sync="dialogVisible" width="25%" :before-close="close">
    <el-form ref="elForm" :model="form" :rules="rules" size="medium" label-width="100px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="场次名称：" prop="name">
            <el-input v-model="form.name"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="活动时间：" prop="dateTime">
            <template>
              <el-date-picker style="width: 100%" v-model="form.dateTime"
                              type="datetimerange"
                              range-separator="至"
                              start-placeholder="开始日期时间"
                              end-placeholder="结束日期时间"
                              placeholder="选择时间范围" value-format="yyyy-MM-dd HH:mm:ss">
              </el-date-picker>
            </template>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="状态：">
            <template>
              <el-radio-group v-model="form.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="2">禁用</el-radio>
              </el-radio-group>
            </template>
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
  </el-dialog>
</template>

<script>
export default {
  name: "MyDialog",
  props: {
    title: String,
    form: Object,
  },
  data() {
    return {
      dialogVisible: false,
      rules: {
        name: [{ required: true, message: "场次名称不能为空", trigger: "blur" }],
        dateTime: [{ required: true, message: "活动日期时间不能为空", trigger: "blur" }],
      },
    }
  },
  methods: {
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

</style>
