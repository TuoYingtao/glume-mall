<template>
  <layout-container>
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <userAvatar :user="user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />用户名称
                <div class="pull-right">{{ roles.name }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone" />客服电话
                <div class="pull-right">{{ roles.mobile }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd :user="user" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </layout-container>
</template>

<script>
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import { mapGetters } from 'vuex'
import store from "@/store";
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'

export default {
  name: "Profile",
  components: { LayoutContainer, userAvatar, userInfo, resetPwd },
  data() {
    return {
      activeTab: "userinfo",
      user: {
        name: store.getters.roles.name,
        mobile: store.getters.roles.mobile,
        avatar: store.getters.avatar
      }
    };
  },
  computed: {
    ...mapGetters(['roles'])
  },
};
</script>
