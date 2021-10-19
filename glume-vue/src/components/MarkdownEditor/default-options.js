// doc: https://nhnent.github.io/tui.editor/api/latest/ToastUIEditor.html#ToastUIEditor
const defaultOptions = {
  minHeight: '300px',
  previewStyle: 'vertical',
  initialValue: "请输入文本内容。。。。。",
  useCommandShortcut: true, //是否使用快捷键执行命令
  useDefaultHTMLSanitizer: true,
  usageStatistics: false, //将主机名发送到谷歌分析
  hideModeSwitch: false, //隐藏模式切换TAB栏
  previewHighlight: true, //是否高亮预览区域
  toolbarItems: [ //工具条条目
    'heading',
    'bold',
    'italic',
    'strike',
    'divider',
    'hr',
    'quote',
    'divider',
    'ul',
    'ol',
    'task',
    'indent',
    'outdent',
    'divider',
    'table',
    'image',
    'link',
    'divider',
    'code',
    'codeblock'
  ],
}
export default defaultOptions
