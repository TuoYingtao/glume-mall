<script>
/**

 Loading             [Boolean,Function]       表格是否启用加载 默认为：false
 headerCellStyle     [Object,Function]        表头单元格的 style 的回调方法，也可以使用一个固定的 Object 为所有表头单元格设置一样的 Style。 默认为：{'text-align':'center'}
 cellStyl            [Object,Function]        单元格的 style 的回调方法，也可以使用一个固定的 Object 为所有单元格设置一样的 Style。默认为： {'text-align':'center'}
 data                Array                    数据源：表格的内容数据


 tableColumn         Array                    数组表格的列数
    label            String                   标题
    prop             String                   字段名（对应数据的字段）
    width            [String,Number]          列的宽度默认为 auto
    type [           String                   列的类型可选添也可为空''，可选：img、time、tag、bottom、table、input、popover
      time   -->   model       String      时间模板默为：{y}-{m}-{d} {h}:{i}:{s}
      tag    -->   filter      Array       [ id: 唯一标识，name: 名称, type: tag颜色与原生el-tag相符 ]
             -->   className   String      给标签设置类名
      bottom -->   model       Array       [ name: 名称， color: 按钮颜色，onClick：按钮的点击回调事件，返回数据为当前项的信息 ]
      table  -->   children    Array       [ 嵌套列：tableColumn ]
      img    -->   plugin      Boolean     是否启用自己的插件,目前只支持： vue-lazyload
             -->   className   String      定义类名
             -->   alt         String      如果无法显示图像，浏览器将显示替代文本
             -->   img         Object      [ width: 图片宽度；height: 图片高度 ] 默认：100 x 100
      input  -->   input       Array       属性与 el-input 相同; 事件: [ onBlur：在 Input 失去焦点时触发；onFocus：在 Input 获得焦点时触发；onChange：仅在输入框失去焦点或用户按下回车时触发 ]
      popover-->   popover     Object      [placement：显示位置；trigger：唤醒显示事件；model：类型；tableColumn：表格列数据] 默认类型为：table
    ]


 **/

function parseTime (time, pattern) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
  let date;
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
      time = parseInt(time)
    } else if (typeof time === 'string') {
      time = time.replace(new RegExp(/-/gm), '/');
    }
    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value] }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  });
  return time_str
}

function tagException(e) {
  throw new Error(`[tag类型的组件]：未定义filter数组。目标{}：${JSON.stringify(e)}`)
}

function btnNotException(e) {
  throw new Error(`[bottom类型的组件]：未定义model数组或未定义数组长度。目标{}：${JSON.stringify(e)}`)
}

function tableNotException(e) {
  throw new Error(`[table类型的组件]：未定义children数组或未定义数组长度。目标{}：${JSON.stringify(e)}`)
}

function popoverNotException(e) {
  throw new Error(`[popover类型的组件]：未定义popover对象或此对象不能为空。目标{}：${JSON.stringify(e)}`)
}

