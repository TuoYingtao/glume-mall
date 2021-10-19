<template>
  <div>
    <div ref="toastuiEditor" :id="id"
         @load="onEditorLoad"
         @focus="onEditorFocus"
         @blur="onEditorBlur"
         @change="onEditorChange"
         @caretChange="onEditorCaretChange"/>
    <el-button class="button" type="primary" @click="save">保存</el-button>
  </div>
</template>

<script>
import 'codemirror/lib/codemirror.css' // codemirror
import 'tui-editor/dist/tui-editor.css' // editor ui
import 'tui-editor/dist/tui-editor-contents.css' // editor content

import Editor from 'tui-editor'
import defaultOptions from './default-options'

export default {
  name: 'MarkdownEditor',
  props: {
    value: {
      type: String,
      default: ''
    },
    id: {
      type: String,
      required: false,
      default() {
        return 'markdown-editor-' + +new Date() + ((Math.random() * 1000).toFixed(0) + '')
      }
    },
    options: {
      type: Object,
      default() {
        return defaultOptions
      }
    },
    mode: {
      type: String,
      default: 'markdown'
    },
    height: {
      type: String,
      required: false,
      default: '300px'
    },
    language: {
      type: String,
      required: false,
      default: 'zh_CN'
    }
  },
  data() {
    return {
      editor: null
    }
  },
  computed: {
    editorOptions() {
      const options = Object.assign({}, defaultOptions, this.options)
      console.log(options)
      options.initialEditType = this.mode
      options.height = this.height
      options.language = this.language
      return options
    }
  },
  watch: {
    value(newValue, preValue) {
      if (newValue !== preValue && newValue !== this.editor.getValue()) {
        this.editor.setValue(newValue)
      }
    },
    language(val) {
      this.destroyEditor()
      this.initEditor()
    },
    height(newValue) {
      this.editor.height(newValue)
    },
    mode(newValue) {
      this.editor.changeMode(newValue)
    }
  },
  mounted() {
    this.initEditor()
  },
  destroyed() {
    this.destroyEditor()
  },
  methods: {
    initEditor() {
      this.editor = new Editor({
        el: document.getElementById(this.id),
        ...this.editorOptions,
        hooks: { // 钩子函数
          addImageBlobHook: (fileOrBlob, callback) => {
            callback('T_T，出错了');
          },
        },
      })
      if (this.value) {
        this.editor.setValue(this.value)
      }
      this.editor.on('change', () => {
        this.$emit('input', this.editor.getValue())
      })
      console.log(this.scroll())
    },
    onEditorLoad() {
      console.log("onEditorLoad")
    },
    onEditorFocus() {
      console.log("onEditorFocus")
    },
    onEditorBlur() {
      console.log("onEditorBlur")
    },
    onEditorChange() {
      console.log("onEditorChange")
    },
    onEditorCaretChange() {
      console.log("onEditorCaretChange")
    },
    destroyEditor() {
      if (!this.editor) return
      this.editor.off('change')
      this.editor.remove()
    },
    setValue(value) {
      this.editor.setValue(value)
    },
    getValue() {
      return this.editor.getValue()
    },
    save() {
      this.$emit("save", this.getHtml())
    },
    setHtml(value) {
      this.editor.setHtml(value)
    },
    getHtml() {
      return this.editor.getHtml()
    },
    scroll() {
      return this.editor;
    },
  }
}
</script>
<style scoped>
  .button {
    margin-top: 30px;
    padding: 10px 30px;
    float: right;
  }
</style>
