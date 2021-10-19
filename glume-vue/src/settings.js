module.exports = {
  title: '果快回收系统',

  /** 是否系统布局配置 */
  showSettings: false,

  /** 是否显示 tagsView */
  tagsView: false,

  /** 是否固定头部 */
  fixedHeader: true,

  /** 是否显示logo */
  sidebarLogo: true,

  /** 是否展示 target 标签 */
  isTarget: true,

  /** 打开新页面关闭上一个标签 target 标签 (is_target 必须为 true) */
  isTargetClose: false,

  /** target 总数设置 */
  targetCunt: 99,

  /** 账号 */
  login_name: "rolerecycleSystem_name",

  /** 密码 */
  login_password: "rolerecycleSystem_password",

  /** 是否记住密码 */
  login_rememberMe: "rolerecycleSystem_rememberMe",

  /** token 凭证 */
  login_token: "rolerecycleSystem_token",

  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description Need show err logs component.
   * The default is only used in the production env
   * If you want to also use it in dev, you can pass ['production', 'development']
   */
  errorLog: 'production',
}
