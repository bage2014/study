import { test, expect } from '@playwright/test'
import { TEST_CONFIG, getFullUrl } from './config'

test.describe('成员管理API测试', () => {
  test('添加成员完整流程', async ({ page }) => {
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
    
    const addButton = page.getByRole('button', { name: '添加成员' })
    await addButton.click()
    
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    await page.locator('input.el-input__inner').first().fill('API测试成员')
    
    let requestFailed = false
    let responseCode = null
    
    page.on('request', (request) => {
      if (request.url().includes('/api/members') && request.method() === 'POST') {
        console.log('POST request to /api/members')
      }
    })
    
    page.on('response', (response) => {
      if (response.url().includes('/api/members') && response.request().method() === 'POST') {
        responseCode = response.status()
        console.log('Response status:', responseCode)
        if (responseCode !== 200) {
          requestFailed = true
        }
      }
    })
    
    const saveButton = page.getByRole('button', { name: '保存', exact: true })
    await saveButton.click()
    
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    console.log('Request failed:', requestFailed)
    console.log('Response code:', responseCode)
    
    const dialog = page.locator('.el-dialog')
    const isDialogVisible = await dialog.isVisible()
    console.log('Dialog visible after save:', isDialogVisible)
    
    const elMessage = await page.evaluate(() => {
      const message = document.querySelector('.el-message')
      return message ? message.textContent : null
    })
    console.log('Element message:', elMessage)
  })
})