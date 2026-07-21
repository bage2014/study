import { test, expect } from '@playwright/test'

test('create pipeline and verify test count display', async ({ page }) => {
  await page.goto('/')
  await page.waitForTimeout(3000)

  const titleInput = page.locator('input[placeholder*="请输入需求标题"]')
  await titleInput.click()
  await titleInput.fill('测试消息功能')
  
  const descriptionInput = page.locator('textarea[placeholder*="请详细描述您的业务需求"]')
  await descriptionInput.click()
  await descriptionInput.fill('添加消息CRUD功能，包括创建、查询、更新、删除消息')
  
  await page.waitForTimeout(500)

  await page.click('button:has-text("提交需求，启动AI流水线")')
  await page.waitForTimeout(5000)

  const pipelineUrl = await page.url()
  console.log('Pipeline URL:', pipelineUrl)

  await page.waitForSelector('.animate-pulse', { state: 'detached', timeout: 300000 })

  await page.waitForTimeout(5000)

  const testGenSection = page.locator('div:has-text("测试生成")')
  await testGenSection.scrollIntoViewIfNeeded()
  await page.waitForTimeout(1000)

  const testGenFiles = testGenSection.locator('button.bg-green-50')
  const testFileCount = await testGenFiles.count()
  console.log('测试文件数量:', testFileCount)

  const testExecSection = page.locator('div:has-text("测试验证")')
  await testExecSection.scrollIntoViewIfNeeded()
  await page.waitForTimeout(1000)

  const totalTests = await testExecSection.locator('.text-2xl.font-bold.text-gray-800').first().textContent()
  const passedTests = await testExecSection.locator('.text-2xl.font-bold.text-green-600').first().textContent()
  
  console.log('测试总数:', totalTests || '0')
  console.log('通过测试:', passedTests || '0')

  await page.screenshot({ path: 'test-result-screenshot.png', fullPage: true })
})
