<template>
  <div class="drawer-container">
    <div>
      <h3 class="drawer-title">系统布局配置</h3>

      <div class="drawer-item">
        <span>主题颜色</span>
        <theme-picker style="float: right;height: 26px;margin: -3px 8px 0 0;" @change="themeChange" />
      </div>

      <div class="drawer-item">
        <span>开启 Tags-Views</span>
        <el-switch v-model="tagsView" class="drawer-switch" />
      </div>

      <div class="drawer-item">
        <span>固定 Header</span>
        <el-switch v-model="fixedHeader" class="drawer-switch" />
      </div>

      <div class="drawer-item">
        <span>显示 Logo</span>
        <el-switch v-model="sidebarLogo" class="drawer-switch" />
      </div>

      <div class="drawer-item">
        <span>是否展示 target 标签</span>
        <el-switch v-model="isTarget" class="drawer-switch" />
      </div>

      <div class="drawer-item">
        <span>打开新页面关闭上一个标签 target 标签 (is_target 必须为 true)</span>
        <el-switch v-model="isTargetClose" class="drawer-switch" />
      </div>

      <div class="drawer-item">
        <span>target 总数设置</span>
        <el-input-number class="drawer-switch" v-model="targetCunt" @change="handleChange" :min="3" :max="9" label="描述文字"/>
      </div>

    </div>
  </div>
</template>

<script>
import ThemePicker from '@/components/ThemePicker'

export default {
  components: { ThemePicker },
  data() {
    return {}
  },
  computed: {
    fixedHeader: {
      get() {
        return this.$store.state.settings.fixedHeader
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'fixedHeader',
          value: val
        })
      }
    },
    tagsView: {
      get() {
        return this.$store.state.settings.tagsView
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'tagsView',
          value: val
        })
      }
    },
    sidebarLogo: {
      get() {
        return this.$store.state.settings.sidebarLogo
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'sidebarLogo',
          value: val
        })
      }
    },
    isTarget: {
      get() {
        return this.$store.state.settings.isTarget
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'isTarget',
          value: val
        })
      }
    },
    isTargetClose: {
      get() {
        return this.$store.state.settings.isTargetClose
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'isTargetClose',
          value: val
        })
      }
    },
    targetCunt: {
      get() {
        return this.$store.state.settings.targetCunt
      },
      set(val) {
        console.log(val)
        this.$store.dispatch('settings/changeSetting', {
          key: 'targetCunt',
          value: val
        })
      }
    }
  },
  methods: {
    themeChange(val) {
      this.$store.dispatch('settings/changeSetting', {
        key: 'theme',
        value: val
      })
    },
    handleChange(value) {
      // this.target_cunt(value)
    }
  }
}
</script>

<style lang="scss" scoped>
.drawer-container {
  padding: 24px;
  font-size: 14px;
  line-height: 1.5;
  word-wrap: break-word;

  .drawer-title {
    margin-bottom: 12px;
    color: rgba(0, 0, 0, .85);
    font-size: 14px;
    line-height: 22px;
  }

  .drawer-item {
    color: rgba(0, 0, 0, .65);
    font-size: 14px;
    padding: 12px 0;
  }

  .drawer-switch {
    float: right
  }
}
</style>
