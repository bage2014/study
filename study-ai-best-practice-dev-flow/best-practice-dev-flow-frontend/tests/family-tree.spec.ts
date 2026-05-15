import { test, expect } from '@playwright/test'

test.describe('家族树功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173/login')
    await page.waitForLoadState('networkidle')
    
    await page.locator('input[placeholder="用户名"]').fill('admin')
    await page.locator('input[placeholder="密码"]').fill('admin123')
    await page.locator('.submit-btn').click()
    
    await page.waitForURL('**/family')
    await page.waitForTimeout(1000)
  })

  test('家族树页面显示正确', async ({ page }) => {
    await page.locator('.el-menu-item', { hasText: '家族树' }).click()
    await page.waitForURL('**/family-tree')
    await expect(page.locator('h1')).toHaveText('家族树')
  })
})
