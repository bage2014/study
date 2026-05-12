import { test, expect } from '@playwright/test'

test.describe('家族管理功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173')
    await page.waitForLoadState('networkidle')
    
    await page.locator('input[placeholder="用户名"]').fill('testuser')
    await page.locator('input[placeholder="密码"]').fill('password123')
    await page.locator('button', { hasText: '登录' }).click()
    
    await page.waitForURL('**/family')
  })

  test('创建家族', async ({ page }) => {
    await page.locator('button', { hasText: '创建家族' }).click()
    
    await page.locator('input.el-input__inner').first().fill('测试家族')
    await page.locator('textarea.el-textarea__inner').fill('这是一个测试家族')
    
    await page.locator('button', { hasText: '创建' }).click()
    
    await expect(page.locator('table')).toContainText('测试家族')
  })

  test('编辑家族', async ({ page }) => {
    await page.locator('table').waitFor()
    
    const editBtn = page.locator('button', { hasText: 'Edit' }).first()
    await editBtn.click()
    
    await page.locator('input.el-input__inner').first().fill('修改后的家族')
    
    await page.locator('button', { hasText: '保存' }).click()
    
    await expect(page.locator('table')).toContainText('修改后的家族')
  })

  test('删除家族', async ({ page }) => {
    await page.locator('table').waitFor()
    
    const deleteBtn = page.locator('button', { hasText: 'Delete' }).first()
    await deleteBtn.click()
    
    await page.locator('button', { hasText: '确定' }).click()
    
    await expect(page.locator('table')).not.toContainText('修改后的家族')
  })
})