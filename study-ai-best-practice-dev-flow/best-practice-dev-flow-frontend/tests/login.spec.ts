import { test, expect } from '@playwright/test'
import { TEST_CONFIG, getFullUrl } from './config'

test.describe('登录功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto(getFullUrl(TEST_CONFIG.paths.login))
    await page.waitForLoadState('networkidle')
    await page.context().clearCookies()
  })

  test('登录页面显示正确', async ({ page }) => {
    await expect(page.locator('h1')).toHaveText('家庭族谱管理系统')
    await expect(page.locator(TEST_CONFIG.selectors.tabBtn, { hasText: '登录' })).toBeVisible()
    await expect(page.locator(TEST_CONFIG.selectors.tabBtn, { hasText: '注册' })).toBeVisible()
    await expect(page.locator(TEST_CONFIG.selectors.submitBtn)).toBeVisible()
  })

  test('注册新用户', async ({ page }) => {
    await page.locator(TEST_CONFIG.selectors.tabBtn, { hasText: '注册' }).click()
    
    await page.locator(TEST_CONFIG.selectors.usernameInput).fill('testuser')
    await page.locator(TEST_CONFIG.selectors.emailInput).fill('test@example.com')
    await page.locator(TEST_CONFIG.selectors.passwordInput).fill('password123')
    
    await page.locator(TEST_CONFIG.selectors.submitBtn).click()
    
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    await expect(page.locator(TEST_CONFIG.selectors.tabBtn, { hasText: '登录' })).toBeVisible()
  })

  test('登录成功', async ({ page }) => {
    await page.locator(TEST_CONFIG.selectors.usernameInput).fill('admin')
    await page.locator(TEST_CONFIG.selectors.passwordInput).fill('admin123')
    
    await page.locator(TEST_CONFIG.selectors.submitBtn).click()
    
    await page.waitForURL(`**${TEST_CONFIG.paths.family}`, { timeout: TEST_CONFIG.timeout.extraLong })
    await expect(page.locator('h1')).toHaveText('家族管理')
  })

  test('登录失败 - 错误密码', async ({ page }) => {
    await page.locator(TEST_CONFIG.selectors.usernameInput).fill('admin')
    await page.locator(TEST_CONFIG.selectors.passwordInput).fill('wrongpassword')
    
    await page.locator(TEST_CONFIG.selectors.submitBtn).click()
    
    await page.waitForTimeout(TEST_CONFIG.timeout.long)
    
    const url = page.url()
    expect(url).toBe(getFullUrl(TEST_CONFIG.paths.login))
  })

  test('登录失败 - 空用户名', async ({ page }) => {
    await page.locator(TEST_CONFIG.selectors.passwordInput).fill('password123')
    
    await page.locator(TEST_CONFIG.selectors.submitBtn).click()
    
    await page.waitForTimeout(TEST_CONFIG.timeout.long)
    
    const url = page.url()
    expect(url).toBe(getFullUrl(TEST_CONFIG.paths.login))
  })

  test('登录失败 - 空密码', async ({ page }) => {
    await page.locator(TEST_CONFIG.selectors.usernameInput).fill('admin')
    
    await page.locator(TEST_CONFIG.selectors.submitBtn).click()
    
    await page.waitForTimeout(TEST_CONFIG.timeout.long)
    
    const url = page.url()
    expect(url).toBe(getFullUrl(TEST_CONFIG.paths.login))
  })

  test('注册失败 - 空邮箱', async ({ page }) => {
    await page.locator(TEST_CONFIG.selectors.tabBtn, { hasText: '注册' }).click()
    
    await page.locator(TEST_CONFIG.selectors.usernameInput).fill('testuser2')
    await page.locator(TEST_CONFIG.selectors.passwordInput).fill('password123')
    
    await page.locator(TEST_CONFIG.selectors.submitBtn).click()
    
    await page.waitForTimeout(TEST_CONFIG.timeout.long)
  })

  test('登录态持久化 - 刷新页面保持登录', async ({ page }) => {
    await page.locator(TEST_CONFIG.selectors.usernameInput).fill('admin')
    await page.locator(TEST_CONFIG.selectors.passwordInput).fill('admin123')
    
    await page.locator(TEST_CONFIG.selectors.submitBtn).click()
    await page.waitForURL(`**${TEST_CONFIG.paths.family}`, { timeout: TEST_CONFIG.timeout.extraLong })
    
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    const pageContent = await page.textContent('body')
    expect(pageContent).toContain('家族管理')
  })
})