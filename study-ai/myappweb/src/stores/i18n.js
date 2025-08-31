import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'

export const useI18nStore = defineStore('i18n', () => {
  const { locale } = useI18n()
  const currentLocale = ref(localStorage.getItem('locale') || 'zh')
  
  function setLocale(lang) {
    currentLocale.value = lang
    locale.value = lang
    localStorage.setItem('locale', lang)
  }
  
  function toggleLocale() {
    const newLocale = currentLocale.value === 'zh' ? 'en' : 'zh'
    setLocale(newLocale)
  }
  
  return { currentLocale, setLocale, toggleLocale }
})