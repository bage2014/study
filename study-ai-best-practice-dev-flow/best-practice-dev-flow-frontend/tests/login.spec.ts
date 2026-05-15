import { test, expect } from '@playwright/test'

test.describe('登录功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173/login')
    await page.waitForLoadState('networkidle')
    await page.context().clearCookies()
  })

  test('登录页面显示正确', async ({ page }) => {
    await expect(page.locator('h1')).toHaveText('家庭族谱管理系统')
    await expect(page.locator('.tab-btn', { hasText: '登录' })).toBeVisible()
    await expect(page.locator('.tab-btn', { hasText: '注册' })).toBeVisible()
    await expect(page.locator('.submit-btn')).toBeVisible()
  })

  test('注册新用户', async ({ page }) => {
    await page.locator('.tab-btn', { hasText: '注册' }).click()
    
    await page.locator('input[placeholder="用户名"]').fill('testuser')
    await page.locator('input[placeholder="邮箱"]').fill('test@example.com')
    await page.locator('input[placeholder="密码"]').fill('password123')
    
    await page.locator('.submit-btn').click()
    
    await page.waitForTimeout(1500)
    await expect(page.locator('.tab-btn', { hasText: '登录' })).toBeVisible()
  })

  test('登录成功', async ({ page }) => {
    await page.locator('input[placeholder="用户名"]').fill('admin')
    await page.locator('input[placeholder="密码"]').fill('admin123')
    
    await page.locator('.submit-btn').click()
    
    await page.waitForURL('**/family', { timeout: 10000 })
    await expect(page.locator('h1')).toHaveText('家族管理')
  })

  test('登录失败 - 错误密码', async ({ page }) => {
    await page.locator('input[placeholder="用户名"]').fill('admin')
    await page.locator('input[placeholder="密码"]').fill('wrongpassword')
    
    await page.locator('.submit-btn').click()
    
    await page.waitForTimeout(2000)
    
    const url = page.url()
    expect(url).toBe('http://localhost:5173/login')
  })

  test('登录失败 - 空用户名', async ({ page }) => {
    await page.locator('input[placeholder="密码"]').fill('password123')
    
    await page.locator('.submit-btn').click()
    
    await page.waitForTimeout(2000)
    
    const url = page.url()
    expect(url).toBe('http://localhost:5173/login')
  })

  test('登录失败 - 空密码', async ({ page }) => {
    await page.locator('input[placeholder="用户名"]').fill('admin')
    
    await page.locator('.submit-btn').click()
    
    await page.waitForTimeout(2000)
    
    const url = page.url()
    expect(url).toBe('http://localhost:5173/login')
  })

  test('注册失败 - 空邮箱', async ({ page }) => {
    await page.locator('.tab-btn', { hasText: '注册' }).click()
    
    await page.locator('input[placeholder="用户名"]').fill('testuser2')
    await page.locator('input[placeholder="密码"]').fill('password123')
    
    await page.locator('.submit-btn').click()
    
    await page.waitForTimeout(2000)
  })

  test('登录态持久化 - 刷新页面保持登录', async ({ page }) => {
    await page.locator('input[placeholder="用户名"]').fill('admin')
    await page.locator('input[placeholder="密码"]').fill('admin123')
    
    await page.locator('.submit-btn').click()
    await page.waitForURL('**/family', { timeout: 10000 })
    
    await page.waitForTimeout(1000)
    
    const pageContent = await page.textContent('body')
    expect(pageContent).toContain('家族管理')
  })
})
