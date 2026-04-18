import { test, expect } from '@playwright/test';

test.describe('Family Management Tests', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[id="email"]', 'bage@qq.com');
    await page.fill('input[id="password"]', 'bage1234');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/home');
  });

  test('should navigate to family management page', async ({ page }) => {
    await page.click('a[href="/family-management"]');
    await expect(page).toHaveURL('/family-management');
  });

  test('should display family list', async ({ page }) => {
    await page.click('a[href="/family-management"]');
    await expect(page).toHaveURL('/family-management');
    await expect(page.locator('h1')).toContainText('家族管理');
  });

  test('should create new family', async ({ page }) => {
    await page.click('a[href="/family-management"]');
    await expect(page).toHaveURL('/family-management');

    const familyName = `测试家族${Date.now()}`;
    await page.click('button:has-text("添加家族")');
    await page.fill('input[id="name"]', familyName);
    await page.fill('textarea[id="description"]', '这是一个测试家族');
    await page.click('button[type="submit"]:has-text("保存")');
  });

  test('should view family details', async ({ page }) => {
    await page.click('a[href="/family-management"]');
    await expect(page).toHaveURL('/family-management');

    const firstFamilyRow = page.locator('tbody tr').first();
    await firstFamilyRow.locator('button').first().click();
  });

  test('should display family administrator', async ({ page }) => {
    await page.click('a[href="/family-management"]');
    await expect(page).toHaveURL('/family-management');

    const firstFamilyRow = page.locator('tbody tr').first();
    await firstFamilyRow.locator('button').first().click();

    await expect(page.locator('text=管理员')).toBeVisible({ timeout: 5000 });
  });

  test('should change family administrator', async ({ page }) => {
    await page.click('a[href="/family-management"]');
    await expect(page).toHaveURL('/family-management');

    const firstFamilyRow = page.locator('tbody tr').first();
    await firstFamilyRow.locator('button').first().click();

    const changeAdminButton = page.locator('button:has-text("更改管理员")');
    if (await changeAdminButton.isVisible()) {
      await changeAdminButton.click();
    }
  });
});

test.describe('Family Tree Tests', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.fill('input[id="email"]', 'bage@qq.com');
    await page.fill('input[id="password"]', 'bage1234');
    await page.click('button[type="submit"]');
    await expect(page).toHaveURL('/home');
  });

  test('should navigate to family tree page', async ({ page }) => {
    await page.click('a[href="/family-tree"]');
    await expect(page).toHaveURL('/family-tree');
  });

  test('should display family tree', async ({ page }) => {
    await page.click('a[href="/family-tree"]');
    await expect(page).toHaveURL('/family-tree');
    await expect(page.locator('h1')).toContainText('家族树');
  });
});
