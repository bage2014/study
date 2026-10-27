import { test, expect } from '@playwright/test'

test.describe('首页', () => {
  test('页面加载正常', async ({ page }) => {
    await page.goto('/')
    await expect(page.locator('h1')).toContainText('研发流程标准前端项目')
  })

  test('计数器功能正常', async ({ page }) => {
    await page.goto('/')

    await expect(page.locator('text=计数器: 0')).toBeVisible()

    await page.click('button:has-text("+1")')
    await expect(page.locator('text=计数器: 1')).toBeVisible()

    await page.click('button:has-text("-1")')
    await expect(page.locator('text=计数器: 0')).toBeVisible()
  })

  test('导航栏显示所有链接', async ({ page }) => {
    await page.goto('/')

    await expect(page.locator('nav a:has-text("首页")')).toBeVisible()
    await expect(page.locator('nav a:has-text("示例")')).toBeVisible()
    await expect(page.locator('nav a:has-text("用户")')).toBeVisible()
  })
})
