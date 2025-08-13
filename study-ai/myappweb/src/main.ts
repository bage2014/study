import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import i18n from './i18n'
import { initTheme } from './utils/theme'

// 初始化主题
initTheme()

const app = createApp(App)

app.use(router)
app.use(i18n)

app.mount('#app')
