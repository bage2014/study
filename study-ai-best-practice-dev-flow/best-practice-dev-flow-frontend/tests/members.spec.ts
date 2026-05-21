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

  test('使用不同选择器点击按钮', async ({ page }) => {
    const buttons = await page.locator('button').all()
    console.log('Total buttons:', buttons.length)
    
    for (let i = 0; i < buttons.length; i++) {
      const text = await buttons[i].textContent()
      if (text && text.includes('添加成员')) {
        console.log(`Found button at index ${i}: ${text}`)
        
        await buttons[i].click()
        await page.waitForTimeout(TEST_CONFIG.timeout.medium)
        
        const dialogs = await page.locator('.el-dialog').count()
        console.log('Dialogs after click:', dialogs)
        
        if (dialogs > 0) {
          console.log('SUCCESS: Dialog appeared!')
          break
        }
      }
    }
  })

  test('成员页面显示正确', async ({ page }) => {
    await expect(page.locator('h1')).toHaveText('成员管理')
    await expect(page.getByRole('button', { name: '添加成员' })).toBeVisible()
    await expect(page.locator(TEST_CONFIG.selectors.searchBar)).toBeVisible()
    await expect(page.locator(TEST_CONFIG.selectors.elTable)).toBeVisible()
  })
})