export default {
  props: {
    loading: {
      type: [Boolean,Function],
      default: () => false
    },
    headerCellStyle: {
      type: [Object,Function],
      default: () => { return {'text-align':'center'} }
    },
    cellStyle: {
      type: [Object,Function],
      default: () => { return {'text-align':'center'} }
    },
    data: {
      type: Array,
      default: () => [
        {id: 1,username: "张三",create_time: 1628747892,status: 1,image: "https://ossload.huishoubaojiadan.com/product_image/24362.png"},
        {id: 2,username: "李四",create_time: "",status: 2,image: "https://ossload.huishoubaojiadan.com/product_image/24362.png"},
        {id: 3,username: "王二",create_time: 1628747892,status: 1,image: "https://ossload.huishoubaojiadan.com/product_image/24362.png"},
      ]
    },
    tableColumn: {
      type: Array,
      default: () => [
        {type: 'img',label: "图片",prop: "image",plugin: true},
        {type: '',label: '用户名1',prop: 'username',},
        {type: 'time',label: '创建时间',prop: 'create_time', model: "{y}-{m}-{d}"},
      ]
    }
  },
  render(h) {
    return ( this.parentTable(h,{tableColumn: this.tableColumn}) )
  },
  data() {
    return {
      is_loading: false,
      is_selection_column: true
    }
  },
  watch: {
    loading: {
      handler(val) {
        this.is_loading = val;
      }
    }
  },
  methods: {
    parentTable(h,tableData) {
      return (
        <el-table v-loading={tableData.is_loading || this.is_loading}
                  data={tableData.data || this.data}
                  header-cell-style={tableData.headerCellStyle || this.headerCellStyle}
                  cell-style={tableData.cellStyle || this.cellStyle}
                  v-on:selection-change={this.handleSelectionChange}>
          {this.elTable.call(this,h,tableData.tableColumn)}
        </el-table>
      )
    },
    elTable(h, element) {
      let itemArray = [];
      try {
        this.handlerColumnMapping(h, itemArray, element);
      } catch (e) {
        console.error(e)
      } finally {
        return [ <el-table-column type="selection" width="80"></el-table-column>, ...itemArray, this.btnTableColumn(h,element) ];
      }
    },
    tableTableColumn(h,element) {
      let itemArray = [];
      let { label,width,children } = element;
      if (children.length == 0) tableNotException(element);
      this.handlerColumnMapping(h,itemArray,children);
      return ( <el-table-column label={label} width={this.handlerWidth(width)}> { ...itemArray } </el-table-column> )
    },
    spanTableColumn(h,element) {
      let { label,prop,width } = element;
      return (
        <el-table-column label={label} width={this.handlerWidth(width)} {...{ scopedSlots: {
            default: props => <span>{props.row[prop] ? props.row[prop] : '暂无数据' }</span>,
          }
        }} />
      )
    },
    timeTableColumn(h,element) {
      let { label,prop,model,width } = element;
      return (
        <el-table-column label={label} width={this.handlerWidth(width)} {...{ scopedSlots: {
            default: props => {
              if (!props.row[prop]) return <span>处理中</span>
              return model ? <span>{ parseTime(props.row[prop],model) }</span> : <span>{ parseTime(props.row[prop]) }</span>
            }
          }
        }} />
      )
    },
    tagTableColumn(h,element) {
      let { label,prop,filter,width,className } = element;
      if (!filter) tagException(element)
      return (
        <el-table-column label={label} width={this.handlerWidth(width)} {...{ scopedSlots: {
            default: props => {
              let tagItem = filter.find(item => props.row[prop] == item.id);
              return <el-tag class={className} type={tagItem.type}>{tagItem.name}</el-tag>
            }
          }
        }} />
      )
    },
    imgTableColumn(h,element) {
      let { label,prop,width,plugin,className,alt,img } = element;
      if (img == null || img == undefined || img == '') img = { width: 100, height: 100 }
      return (
        <el-table-column label={label} width={this.handlerWidth(width)} {...{ scopedSlots: {
            default: props => plugin
            ? <img class={className} style={`width: ${img.width}px;height: ${img.height}px`} alt={alt || 'loading'} v-lazy={props.row[prop]}/>
            : <el-image style={`width: ${img.width}px;height: ${img.height}px`} src={props.row[prop]} lazy />
          }
        }} />
      )
    },
    btnTableColumn(h, element) {
      let item = element.find(item => item.type == "bottom");
      if (item == null || item == undefined || item == '') return [];
      const { label,width,model,size } = item;
      return (
        <el-table-column label={label} width={this.handlerWidth(width)} {...{ scopedSlots: {
            default: props => {
              if (model.length == 0) btnNotException(element);
              return this.handlerBtn(h,model,props,size);
            }
          }
        }} />
      )
    },
    inputTableColumn(h,element) {
      const { label,prop,width,input } = element;
      return (
        <el-table-column label={label} width={this.handlerWidth(width)} {...{ scopedSlots : {
            default: props => {
              return this.handlerInputItem(h,input,prop,props);
            }
          }
        }} />
      )
    },
    popoverTableColumn(h,element) {
      const { label,prop,popover,width } = element;
      if (this.isEmpty(popover)) return popoverNotException(element)
      return (
        <el-table-column label={label} width={this.handlerWidth(width)} {...{ scopedSlots : {
            default: props => {
              return this.handlerPopoverChildren(h,prop,props,popover);
            }
          }
        }} />
      )
    },
    handlerPopoverChildren(h,prop,props,popover) {
      popover.data = props.row[prop];
      this.isEmpty(popover.model) ? popover.model = 'table' : null;
      this.is_selection_column = false;
      return (
        <el-popover placement={popover.placement} width={popover.width} trigger={popover.trigger}>
          { popover.model == 'table' ? this.parentTable(h,popover) : null }
          <el-button size="mini" slot="reference">查看</el-button>
        </el-popover>
      )

    },
    handlerInputItem(h,input,prop,props) {
      const {className,clearable,type,maxlength,minlength,placeholder,disabled,max,min,
        onBlur,onFocus,onChange} = input;
      return (
        <el-input className={[className, 'el__input__class']} v-model={props.row[prop]}
                        placeholder={placeholder || '请输入内容'}
                        max={max} min={min}
                        clearable={clearable || false} type={type.toLowerCase() || 'text'}
                        maxlength={maxlength} minlength={minlength}
                        disabled={disabled || false}
                        v-on:blur={() => this.$emit(onBlur || "", props.row)}
                        v-on:focus={() => this.$emit(onFocus || "", props.row)}
                        v-on:change={() => this.$emit(onChange || "", props.row)}/>
      )
    },
    handlerBtn(h,btn,props,size) {
      let children = [];
      btn.forEach(item => {
        children.push(<el-button icon={item.icon || ''} size={size || item.size} type={item.color} v-on:click={() => {
          this.$emit(item.onClick,props.row)
        }}>{item.name}</el-button>)
      })
      return children;
    },
    handlerColumnMapping(h,itemArray,element) {
      element.forEach(item => {
        if (item.type == "default" || item.type == undefined || item.type == "" || item.type == null)
          itemArray.push(this.spanTableColumn(h,item))
        else if (item.type == "time")
          itemArray.push(this.timeTableColumn(h,item))
        else if (item.type == "tag")
          itemArray.push(this.tagTableColumn(h,item))
        else if (item.type == "img")
          itemArray.push(this.imgTableColumn(h,item))
        else if (item.type == "table")
          itemArray.push(this.tableTableColumn(h,item))
        else if (item.type == "input")
          itemArray.push(this.inputTableColumn(h,item))
        else if (item.type == "popover")
          itemArray.push(this.popoverTableColumn(h,item))
      })
    },
    handlerWidth(width) {
      return width ? width : "auto";
    },
    handleSelectionChange(selection) {
      this.$emit("selection-change",selection);
    },
    isEmpty(data) {
      if (data instanceof Array) return Array.prototype.isPrototypeOf(data) && data.length === 0;
      if (data instanceof Object) return Object.prototype.isPrototypeOf(data) && Object.keys(data).length == 0;
      if (data == null || data == 'null' || data == undefined || data == 'undefined' || data.match(/^[\s]*$/)) {
        return true
      } else {
        return false
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.el__input__class {
  /deep/ input::-webkit-outer-spin-button,
  /deep/ input::-webkit-inner-spin-button {
    -webkit-appearance: none;
  }
}
</style>
