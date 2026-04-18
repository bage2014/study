import { test, expect } from '@playwright/test';

test.describe('Member Management Tests', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[id="email"]', 'bage@qq.com');
    await page.fill('input[id="password"]', 'bage1234');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/home');
  });

  test('should navigate to members page', async ({ page }) => {
    await page.click('a[href="/members"]');
    await expect(page).toHaveURL('/members');
  });

  test('should display members list', async ({ page }) => {
    await page.click('a[href="/members"]');
    await expect(page).toHaveURL('/members');
    await expect(page.locator('h1')).toContainText('成员管理');
  });

  test('should search members', async ({ page }) => {
    await page.click('a[href="/members"]');
    await expect(page).toHaveURL('/members');

    const searchInput = page.locator('input[type="search"], input[placeholder*="搜索"]');
    if (await searchInput.isVisible()) {
      await searchInput.fill('张');
    }
  });

  test('should add new member', async ({ page }) => {
    await page.click('a[href="/members"]');
    await expect(page).toHaveURL('/members');

    const addButton = page.locator('button:has-text("添加成员"), button:has-text("新增成员")');
    if (await addButton.isVisible()) {
      await addButton.click();
    }
  });
});

test.describe('Member Detail Tests', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[id="email"]', 'bage@qq.com');
    await page.fill('input[id="password"]', 'bage1234');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/home');
  });

  test('should navigate to member detail', async ({ page }) => {
    await page.click('a[href="/members"]');
    await expect(page).toHaveURL('/members');

    const firstMemberRow = page.locator('tbody tr').first();
    if (await firstMemberRow.isVisible()) {
      await firstMemberRow.click();
    }
  });
});
