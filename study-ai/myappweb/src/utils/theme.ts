// 定义主题类型
export type Theme = 'light' | 'dark' | 'blue' | 'green'

// 主题配置
const themeConfigs = {
  light: {
    '--primary-color': '#42b983',
    '--primary-light': '#66c99f',
    '--primary-dark': '#3aa876',
    '--text-color': '#333',
    '--text-light': '#666',
    '--bg-color': '#fff',
    '--bg-light': '#f5f5f5',
    '--border-color': '#eaeaea'
  },
  dark: {
    '--primary-color': '#42b983',
    '--primary-light': '#66c99f',
    '--primary-dark': '#3aa876',
    '--text-color': '#fff',
    '--text-light': '#ccc',
    '--bg-color': '#1a1a1a',
    '--bg-light': '#242424',
    '--border-color': '#333'
  },
  blue: {
    '--primary-color': '#646cff',
    '--primary-light': '#8188ff',
    '--primary-dark': '#535bf2',
    '--text-color': '#333',
    '--text-light': '#666',
    '--bg-color': '#fff',
    '--bg-light': '#f5f5f5',
    '--border-color': '#eaeaea'
  },
  green: {
    '--primary-color': '#42b983',
    '--primary-light': '#66c99f',
    '--primary-dark': '#3aa876',
    '--text-color': '#333',
    '--text-light': '#666',
    '--bg-color': '#fff',
    '--bg-light': '#f5f5f5',
    '--border-color': '#eaeaea'
  }
}

// 应用主题
export function applyTheme(theme: Theme) {
  const root = document.documentElement
  const config = themeConfigs[theme]

  for (const [key, value] of Object.entries(config)) {
    root.style.setProperty(key, value)
  }

  // 保存主题到本地存储
  localStorage.setItem('theme', theme)
}

// 初始化主题
export function initTheme() {
  const savedTheme = localStorage.getItem('theme') as Theme || 'light'
  applyTheme(savedTheme)
  return savedTheme
}