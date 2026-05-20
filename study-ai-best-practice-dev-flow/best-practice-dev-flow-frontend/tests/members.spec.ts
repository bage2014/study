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
    
    await page.getByRole('button', { name: '创建家族' }).click()
    await page.waitForTimeout(TEST_CONFIG.timeout.short)
    
    const familyName = '测试家族_' + Date.now()
    await page.locator('input[placeholder="请输入家族名称"]').fill(familyName)
    await page.getByRole('button', { name: '创建', exact: true }).click()
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    await page.getByText('成员管理').click()
    await page.waitForURL(`**${TEST_CONFIG.paths.members}`)
    await page.waitForLoadState('networkidle')
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
  })

  test('成员页面显示正确', async ({ page }) => {
    await expect(page.locator('h1')).toHaveText('成员管理')
    await expect(page.getByRole('button', { name: '添加成员' })).toBeVisible()
    await expect(page.locator(TEST_CONFIG.selectors.searchBar)).toBeVisible()
    await expect(page.locator(TEST_CONFIG.selectors.elTable)).toBeVisible()
  })

  test('成员搜索功能', async ({ page }) => {
    await page.locator('input[placeholder="搜索成员姓名"]').fill('测试')
    await page.locator(TEST_CONFIG.selectors.searchBar).getByRole('button').click()
    await page.waitForTimeout(TEST_CONFIG.timeout.long)
  })
})