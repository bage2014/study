import { test, expect } from '@playwright/test'

test.describe('成员管理功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5174/login')
    await page.waitForLoadState('networkidle')
    
    await page.locator('input[placeholder="用户名"]').fill('admin')
    await page.locator('input[placeholder="密码"]').fill('admin123')
    await page.locator('.submit-btn').click()
    
    await page.waitForURL('**/family')
    await page.waitForTimeout(1500)
    
    await page.getByRole('button', { name: '创建家族' }).click()
    await page.waitForTimeout(500)
    
    const familyName = '测试家族_' + Date.now()
    await page.locator('input[placeholder="请输入家族名称"]').fill(familyName)
    await page.getByRole('button', { name: '创建', exact: true }).click()
    await page.waitForTimeout(1500)
    
    await page.getByText('成员管理').click()
    await page.waitForURL('**/members')
    await page.waitForLoadState('networkidle')
    await page.waitForTimeout(1000)
  })

  test('成员页面显示正确', async ({ page }) => {
    await expect(page.locator('h1')).toHaveText('成员管理')
    await expect(page.getByRole('button', { name: '添加成员' })).toBeVisible()
    await expect(page.locator('.search-bar')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
  })

  test('成员搜索功能', async ({ page }) => {
    await page.locator('input[placeholder="搜索成员姓名"]').fill('测试')
    await page.locator('.search-bar').getByRole('button').click()
    await page.waitForTimeout(800)
  })
})