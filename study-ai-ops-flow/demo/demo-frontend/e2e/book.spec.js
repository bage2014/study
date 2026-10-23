import { test, expect } from '@playwright/test'

test('book list page', async ({ page }) => {
  await page.goto('/')
  await expect(page.locator('h2')).toContainText('图书列表')
})
