import { test, expect } from '@playwright/test'

test.describe('成员管理功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5173/login')
    await page.waitForLoadState('networkidle')
    
    await page.locator('input[placeholder="用户名"]').fill('admin')
    await page.locator('input[placeholder="密码"]').fill('admin123')
    await page.locator('.submit-btn').click()
    
    await page.waitForURL('**/family')
    await page.waitForTimeout(1000)
    
    await page.locator('text=成员管理').click()
    await page.waitForURL('**/members')
    await page.waitForLoadState('networkidle')
    await page.waitForTimeout(500)
  })

  test('成员页面显示正确', async ({ page }) => {
    await expect(page.locator('h1')).toHaveText('成员管理')
    await expect(page.getByText('添加成员')).toBeVisible()
    await expect(page.locator('.search-bar')).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
  })

  test('添加成员功能', async ({ page }) => {
    await page.getByText('添加成员').click()
    await page.waitForTimeout(300)
    
    const dialogTitle = page.locator('.el-dialog__title')
    await expect(dialogTitle).toHaveText('添加成员')
    
    await page.locator('input[placeholder="请输入内容"]').first().fill('测试成员')
    await page.locator('input[placeholder="请选择"]').first().click()
    await page.getByText('男').click()
    await page.locator('input[type="date"]').first().fill('1990-01-01')
    await page.locator('.el-form-item', { hasText: '职业' }).locator('input').fill('工程师')
    await page.locator('.el-form-item', { hasText: '学历' }).locator('input').fill('本科')
    await page.locator('.el-form-item', { hasText: '电话' }).locator('input').fill('13800138000')
    await page.locator('.el-form-item', { hasText: '邮箱' }).locator('input').fill('test@example.com')
    
    await page.getByRole('button', { name: '保存' }).nth(1).click()
    await page.waitForTimeout(500)
    
    await expect(page.locator('.el-table')).toContainText('测试成员')
    await expect(page.locator('.el-table')).toContainText('工程师')
    await expect(page.locator('.el-table')).toContainText('本科')
  })

  test('编辑成员功能', async ({ page }) => {
    const memberName = '编辑测试成员'
    
    await page.getByText('添加成员').click()
    await page.waitForTimeout(300)
    await page.locator('input[placeholder="请输入内容"]').first().fill(memberName)
    await page.locator('input[placeholder="请选择"]').first().click()
    await page.getByText('女').click()
    await page.getByRole('button', { name: '保存' }).nth(1).click()
    await page.waitForTimeout(500)
    
    await page.locator('.el-table').locator('button', { hasText: '编辑' }).first().click()
    await page.waitForTimeout(300)
    
    const dialogTitle = page.locator('.el-dialog__title')
    await expect(dialogTitle).toHaveText('编辑成员')
    
    await page.locator('input[placeholder="请输入内容"]').first().fill(memberName + '_修改')
    await page.locator('.el-form-item', { hasText: '职业' }).locator('input').fill('设计师')
    await page.getByRole('button', { name: '保存' }).nth(1).click()
    await page.waitForTimeout(500)
    
    await expect(page.locator('.el-table')).toContainText(memberName + '_修改')
    await expect(page.locator('.el-table')).toContainText('设计师')
  })

  test('删除成员功能', async ({ page }) => {
    const memberName = '删除测试成员'
    
    await page.getByText('添加成员').click()
    await page.waitForTimeout(300)
    await page.locator('input[placeholder="请输入内容"]').first().fill(memberName)
    await page.locator('input[placeholder="请选择"]').first().click()
    await page.getByText('男').click()
    await page.getByRole('button', { name: '保存' }).nth(1).click()
    await page.waitForTimeout(500)
    
    await expect(page.locator('.el-table')).toContainText(memberName)
    
    await page.locator('.el-table').locator('button', { hasText: '删除' }).first().click()
    await page.waitForTimeout(500)
    
    const hasMember = await page.locator('.el-table').locator('td', { hasText: memberName }).count()
    expect(hasMember).toBe(0)
  })

  test('成员搜索功能', async ({ page }) => {
    const searchName = '搜索测试成员'
    
    await page.getByText('添加成员').click()
    await page.waitForTimeout(300)
    await page.locator('input[placeholder="请输入内容"]').first().fill(searchName)
    await page.locator('input[placeholder="请选择"]').first().click()
    await page.getByText('男').click()
    await page.getByRole('button', { name: '保存' }).nth(1).click()
    await page.waitForTimeout(500)
    
    await page.locator('input[placeholder="搜索成员姓名"]').fill(searchName)
    await page.locator('.search-bar').getByRole('button').click()
    await page.waitForTimeout(500)
    
    await expect(page.locator('.el-table')).toContainText(searchName)
    
    await page.locator('input[placeholder="搜索成员姓名"]').fill('不存在的成员')
    await page.locator('.search-bar').getByRole('button').click()
    await page.waitForTimeout(500)
    
    const hasMember = await page.locator('.el-table').locator('td', { hasText: searchName }).count()
    expect(hasMember).toBe(0)
  })
})