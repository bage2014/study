import { test, expect } from '@playwright/test';

test.describe('用户管理功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173');
    await page.waitForLoadState('networkidle');
    
    await page.locator('.login-btn').click();
    await page.locator('input[placeholder="用户名"]').fill('admin');
    await page.locator('input[placeholder="密码"]').fill('admin123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.user-info')).toBeVisible();
  });

  test('用户列表展示', async ({ page }) => {
    await page.locator('.user-management-btn').click();
    
    await expect(page.locator('table')).toBeVisible();
    await expect(page.locator('table tr')).toHaveCount(3);
  });

  test('添加用户', async ({ page }) => {
    await page.locator('.user-management-btn').click();
    
    await page.locator('.ant-btn-primary').click();
    await page.locator('input[placeholder="请输入用户名（3-20位）"]').fill('testuser');
    await page.locator('input[placeholder="请输入邮箱地址"]').fill('test@example.com');
    await page.locator('input[placeholder="请输入密码（至少6位）"]').fill('password123');
    await page.locator('.ant-modal-footer .ant-btn-primary').click();
    
    await expect(page.locator('.ant-message-success')).toBeVisible();
    await expect(page.locator('table tr')).toHaveCount(4);
  });

  test('添加用户 - 验证失败', async ({ page }) => {
    await page.locator('.user-management-btn').click();
    
    await page.locator('.ant-btn-primary').click();
    await page.locator('.ant-modal-footer .ant-btn-primary').click();
    
    await expect(page.locator('.ant-message-warning')).toBeVisible();
  });

  test('编辑用户', async ({ page }) => {
    await page.locator('.user-management-btn').click();
    
    await page.locator('table tr:last-child .ant-btn-link').first().click();
    await page.locator('input[placeholder="请输入用户名（3-20位）"]').fill('updateduser');
    await page.locator('.ant-modal-footer .ant-btn-primary').click();
    
    await expect(page.locator('.ant-message-success')).toBeVisible();
    await expect(page.locator('table tr:last-child td:nth-child(2)')).toHaveText('updateduser');
  });

  test('删除用户', async ({ page }) => {
    await page.locator('.user-management-btn').click();
    
    const initialRowCount = await page.locator('table tr').count();
    
    await page.locator('table tr:last-child .ant-btn-link').last().click();
    await page.locator('.ant-modal-confirm-btns .ant-btn-primary').click();
    
    await expect(page.locator('.ant-message-success')).toBeVisible();
    await expect(page.locator('table tr')).toHaveCount(initialRowCount - 1);
  });
});