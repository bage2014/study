import { test, expect } from '@playwright/test'

test.describe('用户管理页面', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/users')
  })

  test('页面标题显示正确', async ({ page }) => {
    await expect(page.locator('h2')).toContainText('用户管理')
  })

  test('导航栏包含用户链接', async ({ page }) => {
    const navLink = page.locator('nav a[href="/users"]')
    await expect(navLink).toBeVisible()
    await expect(navLink).toContainText('用户')
  })

  test('空数据时显示暂无数据', async ({ page }) => {
    await expect(page.locator('text=暂无数据')).toBeVisible()
  })

  test('点击新增用户按钮显示表单', async ({ page }) => {
    await page.click('button:has-text("新增用户")')
    await expect(page.locator('input[type="text"]').first()).toBeVisible()
    await expect(page.locator('input[type="email"]')).toBeVisible()
  })

  test('表单包含必填字段', async ({ page }) => {
    await page.click('button:has-text("新增用户")')

    const usernameInput = page.locator('input[type="text"]').first()
    const emailInput = page.locator('input[type="email"]')

    await expect(usernameInput).toBeVisible()
    await expect(emailInput).toBeVisible()
    await expect(usernameInput).toHaveAttribute('required', '')
    await expect(emailInput).toHaveAttribute('required', '')
  })

  test('可以填写表单', async ({ page }) => {
    await page.click('button:has-text("新增用户")')

    await page.fill('input[type="text"]', 'testuser')
    await page.fill('input[type="email"]', 'test@example.com')
    await page.locator('input[type="text"]').nth(1).fill('13800138000')

    await expect(page.locator('input[type="text"]').first()).toHaveValue('testuser')
    await expect(page.locator('input[type="email"]')).toHaveValue('test@example.com')
  })

  test('点击取消按钮隐藏表单', async ({ page }) => {
    await page.click('button:has-text("新增用户")')
    await page.click('button:has-text("取消")')

    await expect(page.locator('form')).not.toBeVisible()
  })

  test('表格列头显示正确', async ({ page }) => {
    await expect(page.locator('th:has-text("ID")')).toBeVisible()
    await expect(page.locator('th:has-text("用户名")')).toBeVisible()
    await expect(page.locator('th:has-text("邮箱")')).toBeVisible()
    await expect(page.locator('th:has-text("手机号")')).toBeVisible()
    await expect(page.locator('th:has-text("操作")')).toBeVisible()
  })
})

test.describe('页面导航', () => {
  test('从首页可以导航到用户页面', async ({ page }) => {
    await page.goto('/')
    await page.click('nav a:has-text("用户")')
    await expect(page).toHaveURL('/users')
    await expect(page.locator('h2')).toContainText('用户管理')
  })

  test('从示例页可以导航到用户页面', async ({ page }) => {
    await page.goto('/samples')
    await page.click('nav a:has-text("用户")')
    await expect(page).toHaveURL('/users')
  })
})
