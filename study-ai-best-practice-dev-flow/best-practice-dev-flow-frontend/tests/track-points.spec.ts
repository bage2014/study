import { test, expect } from '@playwright/test'
import { TEST_CONFIG, getFullUrl } from './config'

test.describe('轨迹追踪功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto(getFullUrl(TEST_CONFIG.paths.login))
    await page.waitForLoadState('networkidle')
    
    await page.locator(TEST_CONFIG.selectors.usernameInput).fill('admin')
    await page.locator(TEST_CONFIG.selectors.passwordInput).fill('admin123')
    await page.locator(TEST_CONFIG.selectors.submitBtn).click()
    
    await page.waitForURL(`**${TEST_CONFIG.paths.family}`)
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
  })

  test('轨迹页面显示正确', async ({ page }) => {
    await page.locator('.el-menu-item', { hasText: '轨迹追踪' }).click()
    await page.waitForURL(`**${TEST_CONFIG.paths.track}`)
    await expect(page.locator('h1')).toHaveText('轨迹点管理')
  })
})