/**
 * 通用js方法封装处理
 * Copyright (c) 2020 TuoYingtao
 */
const baseURL = process.env.VUE_APP_BASE_API
/**
 * 日期格式化
 * @param time
 * @param pattern
 * @returns {string|null}
 */
export function parseTime(time, pattern) {
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





// 回显数据字典
export function selectDictLabel(datas, value) {
	var actions = [];
	Object.keys(datas).some((key) => {
		if (datas[key].dictValue == ('' + value)) {
			actions.push(datas[key].dictLabel);
			return true;
		}
	})
	return actions.join('');
}
// 回显数据字典（字符串数组）
export function selectDictLabels(datas, value, separator) {
	var actions = [];
	var currentSeparator = undefined === separator ? "," : separator;
	var temp = value.split(currentSeparator);
	Object.keys(value.split(currentSeparator)).some((val) => {
		Object.keys(datas).some((key) => {
			if (datas[key].dictValue == ('' + temp[val])) {
				actions.push(datas[key].dictLabel + currentSeparator);
			}
		})
	});
	return actions.join('').substring(0, actions.join('').length - 1);
}
/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 * @param {*} rootId 根Id 默认 0
 */
export function handleTree(data, id, parentId, children, rootId) {
  id = id || 'id';
	parentId = parentId || 'parentId';
	children = children || 'children';
	rootId = rootId || Math.min.apply(Math, data.map(item => { return item[parentId] })) || 0;
	//对源数据深度克隆
	const cloneData = JSON.parse(JSON.stringify(data));
  //循环所有项
	const treeData = cloneData.filter(father => {
		let branchArr = cloneData.filter(child => {
			//返回每一项的子级数组
			return father[id] === child[parentId]
		});
    branchArr.length > 0 ? father.children = branchArr : '';
		//返回第一层
		return father[parentId] === rootId;
	});
	return treeData != '' ? treeData : data;
}
/**
 * 网页离开返回页面；网页标签提示
 */
export function HTMLTitle() {
  const defaultSettings = require('../settings')
  document.addEventListener('visibilitychange', function() {
    const titleName = defaultSettings.title;// 页面标签标题
    let isHidden = document.hidden;
    let title = '(*´∇｀*) 欢迎回来！';
    let title2 = '(oﾟvﾟ)ノ Hi';
    if (isHidden) {
      setTimeout(()=> {
        document.title = title2;
      },1000)
    } else {
      document.title = title;
      setTimeout(()=> {
        document.title = titleName;
      },1000)
    }
  });
}

/**
 * 添加日期范围
 * @param params
 * @param dateRange
 * @returns {*}
 */
export function addDateRange(params, dateRange) {
  var search = params;
  search.beginTime = "";
  search.endTime = "";
  if (null != dateRange && '' != dateRange) {
    search.beginTime = dateRange[0];
    search.endTime = dateRange[1];
  }
  return search;
}
/**
 * 表单重置
 * @param refName 需要重置表单的名字
 */
export function resetForm(refName) {
  if (this.$refs[refName]) {
    this.$refs[refName].resetFields();
  }
}






/* 字符串处理 */
export const stringFun = {
  /**
   * 转换字符串，undefined,null等转化为""
   * @param {String} str 转换字符串
   * @returns {string|*} 返回转换后的字符串
   */
  praseStrEmpty(str) {
    if (!str || str == "undefined" || str == "null") {
      return "";
    }
    return str;
  },
  /**
   * 字符串格式化(%s )
   * @param {String} str 需要格式化的字符串
   * @returns {*} 新的字符串
   */
  sprintf(str) {
    var args = arguments, flag = true, i = 1;
    str = str.replace(/%s/g, function () {
      var arg = args[i++];
      if (typeof arg === 'undefined') {
        flag = false;
        return '';
      }
      return arg;
    });
    return flag ? str : '';
  }
}


/* 数组处理 */
export const arrayFun = {
  /**
   * 删除数据
   * @param {Array} data 数据源
   * @param {String} key 删除条件
   * @param {Number} param 匹配条件
   * @returns {Array} 返回新的数组
   */
  deleteList(data,key,param) {
    const INDEX = data.findIndex(item => item[key] === param);
    data.splice(INDEX,1);
    return data
  },
  /**
   * 查找数据
   * @param {Array} data 数据源
   * @param {String} key 删除条件
   * @param {Number} param 查找的ID
   * @returns {null|Object} 返回一个对象，如果没找到则返回null
   */
  selectArray(data,key,param) {
    let obj = data.find(item => item[key] === param);
    if (stringFun.praseStrEmpty(obj) !== "") {
      return obj;
    }else {
      return null;
    }
  },
  /**
   * 替换数组里的某个对象或元素值
   * @param {Array} data 总数据源
   * @param {String} key 指定替换对象条件
   * @param {Number} param 查找的ID
   * @param {Object} obj 替换的新数据
   * @param {Object} name 需要替换的元素名；如果为空则替换当前索引值
   * @returns {Array} 返回一个新的数组
   */
  selectArrayAll(data,key,param,obj,name) {
    const INDEX = data.findIndex(item => item[key] === param);
    if (stringFun.praseStrEmpty(name) === "" ) {
      data[INDEX] = obj;
    }else {
      data[INDEX][name] = obj;
    }
    return data;
  },
  /**
   * 查找数组中相应键名的对象
   * @param {Array} data 数据源
   * @param {String} key 数据源匹配字段
   * @param {Number} param 筛选条件
   * @returns {*} 返回一个新的数据
   */
  filterArray(data,key,param) {
    let arr = data.filter(item => item[key] === param);
    return arr.length === 0 ? -1 : arr;
  },
  /**
   * 给对象添加新的字段
   * @param {Array} data 数据源
   * @param {String} key 键名
   * @param {Array} value 键值
   */
  addObjField(data,key,value) {
    data.forEach(item => {
      item[key] = value
    });
    return data
  },
  /**
   * 获取数组中相应键名的所有值
   * @param {Array} data 数据源
   * @param {String} key 键名
   * @returns {Array} 返回所有键名值的数组
   */
  requestArray(data,key) {
    let array = [];
    data.forEach(item => {
      if (item[key] !== "" || item[key] !== null || item[key] !== undefined) {
        array.push(item[key])
      }
    })
    return array;
  }
}

export const networkImage = {
  /**
   * @param img js 图片对象
   * @returns {string} 已编码的 DataURL
   */
   getImageDataURL: function (img) {
    // 创建画布
    let canvas = document.createElement('canvas');
    canvas.width = img.width;
    canvas.height = img.height;
    let ctx = canvas.getContext('2d');
    // 将img中的内容画到画布上; 以图片为背景剪裁画布
    ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
    // 获取图片后缀名
    const extension = img.src.substring(img.src.lastIndexOf(".") + 1).toLowerCase();
    // 某些图片 url 可能没有后缀名，默认是 png
    return canvas.toDataURL("iamge/" + extension,1);
  },
  /**
   * 点击下载图片
   * @param url 图片地址
   * @param text 图片名称：下载后的名称
   */
  downloadImage(url,text) {
    let a = document.createElement('a');
    // 此属性的值就是下载时图片的名称，注意，名称中不能有半角点，否则下载时后缀名会错误。
    a.setAttribute("download",text.replace(/\./g,"。"));
    let img = new Image();
    // 设置img的url：添加时间戳，防止浏览器缓存。
    img.src = url + "?time=" + new Date().getTime();
    // 设置crossOrigin属性，否则浏览器会报错。
    img.setAttribute("crossOrigin","Anonymous");
    // 图片未加载完成时操作会报错
    img.onload = function () {
      a.href = getImageDataURL(img);
      // 触发a链接点击事件，浏览器开始下载文件
      a.click();
    };
  }
}


/**
 * 通用下载方法
 */
export function download(fileName) {
  window.location.href = baseURL + "/common/download?fileName=" + encodeURI(fileName) + "&delete=" + true;
}

/**
 * 点击复制
 * @type {{bind(*, {value: *}): void, unbind(*): void, componentUpdated(*, {value: *}): void}}
 */
export const vCopy = { // 名字爱取啥取啥
  /*
   bind 钩子函数，第一次绑定时调用，可以在这里做初始化设置
   el: 作用的 dom 对象
   value: 传给指令的值，也就是我们要 copy 的值
  */
  bind(el, { value }) {
    el.$value = value; // 用一个全局属性来存传进来的值，因为这个值在别的钩子函数里还会用到
    el.handler = () => {
      if (!el.$value) {
        // 值为空的时候，给出提示，我这里的提示是用的 ant-design-vue 的提示，你们随意
        alert({title: '无复制内容',message: '复制成功！',duration: 800});
        return;
      }
      // 动态创建 textarea 标签
      const textarea = document.createElement('textarea');
      // 将该 textarea 设为 readonly 防止 iOS 下自动唤起键盘，同时将 textarea 移出可视区域
      textarea.readOnly = 'readonly';
      textarea.style.position = 'absolute';
      textarea.style.left = '-9999px';
      // 将要 copy 的值赋给 textarea 标签的 value 属性
      textarea.value = el.$value;
      // 将 textarea 插入到 body 中
      document.body.appendChild(textarea);
      // 选中值并复制
      textarea.select();
      textarea.setSelectionRange(0, textarea.value.length);
      const result = document.execCommand('Copy');
      if (result) {
        alert({title: '复制成功',message: '复制成功！',duration: 800});
      }
      document.body.removeChild(textarea);
    };
    // 绑定点击事件，就是所谓的一键 copy 啦
    el.addEventListener('click', el.handler);
  },
  // 当传进来的值更新的时候触发
  componentUpdated(el, { value }) {
    el.$value = value;
  },
  // 指令与元素解绑的时候，移除事件绑定
  unbind(el) {
    el.removeEventListener('click', el.handler);
  },
};
