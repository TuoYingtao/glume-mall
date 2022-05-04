module.exports = {
  title: 'glumemall系统',

  /** 是否系统布局配置 */
  showSettings: true,

  /** 是否显示 tagsView */
  tagsView: true,

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
  login_name: "glumeSystem_name",

  /** 密码 */
  login_password: "glumeSystem_password",

  /** 是否记住密码 */
  login_rememberMe: "glumeSystem_rememberMe",

  /** token 凭证 */
  login_token: "glumeSystem_token",

  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description Need show err logs component.
   * The default is only used in the production env
   * If you want to also use it in dev, you can pass ['production', 'development']
   */
  errorLog: 'production',
}
