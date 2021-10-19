<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="客服电话" prop="mobile">
      <el-input v-model="user.mobile" style="width: 40%" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from "@/api/userInfo";
import { mapGetters } from 'vuex'

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      // 表单校验
      rules: {
        name: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        phonenumber: [
          { required: true, message: "客服不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  computed: {
    ...mapGetters(['roles','avatar'])
  },
  watch: {
    avatar: function(val) {
      this.user.avatar = val
    }
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserProfile(this.user).then(response => {
            this.notSuccess("成功修改信息！");
          });
        }
      });
    },
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/index" });
    }
  }
};
</script>
