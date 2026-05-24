import { test, expect } from '@playwright/test'
import { TEST_CONFIG, getFullUrl } from './config'

test.describe('成员管理调试', () => {
  test('检查添加成员按钮状态', async ({ page }) => {
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
    
    const result = await page.evaluate(() => {
      const buttons = document.querySelectorAll('button')
      let addButton = null
      
      for (const btn of buttons) {
        if (btn.textContent && btn.textContent.includes('添加成员')) {
          addButton = btn
          break
        }
      }
      
      if (!addButton) {
        addButton = document.querySelector('.el-button--primary')
      }
      
      console.log('Add button:', addButton)
      
      if (addButton) {
        const buttonText = addButton.textContent || ''
        const isVisible = window.getComputedStyle(addButton).display !== 'none'
        const isEnabled = !addButton.hasAttribute('disabled')
        const className = addButton.className
        const hasElButtonClass = addButton.classList.contains('el-button')
        
        return {
          buttonExists: true,
          buttonText,
          isVisible,
          isEnabled,
          className,
          hasElButtonClass
        }
      }
      
      return {
        buttonExists: false,
        buttonText: '',
        isVisible: false,
        isEnabled: false,
        className: '',
        hasElButtonClass: false
      }
    })
    
    console.log('Button check result:', result)
    
    const addButton = page.getByRole('button', { name: '添加成员' })
    await addButton.click()
    
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    const dialogVisible = await page.evaluate(() => {
      const dialog = document.querySelector('.el-dialog')
      return dialog && window.getComputedStyle(dialog).display !== 'none'
    })
    
    console.log('Dialog visible:', dialogVisible)
  })
})