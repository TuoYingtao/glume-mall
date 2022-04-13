<template>
  <el-dialog :title="title" :visible.sync="dialogVisible" width="25%" :before-close="handleClose">
    <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
      <el-form-item label="名称：" prop="username">
        <el-col :span="18"><el-input type="text" placeholder="请输入用户名称" v-model="formData.username"/></el-col>
      </el-form-item>
      <el-form-item label="手机号：" prop="mobile">
        <el-col :span="18"><el-input show-word-limit maxlength="11" type="text" placeholder="请输入手机号" v-model="formData.mobile"/></el-col>
      </el-form-item>
      <el-form-item label="邮箱：" prop="email">
        <el-col :span="18"><el-input type="text" placeholder="请输入邮箱" v-model="formData.email"/></el-col>
      </el-form-item>
      <el-form-item label="密码：" prop="password" v-if="!formData.userId">
        <el-col :span="18"><el-input type="text" placeholder="请输入密码" v-model="formData.password"/></el-col>
      </el-form-item>
      <el-form-item label="确认密码：" prop="newPassword" v-if="!formData.userId">
        <el-col :span="18"><el-input type="text" placeholder="请再次输入密码" v-model="formData.newPassword"/></el-col>
      </el-form-item>
      <el-form-item label="选择角色：" prop="roleId" v-if="!formData.userId">
        <el-col :span="18">
          <el-select v-model="formData.roleId" clearable placeholder="选择角色">
            <el-option v-for="item in roleList" :key="item.roleId" :label="item.roleName" :value="item.roleId">
            </el-option>
          </el-select>
        </el-col>
      </el-form-item>
      <el-form-item label="状态：" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :label="0">启用</el-radio>
          <el-radio :label="1">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
          <span class="fr">
            <el-button @click="handleClose">取消</el-button>
            <el-button type="cyan" @click="affirmActive">确认</el-button>
          </span>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
export default {
  name: "DialogModel",
  props: {
    form: Object,
    title: String,
    roleList: Array,
  },
  data() {
    return {
      formData: {status: 0,},
      dialogVisible: false,
      rules: {
        username: [{ required: true, message: '请输入用户名称', trigger: 'blur' }],
        mobile: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        newPassword: [{ required: true, message: '请再次输入密码', trigger: 'blur' }],
        roleId: [{ required: true, message: '请选择角色', trigger: 'blur' }],
      }
    }
  },
  watch: {
    form: {
      handler(newVal) {
        this.formData = newVal;
      },
      deep: true,
    }
  },
  methods: {
    affirmActive() {
      this.$refs["elForm"].validate(valid => {
        if (valid) {
          this.$emit("affirmActive", this.formData);
        }
      })
    },
    open() {
      this.dialogVisible = true;
    },
    close() {
      this.dialogVisible = false;
    },
    handleClose() {
      this.dialogVisible = false;
      this.$emit("handleClose");
    }
  }
}
</script>

<style scoped>
.el-select {
  width: 100%;
}
</style>
