import { test, expect } from '@playwright/test';

test.describe('注册功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173');
    await page.waitForLoadState('networkidle');
  });

  test('注册成功', async ({ page }) => {
    await page.locator('.register-btn').click();
    await page.locator('input[placeholder="用户名"]').first().fill('newuser');
    await page.locator('input[placeholder="邮箱"]').fill('newuser@example.com');
    await page.locator('input[placeholder="密码"]').fill('password123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.ant-message-success')).toBeVisible();
    await expect(page.locator('.user-info')).toBeVisible();
  });

  test('注册失败 - 用户名已存在', async ({ page }) => {
    await page.locator('.register-btn').click();
    await page.locator('input[placeholder="用户名"]').first().fill('admin');
    await page.locator('input[placeholder="邮箱"]').fill('admin@example.com');
    await page.locator('input[placeholder="密码"]').fill('password123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.ant-message-error')).toBeVisible();
  });

  test('注册失败 - 邮箱已存在', async ({ page }) => {
    await page.locator('.register-btn').click();
    await page.locator('input[placeholder="用户名"]').first().fill('anotheruser');
    await page.locator('input[placeholder="邮箱"]').fill('admin@example.com');
    await page.locator('input[placeholder="密码"]').fill('password123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.ant-message-error')).toBeVisible();
  });

  test('注册失败 - 空用户名', async ({ page }) => {
    await page.locator('.register-btn').click();
    await page.locator('input[placeholder="邮箱"]').fill('test@example.com');
    await page.locator('input[placeholder="密码"]').fill('password123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.ant-message-warning')).toBeVisible();
  });

  test('注册失败 - 空邮箱', async ({ page }) => {
    await page.locator('.register-btn').click();
    await page.locator('input[placeholder="用户名"]').first().fill('testuser');
    await page.locator('input[placeholder="密码"]').fill('password123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.ant-message-warning')).toBeVisible();
  });

  test('注册失败 - 空密码', async ({ page }) => {
    await page.locator('.register-btn').click();
    await page.locator('input[placeholder="用户名"]').first().fill('testuser');
    await page.locator('input[placeholder="邮箱"]').fill('test@example.com');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.ant-message-warning')).toBeVisible();
  });

  test('注册失败 - 邮箱格式错误', async ({ page }) => {
    await page.locator('.register-btn').click();
    await page.locator('input[placeholder="用户名"]').first().fill('testuser');
    await page.locator('input[placeholder="邮箱"]').fill('invalid-email');
    await page.locator('input[placeholder="密码"]').fill('password123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.ant-message-error')).toBeVisible();
  });

  test('注册失败 - 密码长度不足', async ({ page }) => {
    await page.locator('.register-btn').click();
    await page.locator('input[placeholder="用户名"]').first().fill('testuser');
    await page.locator('input[placeholder="邮箱"]').fill('test@example.com');
    await page.locator('input[placeholder="密码"]').fill('123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.ant-message-error')).toBeVisible();
  });
});