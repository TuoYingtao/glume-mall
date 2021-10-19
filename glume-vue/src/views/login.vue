<template>
  <div class="login">
    <div class="bj" :style="backgroundMath"></div>
    <div class="content">
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <h3 class="title">{{title}}</h3>
        <el-form-item prop="name">
          <el-input v-model.trim="loginForm.name" type="text" auto-complete="off" placeholder="账号">
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input
            v-model="loginForm.code"
            auto-complete="off"
            placeholder="验证码"
            style="width: 63%"
            @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
          <div class="login-code">
            <img :src="codeUrl" @click="getCode" class="login-code-img"/>
          </div>
        </el-form-item>
        <el-checkbox class="checkbox" v-model="loginForm.rememberMe">记住密码</el-checkbox>
        <el-form-item style="width:100%;">
          <el-button
            class="button-login"
            :loading="loading"
            size="medium"
            type="primary"
            @click.native.prevent="handleLogin">
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
        </el-form-item>
      </el-form>
      <!--  底部  -->
      <div class="el-login-footer">
        <span>Copyright © 2021 {{title}} Rights Reserved.</span>
      </div>
    </div>
<!--    <lock-screen style="position: fixed;"></lock-screen>-->
  </div>
</template>

<script>
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'
import LockScreen from './error/lockScreen'
import {captcha} from "@/api/login";
const defaultSettings = require('@/settings')
export default {
  name: "Login",
  components: { LockScreen },
  data() {
    return {
      number: 0,
      title: defaultSettings.title,
      loginForm: {
        name: '',
        password: '',
        rememberMe: '',
        code: '',
        key: ''
      },
      codeUrl: null,
      loginRules: {
        name: [
          { required: true, trigger: "blur", message: "账号不能为空！" }
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空！" }
        ],
      },
      loading: false,
      redirect: undefined,
      backgrund: {
        backgroundImage: ""
      }
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.number = Math.floor(Math.random() * 4)
    this.getCode();
    this.getCookie();
  },
  computed: {
    backgroundMath: function() {
      return `backgroundImage: url(${require(`../assets/image/${this.number}.jpg`)})`
    }
  },
  methods: {
    getCode() {
      captcha().then(res => {
        this.codeUrl = res.data.image;
        this.loginForm.key = res.data.key;
      })
    },
    getCookie() {
      const name = Cookies.get(this.defaultSettings.login_name);
      const password = Cookies.get(this.defaultSettings.login_password);
      const rememberMe = Cookies.get(this.defaultSettings.login_rememberMe)
      this.loginForm = {
        name: name === undefined ? this.loginForm.name : name,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
      };
    },
    async handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set(this.defaultSettings.login_name, this.loginForm.name, { expires: 30 });
            Cookies.set(this.defaultSettings.login_password, encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set(this.defaultSettings.login_rememberMe, this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove(this.defaultSettings.login_name);
            Cookies.remove(this.defaultSettings.login_password);
            Cookies.remove(this.defaultSettings.login_rememberMe);
          }
          this.$store.dispatch("Login", this.loginForm).then( response => {
            this.$router.push({ path: this.redirect || "/" });
          }).catch(() => {
            this.loading = false;
          });
        }
      });
    },
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
  .button-login {
    top: 50%;
    left: 50%;
    transform: translate(-50%,-50%);
  }
  .checkbox {
    float: left;
    padding-top: 10px;
    padding-bottom: 30px;
    font-weight: 600;
    letter-spacing: 0.5px;
  }
  /* 记住密码复选框样式 */
  .el-checkbox__input.is-checked {
    .el-checkbox__inner {
      background-color: #556B2F ;
      border-color: #556B2F;
    }
    + .el-checkbox__label {
      color: #556B2F;
    }
  }
  /*********************/
  /* 解决双击选择样式背景颜色
  ::selection {
    background: #2D2F36;
  }
  ::-webkit-selection {
    background: #2D2F36;
  }
  ::-moz-selection {
    background: #2D2F36;
  }
  */
  .page {
    background: #e2e2e5;
    display: flex;
    flex-direction: column;
    height: calc(100% - 40px);
    width: calc(100% - 40px);
    position: absolute;
    place-content: center;
  }
  @media (max-width: 767px) {
    .page {
      height: auto;
      margin-bottom: 20px;
      padding-bottom: 20px;
    }
  }
  .el-form-item /deep/ .el-form-item__error {
    letter-spacing: 1px;
    font-weight: bold;
  }
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #0d060e;
}
.bj {
  position: fixed;
  z-index: 10;
  /*background-image: url("https://ljh-web-wx-1253863388.file.myqcloud.com/other/beijing.jpg");*/
  //background-image: url("../assets/image/wallhaven-9mzppw.jpg");
  min-height: calc(100vh - 5px);
  width: calc(100% - 4px);
  border-radius: 8px;
  //min-height: calc(100vh);
  //width: calc(100%);
  background-size: cover;
  filter: blur(0.7px);
}
.content {
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 11;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #000;
}

.login-form {
  position: relative;
  z-index: 9999;
  backdrop-filter: blur(4px);
  transition: 0.3s ease;
  border-radius: 6px;
  background: rgba(255,250,250,0.5);
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  z-index: 10;
  bottom: 30px;
  width: 100%;
  text-align: center;
  color: #EAEAEA;
  font-family: Arial;
  font-size: 14px;
  letter-spacing: 1px;
  backdrop-filter: blur(0.6px);
}
.login-code-img {
  height: 38px;
}
.zhuce {
  float: right;
  font-size: 16px;
  color: rgb(131, 178, 231);
  padding-bottom: 20px;
}
.verificationBotton {
  width: 120px;
  height: 35px;
  margin-left: 10px;
}
</style>
