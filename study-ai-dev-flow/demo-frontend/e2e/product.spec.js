import { test, expect } from '@playwright/test'

test('product list page', async ({ page }) => {
  await page.goto('/')
  await expect(page.locator('h2')).toContainText('商品列表')
})