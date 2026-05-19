import { test, expect } from '@playwright/test'

test.describe('AI功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5174/login')
    await page.waitForLoadState('networkidle')
    
    await page.locator('input[placeholder="用户名"]').fill('admin')
    await page.locator('input[placeholder="密码"]').fill('admin123')
    await page.locator('.submit-btn').click()
    
    await page.waitForURL('**/family')
    await page.waitForTimeout(1000)
  })

  test('AI页面显示正确', async ({ page }) => {
    await page.locator('.el-menu-item', { hasText: 'AI功能' }).click()
    await page.waitForURL('**/ai')
    await expect(page.locator('h1')).toHaveText('AI 功能')
  })
})
