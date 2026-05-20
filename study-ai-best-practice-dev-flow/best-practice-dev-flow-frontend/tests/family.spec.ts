import { test, expect } from '@playwright/test'
import { TEST_CONFIG, getFullUrl } from './config'

test.describe('家族管理功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto(getFullUrl(TEST_CONFIG.paths.login))
    await page.waitForLoadState('networkidle')
    
    await page.locator(TEST_CONFIG.selectors.usernameInput).fill('admin')
    await page.locator(TEST_CONFIG.selectors.passwordInput).fill('admin123')
    await page.locator(TEST_CONFIG.selectors.submitBtn).click()
    
    await page.waitForURL(`**${TEST_CONFIG.paths.family}`)
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
  })

  test('家族页面显示正确', async ({ page }) => {
    await expect(page.locator('h1')).toHaveText('家族管理')
    await expect(page.getByRole('button', { name: '创建家族' })).toBeVisible()
    await expect(page.locator(TEST_CONFIG.selectors.elTable)).toBeVisible()
  })

  test('创建家族功能', async ({ page }) => {
    const familyName = '测试家族_' + Date.now()
    
    await page.getByRole('button', { name: '创建家族' }).click()
    await page.waitForTimeout(TEST_CONFIG.timeout.short)
    
    await page.locator('input[placeholder="请输入家族名称"]').fill(familyName)
    await page.getByRole('button', { name: '创建', exact: true }).click()
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    await expect(page.locator(TEST_CONFIG.selectors.elTable)).toContainText(familyName)
  })

  test('编辑家族功能', async ({ page }) => {
    const familyName = '编辑测试家族'
    
    await page.getByRole('button', { name: '创建家族' }).click()
    await page.waitForTimeout(TEST_CONFIG.timeout.short)
    await page.locator('input[placeholder="请输入家族名称"]').fill(familyName)
    await page.getByRole('button', { name: '创建', exact: true }).click()
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    await page.locator(TEST_CONFIG.selectors.elTable).locator(TEST_CONFIG.selectors.elButton).first().click()
    await page.waitForTimeout(TEST_CONFIG.timeout.short)
    
    await page.locator('input[placeholder="请输入家族名称"]').fill(familyName + '_修改')
    await page.getByRole('button', { name: '保存', exact: true }).click()
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    await expect(page.locator(TEST_CONFIG.selectors.elTable)).toContainText(familyName + '_修改')
  })

  test('删除家族功能', async ({ page }) => {
    const familyName = '删除测试家族'
    
    await page.getByRole('button', { name: '创建家族' }).click()
    await page.waitForTimeout(TEST_CONFIG.timeout.short)
    await page.locator('input[placeholder="请输入家族名称"]').fill(familyName)
    await page.getByRole('button', { name: '创建', exact: true }).click()
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    await expect(page.locator(TEST_CONFIG.selectors.elTable)).toContainText(familyName)
    
    await page.locator(TEST_CONFIG.selectors.elButtonDanger).first().click()
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    const hasFamily = await page.locator(TEST_CONFIG.selectors.elTable).locator('td', { hasText: familyName }).count()
    expect(hasFamily).toBe(0)
  })
})