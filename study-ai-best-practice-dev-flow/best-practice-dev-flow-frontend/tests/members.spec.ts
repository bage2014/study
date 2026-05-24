import { test, expect } from '@playwright/test'
import { TEST_CONFIG, getFullUrl } from './config'

test.describe('成员管理功能', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto(getFullUrl(TEST_CONFIG.paths.login))
    await page.waitForLoadState('networkidle')
    
    await page.locator(TEST_CONFIG.selectors.usernameInput).fill('admin')
    await page.locator(TEST_CONFIG.selectors.passwordInput).fill('admin123')
    await page.locator(TEST_CONFIG.selectors.submitBtn).click()
    
    await page.waitForURL(`**${TEST_CONFIG.paths.family}`)
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    const familyExists = await page.locator('.el-table__row').count()
    if (familyExists === 0) {
      await page.getByRole('button', { name: '创建家族' }).click()
      await page.waitForTimeout(TEST_CONFIG.timeout.short)
      await page.locator('input.el-input__inner').first().fill('测试家族')
      await page.getByRole('button', { name: '创建', exact: true }).click()
      await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    }
    
    await page.getByText('成员管理').click({ force: true })
    await page.waitForURL(`**${TEST_CONFIG.paths.members}`)
    await page.waitForLoadState('networkidle')
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
  })

  test('成员页面显示正确', async ({ page }) => {
    await expect(page.locator('h1')).toHaveText('成员管理')
    const addButton = page.getByRole('button', { name: '添加成员' })
    await expect(addButton).toBeVisible()
    await expect(addButton).toBeEnabled()
    await expect(page.locator(TEST_CONFIG.selectors.searchBar)).toBeVisible()
    await expect(page.locator(TEST_CONFIG.selectors.elTable)).toBeVisible()
  })

  test('添加成员功能', async ({ page }) => {
    const addButton = page.getByRole('button', { name: '添加成员' })
    await addButton.click()
    
    await page.waitForSelector('.el-dialog', { state: 'visible' })
    
    const nameInput = page.locator('.el-dialog').locator('input.el-input__inner').first()
    await nameInput.waitFor({ state: 'visible' })
    await nameInput.fill('测试成员')
    
    await page.waitForTimeout(TEST_CONFIG.timeout.short)
    
    const saveButton = page.getByRole('button', { name: '保存', exact: true })
    await saveButton.click()
    
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
    
    await expect(page.locator('.el-dialog')).not.toBeVisible()
    
    await expect(page.locator(TEST_CONFIG.selectors.elTable)).toContainText('测试成员')
  })

  test('操作按钮显示正确', async ({ page }) => {
    const editButton = page.getByRole('button', { name: '编辑' })
    const deleteButton = page.getByRole('button', { name: '删除' })
    
    await expect(editButton.first()).toBeVisible()
    await expect(deleteButton.first()).toBeVisible()
  })

  test('删除成员显示确认框', async ({ page }) => {
    const deleteButton = page.getByRole('button', { name: '删除' }).first()
    await deleteButton.click()
    
    await page.waitForTimeout(TEST_CONFIG.timeout.short)
    
    const confirmBox = page.locator('.el-message-box')
    await expect(confirmBox).toBeVisible()
    await expect(confirmBox).toContainText('确定要删除该成员吗？')
    
    const cancelButton = page.getByRole('button', { name: '取消' })
    await cancelButton.click()
    
    await expect(confirmBox).not.toBeVisible()
  })

  test('搜索成员功能', async ({ page }) => {
    const searchInput = page.locator(TEST_CONFIG.selectors.searchBar).locator('input')
    await searchInput.fill('测试')
    
    const searchButton = page.locator(TEST_CONFIG.selectors.searchBar).locator('button')
    await searchButton.click()
    
    await page.waitForTimeout(TEST_CONFIG.timeout.medium)
  })
})