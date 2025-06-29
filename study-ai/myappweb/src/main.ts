import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

import ElementPlus from "element-plus";
import "element-plus/dist/index.css";

import "~/styles/index.scss";
import 'uno.css'

// If you want to use ElMessage, import it.
import "element-plus/theme-chalk/src/message.scss"

// 全局变量，用于存储地图初始化状态
let baiduMapInitialized = false;

// 百度地图初始化回调函数
window.initBaiduMap = function() {
  console.log('initBaiduMap-2');
  baiduMapInitialized = true;
};

// 5. 创建并挂载根实例
const app = createApp(App);
app.use(ElementPlus);
app.use(router); //确保 _use_ 路由实例使 整个应用支持路由。
app.mount("#app");
