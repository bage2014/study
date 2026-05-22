import { test, expect } from '@playwright/test'
import { TEST_CONFIG, getFullUrl } from './config'

test.describe('成员管理功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto(getFullUrl(TEST_CONFIG.paths.login))
    await page.waitForLoadState('networkidle')
    
    await page.locator(TEST_CONFIG.selectors.usernameInput).fill('admin')
    await page.locator(TEST_CONFIG.selectors.passwordInput).fill('admin123')
    await page.locator(TEST_CONFIG.selectors.submitBtn).click()
    
    await page.waitForURL(`**${TEST_CONFIG.paths.family}`)
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    await page.getByText('成员管理').click({ force: true })
    await page.waitForURL(`**${TEST_CONFIG.paths.members}`)
    await page.waitForLoadState('networkidle')
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
  })

  test('检查脚本加载和Vue状态', async ({ page }) => {
    const result = await page.evaluate(() => {
      // 检查脚本标签
      const scripts = document.querySelectorAll('script')
      console.log('Number of scripts:', scripts.length)
      
      // 检查是否有Vue相关的脚本
      const vueScripts = Array.from(scripts).filter(s => 
        s.src.includes('vue') || s.textContent?.includes('Vue')
      )
      console.log('Vue-related scripts:', vueScripts.length)
      
      // 检查window上的Vue对象
      const hasVue = typeof window !== 'undefined' && !!window['Vue']
      console.log('Vue on window:', hasVue)
      
      // 检查是否有__VUE__或_vueParentComponent属性
      const app = document.querySelector('#app')
      const vueAttrs = app ? app.getAttributeNames().filter(n => n.includes('vue')) : []
      console.log('App Vue attrs:', vueAttrs)
      
      // 检查是否有data-v-属性的元素（Vue组件标记）
      const vueElements = document.querySelectorAll('[data-v-]')
      console.log('Elements with data-v-:', vueElements.length)
      
      return {
        scriptCount: scripts.length,
        vueScriptCount: vueScripts.length,
        hasVueOnWindow: hasVue,
        appVueAttrs: vueAttrs,
        vueElementCount: vueElements.length
      }
    })
    
    console.log('Script and Vue check result:', result)
  })

  test('成员页面显示正确', async ({ page }) => {
    await expect(page.locator('h1')).toHaveText('成员管理')
    await expect(page.getByRole('button', { name: '添加成员' })).toBeVisible()
    await expect(page.locator(TEST_CONFIG.selectors.searchBar)).toBeVisible()
    await expect(page.locator(TEST_CONFIG.selectors.elTable)).toBeVisible()
  })
})