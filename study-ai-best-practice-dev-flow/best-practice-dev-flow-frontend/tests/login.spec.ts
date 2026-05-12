import { test, expect } from '@playwright/test'

test.describe('登录功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173')
    await page.waitForLoadState('networkidle')
  })

  test('登录页面显示正确', async ({ page }) => {
    await expect(page.locator('h1')).toHaveText('家庭族谱管理系统')
    await expect(page.locator('button', { hasText: '登录' })).toBeVisible()
    await expect(page.locator('button', { hasText: '注册' })).toBeVisible()
  })

  test('注册新用户', async ({ page }) => {
    await page.locator('button', { hasText: '注册' }).click()
    
    await page.locator('input[placeholder="用户名"]').fill('testuser')
    await page.locator('input[placeholder="邮箱"]').fill('test@example.com')
    await page.locator('input[placeholder="密码"]').fill('password123')
    
    await page.locator('button', { hasText: '注册' }).click()
    
    await expect(page.locator('button', { hasText: '登录' })).toBeVisible()
  })

  test('登录成功', async ({ page }) => {
    await page.locator('input[placeholder="用户名"]').fill('testuser')
    await page.locator('input[placeholder="密码"]').fill('password123')
    
    await page.locator('button', { hasText: '登录' }).click()
    
    await page.waitForURL('**/family')
    await expect(page.locator('h1')).toHaveText('家族管理')
  })

  test('登录失败 - 错误密码', async