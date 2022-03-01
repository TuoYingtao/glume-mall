<template>
  <el-dialog :title="title" :visible.sync="dialogVisible" width="25%" :before-close="handleClose">
    <el-form ref="elForm" :model="form" :rules="rules" size="medium" label-width="100px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="角色名称：" prop="roleName">
            <el-input v-model="form.roleName"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="角色标签：" prop="roleTag">
            <el-input v-model="form.roleTag"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="描述：">
            <el-input type="textarea" :rows="3"placeholder="请输入内容" v-model="form.remark"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
          <span class="fr">
            <el-button @click="handleClose">取消</el-button>
            <el-button type="cyan" @click="submitForm">添加</el-button>
          </span>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
export default {
  name: "MyDialog",
  data() {
    return {

    }
  },
  methods: {
    handleClose() {
      this.$emit("handleClose");
    },
    submitForm() {
      this.$emit("submitForm")
    }
  }
}
</script>

<style scoped>

</style>
