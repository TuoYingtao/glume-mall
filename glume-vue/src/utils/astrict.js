import { setItem,getItem,removeItem,clear } from '@/utils/auth'
import router from '../router'


let lastTime = new Date().getTime()
let currentTime = new Date().getTime()
let timeOut = 30 * 60 * 1000  //设置超时时间: 30分钟

window.onload = function () {
  let onmousedown,onmousemove,onmousewhell,onkeydown;
  window.document.onmousedown = function (event) {
    // console.log("鼠标点击事件：" , event)
    setItem("lastTime",new Date().getTime())
    return onmousedown = true;
  }
  window.document.onmousemove = function(event) {
    // console.log("鼠标移动事件：" , event)
    setItem("lastTime",new Date().getTime())
    return onmousemove = true;
  }
  window.document.onmousewhell = function(event) {
    // console.log("鼠标滚动事件：" , event)
    setItem("lastTime",new Date().getTime())
    return onmousewhell = true;
  }
  window.document.onkeydown = function(event) {
    // console.log("键盘事件：" , event)
    setItem("lastTime",new Date().getTime())
    return onkeydown = true;
  }
};

function checkTimeout() {
  console.log("没三分中检测一次")
  currentTime = new Date().getTime()		//更新当前时间
  lastTime = getItem("lastTime");
  if (currentTime - lastTime > timeOut) { //判断是否超时

    // 清除storage的数据(登陆信息和token)
    removeItem("lastTime");
    // 跳到登陆页
    if(router.currentRoute.name == 'lockScreen') return // 当前已经是登陆页时不做跳转
    router.push({ name: 'lockScreen' })
  }
}

export default function () {
  /* 定时器 间隔30秒检测是否长时间未操作页面 */
  window.setInterval(checkTimeout, 3 * 60 * 1000);
}
