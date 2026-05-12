import { test, expect } from '@playwright/test';

test.describe('登录功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173');
    await page.waitForLoadState('networkidle');
  });

  test('登录成功', async ({ page }) => {
    await page.locator('.login-btn').click();
    await page.locator('input[placeholder="用户名"]').fill('admin');
    await page.locator('input[placeholder="密码"]').fill('admin123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.user-info')).toBeVisible();
    await expect(page.locator('.user-info')).toHaveText('admin');
  });

  test('登录失败 - 错误密码', async ({ page }) => {
    await page.locator('.login-btn').click();
    await page.locator('input[placeholder="用户名"]').fill('admin');
    await page.locator('input[placeholder="密码"]').fill('wrongpassword');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.ant-message-error')).toBeVisible();
  });

  test('登录失败 - 空用户名', async ({ page }) => {
    await page.locator('.login-btn').click();
    await page.locator('input[placeholder="密码"]').fill('admin123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.ant-message-warning')).toBeVisible();
  });

  test('登录态持久化 - 刷新页面保持登录状态', async ({ page }) => {
    await page.locator('.login-btn').click();
    await page.locator('input[placeholder="用户名"]').fill('admin');
    await page.locator('input[placeholder="密码"]').fill('admin123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.user-info')).toBeVisible();
    
    await page.reload();
    await page.waitForLoadState('networkidle');
    
    await expect(page.locator('.user-info')).toBeVisible();
  });

  test('退出登录', async ({ page }) => {
    await page.locator('.login-btn').click();
    await page.locator('input[placeholder="用户名"]').fill('admin');
    await page.locator('input[placeholder="密码"]').fill('admin123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.user-info')).toBeVisible();
    
    await page.locator('.logout-btn').click();
    
    await expect(page.locator('.login-btn')).toBeVisible();
    await expect(page.locator('.user-info')).not.toBeVisible();
  });
});