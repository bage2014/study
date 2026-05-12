import { test, expect } from '@playwright/test';

test.describe('轨迹功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173');
    await page.waitForLoadState('networkidle');
    
    await page.locator('.login-btn').click();
    await page.locator('input[placeholder="用户名"]').fill('admin');
    await page.locator('input[placeholder="密码"]').fill('admin123');
    await page.locator('.submit-btn').click();
    
    await expect(page.locator('.user-info')).toBeVisible();
  });

  test('轨迹展示', async ({ page }) => {
    await page.locator('.track-btn').click();
    
    await expect(page.locator('.map-container')).toBeVisible();
    await expect(page.locator('.map-actions')).toBeVisible();
  });

  test('添加轨迹点', async ({ page }) => {
    await page.locator('.track-btn').click();
    
    await page.locator('.map-container').click({ position: { x: 400, y: 300 } });
    
    await expect(page.locator('.ant-modal-title')).toHaveText('添加轨迹点');
    
    await page.locator('input[placeholder="请输入位置名称"]').fill('测试位置');
    await page.locator('textarea[placeholder="请输入描述"]').fill('测试描述');
    await page.locator('.ant-modal-footer .ant-btn-primary').click();
    
    await expect(page.locator('.ant-message-success')).toBeVisible();
  });

  test('添加轨迹点 - 缺少名称', async ({ page }) => {
    await page.locator('.track-btn').click();
    
    await page.locator('.map-container').click({ position: { x: 400, y: 300 } });
    
    await page.locator('.ant-modal-footer .ant-btn-primary').click();
    
    await expect(page.locator('.ant-message-warning')).toBeVisible();
  });

  test('删除轨迹点', async ({ page }) => {
    await page.locator('.track-btn').click();
    
    await page.locator('.map-container').click({ position: { x: 400, y: 300 } });
    await page.locator('input[placeholder="请输入位置名称"]').fill('测试删除');
    await page.locator('.ant-modal-footer .ant-btn-primary').click();
    
    await expect(page.locator('.ant-message-success')).toBeVisible();
    
    await page.locator('.map-container').click({ position: { x: 400, y: 300 } });
    await page.locator('.ant-modal-confirm-btns .ant-btn-primary').click();
    
    await expect(page.locator('.ant-message-success')).toBeVisible();
  });

  test('清空轨迹', async ({ page }) => {
    await page.locator('.track-btn').click();
    
    await page.locator('.map-container').click({ position: { x: 400, y: 300 } });
    await page.locator('input[placeholder="请输入位置名称"]').fill('清空测试');
    await page.locator('.ant-modal-footer .ant-btn-primary').click();
    
    await expect(page.locator('.ant-message-success')).toBeVisible();
    
    await page.locator('.map-actions .ant-btn-primary').click();
    await page.locator('.ant-modal-confirm-btns .ant-btn-primary').click();
    
    await expect(page.locator('.ant-message-success')).toBeVisible();
  });
});