import { useI18n as useVueI18n } from 'vue-i18n'
import { useI18nStore } from '@/stores/i18n'
import { storeToRefs } from 'pinia'

export function useI18n() {
  const { t, locale } = useVueI18n()
  const i18nStore = useI18nStore()
  const { currentLocale } = storeToRefs(i18nStore)
  const { setLocale, toggleLocale } = i18nStore
  
  return {
    t,
    locale,
    currentLocale,
    setLocale,
    toggleLocale
  }
}