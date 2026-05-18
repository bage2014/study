import { test, expect } from '@playwright/test'

test.describe('家族管理功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:5174/login')
    await page.waitForLoadState('networkidle')
    
    await page.locator('input[placeholder="用户名"]').fill('admin')
    await page.locator('input[placeholder="密码"]').fill('admin123')
    await page.locator('.submit-btn').click()
    
    await page.waitForURL('**/family')
    await page.waitForTimeout(1000)
  })

  test('查看家族列表页面显示正确', async ({ page }) => {
    await expect(page.locator('h1')).toHaveText('家族管理')
    await expect(page.locator('button', { hasText: '创建家族' })).toBeVisible()
    await expect(page.locator('.el-table')).toBeVisible()
  })

  test('创建家族', async ({ page }) => {
    const familyName = '测试家族_' + Date.now()
    
    await page.locator('button', { hasText: '创建家族' }).click()
    
    const createModal = page.locator('.el-dialog', { hasText: '创建家族' })
    await expect(createModal).toBeVisible()
    
    await createModal.locator('input[placeholder="请输入家族名称"]').fill(familyName)
    await createModal.locator('textarea[placeholder="请输入家族描述"]').fill('这是一个测试家族')
    
    await createModal.locator('.el-dialog__footer button', { hasText: '创建' }).click()
    
    await page.waitForTimeout(1500)
    await expect(page.locator('.el-message--success').first()).toBeVisible()
    
    const familyRow = page.locator('.el-table__body tr', { hasText: familyName })
    await expect(familyRow).toBeVisible()
  })

  test('编辑家族', async ({ page }) => {
    const familyName = '测试家族_编辑_' + Date.now()
    const updatedFamilyName = '测试家族_编辑完成_' + Date.now()
    
    await page.locator('button', { hasText: '创建家族' }).click()
    const createModal = page.locator('.el-dialog', { hasText: '创建家族' })
    await createModal.locator('input[placeholder="请输入家族名称"]').fill(familyName)
    await createModal.locator('.el-dialog__footer button', { hasText: '创建' }).click()
    await page.waitForTimeout(1500)
    
    const familyRow = page.locator('.el-table__body tr', { hasText: familyName })
    await familyRow.locator('button').first().click()
    
    const updateModal = page.locator('.el-dialog', { hasText: '编辑家族' })
    await expect(updateModal).toBeVisible()
    
    await updateModal.locator('input[placeholder="请输入家族名称"]').fill(updatedFamilyName)
    await updateModal.locator('.el-dialog__footer button', { hasText: '保存' }).click()
    
    await page.waitForTimeout(1500)
    await expect(page.locator('.el-message--success').first()).toBeVisible()
    
    const updatedRow = page.locator('.el-table__body tr', { hasText: updatedFamilyName })
    await expect(updatedRow).toBeVisible()
  })

  test('删除家族', async ({ page }) => {
    const familyName = '测试家族_删除_' + Date.now()
    
    await page.locator('button', { hasText: '创建家族' }).click()
    const createModal = page.locator('.el-dialog', { hasText: '创建家族' })
    await createModal.locator('input[placeholder="请输入家族名称"]').fill(familyName)
    await createModal.locator('.el-dialog__footer button', { hasText: '创建' }).click()
    await page.waitForTimeout(1500)
    await page.locator('.el-message--success').first().waitFor({ state: 'hidden' })
    
    const familyRow = page.locator('.el-table__body tr', { hasText: familyName })
    await familyRow.locator('button').nth(1).click()
    
    await page.locator('.el-message-box__btns button', { hasText: '确定' }).click()
    
    await page.waitForTimeout(1500)
    await expect(page.locator('.el-message--success').first()).toBeVisible()
    
    await expect(page.locator('.el-table__body tr', { hasText: familyName })).toHaveCount(0)
  })

  test('查看家族列表数据列', async ({ page }) => {
    const table = page.locator('.el-table')
    
    await expect(table.locator('thead th', { hasText: '家族名称' })).toBeVisible()
    await expect(table.locator('thead th', { hasText: '描述' })).toBeVisible()
    await expect(table.locator('thead th', { hasText: '成员数量' })).toBeVisible()
    await expect(table.locator('thead th', { hasText: '创建者' })).toBeVisible()
    await expect(table.locator('thead th', { hasText: '创建时间' })).toBeVisible()
    await expect(table.locator('thead th', { hasText: '操作' })).toBeVisible()
  })
})