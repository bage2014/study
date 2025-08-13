import { createI18n } from 'vue-i18n'

// 导入语言文件
import en from './locales/en.json'
import zh from './locales/zh.json'

// 检测用户浏览器语言
const userLocale = navigator.language.includes('zh') ? 'zh' : 'en'

// 创建i18n实例
const i18n = createI18n({
  legacy: false,
  locale: localStorage.getItem('locale') || userLocale,
  fallbackLocale: 'en',
  messages: {
    en,
    zh
  }
})

export default i18n