import { useThemeStore } from '@/stores/theme'
import { storeToRefs } from 'pinia'

export function useTheme() {
  const themeStore = useThemeStore()
  const { currentTheme } = storeToRefs(themeStore)
  const { setTheme, toggleTheme } = themeStore
  
  return {
    currentTheme,
    setTheme,
    toggleTheme
  }
}