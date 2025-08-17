import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import i18n from './i18n'
import { initTheme } from './utils/theme'
// 导入 Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// 导入 Element Plus 中文语言包
import zhCn from 'element-plus/es/locale/lang/zh-cn'

// 初始化主题
const theme = initTheme()

const app = createApp(App)

app.use(router)
app.use(i18n)
app.use(ElementPlus, {
  locale: zhCn,
  // 配置 Element Plus 主题模式
  dark: theme === 'dark'
})

app.mount('#app')
