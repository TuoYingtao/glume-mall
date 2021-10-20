## glumemall组件的使用 TableBox

组件是基于 element-ui 2.15.5 封装的。
由于大部分订单的字段基本一致，在开发的过程中显得非常繁琐、沉重及臃肿；为了提高开发效率将其封装成组件共页面使用；组件主要以 JSON 的方式来定义DOM 结构。

## 引用方法：

```js
import SearchBox from '@/components/searchBox/index'
components: { TableBox }
```

```html
<table-box :loading="loading" :data="orderData" :tableColumn="tableColumn" @selection-change="handleSelectionChange"/>
```

## 属性

| 属性名称        | 类型             | 描述                                                         |
| --------------- | ---------------- | ------------------------------------------------------------ |
| Loading         | Boolean,Function | 表格是否启用加载 默认为：false                               |
| headerCellStyle | Object,Function  | 表头单元格的 style 的回调方法，也可以使用一个固定的 Object 为所有表头单元格设置一样的 Style。 默认为：{'text-align':'center'} |
| cellStyl        | Object,Function  | 单元格的 style 的回调方法，也可以使用一个固定的 Object 为所有单元格设置一样的 Style。默认为： {'text-align':'center'} |
| data            | Array            | 数据源：表格的内容数据                                       |
| tableColumn     | Array,Function   | DOM 结构                                                     |

### tableColumn DOM结构属性（核心）

| 属性名称 | 类型          | 描述                                                         |
| -------- | ------------- | ------------------------------------------------------------ |
| label    | String        | 表头单元格的标题                                             |
| prop     | String        | 字段名（对应数据源渲染的的字段）                             |
| width    | String,Number | 表头单元格列的宽度默认为 auto                                |
| type     | String        | 表头单元格列的类型可选添也可为空''，可选：看下方的《type 表头单元格列属性》 |

```json
[
    {label: '订单编号',prop: 'id',width: 120},
    {type: "img",label: '图片',prop: 'model_image',plugin: true}
]
```



#### type 表头单元格列属性

**type类型**：`time`

| 属性名称 | 类型   | 描述                                  |
| -------- | ------ | ------------------------------------- |
| model    | String | 时间模板默为：{y}-{m}-{d} {h}:{i}:{s} |

```json
{
    type: 'time',
    model: "{y}-{m}-{d}"
}
```

**type类型**：`tag`

| 属性名称  | 类型   | 描述                                                        |
| --------- | ------ | ----------------------------------------------------------- |
| filter    | Array  | [ id: 唯一标识，name: 名称, type: tag颜色与原生el-tag相符 ] |
| className | String | 给标签设置类名                                              |

```json
{
    type: 'tag',
    className: "tag"
    filter:[
        {id: 1,name: "已预约",type: "warning"},
        {id: 2,name: "已受理",type: "info"}
    ]
}
```

**type类型**：`bottom`

| 属性名称 | 类型   | 描述                                                         |
| -------- | ------ | ------------------------------------------------------------ |
| model    | Array  | [ name: 名称， color: 按钮颜色，onClick：按钮的点击回调事件，返回数据为当前项的信息 ] |
| size     | String | 设置按钮大小与 el-bottom 属性一致                            |

```json
{
    type: "bottom",
    size: "mini", 
    model: [
        {name: "删除",color: "danger",onClick: "delete"},
        {name: "编辑",color: "warning",onClick: "amend"}
    ]
}
```

**type类型**：`table`

| 属性名称 | 类型  | 描述                    |
| -------- | ----- | ----------------------- |
| children | Array | [ 嵌套列：tableColumn ] |

**type类型**：`img`

| 属性名称  | 类型    | 描述                                                  |
| --------- | ------- | ----------------------------------------------------- |
| plugin    | Boolean | 是否启用自己的插件,目前只支持： vue-lazyload          |
| className | String  | 定义类名                                              |
| alt       | String  | 如果无法显示图像，浏览器将显示替代文本                |
| img       | Object  | [ width: 图片宽度；height: 图片高度 ] 默认：100 x 100 |

```json
{
    type: "img",
    plugin: true,
    className: "image",
    alt: "图片已失效",
    img: {
        width: 100,
        height: 100
    }
}
```

**type类型**：`input`

| 属性名称 | 类型   | 描述                                                         |
| -------- | ------ | ------------------------------------------------------------ |
| input    | Object | 属性与 el-input 相同; 事件: [ onBlur：在 Input 失去焦点时触发；onFocus：在 Input 获得焦点时触发；onChange：仅在输入框失去焦点或用户按下回车时触发 ] |

```JSON
{
	type: "input",
	input: {
		onBlur: "onBlur",
		onFocus: "onFocus",
		onChange: "onChange",
	}
}
```

**type类型**：`popover`

| 属性名称 | 类型   | 描述                                                         |
| -------- | ------ | ------------------------------------------------------------ |
| popover  | Object | [placement：显示位置；trigger：唤醒显示事件；model：类型；tableColumn：表格列数据] 默认类型为：table |

```JSON
{
    type: 'popover',
 	popover: {
        placement: 'right',
        trigger: 'hover',
        model: 'table',
        tableColumn: [{label: 'label',prop: 'prop'}]
    }
}
```

